package com.quickenloans.referexpress.data;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jdeville on 6/21/17.
 */

public class UserDAO {
    private UserDbHelper mDbHelper;
    private SQLiteDatabase db;

    public static final String PREF_FILE_NAME = "test_prefs";
    public static final String PREF_DEFAULT_USER_ID = "default_user_id";

    private static SharedPreferences mPrefs;
    private static SharedPreferences.Editor mPrefEditor;

    public UserDAO(Context context) {
        mDbHelper = new UserDbHelper(context);
        db = mDbHelper.getWritableDatabase();

        mPrefs = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        mPrefEditor = mPrefs.edit();

    }

    public List<User> selectAll() {
        final String selectQuery = "SELECT * FROM " + UserContract.User.TABLE_NAME;

        db = mDbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(selectQuery, null);


        List<User> userList = new ArrayList<>();

        User item;

        while (c.moveToNext()) {
            item = new User();

            item.setId(c.getInt(c.getColumnIndex(UserContract.User._ID)));
            item.setFirstName(c.getString(c.getColumnIndex(UserContract.User.COLUMN_FIRST_NAME)));
            item.setLastName(c.getString(c.getColumnIndex(UserContract.User.COLUMN_LAST_NAME)));
            item.setEmail(c.getString(c.getColumnIndex(UserContract.User.COLUMN_EMAIL)));


            userList.add(item);

            item = null;


            for (int i = 0; i < userList.size(); i++) {
                Log.e("selectAll itteration:", "loop" + i + " - First Name: " + userList.get(i).getFirstName());
            }

        }


        // test
        for (User user : userList) {
            Log.e("selectAll - user Tes", user.getFirstName());
        }

        c.close();
        db.close();
        return userList;

    }

    public List<String> selectAllNames(List <User> allUsers){

        List<String> fullNameList = new ArrayList<>();

        for (User currentUser : allUsers){
            fullNameList.add(currentUser.getFullName());
        }

        return fullNameList;
    }

    public User select(int id) {
        final String selectQuery = "SELECT * FROM " + UserContract.User.TABLE_NAME
                + " WHERE " + UserContract.User._ID + " = " + id;

        db = mDbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(selectQuery, null);

        User user = new User();

        if (c.moveToFirst()){


                    user.setId(c.getInt(c.getColumnIndex(UserContract.User._ID)));
                    user.setFirstName(c.getString(c.getColumnIndex(UserContract.User.COLUMN_FIRST_NAME)));
                    user.setLastName(c.getString(c.getColumnIndex(UserContract.User.COLUMN_LAST_NAME)));
                    user.setEmail(c.getString(c.getColumnIndex(UserContract.User.COLUMN_EMAIL)));

        }

        c.close();
        db.close();
        return user;
    }

    public int insert(User user) {

        db = mDbHelper.getWritableDatabase();

        Log.e("insert *---------*",
                user.getId() + " "
                        + user.getFirstName() + " "
                        + user.getLastName() + " "
                        + user.getEmail());

        ContentValues values = new ContentValues();
        values.put(UserContract.User.COLUMN_FIRST_NAME, user.getFirstName());
        values.put(UserContract.User.COLUMN_LAST_NAME, user.getLastName());
        values.put(UserContract.User.COLUMN_EMAIL, user.getEmail());

        int id = (int) db.insert(UserContract.User.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public void insert(List<User> users) {

    }

    public long update(int id, User user){
        db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserContract.User.COLUMN_FIRST_NAME, user.getFirstName());
        values.put(UserContract.User.COLUMN_LAST_NAME, user.getLastName());
        values.put(UserContract.User.COLUMN_EMAIL, user.getEmail());

        int updatedRow = (int) db.update(UserContract.User.TABLE_NAME, values, UserContract.User._ID + " = " + id, null);
        db.close();
        return updatedRow;
    }

    public long delete(long id){
        db = mDbHelper.getWritableDatabase();

        int removedRow = db.delete(UserContract.User.TABLE_NAME, UserContract.User._ID + " = " + id, null);
        db.close();
        return removedRow;
    }

    public void updateDefaultUser(int id){
        mPrefEditor.putInt(PREF_DEFAULT_USER_ID, id).commit();
    }

    /**
     * Assigns a new default user ID to SharedPreferences' default_user preference.
     * Selects the newest User entry in the USER database table.
     * If no default user is found, assigns -1, instead.
     */
    public void lottoNewDefaultUser(){
        final List<User> userList = selectAll();
        final int listSize = userList.size();

        if (listSize > 0){
            if (listSize == 1){
                updateDefaultUser(userList.get(0).getId());
            } else {
                updateDefaultUser(userList.get(listSize - 1).getId());
            }
        } else {
            updateDefaultUser(-1);
        }
    }

    public User getDefaultUser(){
        int id = getDefaultUserId();

        if (id != -1){
            return select(id);
        } else {
            return new User();
        }
    }

    public int getDefaultUserId(){
        return mPrefs.getInt(PREF_DEFAULT_USER_ID, -1);
    }
}
