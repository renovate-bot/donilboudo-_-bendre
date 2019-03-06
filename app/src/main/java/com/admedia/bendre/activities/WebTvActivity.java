package com.admedia.bendre.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.admedia.bendre.R;
import com.admedia.bendre.adapters.ViewPagerAdapter;
import com.admedia.bendre.fragments.ChannelFragment;
import com.admedia.bendre.fragments.ChannelVideosFragment;
import com.admedia.bendre.model.Video;
import com.admedia.bendre.util.MenuUtil;

import static com.admedia.bendre.activities.PostDetailsActivity.POST_TYPE;

public class WebTvActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ChannelFragment.OnFragmentInteractionListener,
        ChannelVideosFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_tv);
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

        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs_menu);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new ChannelFragment(), "Chaine");
        adapter.addFragment(new ChannelVideosFragment(), "Videos");
        viewPager.setAdapter(adapter);
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
            Intent intent = new Intent(getApplicationContext(), PostsActivity.class);
            intent.putExtra(POST_TYPE, getString(R.string.menu_a_la_une));
            startActivity(intent);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Video item) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK)
//        {
////            if (doubleBackToExitPressedOnce)
////            {
////                finishAffinity();
////                System.exit(0);
////                return true;
////            }
////            this.doubleBackToExitPressedOnce = true;
////            MessageUtil.getInstance().ToastMessage(getApplicationContext(), getString(R.string.lbl_press_back_to_exit));
////            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 5000);
//        }
        return super.onKeyDown(keyCode, event);
    }
}
