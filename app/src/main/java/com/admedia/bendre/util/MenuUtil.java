package com.admedia.bendre.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.admedia.bendre.R;
import com.admedia.bendre.activities.ContactActivity;
import com.admedia.bendre.activities.KiosqueActivity;
import com.admedia.bendre.activities.LoginActivity;
import com.admedia.bendre.activities.MyMenuActivity;
import com.admedia.bendre.activities.PostsActivity;
import com.admedia.bendre.activities.SettingsActivity;
import com.admedia.bendre.activities.WebTvActivity;
import com.admedia.bendre.model.AppUser;

import static com.admedia.bendre.activities.PostDetailsActivity.POST_TYPE;
import static com.admedia.bendre.util.Constants.USE_CACHE_DATA;

public class MenuUtil {
    private static MenuUtil instance = null;

    public static MenuUtil getInstance() {
        if (instance == null)
        {
            instance = new MenuUtil();
        }

        return instance;
    }

    public void openPage(Context context, int selectedId) {
        Intent intent;

        switch (selectedId)
        {
            case R.id.nav_a_la_une:
                openPostPage(context, context.getString(R.string.menu_a_la_une));
                break;

            case R.id.nav_bendrekan:
                intent = new Intent(context, KiosqueActivity.class);
                intent.putExtra(USE_CACHE_DATA, false);
                context.startActivity(intent);
                break;

            case R.id.nav_burkina:
                openPostPage(context, context.getString(R.string.menu_burkina));
                break;

            case R.id.nav_international:
                openPostPage(context, context.getString(R.string.menu_international));
                break;

            case R.id.nav_opportunite:
                openPostPage(context, context.getString(R.string.menu_opportunite));
                break;

            case R.id.nav_labo:
                openPostPage(context, context.getString(R.string.menu_labo));
                break;

            case R.id.nav_contributeur:
                String url = "https://www.bendre.bf/comment-contribuer/";
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
                break;

            case R.id.nav_webtv:
                context.startActivity(new Intent(context, WebTvActivity.class));
                break;

            case R.id.nav_contact:
                context.startActivity(new Intent(context, ContactActivity.class));
                break;

            case R.id.nav_settings:
                context.startActivity(new Intent(context, SettingsActivity.class));
                break;
        }
    }

    private void openPostPage(Context context, String title) {
        Intent intent = new Intent(context, PostsActivity.class);
        intent.putExtra(POST_TYPE, title);
        intent.putExtra(USE_CACHE_DATA, false);
        context.startActivity(intent);
    }

    private void logout() {

    }

    public void setNavigationView(Activity activity, NavigationView navigationView, DrawerLayout drawer) {
        AppUser currentUser = AuthenticationHelper.getInstance().getConnectedUser(activity.getApplicationContext());
        Button mLogoutButton = navigationView.findViewById(R.id.logout_button);

        if (currentUser != null)
        {
            View loginHeader = LayoutInflater.from(activity).inflate(R.layout.nav_header_connected, navigationView, false);

            TextView username = loginHeader.findViewById(R.id.connected_username);
            username.setText(currentUser.getUserDisplayName());

            Button mMyProfileButton = loginHeader.findViewById(R.id.my_profile_button);
            mMyProfileButton.setOnClickListener(v -> activity.startActivity(new Intent(activity.getApplicationContext(), MyMenuActivity.class)));

            mLogoutButton.setOnClickListener(v -> {
                AuthenticationHelper.getInstance().logout(activity.getApplicationContext(), currentUser.getId());
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(activity.getApplicationContext(), PostsActivity.class);
                intent.putExtra(POST_TYPE, activity.getString(R.string.menu_a_la_une));
                activity.startActivity(intent);
            });

            navigationView.addHeaderView(loginHeader);
        }
        else
        {
            View logoutHeader = LayoutInflater.from(activity).inflate(R.layout.nav_header_not_connected, navigationView, false);
            Button signInButton = logoutHeader.findViewById(R.id.login_button);
            signInButton.setOnClickListener(v -> activity.startActivity(new Intent(activity.getApplicationContext(), LoginActivity.class)));

            if (navigationView.getHeaderCount() == 0)
            {
                navigationView.addHeaderView(logoutHeader);
            }

            if (mLogoutButton != null)
            {
                mLogoutButton.setVisibility(View.GONE);
            }
        }
    }
}
