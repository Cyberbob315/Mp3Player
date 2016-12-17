package com.example.iceman.mp3player.utils;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.example.iceman.mp3player.R;
import com.example.iceman.mp3player.models.Album;
import com.example.iceman.mp3player.models.Artist;
import com.example.iceman.mp3player.models.Song;

import java.util.ArrayList;

/**
 * Created by IceMan on 11/8/2016.
 */

public class AppController extends Application {

    private static AppController mInstance;

    private Service playMusicService;
    private Activity playMusicActivity;
    private Activity mainActivity;
    private ArrayList<Song> lstSong;
    private ArrayList<Album> lstAlbum;
    private ArrayList<Artist> lstArtist;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public Activity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public Service getPlayMusicService() {
        return playMusicService;
    }

    public void setPlayMusicService(Service playMusicService) {
        this.playMusicService = playMusicService;
    }

    public Activity getPlayMusicActivity() {
        return playMusicActivity;
    }

    public void setPlayMusicActivity(Activity playMusicActivity) {
        this.playMusicActivity = playMusicActivity;
    }

    public ArrayList<Song> getLstSong() {
        return lstSong;
    }

    public ArrayList<Album> getLstAlbum() {
        return lstAlbum;
    }

    public ArrayList<Artist> getLstArtist() {
        return lstArtist;
    }

    public static AppController getInstance() {
        return mInstance;
    }

    public ArrayList<Song> getListSong() {
        ArrayList<Song> lstSong = new ArrayList<>();

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID}
                , null, null, MediaStore.Audio.Media.TITLE + " ASC");

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
        this.lstSong = lstSong;
        return lstSong;
    }

    public ArrayList<Album> getListAlbum() {
        ArrayList<Album> lstAlbum = new ArrayList<>();

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums._ID,
                        MediaStore.Audio.Albums.ARTIST}, null, null, MediaStore.Audio.Albums.ALBUM + " ASC");

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
        this.lstAlbum = lstAlbum;
        return lstAlbum;
    }

    public ArrayList<Artist> getListArtist() {
        ArrayList<Artist> lstArtist = new ArrayList<>();

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Artists._ID}, null, null, MediaStore.Audio.Artists.ARTIST + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists._ID));
//                ArrayList<Song> lstSong = getListSongOfArtist(id);

                Artist item = new Artist(id, title);
                lstArtist.add(item);
            } while (cursor.moveToNext());
        }

        this.lstArtist = lstArtist;
        return lstArtist;
    }

    public ArrayList<Song> getListSongOfAlbum(int albumId) {
        ArrayList<Song> lstSong = new ArrayList<>();

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID},
                MediaStore.Audio.Media.ALBUM_ID + "=?", new String[]{String.valueOf(albumId)}, MediaStore.Audio.Media.TITLE + " ASC");

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
                        MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID},
                MediaStore.Audio.Media.ARTIST_ID + "=?", new String[]{String.valueOf(artistId)}, MediaStore.Audio.Media.TITLE + " ASC");

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

    public void setDefaultWallpaper(ImageView imgBackGround) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bgm);
        bitmap = BlurBuilder.blur(this, bitmap);
        imgBackGround.setImageBitmap(bitmap);
    }

}
