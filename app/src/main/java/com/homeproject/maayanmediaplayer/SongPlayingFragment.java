package com.homeproject.maayanmediaplayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class SongPlayingFragment extends Fragment {
    private TextView authorTv;
    private ImageView bigPic;
    private FloatingActionButton fabExit;
    private int position;
    private ArrayList<Song> songArrayList;
    private TextView titleTv;

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActionBar supportActionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.song_playing_fragment_layout, viewGroup, false);
        initViews(inflate);
        Bundle arguments = getArguments();
        this.songArrayList = getArguments().getParcelableArrayList("arrayList");
        int i = arguments.getInt("position");
        this.position = i;
        this.titleTv.setText(this.songArrayList.get(i).getName());
        //this.authorTv.setText(this.songArrayList.get(this.position).getName());
        if(this.songArrayList.get(this.position).getSongID() == 0) {
            Glide.with(inflate).load(this.songArrayList.get(this.position).getImagePath()).into(this.bigPic);
        }
        else{
            this.bigPic.setImageResource(this.songArrayList.get(this.position).getSongID());
        }
        this.fabExit.setOnClickListener(new View.OnClickListener() { // from class: com.homeproject.maayanmediaplayer.SongPlayingFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SongPlayingFragment.this.requireActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    SongPlayingFragment.this.requireActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
        return inflate;
    }

    private void initViews(View view) {
        this.fabExit = (FloatingActionButton) view.findViewById(R.id.add_song_exit_btn);
        this.bigPic = (ImageView) view.findViewById(R.id.song_playing_pic);
        this.titleTv = (TextView) view.findViewById(R.id.song_playing_title);
        this.authorTv = (TextView) view.findViewById(R.id.song_playing_author);
    }
}