package com.admedia.bendre.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.admedia.bendre.R;
import com.admedia.bendre.api.WordPressService;
import com.admedia.bendre.model.AppUser;
import com.admedia.bendre.util.AuthenticationHelper;
import com.admedia.bendre.util.EndpointConstants;
import com.admedia.bendre.util.MessageUtil;
import com.google.gson.JsonObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyProfileActivity extends AppCompatActivity {
    private TextView mFirstName;
    private TextView mLastName;
    private TextView mPassword;
    private TextView mSignInErrorMessage;
    private TextInputLayout mLayoutFirstName;
    private TextInputLayout mLayoutLastName;
    private TextInputLayout mLayoutPassword;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        AppUser user = AuthenticationHelper.getInstance().getConnectedUser(getApplicationContext());

        mFirstName = findViewById(R.id.firstname);
        mLastName = findViewById(R.id.lastname);
        TextView mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        TextView mEmail = findViewById(R.id.email);

        mLayoutFirstName = findViewById(R.id.layout_firstname);
        mLayoutLastName = findViewById(R.id.layout_lastname);
        mLayoutPassword = findViewById(R.id.layout_password);

        mFirstName.setText(user.getFirstName());
        mLastName.setText(user.getLastName());
        mUsername.setText(user.getUserDisplayName());
        mEmail.setText(user.getUserEmail());
        mPassword.setText(user.getPassword());

        mProgressBar = findViewById(R.id.progressbar);

        showProgress(false);

        Button mUpdateProfile = findViewById(R.id.update_profile_button);
        mUpdateProfile.setOnClickListener(v -> {
            if (isValid())
            {
                updateProfile();
            }
        });
    }

    private boolean isValid() {
        boolean isValid = true;
        if (mFirstName.getText() == null || mFirstName.getText().toString().isEmpty())
        {
            mLayoutFirstName.setError(getString(R.string.requiered_field));
            isValid = false;
        }
        if (mLastName.getText() == null || mLastName.getText().toString().isEmpty())
        {
            mLayoutLastName.setError(getString(R.string.requiered_field));
            isValid = false;
        }
        if (mPassword.getText() == null || mPassword.getText().toString().isEmpty())
        {
            mLayoutPassword.setError(getString(R.string.requiered_field));
            isValid = false;
        }

        return isValid;
    }

    private void updateProfile() {
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String password = mPassword.getText().toString();

        showProgress(true);
        new Thread(new UpdateUserProfile(firstName, lastName, password)).start();
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


    class UpdateUserProfile implements Runnable {
        String firstName;
        String lastName;
        String password;

        UpdateUserProfile(String firstName, String lastName, String password) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.password = password;
        }

        @Override
        public void run() {
            AppUser user = AuthenticationHelper.getInstance().getConnectedUser(getApplicationContext());
            if (user != null)
            {
                boolean needToBeUpdate = false;

                String header = "Bearer " + user.getToken();

                JsonObject object = new JsonObject();
                if (!user.getFirstName().equals(firstName))
                {
                    object.addProperty("first_name", firstName);
                    user.setFirstName(firstName);
                    needToBeUpdate = true;
                }
                if (!user.getLastName().equals(lastName))
                {
                    object.addProperty("last_name", lastName);
                    user.setLastName(lastName);
                    needToBeUpdate = true;
                }
                if (!user.getPassword().equals(password))
                {
                    object.addProperty("password", password);
                    user.setPassword(password);
                    needToBeUpdate = true;
                }

                if (needToBeUpdate)
                {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(EndpointConstants.POSTS_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();

                    WordPressService apiService = retrofit.create(WordPressService.class);
                    Call call = apiService.updateUser(header, object);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) {
                            if (response.body() != null)
                            {
                                showProgress(false);
                                AuthenticationHelper.getInstance().updateUserInfo(getApplicationContext(), user);
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
}
