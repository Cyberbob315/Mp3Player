package com.example.iceman.mp3player.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.iceman.mp3player.R;
import com.example.iceman.mp3player.adapter.ArtistAdapter;
import com.example.iceman.mp3player.models.Artist;
import com.example.iceman.mp3player.utils.AppController;

import java.util.ArrayList;


public class FragmentArtist extends Fragment implements SearchView.OnQueryTextListener {
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_detail, menu);
        MenuItem item = menu.findItem(R.id.action_search_detail);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mArtistAdapter.filter(mLstArtist);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });
    }

    private ArrayList<Artist> filter(ArrayList<Artist> lstArtist, String query) {
        query = query.toLowerCase();
        ArrayList<Artist> filteredArtistList = new ArrayList<>();
        for (Artist artist : lstArtist) {
            String text = artist.getName().toLowerCase();
            if (text.contains(query)) {
                filteredArtistList.add(artist);
            }
        }
        return filteredArtistList;
    }

    private void showListArtist() {
        loadArtistList = new LoadArtistList();
        loadArtistList.execute();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mArtistAdapter.filter(filter(mLstArtist, newText));
        return true;
    }

    private class LoadArtistList extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            if (AppController.getInstance().getLstArtist() == null) {
                mLstArtist = AppController.getInstance().getListArtist();
            } else {
                mLstArtist = AppController.getInstance().getListArtist();
            }
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
        if (loadArtistList != null && loadArtistList.getStatus() != AsyncTask.Status.FINISHED) {
            loadArtistList.cancel(true);
        }
    }
}
