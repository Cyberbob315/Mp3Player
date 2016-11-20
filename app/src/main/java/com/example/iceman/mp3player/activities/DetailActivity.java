package com.example.iceman.mp3player.activities;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.iceman.mp3player.R;
import com.example.iceman.mp3player.adapter.MainAdapter;
import com.example.iceman.mp3player.adapter.ViewPagerDetailAdapter;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mTvSong;
    TextView mTvAlbum;
    TextView mTvArtist;
    ViewPager mViewPager;
    ViewPagerDetailAdapter mVPAdapter;

    String idType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initControls();
        initEvents();
        getType();
    }

    private void getType() {
        Intent intent = getIntent();
        idType = intent.getStringExtra(MainAdapter.KEY_MAIN);

        if (idType.equals(getString(R.string.list_song))) {
            mTvSong.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.usui_grey));
            mViewPager.setCurrentItem(0);
        } else if (idType.equals(getString(R.string.album_list))) {
            mTvAlbum.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.usui_grey));
            mViewPager.setCurrentItem(1);
        } else if (idType.equals(getString(R.string.artist_list))) {
            mTvArtist.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.usui_grey));
            mViewPager.setCurrentItem(2);
        }
    }

    private void initEvents() {
        mTvArtist.setOnClickListener(this);
        mTvSong.setOnClickListener(this);
        mTvAlbum.setOnClickListener(this);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mTvSong.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.usui_grey));
                        mTvAlbum.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.black));
                        mTvArtist.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.black));
                        break;
                    case 1:
                        mTvSong.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.black));
                        mTvAlbum.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.usui_grey));
                        mTvArtist.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.black));
                        break;
                    case 2:
                        mTvSong.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.black));
                        mTvAlbum.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.black));
                        mTvArtist.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.usui_grey));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initControls() {
        mTvSong = (TextView) findViewById(R.id.tv_song_detail);
        mTvAlbum = (TextView) findViewById(R.id.tv_album_detail);
        mTvArtist = (TextView) findViewById(R.id.tv_artist_detail);

        mViewPager = (ViewPager) findViewById(R.id.view_pager_detail);
        mVPAdapter = new ViewPagerDetailAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mVPAdapter);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_song_detail:
                mTvSong.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.usui_grey));
                mTvAlbum.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.black));
                mTvArtist.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.black));
                mViewPager.setCurrentItem(0);
                break;
            case R.id.tv_album_detail:
                mTvSong.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.black));
                mTvAlbum.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.usui_grey));
                mTvArtist.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.black));
                mViewPager.setCurrentItem(1);
                break;
            case R.id.tv_artist_detail:
                mTvSong.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.black));
                mTvAlbum.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.black));
                mTvArtist.setBackgroundColor(ContextCompat.getColor(DetailActivity.this, R.color.usui_grey));
                mViewPager.setCurrentItem(2);
                break;
        }
    }
}
