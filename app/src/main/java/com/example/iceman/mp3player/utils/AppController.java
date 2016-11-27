package com.example.iceman.mp3player.utils;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.iceman.mp3player.R;
import com.example.iceman.mp3player.activities.PlayMusicActivity;
import com.example.iceman.mp3player.adapter.SongListAdapter;
import com.example.iceman.mp3player.models.Album;
import com.example.iceman.mp3player.models.Artist;
import com.example.iceman.mp3player.models.Song;

import java.util.ArrayList;

/**
 * Created by IceMan on 11/8/2016.
 */

public class AppController extends Application {

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static AppController getInstance() {
        return mInstance;
    }

    public ArrayList<Song> getListSong() {
        ArrayList<Song> lstSong = new ArrayList<>();

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID}
                , null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String songId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                int albumID = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String albumPath = getCoverArtPath(albumID);
                Song item = new Song(songId, title, album, artist, albumPath, duration, path);

                lstSong.add(item);

            } while (cursor.moveToNext());
        }
        return lstSong;
    }

    public ArrayList<Album> getListAlbum() {
        ArrayList<Album> lstAlbum = new ArrayList<>();

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums._ID,
                        MediaStore.Audio.Albums.ARTIST}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                String pathArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
//                ArrayList<Song> lstSongs = getListSongOfAlbum(id);

//                final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
//                Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, id);
//                Bitmap bitmap = null;
//                try {
//                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), albumArtUri);
//                } catch (IOException e) {
//                    e.printStackTrace();
////                    bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.);
//                }
                Album item = new Album(id, title, artist, pathArt);
                lstAlbum.add(item);
            } while (cursor.moveToNext());
        }
        return lstAlbum;
    }

    public ArrayList<Artist> getListArtist() {
        ArrayList<Artist> lstArtist = new ArrayList<>();

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Artists._ID}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists._ID));
//                ArrayList<Song> lstSong = getListSongOfArtist(id);

                Artist item = new Artist(id, title);
                lstArtist.add(item);
            } while (cursor.moveToNext());
        }

        return lstArtist;
    }

    public ArrayList<Song> getListSongOfAlbum(int albumId) {
        ArrayList<Song> lstSong = new ArrayList<>();

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID}, MediaStore.Audio.Media.ALBUM_ID + "=?", new String[]{String.valueOf(albumId)}, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String songId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                int albumID = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String albumPath = getCoverArtPath(albumID);
                Song item = new Song(songId, title, album, artist, albumPath, duration, path);
                lstSong.add(item);

            } while (cursor.moveToNext());
        }
        return lstSong;
    }


    public ArrayList<Song> getListSongOfArtist(int artistId) {
        ArrayList<Song> lstSong = new ArrayList<>();

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID}, MediaStore.Audio.Media.ARTIST_ID + "=?", new String[]{String.valueOf(artistId)}, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String songId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                int duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                int albumID = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String albumPath = getCoverArtPath(albumID);
                Song item = new Song(songId, title, album, artist, albumPath, duration, path);
                lstSong.add(item);

            } while (cursor.moveToNext());
        }
        return lstSong;
    }

    public String getCoverArtPath(long albumId) {
        Cursor albumCursor = getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + " = ?",
                new String[]{Long.toString(albumId)},
                null
        );
        boolean queryResult = albumCursor.moveToFirst();
        String result = null;
        if (queryResult) {
            result = albumCursor.getString(0);
        }
        albumCursor.close();
        return result;
    }

    public void showNotification(ArrayList<Song> lstSong, int currentPos, ArrayList<Song> lstSongShuffle
            , boolean isShuffle, Song itemCurrent, String albumArtPath) {
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


        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

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
        notificationManager.notify(0, builder.build());

        remoteViews.setOnClickPendingIntent(R.id.btn_prev_noti, pendingIntentPrev);
    }
}
