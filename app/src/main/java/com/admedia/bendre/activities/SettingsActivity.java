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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Switch;

import com.admedia.bendre.R;
import com.admedia.bendre.database.DBManager;
import com.admedia.bendre.util.MenuUtil;
import com.onesignal.OneSignal;

import static com.admedia.bendre.activities.PostDetailsActivity.POST_TYPE;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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

        DBManager manager = new DBManager(getApplicationContext());
        manager.open();
        manager.getUserPreferences();
        manager.close();

        Switch mSwitchNotification = findViewById(R.id.switch_notification);
        mSwitchNotification.setChecked(true);
        mSwitchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked)
            {
                OneSignal.setSubscription(false);
                //TODO save to db this preference
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
