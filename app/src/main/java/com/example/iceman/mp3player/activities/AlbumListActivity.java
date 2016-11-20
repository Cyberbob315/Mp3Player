package com.example.iceman.mp3player.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.example.iceman.mp3player.R;
import com.example.iceman.mp3player.adapter.AlbumListAdapter;
import com.example.iceman.mp3player.adapter.SongListAdapter;
import com.example.iceman.mp3player.models.Song;
import com.example.iceman.mp3player.utils.AppController;

import java.io.File;
import java.util.ArrayList;

public class AlbumListActivity extends AppCompatActivity {

    RecyclerView mRvSongList;
    ImageView mIvAlbumCover;
    ArrayList<Song> mListSong;
    SongListAdapter mAdapter;
    int mAlbumId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);

        initControls();
        getAndShowSongList();
        showCover();
    }

    private void showCover() {

        String path = mListSong.get(0).getAlbumImagePath();
        if (path != null) {
            File file = new File(path);
            Uri uri = Uri.fromFile(file);
            mIvAlbumCover.setImageURI(uri);
        }
    }

    private void getAndShowSongList() {
        Intent intent = getIntent();
        mAlbumId = intent.getExtras().getInt(AlbumListAdapter.ALBUM_KEY);
        mListSong = AppController.getInstance().getListSongOfAlbum(mAlbumId);
        mAdapter = new SongListAdapter(this, mListSong);
        mRvSongList.setAdapter(mAdapter);
    }

    private void initControls() {
        mRvSongList = (RecyclerView) findViewById(R.id.rv_album_list_play);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRvSongList.setLayoutManager(layoutManager);

        mIvAlbumCover = (ImageView) findViewById(R.id.img_album_list_play);
    }
}
