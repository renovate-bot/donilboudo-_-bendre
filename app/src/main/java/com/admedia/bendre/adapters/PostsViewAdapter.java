package com.admedia.bendre.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.admedia.bendre.R;
import com.admedia.bendre.model.Post;
import com.admedia.bendre.model.media.WpFeaturedmedium;
import com.admedia.bendre.util.CategoriesUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostsViewAdapter extends RecyclerView.Adapter<PostsViewAdapter.ViewHolder> {
    private List<Post> posts;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Post post);
    }

    public PostsViewAdapter(List<Post> posts, OnItemClickListener listener) {
        this.posts = posts;
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private TextView mTitle;
        private TextView mCategories;

        ViewHolder(View view) {
            super(view);

            mImage = view.findViewById(R.id.post_picture);
            mTitle = view.findViewById(R.id.post_title);
            mCategories = view.findViewById(R.id.post_categories);
        }

        void bind(final Post post, final OnItemClickListener listener) {
            String title = post.getTitle().getRendered();
            String categories = CategoriesUtil.getInstance().getCategoriesString(post.getCategories());

            this.mTitle.setText(title);
            this.mCategories.setText(categories);

            if (post.getEmbedded() != null && post.getEmbedded().getWpFeaturedmedia() != null)
            {
                WpFeaturedmedium wpFeaturedmedium = post.getEmbedded().getWpFeaturedmedia().get(0);
                if (wpFeaturedmedium != null)
                {
                    Picasso.get().load(wpFeaturedmedium.getSourceUrl()).into(mImage);
                }
            }
            else
            {
                mImage.setImageResource(R.drawable.bendre);
            }

            itemView.setOnClickListener(v -> listener.onItemClick(post));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(posts.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}