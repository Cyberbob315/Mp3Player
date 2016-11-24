package com.example.iceman.mp3player.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.iceman.mp3player.R;
import com.example.iceman.mp3player.activities.PlayMusicActivity;
import com.example.iceman.mp3player.adapter.SongListAdapter;
import com.example.iceman.mp3player.models.Song;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by IceMan on 11/15/2016.
 */

public class PlayMusicService extends Service {

//    private static PlayMusicService musicServiceInstance;

    private static MediaPlayer mediaPlayer;
    private int currentLength;
    private LocalBinder localBinder = new LocalBinder();
    private ArrayList<Song> mData;
    private ArrayList<Song> mDataShuffle;
    private int currentPos;
    private int currentPosShuffle;
    private Song item;
    private String albumArtPath;
    private boolean isShuffle;

    public class LocalBinder extends Binder {

        public PlayMusicService getInstantBoundService() {
            return PlayMusicService.this;
        }

    }

    public void setDataForNotification(ArrayList<Song> lstSong, int currentPos, ArrayList<Song> lstSongShuffle
            , boolean isShuffle, Song item, String albumArtPath){
        this.mData = lstSong;
        this.mDataShuffle = lstSongShuffle;
        this.currentPos = currentPos;
        this.isShuffle = isShuffle;
        this.item = item;
        this.albumArtPath = albumArtPath;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);
        Toast.makeText(this, "Task removed", Toast.LENGTH_SHORT).show();
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("LocalService", "3-onStartCommand");
        Log.e("LocalService", "Received start id " + startId + ": " + intent);
        showNotification();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    private Notification showNotification() {

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notfication);
        Intent intent = new Intent(getApplicationContext(), PlayMusicActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(SongListAdapter.SONG_PATH, item.getPath());
        intent.putExtra(SongListAdapter.SONG_POS, currentPos);
        intent.putExtra(SongListAdapter.LIST_SONG, mData);
        intent.putExtra(PlayMusicActivity.IS_PlAYING, true);
        intent.putExtra(PlayMusicActivity.LIST_SONG_SHUFFLE, mDataShuffle);
        intent.putExtra(PlayMusicActivity.IS_SHUFFLE, isShuffle);


        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        Intent intentPrev = new Intent(PlayMusicActivity.ACTION_PREV);
        intentPrev.putExtra(SongListAdapter.SONG_PATH, item.getPath());
        intentPrev.putExtra(SongListAdapter.SONG_POS, currentPos);
        intentPrev.putExtra(SongListAdapter.LIST_SONG, mData);
        intentPrev.putExtra(PlayMusicActivity.IS_PlAYING, true);
        intentPrev.putExtra(PlayMusicActivity.LIST_SONG_SHUFFLE, mDataShuffle);
        intentPrev.putExtra(PlayMusicActivity.IS_SHUFFLE, isShuffle);
        PendingIntent pendingIntentPrev = PendingIntent.getBroadcast(getApplicationContext(), 0, intentPrev, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        Intent intentPlayPause = new Intent(PlayMusicActivity.ACTION_PLAY_PAUSE);
        intentPlayPause.putExtra(SongListAdapter.SONG_PATH, item.getPath());
        intentPlayPause.putExtra(SongListAdapter.SONG_POS, currentPos);
        intentPlayPause.putExtra(SongListAdapter.LIST_SONG, mData);
        intentPlayPause.putExtra(PlayMusicActivity.IS_PlAYING, true);
        intentPlayPause.putExtra(PlayMusicActivity.LIST_SONG_SHUFFLE, mDataShuffle);
        intentPlayPause.putExtra(PlayMusicActivity.IS_SHUFFLE, isShuffle);
        PendingIntent pendingIntentPlayPause = PendingIntent.getBroadcast(getApplicationContext(), 0, intentPlayPause, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        Intent intentNext = new Intent(PlayMusicActivity.ACTION_NEXT);
        intentNext.putExtra(SongListAdapter.SONG_PATH, item.getPath());
        intentNext.putExtra(SongListAdapter.SONG_POS, currentPos);
        intentNext.putExtra(SongListAdapter.LIST_SONG, mData);
        intentNext.putExtra(PlayMusicActivity.IS_PlAYING, true);
        intentNext.putExtra(PlayMusicActivity.LIST_SONG_SHUFFLE, mDataShuffle);
        intentNext.putExtra(PlayMusicActivity.IS_SHUFFLE, isShuffle);
        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(getApplicationContext(), 0, intentNext, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContent(remoteViews);

        remoteViews.setTextViewText(R.id.tv_song_title_noti, item.getTitle());
        remoteViews.setTextViewText(R.id.tv_artist_noti, item.getArtist());
        if (albumArtPath != null && !albumArtPath.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(albumArtPath);
            remoteViews.setImageViewBitmap(R.id.img_album_art_noti, bitmap);
        } else {
            remoteViews.setImageViewResource(R.id.img_album_art_noti, R.mipmap.ic_launcher);
        }

        builder.setAutoCancel(false);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingIntent);
        Notification n = builder.build();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, n);
        startForeground(1337, n);
        return n;
    }

    private void releaseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            stopMusic();
            mediaPlayer.release();
        }
    }

    public void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void resumeMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            //mediaPlayer.seekTo(getCurrentLength());
            mediaPlayer.start();
        }
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void playMusic(String path) {
        releaseMusic();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent intent = new Intent(PlayMusicActivity.ACTION_COMPLETE_SONG);
                sendBroadcast(intent);
            }
        });
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    public void stopMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public int getTotalTime() {
        return mediaPlayer.getDuration() / 1000;
    }

    public int getCurrentLength() {
        return mediaPlayer.getCurrentPosition() / 1000;
    }

    public void seekTo(int seconds) {
        mediaPlayer.seekTo(seconds * 1000);
    }

    public void setCurrentLength(int currentLength) {
        this.currentLength = currentLength;

    }


}