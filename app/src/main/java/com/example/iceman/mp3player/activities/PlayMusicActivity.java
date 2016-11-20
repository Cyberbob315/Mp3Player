package com.example.iceman.mp3player.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.iceman.mp3player.R;
import com.example.iceman.mp3player.adapter.SongListAdapter;
import com.example.iceman.mp3player.adapter.SongListPlayingAdapter;
import com.example.iceman.mp3player.adapter.ViewPagerPlayAdapter;
import com.example.iceman.mp3player.models.Song;
import com.example.iceman.mp3player.services.PlayMusicService;
import com.example.iceman.mp3player.utils.AppController;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;

public class PlayMusicActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String IS_PlAYING = "is_playing";
    public static final String LIST_SONG_SHUFFLE = "list_song_shuffle";
    public static final String IS_SHUFFLE = "is_shuffle";
    public static final String PLAY_SERVICE = "play_service";

    public static final String ACTION_COMPLETE_SONG = "com.example.iceman.Mp3Player.ACTION_COMPLETE_SONG";
    public static final String ACTION_SWITCH_SONG = "com.example.iceman.Mp3Player.ACTION_SWITCH_SONG";
    public static final String ACTION_PREV = "com.example.iceman.Mp3Player.ACTION_PREV";
    public static final String ACTION_PLAY_PAUSE = "com.example.iceman.Mp3Player.ACTION_PLAY_PAUSE";
    public static final String ACTION_NEXT = "com.example.iceman.Mp3Player.ACTION_NEXT";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        mData = new ArrayList<>();
        getDataFromIntent();
        initControls();
        if (mPlayMusicService == null) {
            initPlayService();
        }

        initEvents();
        registerBroadcastSongComplete();
        registerBroadcastSwitchSong();
    }

    BroadcastReceiver broadcastReceiverSongCompleted = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            nextMusic();
        }
    };

    private void registerBroadcastSongComplete() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_COMPLETE_SONG);
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
        }
    };

    private void registerBroadcastSwitchSong() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SWITCH_SONG);
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
                mPlayMusicService.seekTo(progress);
                if (!mPlayMusicService.isPlaying()) {
                    mPlayMusicService.resumeMusic();
                    btnPlayPause.setImageResource(R.drawable.pb_pause);
                }
                updateSeekBar();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void initControls() {

//        mActionBar = getSupportActionBar();
//        mActionBar.setDisplayShowHomeEnabled(false);
//        mActionBar.setDisplayShowTitleEnabled(false);
//        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        mActionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        LayoutInflater mInflater = LayoutInflater.from(this);
//        View mCustomView = mInflater.inflate(R.layout.custom_bar, null);
//        mActionBar.setCustomView(mCustomView);
//        mActionBar.setDisplayShowCustomEnabled(true);
//
//        mTvSongName = (TextView) mCustomView.findViewById(R.id.tv_song_name_play);
//        mTvArtist = (TextView) mCustomView.findViewById(R.id.tv_artist_play);

        seekBar = (SeekBar) findViewById(R.id.seek_bar_play);
        btnPlayPause = (ImageView) findViewById(R.id.btn_play_pause);
        btnPrev = (ImageView) findViewById(R.id.btn_prev);
        btnNext = (ImageView) findViewById(R.id.btn_next);
        btnShuffle = (ImageView) findViewById(R.id.btn_shuffle);
        btnRepeat = (ImageView) findViewById(R.id.btn_repeat);
        tvTotalTime = (TextView) findViewById(R.id.tv_time_left);
        tvTimePlayed = (TextView) findViewById(R.id.tv_time_played);

        mViewPager = (ViewPager) findViewById(R.id.view_pager_play);
        mVpPlayAdapter = new ViewPagerPlayAdapter(getSupportFragmentManager(), mData);
        mViewPager.setAdapter(mVpPlayAdapter);
        mViewPager.setCurrentItem(1);
    }

    private void playMusic() {
        mPlayMusicService.playMusic(path);
        if (isShuffle) {
            Song item = mDataShuffle.get(currentPosShuffle);
            AppController.getInstance().showNotification(mData, currentPos, mDataShuffle, isShuffle, item, item.getAlbumImagePath());
        } else {
            Song item = mData.get(currentPos);
            AppController.getInstance().showNotification(mData, currentPos, mDataShuffle, isShuffle, item, item.getAlbumImagePath());
        }
//        setName();
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
//            mPlayMusicService = PlayMusicService.getInstance();
            mPlayMusicService.setRepeat(false);
            playMusic();
            updateSeekBar();
            totalTime = mPlayMusicService.getTotalTime();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void updateSeekBar() {
        seekBar.setMax(totalTime);
        seekBar.setProgress(mPlayMusicService.getCurrentLength());
        tvTimePlayed.setText(getTime(mPlayMusicService.getCurrentLength()));
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
                break;
            case R.id.btn_play_pause:
                playPauseMusic();
                break;
            case R.id.btn_prev:
                backMusic();
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
        }
    }

    private void playPauseMusic() {
        if (mPlayMusicService.isPlaying()) {
            btnPlayPause.setImageResource(R.drawable.pb_play);
            mPlayMusicService.pauseMusic();
        } else {
            btnPlayPause.setImageResource(R.drawable.pb_pause);
            mPlayMusicService.resumeMusic();
        }
    }

    private void nextMusic() {
        if (isShuffle) {
            if (currentPosShuffle == mDataShuffle.size()) {
                currentPosShuffle = 0;
            } else {
                currentPosShuffle++;
            }
            path = mDataShuffle.get(currentPosShuffle).getPath();
            currentPos = getCurrentPos();
        } else {
            if (currentPos == mData.size()) {
                currentPos = 0;
            } else {
                currentPos++;
            }
            path = mData.get(currentPos).getPath();
            currentPosShuffle = getCurrentPosShuffle();
        }
        playMusic();

    }

    private void backMusic() {
        if (isShuffle) {
            if (currentPosShuffle == 0) {
                currentPosShuffle = mDataShuffle.size();
            } else {
                currentPosShuffle--;
            }
            path = mDataShuffle.get(currentPosShuffle).getPath();
            currentPos = getCurrentPos();
        } else {
            if (currentPos == 0) {
                currentPos = mData.size();
            } else {
                currentPos--;
            }
            path = mData.get(currentPos).getPath();
            currentPosShuffle = getCurrentPosShuffle();
        }
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

    public class BroadcastPrevMusic extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            backMusic();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unRegisterBroadcastSongComplete();
//        unRegisterBroadcastSwitchSong();
    }
}
