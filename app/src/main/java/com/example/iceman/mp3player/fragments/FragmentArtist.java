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
import com.example.iceman.mp3player.adapter.ArtistAdapter;
import com.example.iceman.mp3player.models.Artist;
import com.example.iceman.mp3player.utils.AppController;

import java.util.ArrayList;


public class FragmentArtist extends Fragment {
    View mView;
    RecyclerView mRvListArtist;
    ArrayList<Artist> mLstArtist;
    ArtistAdapter mArtistAdapter;
    ProgressBar mProgressBar;
    LoadArtistList loadArtistList;

    public FragmentArtist() {
        // Required empty public constructor
    }


    public static FragmentArtist newInstance(String param1, String param2) {
        FragmentArtist fragment = new FragmentArtist();
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
        mView = inflater.inflate(R.layout.fragment_artist, container, false);

        initControls();
        showListArtist();
        return mView;
    }

    private void showListArtist() {
        loadArtistList = new LoadArtistList();
        loadArtistList.execute();
    }

    private class LoadArtistList extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            mLstArtist = AppController.getInstance().getListArtist();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            mProgressBar.setVisibility(View.GONE);
            mArtistAdapter = new ArtistAdapter(getActivity(), mLstArtist);
            mRvListArtist.setAdapter(mArtistAdapter);
        }
    }

    private void initControls() {
        mRvListArtist = (RecyclerView) mView.findViewById(R.id.rv_artist_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRvListArtist.setLayoutManager(layoutManager);
        mProgressBar = (ProgressBar) mView.findViewById(R.id.progress_bar_artist_list);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(loadArtistList != null && loadArtistList.getStatus() != AsyncTask.Status.FINISHED){
            loadArtistList.cancel(true);
        }
    }
}
