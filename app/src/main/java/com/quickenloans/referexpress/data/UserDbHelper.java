package com.quickenloans.referexpress.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jdeville on 6/21/17.
 */

public class UserDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TEAM_MEMBER.db";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + UserContract.User.TABLE_NAME + " (" +
                    UserContract.User._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    UserContract.User.COLUMN_FIRST_NAME + " TEXT," +
                    UserContract.User.COLUMN_LAST_NAME + " TEXT," +
                    UserContract.User.COLUMN_EMAIL + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserContract.User.TABLE_NAME;

    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
