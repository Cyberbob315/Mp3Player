package com.example.iceman.mp3player.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iceman.mp3player.R;
import com.example.iceman.mp3player.models.Artist;

import java.util.ArrayList;

/**
 * Created by IceMan on 11/12/2016.
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolderArtist> {
    Context mContext;
    ArrayList<Artist> mData;
    LayoutInflater mLayoutInflater;

    public ArtistAdapter(Context mContext, ArrayList<Artist> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolderArtist onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_artist, null);
        ViewHolderArtist holder = new ViewHolderArtist(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderArtist holder, int position) {
        holder.tvArtist.setText(mData.get(position).getName());
        holder.setId(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolderArtist extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvArtist;
        int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public ViewHolderArtist(View itemView) {
            super(itemView);
            tvArtist = (TextView) itemView.findViewById(R.id.artist_title_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(mContext, "" + mData.get(id).getLstSong().size(), Toast.LENGTH_SHORT).show();
        }
    }
}
