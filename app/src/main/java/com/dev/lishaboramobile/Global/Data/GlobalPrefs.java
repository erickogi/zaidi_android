package com.dev.lishaboramobile.Global.Data;

import android.content.Context;
import android.content.SharedPreferences;

public class GlobalPrefs {

    private static final String PREF_NAME = "lishaboraglobalprefs";

    private static final String KEY_USER_LOGGED_IN = "userLoggedIn";

    SharedPreferences pref;

    // Editor for Shared preferences

    SharedPreferences.Editor editor;

    // Context

    Context _context;

    // Shared pref mode

    int PRIVATE_MODE = 0;



    public GlobalPrefs(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public int userLoggedIn(){
        return pref.getInt(KEY_USER_LOGGED_IN,0);
    }

    public void setUserLoggedIn(int userLoggedIn){
        editor.putInt(KEY_USER_LOGGED_IN,userLoggedIn);
        editor.commit();
    }


}
