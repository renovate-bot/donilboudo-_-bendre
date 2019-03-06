package com.admedia.bendre.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.admedia.bendre.R;
import com.admedia.bendre.api.WordPressService;
import com.admedia.bendre.model.AppUser;
import com.admedia.bendre.model.Post;
import com.admedia.bendre.model.media.WpFeaturedmedium;
import com.admedia.bendre.util.AuthenticationHelper;
import com.admedia.bendre.util.CategoriesUtil;
import com.admedia.bendre.util.Constants;
import com.admedia.bendre.util.EndpointConstants;
import com.admedia.bendre.util.MessageUtil;
import com.admedia.bendre.util.NetworkUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.admedia.bendre.util.Constants.URL_SUBSCRIPTION;
import static com.admedia.bendre.util.Constants.USE_CACHE_DATA;
import static maes.tech.intentanim.CustomIntent.customType;

public class PostDetailsActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    public static final String SELECTED_POST = "SELECTED_POST";
    public static final String POST_TYPE = "POST_TYPE";
    public static final String IS_FOR_MY_POST_DETAILS = "IS_FOR_MY_POST_DETAILS";

    private Post selectedPost;
    private TextView mPostTitle;
    private TextView mPostDateAndAuthor;
    private TextView mPostCategories;
    private WebView mPostContent;
    private ImageView mPostImage;
    private LinearLayout mRestrictiveContentButtonsLayout;
    private ProgressBar mProgressBar;

    private boolean isMyPostDetails = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        fillData();
        addAuthorInformation();
    }

    private void addAuthorInformation() {
        if (NetworkUtil.isOnline(this))
        {
            new Thread(new GetAuthorInformationRunnable(selectedPost.getAuthor())).start();
        }
        else
        {
            fillData();
        }
    }

    private void init() {
        setContentView(R.layout.activity_post_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> goBack());
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        isMyPostDetails = getIntent().getBooleanExtra(IS_FOR_MY_POST_DETAILS, false);

        mPostTitle = findViewById(R.id.post_title);
        mPostDateAndAuthor = findViewById(R.id.post_date_and_author);
        mPostCategories = findViewById(R.id.post_categories);
        mPostContent = findViewById(R.id.post_content);
        mPostImage = findViewById(R.id.post_image);
        mRestrictiveContentButtonsLayout = findViewById(R.id.restrictiveContentButtons);
        mRestrictiveContentButtonsLayout.setVisibility(View.GONE);
        NestedScrollView mPostContentPanel = findViewById(R.id.post_content_panel);
        mProgressBar = findViewById(R.id.progressbar);

        //get selected post in extra data
        Intent intent = getIntent();
        selectedPost = (Post) intent.getSerializableExtra(SELECTED_POST);

        mPostContentPanel.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()))
            {
                Log.i(TAG, "BOTTOM SCROLL");
            }
        });
    }

    private void updateAuthorName() {
        String authorName = selectedPost.getAuthorName();
        if (authorName == null)
        {
            authorName = "";
        }
        String dateAndAuthor = selectedPost.getDate() + " | " + authorName;
        this.mPostDateAndAuthor.setText(dateAndAuthor);
    }

    private void fillData() {
        showProgress(false);

        this.mPostTitle.setText(selectedPost.getTitle().getRendered());

        String authorName = selectedPost.getAuthorName();
        if (authorName == null)
        {
            authorName = "";
        }
        String dateAndAuthor = selectedPost.getDate() + " | " + authorName;
        this.mPostDateAndAuthor.setText(dateAndAuthor);

        this.mPostCategories.setText(CategoriesUtil.getInstance().getCategoriesString(selectedPost.getCategories()));

        if (AuthenticationHelper.getInstance().getConnectedUser(getApplicationContext()) == null)
        {
            mRestrictiveContentButtonsLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            mRestrictiveContentButtonsLayout.setVisibility(View.GONE);
        }

        String content = selectedPost.getContent().getRendered();
        if (content.contains("S&rsquo;abonner") && content.contains("Se connecter"))
        {
            content = content.replaceAll("S&rsquo;abonner", "");
            content = content.replaceAll("Se connecter", "");
        }

        String encodedHtml = Base64.encodeToString(content.getBytes(), Base64.NO_PADDING);
        mPostContent.loadData(encodedHtml, "text/html", "base64");

        if (selectedPost.getEmbedded() != null && selectedPost.getEmbedded().getWpFeaturedmedia() != null)
        {
            WpFeaturedmedium wpFeaturedmedium = selectedPost.getEmbedded().getWpFeaturedmedia().get(0);
            if (wpFeaturedmedium != null)
            {
                int width = getWindowManager().getDefaultDisplay().getWidth();
                Picasso.get()
                        .load(wpFeaturedmedium.getSourceUrl())
                        .centerInside()
                        .resize(width, 1000)
                        .into(mPostImage);
            }
        }
        else
        {
            mPostImage.setImageResource(R.drawable.bendre);
        }


        //TODO convert date and format with author name
//        Date postDate;
//        try
//        {
//            postDate = new SimpleDateFormat("dd/mm/yyyy", Locale.FRANCE).parse(selectedPost.getDate());
//        }
//        catch (ParseException e)
//        {
//            postDate = new Date();
//        }

    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            onBackPressed();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void login(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra(CategoriesUtil.POST_TYPE, selectedPost.getPostType());
        startActivity(intent);
    }

    public void subscription(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(URL_SUBSCRIPTION));
        startActivity(intent);
    }

    private void goBack() {
        Intent intent = isMyPostDetails ? new Intent(getApplicationContext(), MyPostsActivity.class) : new Intent(getApplicationContext(), PostsActivity.class);
        intent.putExtra(POST_TYPE, selectedPost.getPostType());
        startActivity(intent);
        customType(this, Constants.RIGHT_TO_LEFT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (selectedPost != null)
        {
            if (selectedPost.getStatus().equals("draft"))
            {
                getMenuInflater().inflate(R.menu.edit, menu);
                MenuItem mEdit = menu.findItem(R.id.action_edit);
                mEdit.setOnMenuItemClickListener(item -> {
                    Intent intent = new Intent(getApplicationContext(), NewPostActivity.class);
                    intent.putExtra(SELECTED_POST, selectedPost);
                    startActivity(intent);
                    return false;
                });

                getMenuInflater().inflate(R.menu.delete, menu);
                MenuItem mDelete = menu.findItem(R.id.action_delete);
                mDelete.setOnMenuItemClickListener(item -> {
                    showProgress(true);
                    new Thread(new DeletePostRunnable(selectedPost.getId().intValue())).start();
                    return false;
                });
            }
            else
            {
                getMenuInflater().inflate(R.menu.comments, menu);
                MenuItem mComments = menu.findItem(R.id.action_comments);
                mComments.setOnMenuItemClickListener(item -> {
                    Intent intent = new Intent(getApplicationContext(), CommentsActivity.class);
                    intent.putExtra(SELECTED_POST, selectedPost);
                    startActivity(intent);
                    return false;
                });


                getMenuInflater().inflate(R.menu.share, menu);
                MenuItem mShare = menu.findItem(R.id.action_share);
                mShare.setOnMenuItemClickListener(item -> {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, "Je partage avec toi un article intéressant: " + this.selectedPost.getLink());
                    intent.setType("text/plain");
                    startActivity(intent);
                    return false;
                });
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return false;
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

    class GetAuthorInformationRunnable implements Runnable {
        private Long authorId;

        GetAuthorInformationRunnable(Long authorId) {
            this.authorId = authorId;
        }

        @Override
        public void run() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(EndpointConstants.POSTS_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            WordPressService apiService = retrofit.create(WordPressService.class);
            Call call = apiService.getAuthor(authorId);
            call.enqueue(new Callback<LinkedTreeMap>() {

                @Override
                public void onResponse(@NonNull Call<LinkedTreeMap> call, @NonNull Response<LinkedTreeMap> response) {
                    if (response.body() != null)
                    {
                        Log.i("onResponse", response.body().toString());
                        LinkedTreeMap data = response.body();
                        String name = (String) response.body().get("name");
                        selectedPost.setAuthorName(name);
                        updateAuthorName();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LinkedTreeMap> call, @NonNull Throwable t) {
                    Log.i("onFail", t.getMessage());
                    showProgress(false);
                    MessageUtil.getInstance().ToastMessage(getApplicationContext(), getString(R.string.cannot_fetch_data));
                }
            });
        }
    }

    class DeletePostRunnable implements Runnable {
        private int postId;

        DeletePostRunnable(int postId) {
            this.postId = postId;
        }

        @Override
        public void run() {
            AppUser user = AuthenticationHelper.getInstance().getConnectedUser(getApplicationContext());
            if (user != null)
            {
                String header = "Bearer " + user.getToken();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(EndpointConstants.POSTS_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();

                WordPressService apiService = retrofit.create(WordPressService.class);
                Call call = apiService.deletePost(header, postId);
                call.enqueue(new Callback<LinkedTreeMap>() {

                    @Override
                    public void onResponse(@NonNull Call<LinkedTreeMap> call, @NonNull Response<LinkedTreeMap> response) {
                        if (response.body() != null)
                        {
                            showProgress(false);
                            MessageUtil.getInstance().ToastMessage(getApplicationContext(), "L'article à été bien supprimé");
                            Intent intent = new Intent(getApplicationContext(), MyPostsActivity.class);
                            intent.putExtra(USE_CACHE_DATA, false);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LinkedTreeMap> call, @NonNull Throwable t) {
                        Log.i("onFail", t.getMessage());
                        showProgress(false);
                        MessageUtil.getInstance().ToastMessage(getApplicationContext(), getString(R.string.cannot_fetch_data));
                    }
                });
            }
        }
    }
}
