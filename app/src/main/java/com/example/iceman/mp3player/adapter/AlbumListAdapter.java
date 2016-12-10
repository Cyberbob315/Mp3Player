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
import com.example.iceman.mp3player.activities.AlbumListActivity;
import com.example.iceman.mp3player.models.Album;

import java.util.ArrayList;

/**
 * Created by iceman on 10/11/2016.
 */

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.ViewHolderAlbum> {

    public static final String ALBUM_KEY = "album_key";

    Context mContext;
    ArrayList<Album> mData;
    LayoutInflater mLayoutInflater;

    public AlbumListAdapter(Context mContext, ArrayList<Album> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolderAlbum onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_album, null);
        ViewHolderAlbum holder = new ViewHolderAlbum(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderAlbum holder, int position) {
        Album item = mData.get(position);
        String path =  mData.get(position).getAlbumArtPath();
        if(path != null) {
            Glide.with(mContext).load(path).into(holder.ivImgAlbum);
        }else{
            holder.ivImgAlbum.setImageResource(R.drawable.default_cover_big);
        }
        holder.tvAlbumTitle.setText(item.getTitle());
        holder.tvArtist.setText(item.getArtist());
        holder.setId(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolderAlbum extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivImgAlbum;
        TextView tvAlbumTitle;
        TextView tvArtist;
        int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public ViewHolderAlbum(View itemView) {
            super(itemView);
            ivImgAlbum = (ImageView) itemView.findViewById(R.id.iv_album_img_item);
            tvAlbumTitle = (TextView) itemView.findViewById(R.id.tv_album_title_item);
            tvArtist = (TextView) itemView.findViewById(R.id.tv_artist_album_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, AlbumListActivity.class);
//            intent.putExtra(SongListAdapter.LIST_SONG, mData.get(id).getLstSong());
            intent.putExtra(ALBUM_KEY, mData.get(id).getId());
            mContext.startActivity(intent);
        }
    }
}
