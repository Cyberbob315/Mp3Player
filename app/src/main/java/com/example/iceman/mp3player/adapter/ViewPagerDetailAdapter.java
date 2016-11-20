package com.example.iceman.mp3player.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.iceman.mp3player.fragments.FragmentAlbum;
import com.example.iceman.mp3player.fragments.FragmentArtist;
import com.example.iceman.mp3player.fragments.FragmentSongList;

/**
 * Created by IceMan on 11/8/2016.
 */

public class ViewPagerDetailAdapter extends FragmentStatePagerAdapter {

    public ViewPagerDetailAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new FragmentSongList();
                break;
            case 1:
                fragment = new FragmentAlbum();
                break;
            case 2:
                fragment = new FragmentArtist();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

}
