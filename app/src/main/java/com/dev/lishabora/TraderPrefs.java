package com.dev.lishabora;

import android.content.Context;
import android.content.SharedPreferences;

import com.dev.lishabora.Models.Trader.TraderModel;

public class TraderPrefs {
    // Shared preferences file name

    private static final String PREF_NAME = "lishaboratraderprefs";

    // All Trader Preferences Keys

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private static final String code ="code";
    private static final String entity="entity" ;
    private static final String entitycode="entitycode" ;
    private static final String transactioncode="transactioncode";

    private static final String names="names" ;
    private static final String mobile="mobile" ;
    private static final String password="password" ;
    private static final String apikey="apikey" ;
    private static final String firebasetoken="firebasetoken" ;
    private static final String status ="status";
    private static final String transactiontime="transactiontime" ;
    private static final String synctime ="synctime";
    private static final String transactedby="transactedby" ;

    private static final String issynced ="issynced";
    private static final String isdummy="isdummy" ;


   

//    private static final String names = "names";
//
//    private static final String mobile = "UserId";
//    private static final String email = "UserName";
//    private static final String department = "FirstName";
//    private static final String password = "LastName";
//    private static final String apikey = "Email";
//    private static final String firebasetoken = "Email";
//    private static final String status = "PhoneNumber";
//    private static final String transactiontime = "Designation";

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

   
    public void setAdmin(TraderModel trader){
        editor.putString(names,trader.getNames());
        editor.putString(mobile,trader.getMobile());
        editor.putString(code,trader.getCode());
        editor.putString(entity,trader.getEntity());
        editor.putString(entitycode,trader.getEntitycode());
        editor.putString(transactioncode,trader.getTransactioncode());
        editor.putString(synctime,trader.getSynctime());
        editor.putString(transactedby,trader.getTransactedby());
        editor.putInt(issynced,trader.getSynced());
        editor.putInt(isdummy,trader.getDummy());
        editor.putString(password,trader.getPassword());
        editor.putString(apikey,trader.getApikey());
        editor.putString(firebasetoken,trader.getFirebasetoken());
        editor.putString(status,trader.getStatus());
        editor.putString(transactiontime,trader.getTransactiontime());
        editor.commit();

    }
//    private static final String code ="code";
//    private static final String entity="entity" ;
//    private static final String entitycode="entitycode" ;
//    private static final String transactioncode="transactioncode";
//    private static final String synctime ="synctime";
//    private static final String transactedby="transactedby" ;
//    private static final String issynced ="issynced";
//    private static final String isdummy="isdummy" ;



    public TraderModel getTraderModel(){
        TraderModel traderModel=new TraderModel();
        traderModel.setNames(pref.getString(names,null));
        traderModel.setCode(pref.getString(code,null));
        traderModel.setEntity(pref.getString(entity,null));
        traderModel.setEntitycode(pref.getString(entitycode,null));
        traderModel.setTransactioncode(pref.getString(transactioncode,null));
        traderModel.setSynctime(pref.getString(synctime,null));
        traderModel.setMobile(pref.getString(mobile,null));
        traderModel.setTransactedby(pref.getString(transactedby,null));
        traderModel.setSynced(pref.getInt(issynced,0));
        traderModel.setDummy(pref.getInt(isdummy,0));
        traderModel.setPassword(pref.getString(password,null));
        traderModel.setApikey(pref.getString(apikey,null));
        traderModel.setFirebasetoken(pref.getString(firebasetoken,null));
        traderModel.setStatus(pref.getString(status,null));
        traderModel.setTransactiontime(pref.getString(transactiontime,null));

        return traderModel;

    }



}
