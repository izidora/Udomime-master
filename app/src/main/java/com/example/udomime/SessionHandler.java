package com.example.udomime;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by Abhi on 20 Jan 2018 020.
 */

public class SessionHandler {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_EMPTY = "";
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    //ja dodajem
    private static final String KEY_ID="id";
    private static final String KEY_ULOGA="uloga";
    private static final String KEY_GRAD="grad";
    private static final String KEY_EMAIL="email";
    private static final String KEY_ZUPANIJA="zupanija";
    //ja


    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }

    /**
     * Logs in the user by saving user details and setting session
     *
     * @param username
     * @param fullName
     */

    public void loginUser(String username, String fullName,Integer id,Integer uloga,String email,String grad, String zupannija) {
        mEditor.putString(KEY_USERNAME, username);
        mEditor.putString(KEY_FULL_NAME, fullName);
        //ja dodajem
        mEditor.putInt(KEY_ID,id);
        mEditor.putInt(KEY_ULOGA,uloga);
        mEditor.putString(KEY_EMAIL,email);
        mEditor.putString(KEY_GRAD,grad);
        mEditor.putString(KEY_ZUPANIJA,zupannija);
        //ja
        Toast.makeText(mContext.getApplicationContext(),"HEJUsaosam",Toast.LENGTH_SHORT).show();

        Date date = new Date();

        //Set user session for next 7 days
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

    /**
     * Checks whether user is logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        Date currentDate = new Date();

        long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        /* If shared preferences does not have a value
         then user is not logged in
         */
        if (millis == 0) {
            return false;
        }
        Date expiryDate = new Date(millis);

        /* Check if session is expired by comparing
        current date and Session expiry date
        */
        return currentDate.before(expiryDate);
    }

    /**
     * Fetches and returns user details
     *
     * @return user details
     */
    public User getUserDetails() {
        //Check if user is logged in first
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setUsername(mPreferences.getString(KEY_USERNAME, KEY_EMPTY));
        user.setFullName(mPreferences.getString(KEY_FULL_NAME, KEY_EMPTY));
        //ja dodajem
        user.setId(mPreferences.getInt(KEY_ID, 0));
        user.setUloga(mPreferences.getInt(KEY_ULOGA ,0));
        user.setEmail((mPreferences.getString(KEY_EMAIL,KEY_EMPTY)));
        user.setGrad((mPreferences.getString(KEY_GRAD,KEY_EMPTY)));
        user.setZupanija((mPreferences.getString(KEY_ZUPANIJA,KEY_EMPTY)));
        //ja

        user.setSessionExpiryDate(new Date(mPreferences.getLong(KEY_EXPIRES, 0)));

        return user;
    }

    /**
     * Logs out user by clearing the session
     */
    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();
    }

}