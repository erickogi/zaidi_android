package com.dev.lishaboramobile.Admin.Data;

import android.content.Context;
import android.content.SharedPreferences;

import com.dev.lishaboramobile.Admin.Models.AdminModel;

public class AdminPrefs {
    // Shared preferences file name

    private static final String PREF_NAME = "lishaboraadminprefs";

    // All Shared Preferences Keys

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";


    private static final String NAMES = "names";

    private static final String MOBILE = "mobile";
    private static final String EMAIL = "email";
    private static final String DEPARTMENT = "department";
    private static final String PASSWORD = "password";
    private static final String APIKEY = "apikey";
    private static final String FIREBASE_TOKEN = "firebasetoken";
    private static final String STATUS = "status";
    private static final String TRANSACTION_TIME = "transaction_time";

    // Shared Preferences

    SharedPreferences pref;

    // Editor for Shared preferences

    SharedPreferences.Editor editor;

    // Context

    Context _context;

    // Shared pref mode

    int PRIVATE_MODE = 0;



    public AdminPrefs(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGED_IN,false);
    }

    public void setIsLoggedIn(boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGGED_IN,isLoggedIn);
        editor.commit();
    }


    public void setAdmin(AdminModel admin){
        editor.putString(NAMES,admin.getNames());
        editor.putString(MOBILE,admin.getMobile());
        editor.putString(EMAIL,admin.getEmail());
        editor.putString(DEPARTMENT,admin.getDepartment());
        editor.putString(PASSWORD,admin.getPassword());
        editor.putString(APIKEY,admin.getApikey());
        editor.putString(FIREBASE_TOKEN,admin.getFirebasetoken());
        editor.putString(STATUS,admin.getStatus());
        editor.putString(TRANSACTION_TIME,admin.getTransactiontime());
        editor.commit();

    }

    public AdminModel getAdmin(){
        AdminModel adminModel=new AdminModel();
        adminModel.setNames(pref.getString(NAMES,null));
        adminModel.setEmail(pref.getString(EMAIL,null));
        adminModel.setMobile(pref.getString(MOBILE,null));
        adminModel.setDepartment(pref.getString(DEPARTMENT,null));
        adminModel.setPassword(pref.getString(PASSWORD,null));
        adminModel.setApikey(pref.getString(APIKEY,null));
        adminModel.setFirebasetoken(pref.getString(FIREBASE_TOKEN,null));
        adminModel.setStatus(pref.getString(STATUS,null));
        adminModel.setTransactiontime(pref.getString(TRANSACTION_TIME,null));

        return adminModel;

    }



}
