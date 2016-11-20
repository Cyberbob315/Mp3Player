package com.example.iceman.mp3player.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iceman.mp3player.R;
import com.example.iceman.mp3player.activities.DetailActivity;
import com.example.iceman.mp3player.models.ItemListMain;

import java.util.ArrayList;

/**
 * Created by IceMan on 11/8/2016.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolderMain> {

    Context mContext;
    ArrayList<ItemListMain> mData;
    LayoutInflater mLayoutInflater;
    public static final String KEY_MAIN = "key_main";

    public MainAdapter(Context mContext, ArrayList<ItemListMain> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolderMain onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_rv_main, null);
        ViewHolderMain holder = new ViewHolderMain(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolderMain holder, int position) {
        ItemListMain item = mData.get(position);
        holder.icon.setImageResource(item.getIconId());
        holder.title.setText(item.getTitle());
        holder.songNumbers.setText("" + item.getSongNumbers());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolderMain extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView icon;
        TextView title;
        TextView songNumbers;

        public ViewHolderMain(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.icon_list_main);
            title = (TextView) itemView.findViewById(R.id.title_list_main);
            songNumbers = (TextView) itemView.findViewById(R.id.number_of_item_list_main);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(KEY_MAIN,title.getText().toString());
            mContext.startActivity(intent);
        }
    }
}
