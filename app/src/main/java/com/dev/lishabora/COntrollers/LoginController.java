package com.dev.lishabora.COntrollers;

import android.content.Context;
import android.util.Log;

public class LoginController {

    public static final int ADMIN = 6;
    public static final int TRADER = 1;
    public static final int COLLECTOR = 2;
    public static final int MASTER_TRADER = 3;
    public static final int FARMER = 4;
    public static final int DISTRIBUTER = 5;

    private Context context;
    private int PASSWORD_LENGTH = 4;

    public LoginController(Context context) {
        this.context = context;

    }

    public static boolean isValidPhoneNumber(String mobile) {
        Log.d("enteredPhone", mobile);
        String regEx = "^[0-9]{9}$";
        return mobile.matches(regEx);
    }

    boolean isValidPassword(String password) {

        if (password != null) {
            return password.length() >= PASSWORD_LENGTH;
        }
        return true;
    }


}
