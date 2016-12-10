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
 * Created by IceMan on 12/8/2016.
 */

public class AlbumGridAdapter extends RecyclerView.Adapter<AlbumGridAdapter.ViewHolderAlbumGrid> {

    Context mContext;
    ArrayList<Album> mData;
    LayoutInflater mLayoutInflater;

    public AlbumGridAdapter(Context mContext, ArrayList<Album> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolderAlbumGrid onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_album_grid, null);
        ViewHolderAlbumGrid holder = new ViewHolderAlbumGrid(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderAlbumGrid holder, int position) {
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
    public class ViewHolderAlbumGrid extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        public ViewHolderAlbumGrid (View itemView) {
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
            intent.putExtra(AlbumListAdapter.ALBUM_KEY, mData.get(id).getId());
            mContext.startActivity(intent);
        }
    }
}
