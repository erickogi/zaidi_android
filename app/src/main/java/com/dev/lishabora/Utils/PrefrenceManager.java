package com.dev.lishabora.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.dev.lishabora.Models.Admin.AdminModel;
import com.dev.lishabora.Models.Trader.TraderModel;

public class PrefrenceManager {


    // Shared preferences file name

    private static final String PREF_NAME = "lishaboraprefs";

    // All Shared Preferences Keys

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_TYPE_LOGGED_IN = "typeLoggedIn";

    private static final String dev_folder = "devfolder";
    //ADMIN

    private static final String NAMES = "names";
    private static final String CODE = "code";
    private static final String MOBILE = "mobile";
    private static final String EMAIL = "email";
    private static final String DEPARTMENT = "department";
    private static final String PASSWORD = "password";
    private static final String APIKEY = "apikey";
    private static final String FIREBASE_TOKEN = "firebasetoken";
    private static final String STATUS = "status";
    private static final String TRANSACTION_TIME = "transaction_time";

    //TRADER

    private static final String code = "code";
    private static final String entity = "entity";
    private static final String entitycode = "entitycode";
    private static final String transactioncode = "transactioncode";
    private static final String names = "names";
    private static final String bussinesname = "bussinesname";
    private static final String mobile = "mobile";
    private static final String password = "password";
    private static final String apikey = "apikey";
    private static final String firebasetoken = "firebasetoken";
    private static final String status = "status";
    private static final String transactiontime = "transactiontime";
    private static final String synctime = "synctime";
    private static final String transactedby = "transactedby";
    private static final String issynced = "issynced";
    private static final String isdummy = "isdummy";

    private static final String cycleStart = "cyclestart";
    private static final String cycleEnd = "cycleend";

    private static final String cyclestartdayNumber = "cyclestartnumberend";
    private static final String cycleenddayNumber = "cycleendnumber";


    private static final String routesSettingsDone = "isroutesdone";
    private static final String productsSettingsDone = "isProductSettingsDone";
    private static final String cycleSettingsDone = "isCycleSettingsDone";
    private static final String unitSettingsDone = "isCycleSettingsDone";
    private static final String isFarmerListFirst = "isFarmerListFirst";
    private static final String isRoutesListFirst = "isRouteListFirst";
    private static final String isUnitsListFirst = "isUnitListFirst";
    private static final String isCyclesListFirst = "isCycleListFirst";
    private static final String isProductsListFirst = "isProductListFirst";


    private static final String isTraderFirstTime = "isTraderFirst";
    // Shared Preferences

    SharedPreferences pref;

    // Editor for Shared preferences

    SharedPreferences.Editor editor;

    // Context

    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;


    public PrefrenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getDev_folder() {
        return pref.getString(dev_folder, "MCU");

    }

    public void setDev_folder(String dev_folde) {
        editor.putString(dev_folder, dev_folde);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public int getTypeLoggedIn() {
        return pref.getInt(KEY_TYPE_LOGGED_IN, 0);
    }

    public void setIsLoggedIn(boolean isLoggedIn, int type) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.putInt(KEY_TYPE_LOGGED_IN, type);
        editor.commit();
    }

    public boolean isTraderFirstTime() {

        return pref.getBoolean(isTraderFirstTime, true);
    }

    public void setIsTraderFirstTime(boolean isTraderFirstTime) {
        editor.putBoolean(PrefrenceManager.isTraderFirstTime, isTraderFirstTime);
        editor.commit();
    }

    public AdminModel getAdmin() {
        AdminModel adminModel = new AdminModel();
        adminModel.setCode(pref.getString(CODE, null));
        adminModel.setNames(pref.getString(NAMES, null));
        adminModel.setEmail(pref.getString(EMAIL, null));
        adminModel.setMobile(pref.getString(MOBILE, null));
        adminModel.setDepartment(pref.getString(DEPARTMENT, null));
        adminModel.setPassword(pref.getString(PASSWORD, null));
        adminModel.setApikey(pref.getString(APIKEY, null));
        adminModel.setFirebasetoken(pref.getString(FIREBASE_TOKEN, null));
        adminModel.setStatus(pref.getString(STATUS, null));
        adminModel.setTransactiontime(pref.getString(TRANSACTION_TIME, null));

        return adminModel;

    }

    public void setLoggedUser(AdminModel admin) {
        editor.putString(NAMES, admin.getNames());
        editor.putString(CODE, admin.getCode());
        editor.putString(MOBILE, admin.getMobile());
        editor.putString(EMAIL, admin.getEmail());
        editor.putString(DEPARTMENT, admin.getDepartment());
        editor.putString(PASSWORD, admin.getPassword());
        editor.putString(APIKEY, admin.getApikey());
        editor.putString(FIREBASE_TOKEN, admin.getFirebasetoken());
        editor.putString(STATUS, admin.getStatus());
        editor.putString(TRANSACTION_TIME, admin.getTransactiontime());
        editor.commit();

    }


