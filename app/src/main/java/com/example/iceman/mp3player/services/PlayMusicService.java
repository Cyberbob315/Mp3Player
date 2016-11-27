package com.example.iceman.mp3player.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.iceman.mp3player.R;
import com.example.iceman.mp3player.activities.PlayMusicActivity;
import com.example.iceman.mp3player.adapter.SongListAdapter;
import com.example.iceman.mp3player.models.Song;
import com.example.iceman.mp3player.utils.Constants;

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

    private boolean isRepeat;

    ArrayList<Song> lstSong;
    ArrayList<Song> lstSongShuffle;
    boolean isShuffle;
    int currentPos;
    String albumArtPath;
    Song itemCurrent;


    public class LocalBinder extends Binder {

        public PlayMusicService getInstantBoundService() {
            return PlayMusicService.this;
        }

    }

    public void setDataForNotification(ArrayList<Song> lstSong, ArrayList<Song> lstSongShuffle, boolean isShuffle, int currentPos,
                                       Song itemCurrent, String albumArtPath) {
        this.lstSong = lstSong;
        this.lstSongShuffle = lstSongShuffle;
        this.isShuffle = isShuffle;
        this.currentPos = currentPos;
        this.albumArtPath = albumArtPath;
        this.itemCurrent = itemCurrent;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showNotification();
        return START_NOT_STICKY;
    }

    private void showNotification() {

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notfication);
        Intent intent = new Intent(getApplicationContext(), PlayMusicActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(SongListAdapter.SONG_PATH, itemCurrent.getPath());
        intent.putExtra(SongListAdapter.SONG_POS, currentPos);
        intent.putExtra(SongListAdapter.LIST_SONG, lstSong);
        intent.putExtra(PlayMusicActivity.IS_PlAYING, true);
        intent.putExtra(PlayMusicActivity.LIST_SONG_SHUFFLE, lstSongShuffle);
        intent.putExtra(PlayMusicActivity.IS_SHUFFLE, isShuffle);


        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent intentPrev = new Intent(Constants.ACTION_PREV);
        intentPrev.putExtra(SongListAdapter.SONG_PATH, itemCurrent.getPath());
        intentPrev.putExtra(SongListAdapter.SONG_POS, currentPos);
        intentPrev.putExtra(SongListAdapter.LIST_SONG, lstSong);
        intentPrev.putExtra(PlayMusicActivity.IS_PlAYING, true);
        intentPrev.putExtra(PlayMusicActivity.LIST_SONG_SHUFFLE, lstSongShuffle);
        intentPrev.putExtra(PlayMusicActivity.IS_SHUFFLE, isShuffle);
        PendingIntent pendingIntentPrev = PendingIntent.getBroadcast(getApplicationContext(), 0, intentPrev, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        Intent intentPlayPause = new Intent(Constants.ACTION_PLAY_PAUSE);
        intentPlayPause.putExtra(SongListAdapter.SONG_PATH, itemCurrent.getPath());
        intentPlayPause.putExtra(SongListAdapter.SONG_POS, currentPos);
        intentPlayPause.putExtra(SongListAdapter.LIST_SONG, lstSong);
        intentPlayPause.putExtra(PlayMusicActivity.IS_PlAYING, true);
        intentPlayPause.putExtra(PlayMusicActivity.LIST_SONG_SHUFFLE, lstSongShuffle);
        intentPlayPause.putExtra(PlayMusicActivity.IS_SHUFFLE, isShuffle);
        PendingIntent pendingIntentPlayPause = PendingIntent.getBroadcast(getApplicationContext(), 0, intentPlayPause, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        Intent intentNext = new Intent(Constants.ACTION_NEXT);
        intentNext.putExtra(SongListAdapter.SONG_PATH, itemCurrent.getPath());
        intentNext.putExtra(SongListAdapter.SONG_POS, currentPos);
        intentNext.putExtra(SongListAdapter.LIST_SONG, lstSong);
        intentNext.putExtra(PlayMusicActivity.IS_PlAYING, true);
        intentNext.putExtra(PlayMusicActivity.LIST_SONG_SHUFFLE, lstSongShuffle);
        intentNext.putExtra(PlayMusicActivity.IS_SHUFFLE, isShuffle);
        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(getApplicationContext(), 0, intentNext, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setAutoCancel(false);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingIntent);
        builder.setContent(remoteViews);

        remoteViews.setTextViewText(R.id.tv_song_title_noti, itemCurrent.getTitle());
        remoteViews.setTextViewText(R.id.tv_artist_noti, itemCurrent.getArtist());

        if (albumArtPath != null && !albumArtPath.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(albumArtPath);
            remoteViews.setImageViewBitmap(R.id.img_album_art_noti, bitmap);
        } else {
            remoteViews.setImageViewResource(R.id.img_album_art_noti, R.mipmap.ic_launcher);
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification n = builder.build();
        notificationManager.notify(1, n);

        remoteViews.setOnClickPendingIntent(R.id.btn_prev_noti, pendingIntentPrev);

        //startForeground(1337, n);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

//    @Override
//    public boolean onUnbind(Intent intent) {
//        mediaPlayer.stop();
//        mediaPlayer.release();
//        return false;
//    }

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
                Intent intent = new Intent(Constants.ACTION_COMPLETE_SONG);
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
