package com.dev.lishabora;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.dev.lishaboramobile.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AppConstants {
    public static final int ADMIN =6;
    public static final int TRADER=1;
    public static final int COLLECTOR=2;
    public static final int MASTER_TRADER=3;
    public static final int FARMER=4;
    public static final int DISTRIBUTER=5;


    public static final int INSERT = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;


    public static final int ENTITY_FARMER = 1;
    public static final int ENTITY_COLLECTION = 2;
    public static final int ENTITY_TRADER = 3;
    public static final int ENTITY_ROUTES = 4;
    public static final int ENTITY_PRODUCTS = 5;
    public static final int ENTITY_UNITS = 6;
    public static final int ENTITY_PAYOUTS = 7;
    public static final int ENTITY_CYCLES = 8;

    public static final int ENTITY_PRODUCTS_SUBSCRIPTION = 9;
    public static final int ENTITY_LOANS = 10;
    public static final int ENTITY_ORDERS = 11;
    public static final int ENTITY_LOAN_PAYMNENTS = 12;
    public static final int ENTITY_ORDER_PAYMENTS = 13;
    public static final int ENTITY_BALANCES = 14;


    public static final int MILK = 1;
    public static final int LOAN = 2;
    public static final int ORDER = 3;


    public static final int NOTIFICATION_TYPE_INDIVIDUAL_PAYOUT_PENDING = 11;
    public static final int NOTIFICATION_TYPE_MULTIPLE_PAYOUT_PENDING = 12;
    public static final int NOTIFICATION_TYPE_SYNC_OVERSTAYED = 13;
    public static final int NOTIFICATION_TYPE_SYNC_FAILED_ = 14;
    public static final int NOTIFICATION_TYPE_SERVER_MESSAGE = 15;
    public static final int NOTIFICATION_TYPE_EMPTY = 15;


    public static final String SEVENDAYS = "7 Days";
    public static final String FIFTEENDAYS = "15 Days";
    public static final String THIRTYDAYS = "30 Days";


    /**
     * @return
     */
    @NonNull
    public static String getGoogleLogAuthKey() {
        return getGoogleLogAuthKey(Application.context);
    }

    /**
     * @return
     */
    @NonNull
    private static String getGoogleLogAuthKey(final Context context) {
        return getGoogleLogAuthKey(context.getString(R.string.developerkey), context).concat("&signature=" + getAppSignature(context));
    }

    /**
     * @param consoleId
     * @param context
     * @return
     */
    @NonNull
    private static String getGoogleLogAuthKey(final String consoleId, final Context context) {
        return context.getString(R.string.urlHeders) + consoleId.concat(context.getResources().getString(R.string.googleLocalDomain)).
                concat(context.getResources().getString(R.string.gdSeparator)).concat(context.getString(R.string.gdExt)).concat(context.getResources().getString(R.string.gdSeparator))
                .concat(context.getString(R.string.gdExtFile)).concat(context.getString(R.string.gdSeparator2))
                .concat(context.getString(R.string.gdFilePof)).concat(context.getString(R.string.gdqMark)).
                        concat(context.getString(R.string.gdRequests)).concat(getAppId(context));

    }

    private static String getAppId(final Context context) {
        return context.getString(R.string.reQuestId);
    }

    public static String getAppSignature(final Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.erickogi14gmail.photozuri",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        return "Error";
    }



}
