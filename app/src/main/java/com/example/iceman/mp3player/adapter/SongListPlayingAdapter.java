package com.example.iceman.mp3player.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.iceman.mp3player.R;
import com.example.iceman.mp3player.fragments.FragmentPlay;
import com.example.iceman.mp3player.models.Song;
import com.example.iceman.mp3player.utils.Constants;

import java.util.ArrayList;

/**
 * Created by IceMan on 11/20/2016.
 */

public class SongListPlayingAdapter extends RecyclerView.Adapter<SongListPlayingAdapter.ViewHolderSongPlaying> {

    public static final String KEY_ID_SWITH = "key_id_switch";

    Context mContext;
    ArrayList<Song> mData;
    LayoutInflater mLayoutInflater;

    public SongListPlayingAdapter(Context mContext, ArrayList<Song> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolderSongPlaying onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_song_playing, null);
        ViewHolderSongPlaying holder = new ViewHolderSongPlaying(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderSongPlaying holder, int position) {
        Song item = mData.get(position);
        holder.setId(position);
        String path =  mData.get(position).getAlbumImagePath();
        if(path != null) {
            Glide.with(mContext).load(path).into(holder.imgAlbum);
        }else{
            holder.imgAlbum.setImageResource(R.drawable.default_cover_big);
        }
        holder.tvTitle.setText(item.getTitle());
        holder.tvArtist.setText(item.getArtist());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolderSongPlaying extends RecyclerView.ViewHolder implements View.OnClickListener {
        int id;
        private ImageView imgAlbum;
        private TextView tvTitle;
        private TextView tvArtist;


        public ViewHolderSongPlaying(View itemView) {
            super(itemView);
            imgAlbum = (ImageView) itemView.findViewById(R.id.img_album_song_play);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_song_name_play);
            tvArtist = (TextView) itemView.findViewById(R.id.tv_artist_song_play);
            itemView.setOnClickListener(this);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Constants.ACTION_SWITCH_SONG);
            intent.putExtra(KEY_ID_SWITH, id);
            mContext.sendBroadcast(intent);

            Intent intent1 = new Intent(Constants.ACTION_CHANGE_ALBUM_ART);
            intent1.putExtra(FragmentPlay.KEY_ALBUM_PLAY,mData.get(id).getAlbumImagePath());
            mContext.sendBroadcast(intent1);
        }
    }
}
