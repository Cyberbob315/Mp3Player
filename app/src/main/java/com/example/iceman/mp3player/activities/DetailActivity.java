package com.example.iceman.mp3player.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.iceman.mp3player.R;
import com.example.iceman.mp3player.adapter.MainAdapter;
import com.example.iceman.mp3player.adapter.ViewPagerDetailAdapter;
import com.example.iceman.mp3player.utils.AppController;
import com.example.iceman.mp3player.utils.Common;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager mViewPager;
    ViewPagerDetailAdapter mVPAdapter;
    ImageView imgBackGround;
    TabLayout tabLayoutDetail;
    String idType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initControls();
        Common.setStatusBarTranslucent(true,this);
        initEvents();
        getType();
        AppController.getInstance().setDefaultWallpaper(imgBackGround);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_search_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    private void getType() {
        Intent intent = getIntent();
        idType = intent.getStringExtra(MainAdapter.KEY_MAIN);

        if (idType.equals(getString(R.string.list_song))) {
            mViewPager.setCurrentItem(0);
        } else if (idType.equals(getString(R.string.album_list))) {
            mViewPager.setCurrentItem(1);
        } else if (idType.equals(getString(R.string.artist_list))) {
            mViewPager.setCurrentItem(2);
        }
    }

    private void initEvents() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initControls() {
        imgBackGround = (ImageView) findViewById(R.id.img_back_ground_detail);


        tabLayoutDetail = (TabLayout) findViewById(R.id.tablayout_detail);
        mViewPager = (ViewPager) findViewById(R.id.view_pager_detail);
        tabLayoutDetail.setupWithViewPager(mViewPager);
        mVPAdapter = new ViewPagerDetailAdapter(getSupportFragmentManager(), this);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mVPAdapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