    public void setLoggedUser(TraderModel trader) {
        editor.putString(names, trader.getNames());
        editor.putString(mobile, trader.getMobile());
        editor.putString(bussinesname, trader.getBusinessname());
        editor.putString(code, trader.getCode());
        editor.putString(entity, trader.getEntity());
        editor.putString(entitycode, trader.getEntitycode());
        editor.putString(transactioncode, trader.getTransactioncode());
        editor.putString(synctime, trader.getSynctime());
        editor.putString(transactedby, trader.getTransactedby());
        editor.putInt(issynced, trader.getSynced());
        editor.putInt(isdummy, trader.getDummy());
        editor.putString(password, trader.getPassword());
        editor.putString(apikey, trader.getApikey());
        //editor.putString(firebasetoken, trader.getFirebasetoken());
        editor.putString(status, trader.getStatus());
        editor.putString(transactiontime, trader.getTransactiontime());
        editor.putString(cycleStart, trader.getCycleStartDay());
        editor.putString(cycleEnd, trader.getCycleEndDay());

        editor.putInt(cyclestartdayNumber, trader.getCycleStartDayNumber());
        editor.putInt(cycleenddayNumber, trader.getCycleEndDayNumber());

        editor.commit();

    }

    public String getCode() {
        return pref.getString(code, null);
    }

    public TraderModel getTraderModel() {
        TraderModel traderModel = new TraderModel();
        traderModel.setNames(pref.getString(names, null));
        traderModel.setBusinessname(pref.getString(bussinesname, null));
        traderModel.setCode(pref.getString(code, null));
        traderModel.setEntity(pref.getString(entity, null));
        traderModel.setEntitycode(pref.getString(entitycode, null));
        traderModel.setTransactioncode(pref.getString(transactioncode, null));
        traderModel.setSynctime(pref.getString(synctime, null));
        traderModel.setMobile(pref.getString(mobile, null));
        traderModel.setTransactedby(pref.getString(transactedby, null));
        traderModel.setSynced(pref.getInt(issynced, 0));
        traderModel.setDummy(pref.getInt(isdummy, 0));
        traderModel.setPassword(pref.getString(password, null));
        traderModel.setApikey(pref.getString(apikey, null));
        traderModel.setFirebasetoken(pref.getString(firebasetoken, "empty"));
        traderModel.setStatus(pref.getString(status, null));
        traderModel.setTransactiontime(pref.getString(transactiontime, null));
        traderModel.setCycleStartDay(pref.getString(cycleStart, ""));
        traderModel.setCycleEndDay(pref.getString(cycleEnd, ""));

        traderModel.setCycleStartDayNumber(pref.getInt(cyclestartdayNumber, 0));
        traderModel.setCycleEndDayNumber(pref.getInt(cycleenddayNumber, 0));

        return traderModel;

    }

    public void setRoutesSettingsDone(boolean status) {
        editor.putBoolean(routesSettingsDone, status);
        editor.commit();
    }

    public void setProductsSettingsDone(boolean status) {
        editor.putBoolean(productsSettingsDone, status);
        editor.commit();
    }

    public void setUnitSettingsDone(boolean status) {
        editor.putBoolean(unitSettingsDone, status);
        editor.commit();
    }

    public void setCycleSettingsDone(boolean status) {
        editor.putBoolean(cycleSettingsDone, status);
        editor.commit();
    }

    public boolean[] getSettingStatus() {
        boolean[] settings = new boolean[4];
        settings[0] = pref.getBoolean(routesSettingsDone, false);
        settings[1] = pref.getBoolean(productsSettingsDone, false);
        settings[2] = pref.getBoolean(unitSettingsDone, false);
        settings[3] = pref.getBoolean(cycleSettingsDone, false);

        return settings;
    }


    public boolean isFarmerListFirstTime() {
        return pref.getBoolean(isFarmerListFirst, true);

    }

    public void setIsFarmerListFirst(boolean s) {
        editor.putBoolean(isFarmerListFirst, s);
        editor.commit();
    }

    public boolean isRoutesListFirstTime() {
        return pref.getBoolean(isRoutesListFirst, true);

    }

    public void setIsRoutesListFirst(boolean s) {
        editor.putBoolean(isRoutesListFirst, s);
        editor.commit();
    }

    public boolean isUnitListFirstTime() {
        return pref.getBoolean(isUnitsListFirst, true);

    }

    public void setIsUnitsListFirst(boolean s) {
        editor.putBoolean(isUnitsListFirst, s);
        editor.commit();
    }

    public boolean isProductListFirstTime() {
        return pref.getBoolean(isProductsListFirst, true);

    }

    public void setIsProductsListFirst(boolean s) {
        editor.putBoolean(isProductsListFirst, s);
        editor.commit();
    }

    public boolean isCycleListFirstTime() {
        return pref.getBoolean(isCyclesListFirst, true);

    }

    public void setIsCyclesListFirst(boolean s) {
        editor.putBoolean(isCyclesListFirst, s);
        editor.commit();
    }

    public String getFirebase() {
        return pref.getString(firebasetoken, "");
    }

    public void setFirebase(String refreshedToken) {
        editor.putString(firebasetoken, refreshedToken);
        editor.commit();
    }
}
