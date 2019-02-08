package com.admedia.bendre.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.admedia.bendre.R;
import com.admedia.bendre.database.DBManager;
import com.admedia.bendre.model.UserPreferences;
import com.onesignal.OneSignal;

import static com.admedia.bendre.activities.PostDetailsActivity.POST_TYPE;
import static com.admedia.bendre.util.Constants.USE_CACHE_DATA;

public class SplashActivity extends AppCompatActivity {
    //Duration of wait
    private final static int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //we will subscribe this device to a topic
//        NotificationHelper.getInstance().subscribeToTopic();

        DBManager manager = new DBManager(getApplicationContext());
        manager.open();
        UserPreferences preferences = manager.getUserPreferences();
        if (preferences == null)
        {
            preferences = new UserPreferences();
            preferences.setEnableNotifications(true);
            manager.addPreferences(preferences);

            // OneSignal Initialization
            OneSignal.startInit(this)
                    .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                    .unsubscribeWhenNotificationsAreDisabled(true)
                    .init();
        }
        manager.close();

        /* New Handler to start the Menu-Activity and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), PostsActivity.class);
                intent.putExtra(POST_TYPE, getString(R.string.menu_a_la_une));
                intent.putExtra(USE_CACHE_DATA, false);
                startActivity(intent);

                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
