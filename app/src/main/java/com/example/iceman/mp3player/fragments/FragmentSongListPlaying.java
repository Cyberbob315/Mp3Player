package com.example.iceman.mp3player.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.iceman.mp3player.R;
import com.example.iceman.mp3player.adapter.SongListPlayingAdapter;
import com.example.iceman.mp3player.models.Song;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSongListPlaying#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSongListPlaying extends Fragment {

    public static final String KEY_SONG_LIST = "key_song_list";

    ArrayList<Song> mLstSongPlaying;
    RecyclerView mRvListSongPlaying;
    SongListPlayingAdapter mAdapter;
    View mView;

    public FragmentSongListPlaying() {
        // Required empty public constructor
    }

    public static FragmentSongListPlaying newInstance(ArrayList<Song> mData) {
        FragmentSongListPlaying fragment = new FragmentSongListPlaying();
        Bundle args = new Bundle();
        args.putSerializable(KEY_SONG_LIST, mData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLstSongPlaying = (ArrayList<Song>) getArguments().getSerializable(KEY_SONG_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_frament_song_list_playing, container, false);
        initControls();
        showListSongPlaying();
        return mView;
    }

    private void showListSongPlaying() {
        mAdapter = new SongListPlayingAdapter(getActivity(), mLstSongPlaying);
        mRvListSongPlaying.setAdapter(mAdapter);
    }

    private void initControls() {
        mRvListSongPlaying = (RecyclerView) mView.findViewById(R.id.rv_song_list_playing);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRvListSongPlaying.setLayoutManager(layoutManager);
    }

}
