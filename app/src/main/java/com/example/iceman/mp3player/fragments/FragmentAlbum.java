package com.example.iceman.mp3player.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
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
import com.example.iceman.mp3player.adapter.AlbumGridAdapter;
import com.example.iceman.mp3player.adapter.AlbumListAdapter;
import com.example.iceman.mp3player.models.Album;
import com.example.iceman.mp3player.utils.AppController;
import com.example.iceman.mp3player.utils.GridSpacingItemDecoration;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAlbum#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAlbum extends Fragment implements SearchView.OnQueryTextListener {

    ProgressBar mProgressBar;
    View mView;
    RecyclerView mRvAlbumList;
    AlbumListAdapter mAlbumAdapter;
    AlbumGridAdapter mAlbumGridAdapter;
    ArrayList<Album> mLstAlbum;
    LoadAlbumList loadAlbumList;


    public FragmentAlbum() {
        // Required empty public constructor
    }


    public static FragmentAlbum newInstance(String param1, String param2) {
        FragmentAlbum fragment = new FragmentAlbum();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_album, container, false);
        initControls();
        showAlbumList();
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
                mAlbumGridAdapter.setFilter(mLstAlbum);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });

    }

    private void initControls() {
        mRvAlbumList = (RecyclerView) mView.findViewById(R.id.rv_album_list);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        mRvAlbumList.setLayoutManager(layoutManager);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRvAlbumList.setLayoutManager(layoutManager);

        int spanCount = 2; // 2 columns
        int spacing = 40; // 40px
        boolean includeEdge = true;
        mRvAlbumList.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        mProgressBar = (ProgressBar) mView.findViewById(R.id.progress_bar_album_list);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAlbumGridAdapter.setFilter(filter(mLstAlbum, newText));
        return true;
    }

    private ArrayList<Album> filter(ArrayList<Album> lstAlbum, String query) {
        query = query.toLowerCase();
        ArrayList<Album> filteredAlbumList = new ArrayList<>();

        for (Album album : lstAlbum) {
            String text = album.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredAlbumList.add(album);
            }
        }
        return filteredAlbumList;
    }

    private class LoadAlbumList extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            if(AppController.getInstance().getLstArtist() == null) {
                mLstAlbum = AppController.getInstance().getListAlbum();
            }else{
                mLstAlbum = AppController.getInstance().getListAlbum();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
//            mAlbumAdapter = new AlbumListAdapter(getActivity(), mLstAlbum);
//            mRvAlbumList.setAdapter(mAlbumAdapter);

            mAlbumGridAdapter = new AlbumGridAdapter(getActivity(), mLstAlbum);
            mRvAlbumList.setAdapter(mAlbumGridAdapter);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void showAlbumList() {
        loadAlbumList = new LoadAlbumList();
        loadAlbumList.execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (loadAlbumList != null && loadAlbumList.getStatus() != AsyncTask.Status.FINISHED) {
            loadAlbumList.cancel(true);
        }
    }
}
