package com.admedia.bendre.fragments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.admedia.bendre.R;
import com.admedia.bendre.fragments.VideoFragment.OnListFragmentInteractionListener;
import com.admedia.bendre.model.Video;

import java.util.List;

public class MyVideoRecyclerViewAdapter extends RecyclerView.Adapter<MyVideoRecyclerViewAdapter.ViewHolder> {

    private final List<Video> mValues;
    private final OnListFragmentInteractionListener mListener;

    MyVideoRecyclerViewAdapter(List<Video> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitle.setText(mValues.get(position).getTitle());

        String path = mValues.get(position).getUrl();
        holder.mPlayer.setVideoPath(path);
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
        final VideoView mPlayer;
        final TextView mTitle;
        Video mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mPlayer = view.findViewById(R.id.video_item_player);
            mTitle = view.findViewById(R.id.video_item_title);
        }
    }
}
