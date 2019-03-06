package com.admedia.bendre.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.admedia.bendre.util.CategoriesUtil;
import com.admedia.bendre.util.Constants;
import com.admedia.bendre.util.EndpointConstants;
import com.admedia.bendre.util.MenuUtil;
import com.admedia.bendre.util.MessageUtil;
import com.admedia.bendre.util.NetworkUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.admedia.bendre.activities.PostDetailsActivity.POST_TYPE;
import static com.admedia.bendre.activities.PostDetailsActivity.SELECTED_POST;
import static com.admedia.bendre.util.Constants.USE_CACHE_DATA;
import static maes.tech.intentanim.CustomIntent.customType;

public class PostsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvPosts;
    private ProgressBar mProgressBar;
    private Button mLogout;
    private NavigationView navigationView;
    private DrawerLayout drawer;

    private String postType;
    private List<Post> posts;
    private int currentPageNumber;
    private boolean loadMore;
    private int totalNumberOfPages;
    private int totalNumberOfPosts;
    private AppUser loggedInUser;
    private boolean useCacheData;
    private boolean doubleBackToExitPressedOnce = false;

    private Call<JsonArray> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        fetchData();
    }

    private void init() {
        setContentView(R.layout.activity_posts);

        currentPageNumber = 1;
        loadMore = false;
        mLogout = findViewById(R.id.logout_button);

        loggedInUser = AuthenticationHelper.getInstance().getConnectedUser(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = findViewById(R.id.drawer_layout_posts);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        MenuUtil.getInstance().setNavigationView(this, navigationView, drawer);

        postType = getIntent().getStringExtra(POST_TYPE);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(postType);
        }
        useCacheData = getIntent().getBooleanExtra(USE_CACHE_DATA, true);

        rvPosts = findViewById(R.id.posts);

        mProgressBar = findViewById(R.id.progressbar);

        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    if (NetworkUtil.isOnline(this))
                    {
                        currentPageNumber = 1;
                        useCacheData = false;
                        fetchPosts(1);
                    }
                    else
                    {
                        swipeRefreshLayout.setRefreshing(false);
                        MessageUtil.getInstance().ToastMessage(getApplicationContext(), getString(R.string.no_internet_connexion));
                    }
                }
        );
    }

    private void fetchData() {
        fetchPosts(1);
    }

    private void fetchPosts(int pageNumber) {
        String categoriesString = postType != null ? CategoriesUtil.getInstance().getCategoriesString(getApplicationContext(), postType) : "";

        if (!useCacheData)
        {
            fetchDataFromRemote(categoriesString, pageNumber);
        }
        else
        {
            fetchDataFromCache(categoriesString, pageNumber);
        }
    }

    private void fetchDataFromRemote(String categoriesString, int pageNumber) {
        if (NetworkUtil.isOnline(this))
        {
            new Thread(new PostsRunnable(categoriesString)).start();
        }
        else
        {
            MessageUtil.getInstance().ToastMessage(getApplicationContext(), getString(R.string.no_internet_connexion));
            fetchDataFromCache(categoriesString, pageNumber);
        }
    }

    private void fetchDataFromCache(String categoriesString, int pageNumber) {
        try
        {
            Object data = CachesUtil.getInstance().readCachedFile(getApplicationContext(), "posts-" + postType);
            List array = Collections.singletonList(data);
            posts = (List<Post>) array.get(0);
            fillData(posts);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            if (NetworkUtil.isOnline(this))
            {
                fetchDataFromRemote(categoriesString, pageNumber);
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
                fetchDataFromRemote(categoriesString, pageNumber);
            }
            else
            {
                posts = new ArrayList<>();
                showProgress(false);
            }
        }
    }

    private void fillData(List<Post> posts) {
        showProgress(false);
        if (swipeRefreshLayout != null)
        {
            swipeRefreshLayout.setRefreshing(false);
        }

        if (posts != null)
        {
            try
            {
                String key = "posts-" + postType;
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
                startActivity(intent);
                customType(this, Constants.LEFT_TO_RIGHT);
            }));

            rvPosts.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0)
                    {
                        if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN))
                        {
                            if (currentPageNumber < totalNumberOfPages)
                            {
                                currentPageNumber++;
                                loadMore = true;
                                fetchPosts(currentPageNumber);
                            }
                        }
                    }
                }
            });

            if (posts.size() > 0)
            {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                rvPosts.setLayoutManager(linearLayoutManager);
            }
            else
            {
                rvPosts.setVisibility(View.GONE);

                ConstraintLayout layout = findViewById(R.id.posts_container);

                TextView textView = new TextView(getApplicationContext());
                textView.setText(getString(R.string.no_news));
                textView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setTextSize(20);
                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                RelativeLayout relativeLayout = new RelativeLayout(getApplicationContext());
                relativeLayout.addView(textView);
                layout.addView(relativeLayout);
            }
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showProgress(true);

                String header = "";
                if (loggedInUser != null)
                {
                    header = "Bearer " + loggedInUser.getToken();
                }

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(EndpointConstants.POSTS_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();

                WordPressService apiService = retrofit.create(WordPressService.class);
                call = apiService.searchPosts(header, true, "publish", currentPageNumber, query);
                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                        if (response.body() != null)
                        {
                            if (!loadMore)
                            {
                                posts = new ArrayList<>();
                            }

                            //get numbers of pages and posts
                            totalNumberOfPages = Integer.parseInt(Objects.requireNonNull(response.headers().get("X-WP-TotalPages")));
                            totalNumberOfPosts = Integer.parseInt(Objects.requireNonNull(response.headers().get("X-WP-Total")));

                            JsonArray jsonElements = response.body();
                            for (int counter = 0; counter < jsonElements.size(); counter++)
                            {
                                JsonElement element = jsonElements.get(counter);
                                Post post = new Post((JsonObject) element);
                                post.setPostType(postType);
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
                        MessageUtil.getInstance().ToastMessage(getApplicationContext(), getString(R.string.cannot_fetch_data));
                    }
                });

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mSearchView.setOnCloseListener(() -> false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuUtil.getInstance().openPage(getApplicationContext(), item.getItemId());
        drawer = findViewById(R.id.drawer_layout_posts);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (!postType.equals(getString(R.string.menu_a_la_une)))
        {
            Intent intent = new Intent(getApplicationContext(), PostsActivity.class);
            intent.putExtra(POST_TYPE, getString(R.string.menu_a_la_une));
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (doubleBackToExitPressedOnce)
            {
                finishAffinity();
                System.exit(0);
                return true;
            }
            if (postType.equals(getString(R.string.menu_a_la_une)))
            {
                this.doubleBackToExitPressedOnce = true;
                MessageUtil.getInstance().ToastMessage(getApplicationContext(), getString(R.string.lbl_press_back_to_exit));
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 5000);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void logout(View view) {
        mLogout.setVisibility(View.GONE);

        View headerView = navigationView.getHeaderView(0);
        TextView textView = headerView.findViewById(R.id.textView5);
        textView.setVisibility(View.VISIBLE);
        Button button = headerView.findViewById(R.id.login_button);
        button.setText(R.string.se_connecter);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        AuthenticationHelper.getInstance().logout(getApplicationContext(), loggedInUser.getId());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.call != null)
        {
            this.call.cancel();
            this.call = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.call != null)
        {
            this.call.cancel();
            this.call = null;
        }
    }

    class PostsRunnable implements Runnable {
        private String categoriesString;

        PostsRunnable(String categoriesString) {
            this.categoriesString = categoriesString;
        }

        @Override
        public void run() {
            String header = "";
            if (loggedInUser != null)
            {
                header = "Bearer " + loggedInUser.getToken();
            }

            int cacheSize = 10 * 1024 * 1024; // 10 MB
            Cache cache = new Cache(getCacheDir(), cacheSize);

            OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(cache).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(EndpointConstants.POSTS_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            WordPressService apiService = retrofit.create(WordPressService.class);
            call = apiService.getPosts(header, true, categoriesString, "publish", "date", "desc", 25);
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                    if (response.body() != null)
                    {
                        if (!loadMore)
                        {
                            posts = new ArrayList<>();
                        }

                        //get numbers of pages and posts
                        totalNumberOfPages = Integer.parseInt(Objects.requireNonNull(response.headers().get("X-WP-TotalPages")));
                        totalNumberOfPosts = Integer.parseInt(Objects.requireNonNull(response.headers().get("X-WP-Total")));

                        JsonArray jsonElements = response.body();
                        for (int counter = 0; counter < jsonElements.size(); counter++)
                        {
                            JsonElement element = jsonElements.get(counter);
                            Post post = new Post((JsonObject) element);
                            post.setPostType(postType);
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
                    if (!call.isCanceled())
                    {
                        MessageUtil.getInstance().ToastMessage(getApplicationContext(), getString(R.string.cannot_fetch_data));
                    }
                    fillData(null);
                }
            });
        }
    }
}
