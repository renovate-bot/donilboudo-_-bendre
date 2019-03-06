package com.admedia.bendre.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.admedia.bendre.R;
import com.admedia.bendre.model.Post;
import com.admedia.bendre.model.media.WpFeaturedmedium;
import com.admedia.bendre.util.CategoriesUtil;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.util.List;

public class PostsViewAdapter extends RecyclerView.Adapter<PostsViewAdapter.ViewHolder> {
    private List<Post> posts;
    private OnItemClickListener listener;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(Post post);
    }

    public PostsViewAdapter(List<Post> posts, Context context, OnItemClickListener listener) {
        this.posts = posts;
        this.listener = listener;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private TextView mTitle;
        private LinearLayout mCategories;
        private TextView mPostDate;

        ViewHolder(View view) {
            super(view);

            mImage = view.findViewById(R.id.post_picture);
            mTitle = view.findViewById(R.id.post_title);
            mCategories = view.findViewById(R.id.post_categories);
            mPostDate = view.findViewById(R.id.post_date);
        }

        void bind(final Post post, final OnItemClickListener listener) {
            String title = post.getTitle().getRendered();
            int counter = 0;
            if (mCategories.getChildCount() == 0)
            {
                for (Long category : post.getCategories())
                {
                    String categoryString = CategoriesUtil.getInstance().getCategoryString(category);
                    if (!categoryString.isEmpty())
                    {
                        TextView textView = new TextView(context);
                        textView.setText(categoryString);
                        if (categoryString.equals("A la une"))
                        {
                            textView.setBackgroundColor(context.getResources().getColor(R.color.aLaUne));
                        }
                        else
                        {
                            textView.setBackgroundColor(context.getResources().getColor(R.color.primary));
                        }
                        textView.setTextColor(context.getResources().getColor(R.color.white));
                        textView.setTextSize(18);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        if (counter > 0)
                        {
                            params.leftMargin = 50;
                        }
                        textView.setLayoutParams(params);

                        mCategories.addView(textView);
                        counter++;
                    }
                }
            }

            this.mTitle.setText(title);

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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                LocalDateTime dateTime = LocalDateTime.parse(post.getModifiedGmt());
                String day = String.valueOf(dateTime.getDayOfMonth());
                String month = String.valueOf(dateTime.getMonthValue());
                String year = String.valueOf(dateTime.getYear());
                String hours = String.valueOf(dateTime.getHour());
                String minutes = String.valueOf(dateTime.getMinute());

                if (month.length() == 1)
                {
                    month = "0" + month;
                }
                String postDate = day + "-" + month + "-" + year + " à " + hours + ":" + minutes;
                mPostDate.setText(postDate);
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