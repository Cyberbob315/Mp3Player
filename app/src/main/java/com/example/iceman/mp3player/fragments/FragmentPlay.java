package com.example.iceman.mp3player.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.iceman.mp3player.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlay#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlay extends Fragment {

    public static final String KEY_ALBUM_PLAY = "key_album_play";

    View mView;
    ImageView mIvAlbum;
    String path;

    public FragmentPlay() {
        // Required empty public constructor
    }

    public static FragmentPlay newInstance(String imgPath) {
        FragmentPlay fragment = new FragmentPlay();
        Bundle args = new Bundle();
        args.putString(KEY_ALBUM_PLAY, imgPath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            path = getArguments().getString(KEY_ALBUM_PLAY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_play, container, false);
        initControls();
        showImage();
        return mView;
    }

    private void showImage() {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        mIvAlbum.setImageBitmap(bitmap);
    }

    private void initControls() {
        mIvAlbum = (ImageView) mView.findViewById(R.id.img_album_play);
    }

}
