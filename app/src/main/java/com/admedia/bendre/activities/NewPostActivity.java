package com.admedia.bendre.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.admedia.bendre.R;
import com.admedia.bendre.api.WordPressService;
import com.admedia.bendre.model.AppUser;
import com.admedia.bendre.model.Post;
import com.admedia.bendre.util.AuthenticationHelper;
import com.admedia.bendre.util.EndpointConstants;
import com.admedia.bendre.util.MessageUtil;
import com.google.gson.JsonObject;

import net.dankito.richtexteditor.android.RichTextEditor;
import net.dankito.richtexteditor.android.toolbar.AllCommandsEditorToolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.admedia.bendre.activities.PostDetailsActivity.SELECTED_POST;

public class NewPostActivity extends AppCompatActivity {
    private RichTextEditor editor;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.lbl_new_post);
        }

        TextInputEditText mTitle = findViewById(R.id.new_post_title);
        TextInputLayout mLayoutTitle = findViewById(R.id.layout_new_post_title);

        Post post = (Post) getIntent().getSerializableExtra(SELECTED_POST);

        editor = findViewById(R.id.editor);

        AllCommandsEditorToolbar editorToolbar = findViewById(R.id.editorToolbar);
        editorToolbar.setEditor(editor);

        editor.setEditorFontSize(20);
        editor.setPadding((int) (4 * getResources().getDisplayMetrics().density));

        if (post != null)
        {
            editor.setHtml(post.getContent().getRendered());
            mTitle.setText(post.getTitle().getRendered());
        }

        mProgressBar = findViewById(R.id.progressbar);

        Button savePost = findViewById(R.id.savePost);
        if (post != null)
        {
            savePost.setText(getString(R.string.lbl_update));
        }
        savePost.setOnClickListener(v -> {
            boolean isValid = true;
            if (mTitle.getText() == null || mTitle.getText().toString().isEmpty())
            {
                mLayoutTitle.setError(getString(R.string.requiered_field));
                isValid = false;
            }

            if (editor.getHtml().isEmpty())
            {
                isValid = false;
                MessageUtil.getInstance().ToastMessage(getApplicationContext(), getString(R.string.required_new_post_content));
            }

            if (isValid)
            {
                showProgress(true);
                new Thread(new SavePostRunnable(mTitle.getText().toString(), editor.getHtml())).start();
            }
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
    }

    class SavePostRunnable implements Runnable {
        private String title;
        private String content;

        SavePostRunnable(String title, String content) {
            this.title = title;
            this.content = content;
        }

        @Override
        public void run() {
            AppUser user = AuthenticationHelper.getInstance().getConnectedUser(getApplicationContext());
            if (user != null)
            {
                String header = "Bearer " + user.getToken();

                JsonObject object = new JsonObject();
                object.addProperty("author", user.getId());
                object.addProperty("content", content);
                object.addProperty("status", "draft");
                object.addProperty("title", title);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(EndpointConstants.POSTS_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();

                WordPressService apiService = retrofit.create(WordPressService.class);
                Call call = apiService.savePost(header, object);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) {
                        if (response.body() != null)
                        {
                            showProgress(false);
                            startActivity(new Intent(getApplicationContext(), MyMenuActivity.class));
                            MessageUtil.getInstance().ToastMessage(getApplicationContext(), getString(R.string.successful_save_post));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                        showProgress(false);
                        MessageUtil.getInstance().ToastMessage(getApplicationContext(), getString(R.string.cannot_fetch_data));
                    }
                });
            }
        }
    }
}
