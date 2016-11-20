package com.example.iceman.mp3player.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.iceman.mp3player.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlay#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlay extends Fragment {


    public FragmentPlay() {
        // Required empty public constructor
    }

    public static FragmentPlay newInstance(String imgPath) {
        FragmentPlay fragment = new FragmentPlay();
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
        return inflater.inflate(R.layout.fragment_play, container, false);
    }

}
