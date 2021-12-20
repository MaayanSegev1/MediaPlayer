package com.homeproject.maayanmediaplayer;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.File;

/* loaded from: classes.dex */
public class SongAdditionFragment extends Fragment {
    private MaterialButton addSongBtn;
    private EditText authorEt;
    songAdditionListener callback;
    private FloatingActionButton camera;
    ActivityResultLauncher<Uri> cameraResultLauncher;
    SharedPreferences.Editor editor;
    private FloatingActionButton exitBtn;
    private FloatingActionButton gallery;
    ActivityResultLauncher<String> galleryResultLauncher;
    private EditText linkEt;
    ActivityResultLauncher<String> permissionsLauncher;
    private File picFile;
    private ImageView picImageView;
    private Uri picUri;
    SharedPreferences sp;
    private EditText titleEt;
    private boolean isFromCamera = false;
    private boolean isFromGallery = false;
    private boolean picAssigned = false;

    /* loaded from: classes.dex */
    interface songAdditionListener {
        void onAddSongClicked(Song song);
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initLaunchers();
        SharedPreferences preferences = requireActivity().getPreferences(0);
        this.sp = preferences;
        this.editor = preferences.edit();
        ActionBar supportActionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.callback = (songAdditionListener) context;
        } catch (ClassCastException unused) {
            throw new ClassCastException("Must implement songAdditionListener interface!");
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.song_addition_fragment_layout, viewGroup, false);
        initViews(inflate);
        initListeners();
        return inflate;
    }

    private void initListeners() {
        this.exitBtn.setOnClickListener(new View.OnClickListener() { // from class: il.co.hit.mediaplayer.SongAdditionFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SongAdditionFragment.this.requireActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    SongAdditionFragment.this.requireActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
        this.gallery.setOnClickListener(new View.OnClickListener() { // from class: il.co.hit.mediaplayer.SongAdditionFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SongAdditionFragment.this.galleryResultLauncher.launch("image/*");
            }
        });
        this.camera.setOnClickListener(new View.OnClickListener() { // from class: il.co.hit.mediaplayer.SongAdditionFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(SongAdditionFragment.this.requireContext(), "android.permission.CAMERA") != 0) {
                    SongAdditionFragment.this.permissionsLauncher.launch("android.permission.CAMERA");
                    return;
                }
                int i = SongAdditionFragment.this.sp.getInt("fileIndex", 1);
                SongAdditionFragment.this.picFile = new File(SongAdditionFragment.this.requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo" + i + ".jpg");
                SongAdditionFragment songAdditionFragment = SongAdditionFragment.this;
                songAdditionFragment.picUri = FileProvider.getUriForFile(songAdditionFragment.requireContext(), SongAdditionFragment.this.requireContext().getApplicationContext().getPackageName() + ".provider", SongAdditionFragment.this.picFile);
                SongAdditionFragment.this.editor.putInt("fileIndex", i + 1).commit();
                SongAdditionFragment.this.cameraResultLauncher.launch(SongAdditionFragment.this.picUri);
            }
        });
        this.addSongBtn.setOnClickListener(new View.OnClickListener() { // from class: il.co.hit.mediaplayer.SongAdditionFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Song song = new Song();
                String trim = SongAdditionFragment.this.titleEt.getText().toString().trim();
                String trim2 = SongAdditionFragment.this.authorEt.getText().toString().trim();
                String trim3 = SongAdditionFragment.this.linkEt.getText().toString().trim();

                if (SongAdditionFragment.this.detailsValidation(trim, trim2, trim3) && SongAdditionFragment.this.picAssigned) {
                    if (SongAdditionFragment.this.isFromCamera) {
                        song.setImagePath(SongAdditionFragment.this.picFile.getAbsolutePath());
                        SongAdditionFragment.this.isFromCamera = false;
                        SongAdditionFragment.this.picAssigned = true;
                    }
                    if (SongAdditionFragment.this.isFromGallery) {
                        song.setImagePath(SongAdditionFragment.this.picUri.toString());
                        SongAdditionFragment.this.isFromGallery = false;
                        SongAdditionFragment.this.picAssigned = true;
                    }
                    song.setName(trim);
                    song.setURL(trim3);
                    SongAdditionFragment.this.callback.onAddSongClicked(song);
                    if (SongAdditionFragment.this.requireActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        SongAdditionFragment.this.requireActivity().getSupportFragmentManager().popBackStack();
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean detailsValidation(String str, String str2, String str3) {
        return !str.equals("") && !str2.equals("") && !str3.equals("");
    }

    private void initViews(View view) {
        this.exitBtn = (FloatingActionButton) view.findViewById(R.id.add_song_exit_btn);
        this.camera = (FloatingActionButton) view.findViewById(R.id.add_song_fragment_camera);
        this.gallery = (FloatingActionButton) view.findViewById(R.id.add_song_fragment_gallery);
        this.picImageView = (ImageView) view.findViewById(R.id.add_song_pic);
        this.titleEt = (EditText) view.findViewById(R.id.add_song_name_edittext);
        this.authorEt = (EditText) view.findViewById(R.id.add_song_author_edittext);
        this.linkEt = (EditText) view.findViewById(R.id.add_song_link_edittext);
        this.addSongBtn = (MaterialButton) view.findViewById(R.id.add_song_material_button);
    }

    private void initLaunchers() {
        this.cameraResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() { // from class: il.co.hit.mediaplayer.SongAdditionFragment.5
            public void onActivityResult(Boolean bool) {
                if (bool.booleanValue()) {
                    SongAdditionFragment.this.picAssigned = true;
                    SongAdditionFragment.this.isFromCamera = true;
                    Glide.with(SongAdditionFragment.this.requireActivity()).load(SongAdditionFragment.this.picFile.getAbsoluteFile()).into(SongAdditionFragment.this.picImageView);
                }
            }
        });
        this.galleryResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() { // from class: il.co.hit.mediaplayer.SongAdditionFragment.6
            public void onActivityResult(Uri uri) {
                SongAdditionFragment.this.picAssigned = true;
                SongAdditionFragment.this.isFromGallery = true;
                SongAdditionFragment.this.picUri = uri;
                Glide.with(SongAdditionFragment.this.requireActivity()).load(uri).into(SongAdditionFragment.this.picImageView);
            }
        });
        this.permissionsLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() { // from class: il.co.hit.mediaplayer.SongAdditionFragment.7
            public void onActivityResult(Boolean bool) {
                String str;
                if (bool.booleanValue()) {
                    str = SongAdditionFragment.this.getResources().getString(R.string.permission_granted);
                } else {
                    str = SongAdditionFragment.this.getResources().getString(R.string.must_grant_permission);
                }
                Toast.makeText(SongAdditionFragment.this.getContext(), str + "", 0).show();
            }
        });
    }
}