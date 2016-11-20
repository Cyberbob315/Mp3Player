package com.example.iceman.mp3player.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.iceman.mp3player.R;
import com.example.iceman.mp3player.adapter.SongListAdapter;
import com.example.iceman.mp3player.models.Song;
import com.example.iceman.mp3player.utils.AppController;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSongList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSongList extends Fragment {

    View view;
    RecyclerView mRvListSong;
    ArrayList<Song> mListSong;
    SongListAdapter mSongAdater;
    ProgressBar mProgressBar;
    LoadListSong loadListSong;

    public FragmentSongList() {
        // Required empty public constructor
    }

    public static FragmentSongList newInstance() {
        FragmentSongList fragment = new FragmentSongList();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_song_list, container, false);
        initControls();
        showListSong();
        return view;
    }

    private void showListSong() {

        loadListSong = new LoadListSong();
        loadListSong.execute();
    }
    private class LoadListSong extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            mListSong = AppController.getInstance().getListSong();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            mSongAdater = new SongListAdapter(getActivity(), mListSong);
            mRvListSong.setAdapter(mSongAdater);
            mProgressBar.setVisibility(View.GONE);
        }
    };

    private void initControls() {
        mRvListSong = (RecyclerView) view.findViewById(R.id.rv_song_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRvListSong.setLayoutManager(layoutManager);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar_song_list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(loadListSong != null && loadListSong.getStatus() != AsyncTask.Status.FINISHED){
            loadListSong.cancel(true);
        }
    }
}
