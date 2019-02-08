package com.admedia.bendre.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.admedia.bendre.R;
import com.admedia.bendre.model.AppUser;
import com.admedia.bendre.util.AuthenticationHelper;

import java.util.Objects;

public class MyProfileActivity extends AppCompatActivity {
    private TextView mFirstName, mLastName, mUsername, mPassword, mEmail;
    private TextView mSignInErrorMessage;
    private TextInputLayout mLayoutFirstName, mLayoutLastName, mLayoutUsername, mLayoutPassword, mLayoutEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mFirstName = findViewById(R.id.firstname);
        mLastName = findViewById(R.id.lastname);
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mEmail = findViewById(R.id.email);

        mLayoutFirstName = findViewById(R.id.layout_firstname);
        mLayoutLastName = findViewById(R.id.layout_lastname);
        mLayoutEmail = findViewById(R.id.layout_email);
        mLayoutUsername = findViewById(R.id.layout_username);
        mLayoutPassword = findViewById(R.id.layout_password);

        AppUser user = AuthenticationHelper.getInstance().getConnectedUser(getApplicationContext());
//        mFirstName.setText(user.get);

    }

}
