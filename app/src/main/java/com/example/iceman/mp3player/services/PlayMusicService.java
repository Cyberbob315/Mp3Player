package com.example.iceman.mp3player.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.iceman.mp3player.R;
import com.example.iceman.mp3player.activities.PlayMusicActivity;

import java.io.IOException;

/**
 * Created by IceMan on 11/15/2016.
 */

public class PlayMusicService extends Service {

//    private static PlayMusicService musicServiceInstance;

    private static MediaPlayer mediaPlayer;

    private int currentLength;

    private LocalBinder localBinder = new LocalBinder();

    private boolean isRepeat;

    public class LocalBinder extends Binder {

        public PlayMusicService getInstantBoundService() {
            return PlayMusicService.this;
        }

    }

//    public static PlayMusicService getInstance() {
//        if (musicServiceInstance == null) {
//            musicServiceInstance = new PlayMusicService();
//        }
//        return musicServiceInstance;
//    }


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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("BothService running indefinitely")
                .setSmallIcon(R.mipmap.ic_launcher);
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
            mediaPlayer.setLooping(isRepeat);
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

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }

    public boolean isRepeat() {
        return isRepeat;
    }
}
