package com.admedia.bendre.activities;

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
import com.admedia.bendre.fragments.VideoFragment;
import com.admedia.bendre.model.Video;
import com.admedia.bendre.util.MenuUtil;

public class WebTvActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ChannelFragment.OnFragmentInteractionListener,
        VideoFragment.OnListFragmentInteractionListener {

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
        adapter.addFragment(new ChannelFragment(), "Chaine");
        adapter.addFragment(new VideoFragment(), "Videos");
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Video item) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            finishAffinity();
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
