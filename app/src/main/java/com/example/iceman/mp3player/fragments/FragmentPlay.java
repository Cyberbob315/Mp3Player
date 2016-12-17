package com.example.iceman.mp3player.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.iceman.mp3player.R;
import com.example.iceman.mp3player.utils.Constants;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPlay#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPlay extends Fragment {

    public static final String KEY_ALBUM_PLAY = "key_album_play";

    View mView;
    CircleImageView mIvAlbum;
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
        registerBroadcastAlbumArt();
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

    BroadcastReceiver broadcastReceiverAlbumArt = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            path = intent.getExtras().getString(KEY_ALBUM_PLAY);
            showImage();
        }
    };

    private void registerBroadcastAlbumArt() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_CHANGE_ALBUM_ART);
        getActivity().registerReceiver(broadcastReceiverAlbumArt, intentFilter);
    }

    private void unregisterBroadcastAlbumArt() {
        getActivity().unregisterReceiver(broadcastReceiverAlbumArt);
    }


    private void showImage() {


        if (path != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            mIvAlbum.setImageBitmap(bitmap);
        } else {
            mIvAlbum.setImageResource(R.drawable.default_cover_big);
        }
//        Animation rotateAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_animation);
//        mIvAlbum.startAnimation(rotateAnimation);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBroadcastAlbumArt();
    }

    private void initControls() {
        mIvAlbum = (CircleImageView) mView.findViewById(R.id.img_album_play);
    }

}
