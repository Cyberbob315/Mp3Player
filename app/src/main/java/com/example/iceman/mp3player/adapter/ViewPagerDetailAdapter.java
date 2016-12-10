package com.example.iceman.mp3player.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.iceman.mp3player.R;
import com.example.iceman.mp3player.fragments.FragmentAlbum;
import com.example.iceman.mp3player.fragments.FragmentArtist;
import com.example.iceman.mp3player.fragments.FragmentSongList;

/**
 * Created by IceMan on 11/8/2016.
 */

public class ViewPagerDetailAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    public ViewPagerDetailAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
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

    @Override
    public CharSequence getPageTitle(int position) {
        String pageTitle = mContext.getString(R.string.list_song);
        switch (position) {
            case 0:
                pageTitle = mContext.getString(R.string.list_song);
                break;
            case 1:
                pageTitle = mContext.getString(R.string.album_list);
                break;
            case 2:
                pageTitle = mContext.getString(R.string.artist_list);
                break;
            default:
                pageTitle = mContext.getString(R.string.list_song);
                break;
        }

        return pageTitle;
    }
}
