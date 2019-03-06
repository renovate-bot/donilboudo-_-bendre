package com.admedia.bendre.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.admedia.bendre.model.AppUser;
import com.admedia.bendre.model.UserPreferences;

public class DBManager {
    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public void open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public UserPreferences getUserPreferences() {
        UserPreferences preferences = null;
        try
        {
            String[] columns = new String[]{DatabaseHelper._ID, DatabaseHelper.ENABLE_NOTIFICATION};
            Cursor cursor = database.query(DatabaseHelper.USER_PREFERENCES_TABLE_NAME, columns, null, null, null, null, null);
            if (cursor != null)
            {
                if (cursor.moveToFirst())
                {
                    while (!cursor.isAfterLast())
                    {
                        int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID));
                        int enableNotif = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ENABLE_NOTIFICATION));

                        preferences = new UserPreferences(id, enableNotif);
                        cursor.moveToNext();
                    }
                }
                cursor.close();
            }
        }
        catch (SQLiteException ex)
        {
            return null;
        }

        return preferences;
    }

    public void addPreferences(UserPreferences preferences) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.ENABLE_NOTIFICATION, preferences.isEnableNotifications() ? 1 : 0);
        database.insert(DatabaseHelper.USER_PREFERENCES_TABLE_NAME, null, contentValue);
    }

    public int updateUserPreferences(long _id, boolean enableNotif) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ENABLE_NOTIFICATION, enableNotif);
        return database.update(DatabaseHelper.USER_PREFERENCES_TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
    }

    public void insertUser(AppUser user) {
        if (isUserAlreadyExist(user.getUserEmail()))
        {
            ContentValues contentValue = new ContentValues();
            contentValue.put(DatabaseHelper.TOKEN, user.getToken());
            contentValue.put(DatabaseHelper.EMAIL, user.getUserEmail());
            contentValue.put(DatabaseHelper.NICE_NAME, user.getUserNicename());
            contentValue.put(DatabaseHelper.DISPLAY_NAME, user.getUserDisplayName());
            contentValue.put(DatabaseHelper.PASSWORD, user.getPassword());
            contentValue.put(DatabaseHelper.PASSWORD, user.getPassword());

            database.insert(DatabaseHelper.APP_USER_TABLE_NAME, null, contentValue);
        }
        else
        {
//            updateUser()
        }
    }

    private boolean isUserAlreadyExist(String email) {
        boolean alreadyExist = false;
        try
        {
            String[] columns = new String[]{DatabaseHelper._ID, DatabaseHelper.TOKEN, DatabaseHelper.EMAIL, DatabaseHelper.NICE_NAME, DatabaseHelper.DISPLAY_NAME};
            String[] selectionArgs = new String[]{email};
            try (Cursor cursor = database.query(DatabaseHelper.APP_USER_TABLE_NAME, columns, DatabaseHelper.EMAIL + "=?", selectionArgs, null, null, null))
            {
                if (cursor != null)
                {
                    if (cursor.getCount() > 0)
                    {
                        alreadyExist = true;
                    }
                }
            }
        }
        catch (SQLiteException ex)
        {
        }

        return alreadyExist;
    }

    public AppUser getUser() {
        AppUser user = null;
        try
        {
            String[] columns = new String[]{DatabaseHelper._ID, DatabaseHelper.TOKEN, DatabaseHelper.EMAIL, DatabaseHelper.NICE_NAME, DatabaseHelper.DISPLAY_NAME};
            Cursor cursor = database.query(DatabaseHelper.APP_USER_TABLE_NAME, columns, null, null, null, null, null);
            if (cursor != null)
            {
                if (cursor.moveToFirst())
                {
                    while (!cursor.isAfterLast())
                    {
                        String id = cursor.getString(cursor.getColumnIndex(DatabaseHelper._ID));
                        String token = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TOKEN));
                        String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EMAIL));
                        String niceName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NICE_NAME));
                        String displayName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DISPLAY_NAME));
                        String password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.PASSWORD));
                        String firstName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FIRST_NAME));
                        String lastName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LAST_NAME));

                        user = new AppUser(token, email, niceName, displayName, password, firstName, lastName);

                        cursor.moveToNext();
                    }
                }
                cursor.close();
            }
        }
        catch (SQLiteException ex)
        {
            return null;
        }
        return user;
    }

    public int updateUser(long _id, String token) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TOKEN, token);
        return database.update(DatabaseHelper.APP_USER_TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
    }

    public void updateUserInfo(AppUser user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.FIRST_NAME, user.getFirstName());
        contentValues.put(DatabaseHelper.LAST_NAME, user.getLastName());
        contentValues.put(DatabaseHelper.PASSWORD, user.getPassword());
        database.update(DatabaseHelper.APP_USER_TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + user.getId(), null);
    }


    public void deleteUser(long _id) {
        database.delete(DatabaseHelper.APP_USER_TABLE_NAME, DatabaseHelper._ID + "=?", new String[]{"6"});
    }

}
