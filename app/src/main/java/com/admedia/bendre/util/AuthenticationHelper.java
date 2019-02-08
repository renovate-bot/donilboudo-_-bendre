package com.admedia.bendre.util;

import android.content.Context;

import com.admedia.bendre.database.DBManager;
import com.admedia.bendre.model.AppUser;

public class AuthenticationHelper {
    public static AuthenticationHelper instance = null;
    private AppUser connectedUser;

    private AuthenticationHelper() {
    }

    public static AuthenticationHelper getInstance() {
        if (instance == null)
        {
            instance = new AuthenticationHelper();
        }

        return instance;
    }

    public void setConnectedUser(AppUser connectedUser) {
        this.connectedUser = connectedUser;
    }

    public AppUser getConnectedUser(Context context) {
        if (connectedUser == null)
        {

            DBManager dbManager = new DBManager(context);
            dbManager.open();
            connectedUser = dbManager.getUser();
            dbManager.close();
        }
        return connectedUser;
    }

    public void login(Context context, AppUser user) {
        this.connectedUser = user;

        DBManager dbManager = new DBManager(context);
        dbManager.open();
        dbManager.insertUser(user);
        dbManager.close();
    }

    public void logout(Context context, int id) {
        DBManager dbManager = new DBManager(context);
        dbManager.open();
        dbManager.deleteUser(id);
        dbManager.close();
        this.connectedUser = null;
    }
}
