package com.example.iceman.mp3player.models;

/**
 * Created by IceMan on 11/8/2016.
 */

public class ItemListMain {

    private int iconId;
    private String title;
    private int songNumbers;

    public ItemListMain(int iconId, String title, int songNumbers) {
        this.iconId = iconId;
        this.title = title;
        this.songNumbers = songNumbers;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSongNumbers() {
        return songNumbers;
    }

    public void setSongNumbers(int songNumbers) {
        this.songNumbers = songNumbers;
    }
}
