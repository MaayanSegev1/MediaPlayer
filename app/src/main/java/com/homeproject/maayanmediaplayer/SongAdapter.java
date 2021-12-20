package com.homeproject.maayanmediaplayer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private final List<Song> songsList;
    private MySongListener listener;
    private Context packageContext;



    interface MySongListener {
        void onSongClicked(int position, View view);
        void onSongLongClicked(int position, View view);
    }



    public void setListener(MySongListener listener) {
        this.listener = listener;
    }

    public SongAdapter(List<Song> songsList, Context packageContext) {
        this.songsList = songsList;
        this.packageContext = packageContext;
    }


    public class SongViewHolder extends RecyclerView.ViewHolder {

        TextView nameSong;
        ImageView imageSong;

        public SongViewHolder(@NonNull View itemView, Context packageContext) {
            super(itemView);

            nameSong = itemView.findViewById(R.id.songTextName);
            imageSong = itemView.findViewById(R.id.image_song);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null) {
                        Log.d("maayan", "lisiner on..");
                        listener.onSongClicked(getAdapterPosition(), v);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });
        }
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item, parent, false);
        SongViewHolder songViewHolder = new SongViewHolder(view, this.packageContext);
        return songViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songsList.get(position);
        holder.nameSong.setText(song.getName());
        holder.imageSong.setImageResource(song.getSongID());
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


}
