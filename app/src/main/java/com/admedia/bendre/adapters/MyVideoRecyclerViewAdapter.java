package com.admedia.bendre.adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.admedia.bendre.R;
import com.admedia.bendre.fragments.ChannelVideosFragment.OnListFragmentInteractionListener;
import com.admedia.bendre.fragments.YoutubeFragment;
import com.admedia.bendre.model.Video;

import java.util.List;

public class MyVideoRecyclerViewAdapter extends RecyclerView.Adapter<MyVideoRecyclerViewAdapter.ViewHolder> {

    private final List<Video> mValues;
    private final OnListFragmentInteractionListener mListener;
    private FragmentManager fragmentManager;

    MyVideoRecyclerViewAdapter(List<Video> items, OnListFragmentInteractionListener listener, FragmentManager fragmentManager) {
        this.mValues = items;
        this.mListener = listener;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitle.setText(mValues.get(position).getTitle());

        String path = mValues.get(position).getUrl();
//        holder.mPlayer.setVideoPath(path);

        Bundle bundle = new Bundle();
        bundle.putString("VIDEO_ID", path);
        YoutubeFragment fragment = new YoutubeFragment();
        fragment.setArguments(bundle);
        if (this.fragmentManager != null)
        {
            this.fragmentManager.beginTransaction()
                    .replace(holder.mPlayer.getId(), fragment)
                    .addToBackStack(null)
                    .commit();
        }
//        holder.mContentView.setText(mValues.get(position).content);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener)
            {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        RelativeLayout mPlayer;
        TextView mTitle;
        Video mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
//            mPlayer = view.findViewById(R.id.video_item_player);
//            mTitle = view.findViewById(R.id.video_item_title);
        }
    }
}
