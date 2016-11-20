package com.example.iceman.mp3player.models;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by iceman on 10/11/2016.
 */

public class Album implements Serializable {

    private int id;
    private String title;
    private String artist;
    ArrayList<Song> lstSong;
    Bitmap albumArt;
    String albumArtPath;

    public Album(int id, String title, String artist, String albumArtPath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.albumArtPath = albumArtPath;
    }

    public Album(int id, String title, String artist, ArrayList<Song> lstSong, Bitmap albumArt) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.lstSong = lstSong;
        this.albumArt = albumArt;
    }

    public String getAlbumArtPath() {
        return albumArtPath;
    }

    public void setAlbumArtPath(String albumArtPath) {
        this.albumArtPath = albumArtPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public ArrayList<Song> getLstSong() {
        return lstSong;
    }

    public void setLstSong(ArrayList<Song> lstSong) {
        this.lstSong = lstSong;
    }

    public Bitmap getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(Bitmap albumArt) {
        this.albumArt = albumArt;
    }
}
