package com.admedia.bendre.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.admedia.bendre.R;
import com.admedia.bendre.util.AuthenticationHelper;
import com.admedia.bendre.util.MenuUtil;

import static com.admedia.bendre.activities.PostsActivity.SHOW_ONLY_MY_POSTS;

public class MyMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MenuUtil.getInstance().setNavigationView(this, navigationView, drawer);

        TextView username = findViewById(R.id.connected_username);
        username.setText(AuthenticationHelper.getInstance().getConnectedUser(getApplicationContext()).getUserDisplayName());
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuUtil.getInstance().openPage(getApplicationContext(), item.getItemId());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void newPost(View view) {
        Intent intent = new Intent(getApplicationContext(), NewPostActivity.class);
        startActivity(intent);
    }

    public void myPosts(View view) {
        Intent intent = new Intent(getApplicationContext(), PostsActivity.class);
        intent.putExtra(SHOW_ONLY_MY_POSTS, true);
        startActivity(intent);
    }

    public void myProfile(View view) {
        startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
    }
}
