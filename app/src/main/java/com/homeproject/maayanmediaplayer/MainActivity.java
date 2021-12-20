package com.homeproject.maayanmediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SongAdditionFragment.songAdditionListener {

    public static String LOG_TAG = "MaayanMediaPlayer";

    FloatingActionButton addSongButton;
    SharedPreferences.Editor editor;
    int fileIndex = 0;
    boolean isFirstRun;
    SongAdapter songAdapter;
    SongAdditionFragment songAdditionFragment;
    ArrayList<Song> songList = getSongs();
    SongPlayingFragment songPlayingFragment;
    SharedPreferences sp;
    RecyclerView songsRV;


    @Override
    public void onStop() {
        super.onStop();
        FileHandler.writeToFile(this, this.songList);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        CardView songView = findViewById(R.id.songItem);
        RecyclerView recyclerView = findViewById(R.id.recycler_songs);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songsRV = recyclerView;
        SharedPreferences sharedPreferences = getSharedPreferences("index", 0);
        this.sp = sharedPreferences;
        this.editor = sharedPreferences.edit();
        this.isFirstRun = this.sp.getBoolean("isFirstRun", true);
        extractOrStoreSongListInFile();


        Intent intent = new Intent();
        intent.putExtra("List", songList);
        intent.putExtra("command", "new_instance");




        songAdapter = new SongAdapter(songList, this);
        songAdapter.setListener(new SongAdapter.MySongListener() {
            @Override
            public void onSongClicked(int position, View view) {
                // todo listener to play this song
                Intent intent = new Intent(MainActivity.this, SongsService.class);
                intent.putExtra("command", "play_song_by_position");
                intent.putExtra("position", position);
                intent.putExtra("list", songList);
                try {
                    startService(intent);
                } catch (Exception e) {
                    Log.e("maayan", "start service song", e);
                }
                MainActivity.this.songPlayingFragment = new SongPlayingFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putParcelableArrayList("arrayList", MainActivity.this.songList);
                bundle2.putInt("position", position);
                MainActivity.this.songPlayingFragment.setArguments(bundle2);
                if (MainActivity.this.getSupportFragmentManager().findFragmentByTag("SongPlaying") == null) {
                    MainActivity.this.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_from_left).add(16908290, MainActivity.this.songPlayingFragment, "SongPlaying").addToBackStack("SongPlaying").commit();
                }
            }

            @Override
            public void onSongLongClicked(int position, View view) {
                //add a dialog for checked again before we deleted
                songList.remove(position);
                songAdapter.notifyItemRemoved(position);
                FileHandler.writeToFile(MainActivity.this , songList);
                if (MainActivity.this.getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    MainActivity.this.getSupportFragmentManager().popBackStack();
                }
            }
        });



        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback( 0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int adapterPosition = viewHolder.getAdapterPosition();
                int adapterPosition2 = target.getAdapterPosition();
                Collections.swap(MainActivity.this.songList, adapterPosition, adapterPosition2);
                ((RecyclerView.Adapter) Objects.requireNonNull(MainActivity.this.songsRV.getAdapter())).notifyItemMoved(adapterPosition, adapterPosition2);
                MainActivity mainActivity = MainActivity.this;
                FileHandler.writeToFile(mainActivity, mainActivity.songList);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        if (direction==ItemTouchHelper.RIGHT) {
                            songList.remove(viewHolder.getAdapterPosition());
                            songAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                        }
                        else if (direction == ItemTouchHelper.LEFT){
                            //open the image of this song
                            songList.remove(viewHolder.getAdapterPosition());
                            songAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                        }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(songAdapter);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.add_song_button_activity_main);
        this.addSongButton = floatingActionButton;
        floatingActionButton.setOnClickListener(new View.OnClickListener() { // from class: il.co.hit.mediaplayer.MainActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MainActivity.this.songAdditionFragment = new SongAdditionFragment();
                MainActivity.this.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, 0, 0, R.anim.exit_from_left).add(16908290, MainActivity.this.songAdditionFragment, "createSong").addToBackStack("createSong").commit();
            }
        });


    }

    private void extractOrStoreSongListInFile() {
        if (!isFirstRun) {
            songList = FileHandler.readFromFile(this);
        } else {
            songList = getSongs();

            //store for the next time on file
            FileHandler.writeToFile(this, songList);
            int i2 = this.sp.getInt("fileIndex", 0);
            this.fileIndex = i2;
            int i3 = i2 + 1;
            this.fileIndex = i3;
            this.editor.putInt("fileIndex", i3).putBoolean("isFirstRun", false).commit();
        }
    }

    @NonNull
    private ArrayList<Song> getSongs() {
        ArrayList<Song> songs = new ArrayList<>();
        songs.add(new Song("Bob Marley", "https://www.syntax.org.il/xtra/bob2.mp3", R.drawable.bob_marley_image));
        songs.add(new Song("Bad Bunny", "https://www.syntax.org.il/xtra/bob1.m4a", R.drawable.bad_bunny_image));
        songs.add(new Song("Ozuna", "https://www.naijagreen.com/wp-content/uploads/music/2021/08/Coldplay_-_Magic_[NaijaGreen.Com]_.mp3", R.drawable.ozuna_image));

        songs.add(new Song("Bob Marley", "https://www.syntax.org.il/xtra/bob2.mp3", R.drawable.bob_marley_image));
        songs.add(new Song("Bad Bunny", "https://www.syntax.org.il/xtra/bob1.m4a", R.drawable.bad_bunny_image));
        songs.add(new Song("Ozuna", "https://www.naijagreen.com/wp-content/uploads/music/2021/08/Coldplay_-_Magic_[NaijaGreen.Com]_.mp3", R.drawable.ozuna_image));
        return songs;
    }

    @Override
    public void onAddSongClicked(Song songNew) {
        this.songList.add(songNew);
        this.songAdapter.notifyItemInserted(this.songList.size() - 1);
        FileHandler.writeToFile(this, this.songList);
        int i = this.fileIndex + 1;
        this.fileIndex = i;
        this.editor.putInt("fileIndex", i).commit();
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right , 0, 0, R.anim.exit_from_left).remove(this.songAdditionFragment).commit();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) { }

}