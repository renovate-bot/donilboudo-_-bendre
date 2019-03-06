package com.admedia.bendre.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.admedia.bendre.R;
import com.admedia.bendre.api.WordPressService;
import com.admedia.bendre.model.AppUser;
import com.admedia.bendre.model.user.RegistrationUser;
import com.admedia.bendre.util.AuthenticationHelper;
import com.admedia.bendre.util.EndpointConstants;
import com.admedia.bendre.util.MessageUtil;
import com.admedia.bendre.util.NetworkUtil;
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

public class RegistrationActivity extends AppCompatActivity {
    private TextView mUsername;
    private TextView mPassword;
    private TextView mEmail;
    private TextInputLayout mLayoutUsername, mLayoutPassword, mLayoutEmail;
    private View mProgressView;
    private LinearLayout mRegistrationForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mProgressView = findViewById(R.id.progressbar);
        showProgress(false);

        mUsername = findViewById(R.id.signup_username);
        mPassword = findViewById(R.id.signup_password);
        mEmail = findViewById(R.id.signup_email);

        mLayoutUsername = findViewById(R.id.layout_username);
        mLayoutPassword = findViewById(R.id.layout_password);
        mLayoutEmail = findViewById(R.id.layout_email);

        mRegistrationForm = findViewById(R.id.registration_form);

        Button mLoginButton = findViewById(R.id.registration_button);
        mLoginButton.setOnClickListener(view -> {
            if (!validateForm())
            {
                return;
            }
            attemptRegistration();
        });
    }

    private void attemptRegistration() {
        if (NetworkUtil.isOnline(this))
        {
            String username = mUsername.getText().toString();
            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();

            showProgress(true);
//        mRegistrationForm.setVisibility(View.GONE);
            new Thread(new RegistrationRunnable(username, email, password)).start();
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
        else if (TextUtils.isEmpty(mEmail.getText().toString()))
        {
            mLayoutEmail.setError(getString(R.string.error_field_required));
            return false;
        }
        else
        {
            mLayoutUsername.setError(null);
            mLayoutPassword.setError(null);
            mLayoutEmail.setError(null);
            return true;
        }
    }

    private void showProgress(boolean show) {
        if (!show)
        {
            if (mProgressView != null)
            {
                mProgressView.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            if (mProgressView != null)
            {
                mProgressView.setVisibility(View.VISIBLE);
            }
        }
    }

    class RegistrationRunnable implements Runnable {
        private RegistrationUser user;

        RegistrationRunnable(String username, String email, String password) {
            user = new RegistrationUser(username, email, password);
        }

        @Override
        public void run() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(EndpointConstants.POSTS_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            WordPressService apiService = retrofit.create(WordPressService.class);
            Call call = apiService.register(user);
            call.enqueue(new Callback<LinkedTreeMap>() {

                @Override
                public void onResponse(@NonNull Call<LinkedTreeMap> call, @NonNull Response<LinkedTreeMap> response) {
                    if (response.body() != null)
                    {
                        Log.i("onResponse", response.body().toString());
                        new Thread(new LoginUserRunnable(user)).start();
                    }
                    else
                    {
                        new Handler().post(() -> {
                            mRegistrationForm.setVisibility(View.VISIBLE);
                            TextView textView = findViewById(R.id.invalid_username_password);
                            textView.setVisibility(View.VISIBLE);
                            showProgress(false);
                        });
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LinkedTreeMap> call, @NonNull Throwable t) {
                    Log.i("onFail", t.getMessage());
                    showProgress(false);
                    MessageUtil.getInstance().ToastMessage(getApplicationContext(), getString(R.string.no_internet_connexion));
                }
            });
        }
    }

    class LoginUserRunnable implements Runnable {
        private RegistrationUser registrationUser;

        LoginUserRunnable(RegistrationUser registrationUser) {
            this.registrationUser = registrationUser;
        }

        @Override
        public void run() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(EndpointConstants.WP_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            WordPressService apiService = retrofit.create(WordPressService.class);
            Call call = apiService.login(registrationUser.getUsername(), registrationUser.getPassword());
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
                        AppUser user = new AppUser(token, userEmail, userNicename, userDisplayName, registrationUser.getPassword(), "", "");
                        new Thread(new CompleteUserInfoRunnable(user)).start();
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
                    MessageUtil.getInstance().ToastMessage(getApplicationContext(), getString(R.string.cannot_fetch_data));
                }
            });
        }
    }

    class CompleteUserInfoRunnable implements Runnable {
        private AppUser user;

        CompleteUserInfoRunnable(AppUser user) {
            this.user = user;
        }

        @Override
        public void run() {
            String header = "Bearer " + user.getToken();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(EndpointConstants.POSTS_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            WordPressService apiService = retrofit.create(WordPressService.class);
            Call<LinkedTreeMap> call = apiService.getMe(header);
            call.enqueue(new Callback<LinkedTreeMap>() {
                @Override
                public void onResponse(@NonNull Call<LinkedTreeMap> call, @NonNull Response<LinkedTreeMap> response) {
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

                    showProgress(false);
                    Intent intent = new Intent(getApplicationContext(), MyMenuActivity.class);
                    startActivity(intent);
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
