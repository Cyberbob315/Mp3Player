package com.example.iceman.mp3player.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iceman.mp3player.R;
import com.example.iceman.mp3player.adapter.SongListAdapter;
import com.example.iceman.mp3player.adapter.SongListPlayingAdapter;
import com.example.iceman.mp3player.adapter.ViewPagerPlayAdapter;
import com.example.iceman.mp3player.fragments.FragmentPlay;
import com.example.iceman.mp3player.models.Song;
import com.example.iceman.mp3player.services.PlayMusicService;
import com.example.iceman.mp3player.utils.AppController;
import com.example.iceman.mp3player.utils.BlurBuilder;
import com.example.iceman.mp3player.utils.Constants;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

public class PlayMusicActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String IS_PlAYING = "is_playing";
    public static final String LIST_SONG_SHUFFLE = "list_song_shuffle";
    public static final String IS_SHUFFLE = "is_shuffle";


    private SeekBar seekBar;
    PlayMusicService mPlayMusicService;
    String path;
    ImageView btnPlayPause;
    ImageView btnNext;
    ImageView btnPrev;
    ImageView btnShuffle;
    ImageView btnRepeat;
    TextView tvTimePlayed;
    TextView tvTotalTime;
    RelativeLayout layout;
    int totalTime;
    ArrayList<Song> mData;
    ArrayList<Song> mDataShuffle;
    int currentPos;
    int currentPosShuffle;
    boolean isShuffle = false;
    boolean isPlaying;
    TextView mTvSongName;
    TextView mTvArtist;
    ViewPager mViewPager;
    ViewPagerPlayAdapter mVpPlayAdapter;
    boolean isSeeking;
    ImageView btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_playing);
        setSupportActionBar(toolbar);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this,R.drawable.abc_ic_menu_moreoverflow_mtrl_alpha));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppController.getInstance().setPlayMusicActivity(this);
        mData = new ArrayList<>();
        getDataFromIntent();
        initControls();
        if (!isPlaying) {
            initPlayService();

        } else {
            mPlayMusicService = (PlayMusicService) AppController.getInstance().getPlayMusicService();
            updateSeekBar();
            totalTime = mPlayMusicService.getTotalTime();
            mPlayMusicService.showNotification();
            setName();
        }
        setStatusBarTranslucent(true);

        initEvents();
        registerBroadcastSongComplete();
        registerBroadcastSwitchSong();
        registerUnbindService();
        setAlbumArt();

        updateHomeActivity();

    }

    private void updateHomeActivity() {
        Intent intent = new Intent(Constants.ACTION_UPDATE_PlAY_STATUS);
        sendBroadcast(intent);
    }

    private void setAlbumArt() {
        Intent intent1 = new Intent(Constants.ACTION_CHANGE_ALBUM_ART);
        intent1.putExtra(FragmentPlay.KEY_ALBUM_PLAY, mData.get(currentPos).getAlbumImagePath());
        sendBroadcast(intent1);
        Bitmap bitmap;
        String albumPath;

        if (!isShuffle) {
            albumPath = mData.get(currentPos).getAlbumImagePath();

        } else {
            albumPath = mDataShuffle.get(currentPosShuffle).getAlbumImagePath();
        }
        if (albumPath != null) {
            bitmap = BitmapFactory.decodeFile(albumPath);
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_wallpaper);
        }
        bitmap = BlurBuilder.blur(this, bitmap);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        layout.setBackground(bitmapDrawable);
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    BroadcastReceiver broadcastReceiverSongCompleted = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            nextMusic();
            totalTime = mPlayMusicService.getTotalTime();
            updateSeekBar();
            updateHomeActivity();
            setAlbumArt();
        }
    };

    private void registerBroadcastSongComplete() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_COMPLETE_SONG);
        registerReceiver(broadcastReceiverSongCompleted, intentFilter);
    }

    private void unRegisterBroadcastSongComplete() {
        unregisterReceiver(broadcastReceiverSongCompleted);
    }


    BroadcastReceiver broadcastReceiverSwitchSong = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            currentPos = intent.getExtras().getInt(SongListPlayingAdapter.KEY_ID_SWITH);
            currentPosShuffle = getCurrentPosShuffle();
            path = mData.get(currentPos).getPath();
            playMusic();
            mPlayMusicService.setShowNotification(false);
            mPlayMusicService.showNotification();
            mPlayMusicService.setShowNotification(true);
        }
    };

    private void registerBroadcastSwitchSong() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_SWITCH_SONG);
        registerReceiver(broadcastReceiverSwitchSong, intentFilter);
    }

    private void unRegisterBroadcastSwitchSong() {
        unregisterReceiver(broadcastReceiverSwitchSong);
    }

    private void initEvents() {
        btnPlayPause.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnShuffle.setOnClickListener(this);
        btnRepeat.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvTimePlayed.setText(getTime(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlayMusicService.seekTo(seekBar.getProgress());
                if (!mPlayMusicService.isPlaying()) {
                    mPlayMusicService.resumeMusic();
                    btnPlayPause.setImageResource(R.drawable.pb_pause);
                }
                isSeeking = false;
                updateSeekBar();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        btnSearch.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        btnSearch.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
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

    private void initControls() {

        mTvSongName = (TextView) findViewById(R.id.tv_song_name_play);
        mTvArtist = (TextView) findViewById(R.id.tv_artist_play);

        layout = (RelativeLayout) findViewById(R.id.activity_play_music);
        seekBar = (SeekBar) findViewById(R.id.seek_bar_play);
        btnPlayPause = (ImageView) findViewById(R.id.btn_play_pause);
        btnPrev = (ImageView) findViewById(R.id.btn_prev);
        btnNext = (ImageView) findViewById(R.id.btn_next);
        btnShuffle = (ImageView) findViewById(R.id.btn_shuffle);
        btnRepeat = (ImageView) findViewById(R.id.btn_repeat);
        tvTotalTime = (TextView) findViewById(R.id.tv_time_left);
        tvTimePlayed = (TextView) findViewById(R.id.tv_time_played);
        btnSearch = (ImageView) findViewById(R.id.btn_search_playing);

        mViewPager = (ViewPager) findViewById(R.id.view_pager_play);
        mVpPlayAdapter = new ViewPagerPlayAdapter(getSupportFragmentManager(), mData);
        mViewPager.setAdapter(mVpPlayAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setCurrentItem(1);
        isSeeking = false;
    }

    private void playMusic() {
        mPlayMusicService.playMusic(path);
        if (isShuffle) {
            Song item = mDataShuffle.get(currentPosShuffle);
            mPlayMusicService.setDataForNotification(mData, mDataShuffle, isShuffle, currentPos, item, item.getAlbumImagePath());
        } else {
            Song item = mData.get(currentPos);
            mPlayMusicService.setDataForNotification(mData, mDataShuffle, isShuffle, currentPos, item, item.getAlbumImagePath());
        }
        Intent intent1 = new Intent(this, PlayMusicService.class);
        startService(intent1);
        setName();
        setAlbumArt();
        mPlayMusicService.setShowNotification(false);
        mPlayMusicService.showNotification();
        mPlayMusicService.setShowNotification(true);
        updateHomeActivity();
    }


    private static String getTime(int totalTimeInSec) {
        int min = totalTimeInSec / 60;
        int sec = totalTimeInSec - min * 60;
        NumberFormat formatter = new DecimalFormat("##");

        return formatter.format(min) + ":" + formatter.format(sec);
    }


    private void getDataFromIntent() {
        Intent intent = getIntent();
        isPlaying = intent.getExtras().getBoolean(IS_PlAYING);
        if (isPlaying) {
            path = intent.getExtras().getString(SongListAdapter.SONG_PATH);
            currentPos = intent.getExtras().getInt(SongListAdapter.SONG_POS);
            mData = (ArrayList<Song>) intent.getExtras().getSerializable(SongListAdapter.LIST_SONG);
            isShuffle = intent.getExtras().getBoolean(IS_SHUFFLE);
            mDataShuffle = (ArrayList<Song>) intent.getExtras().getSerializable(LIST_SONG_SHUFFLE);
            currentPosShuffle = getCurrentPosShuffle();
        } else {
            path = intent.getExtras().getString(SongListAdapter.SONG_PATH);
            currentPos = intent.getExtras().getInt(SongListAdapter.SONG_POS);
            mData = (ArrayList<Song>) intent.getExtras().getSerializable(SongListAdapter.LIST_SONG);
            mDataShuffle = (ArrayList<Song>) mData.clone();
            Collections.shuffle(mDataShuffle);
            currentPosShuffle = getCurrentPosShuffle();
        }


    }

    private void initPlayService() {
        Intent intent = new Intent(this, PlayMusicService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayMusicService.LocalBinder binder = (PlayMusicService.LocalBinder) service;
            mPlayMusicService = binder.getInstantBoundService();
            AppController.getInstance().setPlayMusicService(mPlayMusicService);
            mPlayMusicService.setRepeat(false);
            playMusic();
            updateSeekBar();
            totalTime = mPlayMusicService.getTotalTime();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("CHECK", "DISCONECTED");
        }
    };

    private void updateSeekBar() {
        seekBar.setMax(totalTime);
        int currentLength = mPlayMusicService.getCurrentLength();

        if (!isSeeking) {
            seekBar.setProgress(currentLength);
            tvTimePlayed.setText(getTime(currentLength));
        }
        tvTotalTime.setText(getTime(totalTime));
        Handler musicHandler = new Handler();
        musicHandler.post(new Runnable() {
            @Override
            public void run() {
                updateSeekBar();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                nextMusic();
                mPlayMusicService.setShowNotification(false);
                mPlayMusicService.showNotification();
                mPlayMusicService.setShowNotification(true);
                break;
            case R.id.btn_play_pause:
                playPauseMusic();
                mPlayMusicService.setShowNotification(false);
                mPlayMusicService.showNotification();
                mPlayMusicService.setShowNotification(true);
                break;
            case R.id.btn_prev:
                backMusic();
                mPlayMusicService.setShowNotification(false);
                mPlayMusicService.showNotification();
                mPlayMusicService.setShowNotification(true);
                break;
            case R.id.btn_shuffle:
                if (isShuffle) {
                    btnShuffle.setImageResource(R.drawable.ic_widget_shuffle_off);
                    isShuffle = false;
                } else {
                    btnShuffle.setImageResource(R.drawable.ic_widget_shuffle_on);
                    isShuffle = true;
                }

                break;
            case R.id.btn_repeat:
                if (mPlayMusicService.isRepeat()) {
                    btnRepeat.setImageResource(R.drawable.ic_widget_repeat_all);
                    mPlayMusicService.setRepeat(false);
                } else {
                    btnRepeat.setImageResource(R.drawable.ic_widget_repeat_one);
                    mPlayMusicService.setRepeat(true);
                }
                break;
            case R.id.btn_search_playing:
                break;
        }
    }

    public void playPauseMusic() {
        if (mPlayMusicService.isPlaying()) {
            btnPlayPause.setImageResource(R.drawable.pb_play);
            mPlayMusicService.pauseMusic();
        } else {
            btnPlayPause.setImageResource(R.drawable.pb_pause);
            mPlayMusicService.resumeMusic();
        }
        mPlayMusicService.changePlayPauseState();
        updateHomeActivity();
    }

    public void resumeMusic() {
        if (!mPlayMusicService.isPlaying()) {
            btnPlayPause.setImageResource(R.drawable.pb_pause);
            mPlayMusicService.resumeMusic();
            updateHomeActivity();
        }
    }

    public void pauseMusic() {
        if (mPlayMusicService.isPlaying()) {
            btnPlayPause.setImageResource(R.drawable.pb_play);
            mPlayMusicService.pauseMusic();
            updateHomeActivity();
        }
    }

    public void nextMusic() {
        if (isShuffle) {
            if (currentPosShuffle == mDataShuffle.size() - 1) {
                currentPosShuffle = 0;
            } else {
                currentPosShuffle++;
            }
            path = mDataShuffle.get(currentPosShuffle).getPath();
            currentPos = getCurrentPos();
        } else {
            if (currentPos == mData.size() - 1) {
                currentPos = 0;
            } else {
                currentPos++;
            }
            path = mData.get(currentPos).getPath();
            currentPosShuffle = getCurrentPosShuffle();
        }
        setAlbumArt();
        playMusic();

    }

    public void backMusic() {
        if (isShuffle) {
            if (currentPosShuffle == 0) {
                currentPosShuffle = mDataShuffle.size() - 1;
            } else {
                currentPosShuffle--;
            }
            path = mDataShuffle.get(currentPosShuffle).getPath();
            currentPos = getCurrentPos();
        } else {
            if (currentPos == 0) {
                currentPos = mData.size() - 1;
            } else {
                currentPos--;
            }
            path = mData.get(currentPos).getPath();
            currentPosShuffle = getCurrentPosShuffle();
        }
        setAlbumArt();
        playMusic();
    }

    private int getCurrentPosShuffle() {
        int pos = -1;
        for (int i = 0; i < mDataShuffle.size(); i++) {
            if (mDataShuffle.get(i).getId().equals(getCurrentSongId())) {
                pos = i;
            }
        }
        return pos;
    }

    private int getCurrentPos() {
        int pos = -1;
        for (int i = 0; i < mDataShuffle.size(); i++) {
            if (mDataShuffle.get(i).getId().equals(getCurrentSongId())) {
                return i;
            }
        }
        return pos;
    }

    private String getCurrentSongId() {
        if (isShuffle) {
            return mDataShuffle.get(currentPosShuffle).getId();
        } else {
            return mData.get(currentPos).getId();
        }
    }

    private void setName() {
        if (isShuffle) {
            mTvSongName.setText(mDataShuffle.get(currentPosShuffle).getTitle());
            mTvArtist.setText(mDataShuffle.get(currentPosShuffle).getArtist());
        } else {
            mTvSongName.setText(mData.get(currentPos).getTitle());
            mTvArtist.setText(mData.get(currentPos).getArtist());
        }
    }

    //    public class BroadcastPrevMusic extends BroadcastReceiver {
//        public BroadcastPrevMusic() {
//        }
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            backMusic();
//        }
//    }
    public static class BroadcastPrevMusic extends BroadcastReceiver {
        public BroadcastPrevMusic() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Back Music", Toast.LENGTH_SHORT).show();
        }
    }

    public void unBindMusicService() {
        stopService(new Intent(this, PlayMusicService.class));
        unbindService(serviceConnection);
        Log.d("CHECK", "UNBIND");
    }

    BroadcastReceiver unbindService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (serviceConnection != null) {
                unbindService(serviceConnection);
            }
        }
    };

    private void registerUnbindService() {
        if (unbindService != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("unbind");
            registerReceiver(unbindService, filter);
        }
    }

    private void unregisterUnbindService() {
        if (unbindService != null) {
            unregisterReceiver(unbindService);
        }
    }

    public void changePlayButtonState() {
        btnPlayPause.setImageResource(R.drawable.pb_play);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBroadcastSongComplete();
        unRegisterBroadcastSwitchSong();
        unregisterUnbindService();
        AppController.getInstance().setPlayMusicActivity(null);
    }
}
