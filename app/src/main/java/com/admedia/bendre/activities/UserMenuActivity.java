package com.admedia.bendre.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.admedia.bendre.R;
import com.admedia.bendre.model.AppUser;
import com.admedia.bendre.util.AuthenticationHelper;
import com.admedia.bendre.util.MenuUtil;

import static com.admedia.bendre.activities.PostDetailsActivity.POST_TYPE;
import static com.admedia.bendre.activities.PostsActivity.SHOW_ONLY_MY_POSTS;

public class UserMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AppUser connectedUser;
    private AppUser loggedInUser;

    private Button mLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        setContentView(R.layout.activity_user_menu);
        mLogout = findViewById(R.id.logout_button);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        DrawerLayout drawer = findViewById(R.id.drawer_layout_menu_user);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
//            @Override
//            public void onDrawerSlide(@NonNull View view, float v) {
//                if (loggedInUser != null)
//                {
//                    TextView textView = view.findViewById(R.id.textView5);
//                    textView.setVisibility(View.GONE);
//
//                    Button button = view.findViewById(R.id.login_button);
//                    button.setText("Mon menu");
//                    button.setOnClickListener(v1 -> {
//                        Intent intent = new Intent(getApplicationContext(), UserMenuActivity.class);
//                        startActivity(intent);
//                    });
//                }
//            }
//
//            @Override
//            public void onDrawerOpened(@NonNull View view) {
//            }
//
//            @Override
//            public void onDrawerClosed(@NonNull View view) {
//            }
//
//            @Override
//            public void onDrawerStateChanged(int i) {
//            }
//        });
//
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        if (loggedInUser != null)
//        {
//            mLogout.setVisibility(View.VISIBLE);
//
//            View headerView = navigationView.getHeaderView(0);
//            TextView textView = headerView.findViewById(R.id.textView5);
//            textView.setVisibility(View.GONE);
//
//            Button button = headerView.findViewById(R.id.login_button);
//            button.setText("Mon menu");
//            button.setOnClickListener(v -> {
//                Intent intent = new Intent(getApplicationContext(), UserMenuActivity.class);
//                startActivity(intent);
//            });
//        }
//        else
//        {
//            mLogout.setVisibility(View.GONE);
//
//            View headerView = navigationView.getHeaderView(0);
//            TextView textView = headerView.findViewById(R.id.textView5);
//            textView.setVisibility(View.VISIBLE);
//            Button button = headerView.findViewById(R.id.login_button);
//            button.setText(R.string.se_connecter);
//            button.setOnClickListener(v -> {
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intent);
//            });
//        }

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("Menu");
        }

        connectedUser = AuthenticationHelper.getInstance().getConnectedUser(getApplicationContext());
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), PostsActivity.class);
        intent.putExtra(POST_TYPE, getString(R.string.menu_a_la_une));
        startActivity(intent);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuUtil.getInstance().openPage(getApplicationContext(), item.getItemId());
        DrawerLayout drawer = findViewById(R.id.drawer_layout_posts);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
