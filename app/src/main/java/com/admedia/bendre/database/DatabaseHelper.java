package com.admedia.bendre.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    static final String APP_USER_TABLE_NAME = "APP_USER";
    static final String USER_PREFERENCES_TABLE_NAME = "USER_PREFERENCE";

    // Table columns
    static final String _ID = "_id";
    private static final String DEVICE_ID = "deviceID";

    //APP USER table columns
    static final String TOKEN = "token";
    static final String EMAIL = "user_email";
    static final String NICE_NAME = "nicename";
    static final String DISPLAY_NAME = "display_name";
    static final String PASSWORD = "password";
    static final String FIRST_NAME = "first_name";
    static final String LAST_NAME = "last_name";

    //USER PREFERENCES columns
    static final String ENABLE_NOTIFICATION = "enable_notification";

    // Database Information
    private static final String DB_NAME = "BENDRE.DB";

    // database version
    private static final int DB_VERSION = 1;

    // Creating user table query
    private static final String CREATE_APP_USER_TABLE = "create table "
            + APP_USER_TABLE_NAME
            + "("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TOKEN + " TEXT NOT NULL, "
            + EMAIL + " TEXT, "
            + NICE_NAME + " TEXT, "
            + DISPLAY_NAME + " TEXT, "
            + PASSWORD + " TEXT, "
            + FIRST_NAME + " TEXT, "
            + LAST_NAME + " TEXT "
            + ")";

    // Creating user table query
    private static final String CREATE_USER_PREFERENCES_TABLE = "create table "
            + USER_PREFERENCES_TABLE_NAME
            + "("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ENABLE_NOTIFICATION + " INTEGER NOT NULL "
            + ")";

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_APP_USER_TABLE);
        db.execSQL(CREATE_USER_PREFERENCES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + APP_USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USER_PREFERENCES_TABLE_NAME);
        onCreate(db);
    }
}