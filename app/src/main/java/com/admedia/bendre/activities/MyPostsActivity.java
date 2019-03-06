package com.admedia.bendre.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.admedia.bendre.R;
import com.admedia.bendre.api.WordPressService;
import com.admedia.bendre.adapters.PostsViewAdapter;
import com.admedia.bendre.model.AppUser;
import com.admedia.bendre.model.Post;
import com.admedia.bendre.util.AuthenticationHelper;
import com.admedia.bendre.util.CachesUtil;
import com.admedia.bendre.util.EndpointConstants;
import com.admedia.bendre.util.MessageUtil;
import com.admedia.bendre.util.NetworkUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.admedia.bendre.activities.PostDetailsActivity.IS_FOR_MY_POST_DETAILS;
import static com.admedia.bendre.activities.PostDetailsActivity.SELECTED_POST;
import static com.admedia.bendre.util.Constants.USE_CACHE_DATA;

public class MyPostsActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvPosts;
    private ProgressBar mProgressBar;
    FloatingActionButton fabNewPost;

    private List<Post> posts;
    private boolean useCacheData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        fetchData();
    }

    private void init() {
        setContentView(R.layout.activity_my_posts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        useCacheData = getIntent().getBooleanExtra(USE_CACHE_DATA, true);

        rvPosts = findViewById(R.id.my_posts);
        mProgressBar = findViewById(R.id.progressbar);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);

        fabNewPost = findViewById(R.id.fab_new_post);
        fabNewPost.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), NewPostActivity.class);
            startActivity(intent);
        });
    }

    private void showProgress(boolean show) {
        if (!show)
        {
            if (mProgressBar != null)
            {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            if (mProgressBar != null)
            {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    private void fillData(List<Post> posts) {
        showProgress(false);
        if (swipeRefreshLayout != null)
        {
            swipeRefreshLayout.setRefreshing(false);
        }

        try
        {
            String key = "myPosts";
            CachesUtil.getInstance().removeCache(getApplicationContext(), key);
            CachesUtil.getInstance().createCachedFileForPosts(getApplicationContext(), key, posts);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        rvPosts.setAdapter(new PostsViewAdapter(posts, getApplicationContext(), post -> {
            Intent intent = new Intent(getApplicationContext(), PostDetailsActivity.class);
            intent.putExtra(SELECTED_POST, post);
            intent.putExtra(IS_FOR_MY_POST_DETAILS, true);
            startActivity(intent);
        }));

//        rvPosts.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                if (dy > 0)
//                {
//                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN))
//                    {
//                        if (currentPageNumber < totalNumberOfPages)
//                        {
//                            currentPageNumber++;
//                            loadMore = true;
//                            fetchPosts(currentPageNumber);
//                        }
//                    }
//                }
//            }
//        });

        if (posts.size() > 0)
        {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rvPosts.setLayoutManager(linearLayoutManager);
        }
        else
        {
            rvPosts.setVisibility(View.GONE);

            ConstraintLayout layout = findViewById(R.id.my_posts_container);

            TextView textView = new TextView(getApplicationContext());
            textView.setText(getString(R.string.no_created_articles));
            textView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setTextSize(20);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());
            relativeLayout.addView(textView);
            layout.addView(relativeLayout);
        }
    }

    private void fetchData() {
        showProgress(true);
        if (!useCacheData)
        {
            fetchDataFromRemote(1);
        }
        else
        {
            fetchDataFromCache(1);
        }
    }

    private void fetchDataFromRemote(int pageNumber) {
        if (NetworkUtil.isOnline(this))
        {
            new Thread(new GetMyPostsRunnable()).start();
        }
        else
        {
            MessageUtil.getInstance().ToastMessage(getApplicationContext(), getString(R.string.no_internet_connexion));
            fetchDataFromCache(pageNumber);
        }
    }

    private void fetchDataFromCache(int pageNumber) {
        try
        {
            Object data = CachesUtil.getInstance().readCachedFile(getApplicationContext(), "myPosts");
            List array = Collections.singletonList(data);
            posts = (List<Post>) array.get(0);
            fillData(posts);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            if (NetworkUtil.isOnline(this))
            {
                fetchDataFromRemote(pageNumber);
            }
            else
            {
                posts = new ArrayList<>();
                showProgress(false);
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            if (NetworkUtil.isOnline(this))
            {
                fetchDataFromRemote(pageNumber);
            }
            else
            {
                posts = new ArrayList<>();
                showProgress(false);
            }
        }
    }


    class GetMyPostsRunnable implements Runnable {

        @Override
        public void run() {
            String header = "";
            AppUser user = AuthenticationHelper.getInstance().getConnectedUser(getApplicationContext());
            if (user != null)
            {
                header = "Bearer " + user.getToken();
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(EndpointConstants.POSTS_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            WordPressService apiService = retrofit.create(WordPressService.class);
            assert user != null;
            String status = "draft+publish";
            Call<JsonArray> call = apiService.getPostsByAuthor(header, true, status, 1, user.getId());
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                    if (response.body() != null)
                    {
                        posts = new ArrayList<>();

                        JsonArray jsonElements = response.body();
                        for (int counter = 0; counter < jsonElements.size(); counter++)
                        {
                            JsonElement element = jsonElements.get(counter);
                            Post post = new Post((JsonObject) element);
                            //post.setPostType(postType);
                            posts.add(post);
                        }
                        fillData(posts);
                    }
                    else
                    {
                        fillData(new ArrayList<>());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t) {
                    showProgress(false);

                    if (t.getMessage().contains("Expired token") || t.getMessage().contains("jwt_auth_invalid_token"))
                    {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                    else
                    {
                        MessageUtil.getInstance().ToastMessage(getApplicationContext(), getString(R.string.cannot_fetch_data));
                    }
                }
            });
        }
    }

}
