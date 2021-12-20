package com.homeproject.maayanmediaplayer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.Notification;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;
import java.util.ArrayList;


public class SongsService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    MediaPlayer mediaPlayer = new MediaPlayer();
    ArrayList<Song> mediaSongList;
    int currentPlaying = 0;
    final int NOTIFICATION_ID = 1;
    private NotificationCompat.Builder builder;
    private NotificationManager mNotificationManager;
    private Notification notification;
    private RemoteViews notificationLayout;
    private int imageSong;
    private String songPic;
    private String songName;
    private String channelName = "Music channel";
    private static final String NOTIFICATION_CHANNEL_ID = "maayan_media_player";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        createMediaPlayer();

        notificationLayout = new RemoteViews(getPackageName(), R.layout.custom_notification_layout);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle(channelName)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = channelName;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            mNotificationManager = getSystemService(NotificationManager.class);
            mNotificationManager.createNotificationChannel(channel);

            Intent playIntent = new Intent(this, SongsService.class);
            playIntent.putExtra("command", "play");
            notificationLayout.setOnClickPendingIntent(R.id.play_pause_btn, PendingIntent.getService(this, 0, playIntent, 134217728));
            Intent pauseIntent = new Intent(this, SongsService.class);
            pauseIntent.putExtra("command", "pause");
            notificationLayout.setOnClickPendingIntent(R.id.play_pause_btn, PendingIntent.getService(this, 1, pauseIntent, 134217728));
            Intent nextIntent = new Intent(this, SongsService.class);
            nextIntent.putExtra("command", "next");
            notificationLayout.setOnClickPendingIntent(R.id.next_btn, PendingIntent.getService(this, 2, nextIntent, 134217728));
            Intent prevIntent = new Intent(this, SongsService.class);
            prevIntent.putExtra("command", "prev");
            notificationLayout.setOnClickPendingIntent(R.id.previous_btn, PendingIntent.getService(this, 3, prevIntent, 134217728));
            Intent closeIntent = new Intent(this, SongsService.class);
            closeIntent.putExtra("command", "close");
            notificationLayout.setOnClickPendingIntent(R.id.exit_btn_notif, PendingIntent.getService(this, 4, closeIntent, 134217728));
            builder.setCustomBigContentView(notificationLayout);
            builder.setSmallIcon(17301540);
            startForeground(1, builder.build());

        }


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String command = intent.getStringExtra("command");

        switch (command) {
            case "play_song_by_position":
                if (mediaSongList == null) {
                    mediaSongList = FileHandler.readFromFile(this);
                }
                int intExtra = intent.getIntExtra("position", 0);
                currentPlaying = intExtra;

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }

                try {
                    mediaPlayer.setDataSource(mediaSongList.get(currentPlaying).getURL());
                    mediaPlayer.prepare();//prepareAsync();
                    mediaPlayer.start();

                    songName = mediaSongList.get(currentPlaying).getName();
                    imageSong = mediaSongList.get(currentPlaying).getSongID();

                    this.notificationLayout.setTextViewText(R.id.title_txt_notif, songName);
                    this.notificationLayout.setImageViewResource(R.id.pic_notif, imageSong); //this sold be set the image song on notification
                    builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                            .setSmallIcon(android.R.drawable.ic_media_play)
                            .setContentTitle(channelName)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                            .setCustomContentView(notificationLayout);
                    mNotificationManager.notify(NOTIFICATION_ID, builder.build());

                } catch (Exception e) {
                    Log.e("a", "a", e);
                }
                break;
            case "play":
                if (!mediaPlayer.isPlaying())
                    mediaPlayer.start();
                break;
            case "next":
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                startSong(true);
                break;
            case "prev":
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                startSong(false);
                break;
            case "pause":
                if (mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                break;
            case "close":
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startSong(boolean isNext) {
        if (isNext) {
            this.currentPlaying++;
            if (currentPlaying == mediaSongList.size())
                this.currentPlaying = 0;
        } else if (isNext == false) {
            this.currentPlaying--;
            if (currentPlaying < 0)
                this.currentPlaying = mediaSongList.size() - 1;
        }
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(mediaSongList.get(currentPlaying).getURL());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        songName = mediaSongList.get(currentPlaying).getName();
        imageSong = mediaSongList.get(currentPlaying).getSongID();
        this.notificationLayout.setTextViewText(R.id.title_txt_notif, songName);
        this.notificationLayout.setImageViewResource(R.id.pic_notif, imageSong);
        mNotificationManager.notify(1, builder.build());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        startSong(true);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MediaPlayer mMediaPlayer = this.mediaPlayer;
        if (mMediaPlayer != null)
            if (mMediaPlayer.isPlaying())
                mMediaPlayer.stop();
        mMediaPlayer.release();
    }

    private void buildCustomIntents() {
        Intent intent = new Intent(this, SongsService.class);
        intent.putExtra("command", "play/pause");
        this.notificationLayout.setOnClickPendingIntent(R.id.play_pause_btn, PendingIntent.getService(this, 0, intent, 134217728));
        Intent intent2 = new Intent(this, SongsService.class);
        intent2.putExtra("command", "previous");
        intent2.putExtra("songArray", this.mediaSongList);
        this.notificationLayout.setOnClickPendingIntent(R.id.previous_btn, PendingIntent.getService(this, 2, intent2, 134217728));
        Intent intent3 = new Intent(this, SongsService.class);
        intent3.putExtra("command", "next");
        intent3.putExtra("songArray", this.mediaSongList);
        this.notificationLayout.setOnClickPendingIntent(R.id.next_btn, PendingIntent.getService(this, 3, intent3, 134217728));
        Intent intent4 = new Intent(this, SongsService.class);
        intent4.putExtra("command", "stop");
        this.notificationLayout.setOnClickPendingIntent(R.id.exit_btn_notif, PendingIntent.getService(this, 4, intent4, 134217728));
    }


    private void createMediaPlayer() {
        MediaPlayer mMediaPlayer = this.mediaPlayer;
        if (mMediaPlayer == null) {
            MediaPlayer mMediaPlayer2 = new MediaPlayer();
            this.mediaPlayer = mMediaPlayer2;
            mMediaPlayer2.setOnPreparedListener(this);
            this.mediaPlayer.setOnCompletionListener(this);
            return;
        }
        mMediaPlayer.reset();
    }
}
     