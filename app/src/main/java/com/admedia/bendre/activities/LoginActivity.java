package com.admedia.bendre.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.admedia.bendre.R;
import com.admedia.bendre.WordPressService;
import com.admedia.bendre.model.AppUser;
import com.admedia.bendre.util.AuthenticationHelper;
import com.admedia.bendre.util.CategoriesUtil;
import com.admedia.bendre.util.EndpointConstants;
import com.admedia.bendre.util.MenuUtil;
import com.admedia.bendre.util.MessageUtil;
import com.admedia.bendre.util.NetworkUtil;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.admedia.bendre.activities.PostDetailsActivity.POST_TYPE;

public class LoginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "SignInActivity";
    private TextView mUsername;
    private TextView mPassword;
    private TextView mSignInErrorMessage;
    private TextInputLayout mLayoutUsername, mLayoutPassword;
    private View mProgressView;
    private View mLoginFormView;
    private String postType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        postType = getIntent().getStringExtra(POST_TYPE);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mUsername = findViewById(R.id.signin_username);
        mPassword = findViewById(R.id.signin_password);

        mLayoutUsername = findViewById(R.id.layout_username);
        mLayoutPassword = findViewById(R.id.layout_password);

        Button mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(view -> {
            if (!validateForm())
            {
                return;
            }
            attemptLogin();
        });
    }

    private void attemptLogin() {
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        if (NetworkUtil.isOnline(this))
        {
            showProgress(true);
            login(username, password);
        }
        else
        {
            MessageUtil.getInstance().ToastMessage(getApplicationContext(), getString(R.string.no_internet_connexion));
        }
    }

    private boolean validateForm() {
        if (TextUtils.isEmpty(mUsername.getText().toString()))
        {
            mLayoutUsername.setError(getString(R.string.error_field_required));
            return false;
        }
        else if (TextUtils.isEmpty(mPassword.getText().toString()))
        {
            mLayoutPassword.setError(getString(R.string.error_field_required));
            return false;
        }
        else
        {
            mLayoutUsername.setError(null);
            mLayoutPassword.setError(null);
            return true;
        }
    }

    private void login(String username, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EndpointConstants.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        WordPressService apiService = retrofit.create(WordPressService.class);
        Call call = apiService.login(username, password);
        call.enqueue(new Callback<LinkedTreeMap>() {

            @Override
            public void onResponse(@NonNull Call<LinkedTreeMap> call, @NonNull Response<LinkedTreeMap> response) {
                if (response.body() != null)
                {
                    Log.i("onResponse", response.body().toString());
                    LinkedTreeMap data = response.body();
                    String token = (String) data.get("token");
                    String userEmail = (String) data.get("user_email");
                    String userNicename = (String) data.get("user_nicename");
                    String userDisplayName = (String) data.get("user_display_name");

                    //get user token and save in db
                    AppUser user = new AppUser(token, userEmail, userNicename, userDisplayName);

                    completeUserInfo(user);
                }
                else
                {
                    TextView textView = findViewById(R.id.invalid_username_password);
                    textView.setVisibility(View.VISIBLE);
                    showProgress(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LinkedTreeMap> call, @NonNull Throwable t) {
                Log.i("onFail", t.getMessage());
                showProgress(false);
                MessageUtil.getInstance().ToastMessage(getApplicationContext(), "Impossible de se connecter, veuillez reessayer plus tard");
            }
        });
    }

    private void completeUserInfo(AppUser user) {
        String header = "Bearer " + user.getToken();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EndpointConstants.postsUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        WordPressService apiService = retrofit.create(WordPressService.class);
        Call<JsonObject> call = apiService.getMe(header);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                try
                {
                    String data = Objects.requireNonNull(response.errorBody()).string();
                    JSONObject object = new JSONObject(data);
                    int userId = object.getInt("id");
                    user.setId(userId);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                AuthenticationHelper.getInstance().login(getApplicationContext(), user);

                Intent intent;
                if (postType != null && !postType.isEmpty())

                {
                    intent = new Intent(getApplicationContext(), PostsActivity.class);
                    intent.putExtra(CategoriesUtil.POST_TYPE, postType);
                }
                else
                {
                    intent = new Intent(getApplicationContext(), MyMenuActivity.class);
                }

                startActivity(intent);

                showProgress(false);
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.i("onFail", t.getMessage());
                showProgress(false);
                MessageUtil.getInstance().ToastMessage(getApplicationContext(), "Impossible de se connecter, veuillez reessayer plus tard");
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.login_activity2, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings)
//        {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuUtil.getInstance().openPage(getApplicationContext(), item.getItemId());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(getApplicationContext(), PostsActivity.class);
//        intent.putExtra(CategoriesUtil.POST_TYPE, getString(R.string.menu_a_la_une));
//        startActivity(intent);
//    }

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
}
