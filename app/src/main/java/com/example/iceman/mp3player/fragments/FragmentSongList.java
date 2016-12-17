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
import com.example.iceman.mp3player.adapter.SongListAdapter;
import com.example.iceman.mp3player.models.Song;
import com.example.iceman.mp3player.utils.AppController;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSongList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSongList extends Fragment implements SearchView.OnQueryTextListener {

    View view;
    RecyclerView mRvListSong;
    ArrayList<Song> mListSong;
    SongListAdapter songAdapter;
    ProgressBar mProgressBar;
    LoadListSong loadListSong;
    boolean isLoading = true;

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

    }

    private void showListSong() {
        loadListSong = new LoadListSong();
        loadListSong.execute();
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
                songAdapter.setFilter(mListSong);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final ArrayList<Song> filteredSongList = filter(mListSong, newText);
        songAdapter.setFilter(filteredSongList);
        return true;
    }

    private ArrayList<Song> filter(ArrayList<Song> lstSong, String query) {
        query = query.toLowerCase();
        final ArrayList<Song> filteredSongList = new ArrayList<>();
        for (Song song : lstSong) {
            final String text = song.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredSongList.add(song);
            }
        }
        return filteredSongList;
    }

    private class LoadListSong extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            isLoading = true;
            if(AppController.getInstance().getLstSong() == null) {
                mListSong = AppController.getInstance().getListSong();
            }else{
                mListSong = AppController.getInstance().getListSong();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            songAdapter = new SongListAdapter(getActivity(), mListSong);
            mRvListSong.setAdapter(songAdapter);
            mProgressBar.setVisibility(View.GONE);
            isLoading = false;
        }
    }

    ;

    private void initControls() {
        mRvListSong = (RecyclerView) view.findViewById(R.id.rv_song_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRvListSong.setLayoutManager(layoutManager);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar_song_list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (loadListSong != null && loadListSong.getStatus() != AsyncTask.Status.FINISHED) {
            loadListSong.cancel(true);
        }
    }
}
