package com.example.iceman.mp3player.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.iceman.mp3player.R;
import com.example.iceman.mp3player.adapter.MainAdapter;
import com.example.iceman.mp3player.adapter.SongListAdapter;
import com.example.iceman.mp3player.models.ItemListMain;
import com.example.iceman.mp3player.services.PlayMusicService;
import com.example.iceman.mp3player.utils.AppController;
import com.example.iceman.mp3player.utils.Constants;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView mRvListMain;
    ArrayList<ItemListMain> mListMain;
    MainAdapter mMainAdapter;
    LinearLayout currentPlayingBar;
    PlayMusicService musicService;
    ImageView btnPlayPauseCurrent;
    ImageView btnNextCurrent;
    ImageView imgAlbumArtCurrent;
    TextView tvTitle;
    TextView tvArtist;
    ImageView imgBackGround;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this,R.drawable.abc_ic_menu_moreoverflow_mtrl_alpha));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initControls();
        initEvents();
        showListMain();
        if (musicService != null) {
            updatePlayingState();
            showCurrentSong();
        }
        registerBroadcastUpdatePlaying();
        initDefaultWallpaper();
    }

    private void initDefaultWallpaper() {
        AppController.getInstance().setDefaultWallpaper(imgBackGround);
    }



    private void showListMain() {
        mListMain = new ArrayList<>();
        int numSongs = AppController.getInstance().getListSong().size();
        int numAlbums = AppController.getInstance().getListAlbum().size();
        int numArtists = AppController.getInstance().getListArtist().size();
        mListMain.add(new ItemListMain(R.drawable.ic_mm_song, getString(R.string.list_song), numSongs));
        mListMain.add(new ItemListMain(R.drawable.ic_album_white, getString(R.string.album_list), numAlbums));
        mListMain.add(new ItemListMain(R.drawable.ic_artist, getString(R.string.artist_list), numArtists));
        mMainAdapter = new MainAdapter(MainActivity.this, mListMain);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        mRvListMain.setLayoutManager(layoutManager);
        mRvListMain.setAdapter(mMainAdapter);

    }

    private void initControls() {
        mRvListMain = (RecyclerView) findViewById(R.id.rv_list_main);
        currentPlayingBar = (LinearLayout) findViewById(R.id.current_playing_bar);
        btnPlayPauseCurrent = (ImageView) findViewById(R.id.btn_play_pause_current);
        btnNextCurrent = (ImageView) findViewById(R.id.btn_next_current);
        imgAlbumArtCurrent = (ImageView) findViewById(R.id.img_album_current_bar);
        tvTitle = (TextView) findViewById(R.id.tv_song_title_current);
        tvArtist = (TextView) findViewById(R.id.tv_artist_current);
        imgBackGround = (ImageView) findViewById(R.id.img_wallpaper_main);
        musicService = (PlayMusicService) AppController.getInstance().getPlayMusicService();

        if (musicService != null) {
            currentPlayingBar.setVisibility(View.VISIBLE);
        } else {
            currentPlayingBar.setVisibility(View.GONE);
        }
    }

    private void initEvents() {
        currentPlayingBar.setOnClickListener(this);
        btnPlayPauseCurrent.setOnClickListener(this);
        btnNextCurrent.setOnClickListener(this);
    }

    BroadcastReceiver broadcastReceiverUpdatePlaying = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            musicService = (PlayMusicService) AppController.getInstance().getPlayMusicService();
            if (musicService != null) {
                currentPlayingBar.setVisibility(View.VISIBLE);
            } else {
                currentPlayingBar.setVisibility(View.GONE);
            }
            showCurrentSong();
            if (musicService != null) {
                if (musicService.isPlaying()) {
                    btnPlayPauseCurrent.setImageResource(R.drawable.pb_pause);
                } else {
                    btnPlayPauseCurrent.setImageResource(R.drawable.pb_play);
                }
            }
        }
    };

    private void registerBroadcastUpdatePlaying() {
        IntentFilter intentFilter = new IntentFilter(Constants.ACTION_UPDATE_PlAY_STATUS);
        registerReceiver(broadcastReceiverUpdatePlaying, intentFilter);
    }

    private void unRegisterBroadcastUpdatePlaying() {
        unregisterReceiver(broadcastReceiverUpdatePlaying);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void updatePlayingState() {
        if (musicService.isPlaying()) {
            btnPlayPauseCurrent.setImageResource(R.drawable.pb_pause);
        } else {
            btnPlayPauseCurrent.setImageResource(R.drawable.pb_play);
        }
    }

    private void showCurrentSong() {
        if (musicService != null) {
            tvTitle.setText(musicService.getCurrentSong().getTitle());
            tvArtist.setText(musicService.getCurrentSong().getArtist());
            String albumPath = musicService.getCurrentSong().getAlbumImagePath();
            if (albumPath != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(albumPath);
                imgAlbumArtCurrent.setImageBitmap(bitmap);
            } else {
                imgAlbumArtCurrent.setImageResource(R.drawable.default_cover);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.current_playing_bar:
                if (musicService != null) {
                    Intent intent = new Intent(MainActivity.this, PlayMusicActivity.class);
                    intent.putExtra(SongListAdapter.SONG_PATH, musicService.getCurrentSong().getPath());
                    intent.putExtra(SongListAdapter.SONG_POS, musicService.getCurrentSongPos());
                    intent.putExtra(SongListAdapter.LIST_SONG, musicService.getLstSongPlaying());
                    intent.putExtra(PlayMusicActivity.IS_PlAYING, true);
                    intent.putExtra(PlayMusicActivity.LIST_SONG_SHUFFLE, musicService.getLstSongShuffle());
                    intent.putExtra(PlayMusicActivity.IS_SHUFFLE, musicService.isShuffle());

                    Log.d("SONG_PATH",musicService.getCurrentSong().getPath());
                    Log.d("SONG_POS",musicService.getCurrentSongPos() + "");
                    Log.d("LIST_SONG",musicService.getLstSongPlaying() + "");
                    startActivity(intent);
                }
                break;
            case R.id.btn_play_pause_current:
                if (musicService != null) {
                    Intent intent = new Intent(Constants.ACTION_PLAY_PAUSE);
                    if (musicService.isPlaying()) {
                        btnPlayPauseCurrent.setImageResource(R.drawable.pb_play);
                    } else {
                        btnPlayPauseCurrent.setImageResource(R.drawable.pb_pause);
                    }
                    sendBroadcast(intent);
                    showCurrentSong();
                }
                break;
            case R.id.btn_next_current:
                if (musicService != null) {
                    Intent intent = new Intent(Constants.ACTION_NEXT);
                    sendBroadcast(intent);
                    showCurrentSong();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBroadcastUpdatePlaying();
    }
}
