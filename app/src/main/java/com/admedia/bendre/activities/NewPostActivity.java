package com.admedia.bendre.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.admedia.bendre.R;
import com.admedia.bendre.WordPressService;
import com.admedia.bendre.model.AppUser;
import com.admedia.bendre.model.Content;
import com.admedia.bendre.model.Post;
import com.admedia.bendre.model.Title;
import com.admedia.bendre.util.AuthenticationHelper;
import com.admedia.bendre.util.EndpointConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewPostActivity extends AppCompatActivity {
    public class Header {
        private String authorization;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("Nouvel article");
        }
    }


    public void savePost(View view) {
        AppUser user = AuthenticationHelper.getInstance().getConnectedUser(getApplicationContext());
        if (user != null)
        {
            String header = "Bearer " + user.getToken();
            Post post = new Post();
            Title title = new Title();
            title.setRendered("Mon titre");
            post.setTitle(title);
            Content content = new Content();
            content.setRendered("Ma description");
            post.setContent(content);
            post.setStatus("publish");

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(EndpointConstants.postsUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            WordPressService apiService = retrofit.create(WordPressService.class);
            Call call = apiService.savePost(header, post);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    if (response.body() != null)
                    {

                    }
                }

                @Override
                public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                    Log.i("onFail", t.getMessage());
                }
            });
        }
    }

}
