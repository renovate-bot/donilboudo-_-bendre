package com.admedia.bendre.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;

import com.admedia.bendre.R;
import com.admedia.bendre.model.Post;
import com.admedia.bendre.model.media.WpFeaturedmedium;
import com.admedia.bendre.util.AuthenticationHelper;
import com.admedia.bendre.util.CategoriesUtil;
import com.squareup.picasso.Picasso;

public class PostDetailsActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getName();
    public static final String SELECTED_POST = "SELECTED_POST";
    public static final String POST_TYPE = "POST_TYPE";

    private Post selectedPost;
    private TextView mPostTitle;
    private TextView mPostDateAndAuthor;
    private TextView mPostCategories;
    private WebView mPostContent;
    private ImageView mPostImage;
    private LinearLayout mRestrictiveContentButtonsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        fillData();
    }

    private void init() {
        setContentView(R.layout.activity_post_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> goBack());
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }

        mPostTitle = findViewById(R.id.post_title);
        mPostDateAndAuthor = findViewById(R.id.post_date_and_author);
        mPostCategories = findViewById(R.id.post_categories);
        mPostContent = findViewById(R.id.post_content);
        mPostImage = findViewById(R.id.post_image);
        mRestrictiveContentButtonsLayout = findViewById(R.id.restrictiveContentButtons);
        NestedScrollView mPostContentPanel = findViewById(R.id.post_content_panel);

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

    private void fillData() {
        this.mPostTitle.setText(selectedPost.getTitle().getRendered());

        String dateAndAuthor = selectedPost.getDate() + " | Par Fabrice Ilboudo";
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
        String url = "https://www.bendre.bf/package-lecteur-abonne/";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void goBack() {
        Intent intent = new Intent(getApplicationContext(), PostsActivity.class);
        intent.putExtra(POST_TYPE, selectedPost.getPostType());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

        return super.onCreateOptionsMenu(menu);
    }

}
