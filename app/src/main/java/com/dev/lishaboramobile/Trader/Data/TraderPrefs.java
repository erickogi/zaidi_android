package com.dev.lishaboramobile.Trader.Data;

import android.content.Context;
import android.content.SharedPreferences;

import com.dev.lishaboramobile.Admin.Models.AdminModel;

public class TraderPrefs {
    // Shared preferences file name

    private static final String PREF_NAME = "lishaboraprefs";

    // All Shared Preferences Keys

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";


    private static final String names = "names";

    private static final String mobile = "UserId";
    private static final String email = "UserName";
    private static final String department = "FirstName";
    private static final String password = "LastName";
    private static final String apikey = "Email";
    private static final String firebasetoken = "Email";
    private static final String status = "PhoneNumber";
    private static final String transactiontime = "Designation";

    // Shared Preferences

    SharedPreferences pref;

    // Editor for Shared preferences

    SharedPreferences.Editor editor;

    // Context

    Context _context;

    // Shared pref mode

    int PRIVATE_MODE = 0;



    public TraderPrefs(Context context) {
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
        editor.putString(names,admin.getNames());
        editor.putString(mobile,admin.getMobile());
        editor.putString(email,admin.getEmail());
        editor.putString(department,admin.getDepartment());
        editor.putString(password,admin.getPassword());
        editor.putString(apikey,admin.getApikey());
        editor.putString(firebasetoken,admin.getFirebasetoken());
        editor.putString(status,admin.getStatus());
        editor.putString(transactiontime,admin.getTransactiontime());
        editor.commit();

    }

    public AdminModel getAdmin(){
        AdminModel adminModel=new AdminModel();
        adminModel.setNames(pref.getString(names,null));
        adminModel.setEmail(pref.getString(email,null));
        adminModel.setMobile(pref.getString(mobile,null));
        adminModel.setDepartment(pref.getString(department,null));
        adminModel.setPassword(pref.getString(password,null));
        adminModel.setApikey(pref.getString(apikey,null));
        adminModel.setFirebasetoken(pref.getString(firebasetoken,null));
        adminModel.setStatus(pref.getString(status,null));
        adminModel.setTransactiontime(pref.getString(transactiontime,null));

        return adminModel;

    }



}
