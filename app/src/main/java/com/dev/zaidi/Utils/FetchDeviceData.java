package com.dev.zaidi.Utils;

import android.content.Context;
import android.os.Build;

/**
 * Created by Eric on 1/8/2018.
 */

public class FetchDeviceData {
//    public static LinkedList<MessagesModel> messagesModels;
//
//
//    public static LinkedList<MessagesModel> getMessagesModels(String query, Context context) {
//        messagesModels = new LinkedList<>();
//
//        StringBuilder smsBuilder = new StringBuilder();
//        final String SMS_URI_INBOX = "content://sms/inbox";
//        final String SMS_URI_ALL = "content://sms/";
//        try {
//            Uri uri = Uri.parse(SMS_URI_INBOX);
//            String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
//            Cursor cur = context.getContentResolver().query(uri, projection, "address='MPESA'", null, "date desc");
//            // MessagesModel messagesModel=new MessagesModel();
//            // "address='Mpesa'"
//            Calendar c = Calendar.getInstance();
//            Long date = c.getTimeInMillis();
//            c.add(Calendar.DAY_OF_YEAR, -30);
//
//            Long month = c.getTimeInMillis();
//
//            if (cur.moveToFirst()) {
//                int index_Address = cur.getColumnIndex("address");
//                int index_Person = cur.getColumnIndex("person");
//                int index_Body = cur.getColumnIndex("body");
//                int index_Date = cur.getColumnIndex("date");
//                int index_Type = cur.getColumnIndex("type");
//                do {
//                    String strAddress = cur.getString(index_Address);
//                    int intPerson = cur.getInt(index_Person);
//                    String strbody = cur.getString(index_Body);
//                    long longDate = cur.getLong(index_Date);
//                    int int_Type = cur.getInt(index_Type);
//                    MessagesModel messagesModel = new MessagesModel();
//                    messagesModel.setPerson(String.valueOf(intPerson));
//                    messagesModel.setBody(strbody);
//                    messagesModel.setDate(String.valueOf(longDate));
//                    messagesModel.setType(String.valueOf(int_Type));
//                    messagesModel.setAddress(strAddress);
//
//                    if (longDate > month) {
//
//                        messagesModels.add(messagesModel);
//                    }
//
//
//                    smsBuilder.append("[ ");
//                    smsBuilder.append(strAddress + ", ");
//                    smsBuilder.append(intPerson + ", ");
//                    smsBuilder.append(strbody + ", ");
//                    smsBuilder.append(longDate + ", ");
//                    smsBuilder.append(int_Type);
//                    smsBuilder.append(" ]\n\n");
//
//                    Log.d("sms", smsBuilder.toString());
//                } while (cur.moveToNext());
//
//                if (!cur.isClosed()) {
//                    cur.close();
//                    cur = null;
//                }
//            } else {
//                smsBuilder.append("no result!");
//            } // end if
//
//        } catch (SQLiteException ex) {
//            Log.d("SQLiteException", ex.getMessage());
//        }
//
//
//        return messagesModels;
//
//
//    }

    private void getCallLogDetail(Context context) {
//        String[] projection = new String[]{
//                BaseColumns._ID,
//                CallLog.Calls.NUMBER,
//                CallLog.Calls.TYPE,
//                CallLog.Calls.DURATION
//        };
//        ContentResolver resolver = context.getContentResolver();
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Cursor cur = resolver.query(
//                CallLog.Calls.CONTENT_URI,
//                projection,
//                null,
//                null,
//                CallLog.Calls.DEFAULT_SORT_ORDER);
//        if (!cur.isAfterLast()) {
//            int numberColumn = cur.getColumnIndex(CallLog.Calls.NUMBER);
//            int typeColumn = cur.getColumnIndex(CallLog.Calls.TYPE);
//            int durationcolumn = cur.getColumnIndex(CallLog.Calls.DURATION);
//            String number = cur.getString(numberColumn);
//            String type = cur.getString(typeColumn);
//            String duration = cur.getString(durationcolumn);
//            cur.moveToNext();
//
//        }

    }

    public SystemInfo fetchDetails() {

        String board = Build.BOARD;
        String bootloader = Build.BOOTLOADER;
        String brand = Build.BRAND;
        String cpu_abi = Build.CPU_ABI;
        String cpu_ab12 = Build.CPU_ABI2;
        String device = Build.DEVICE;
        String diaplay = Build.DISPLAY;
        String fingerprint = Build.FINGERPRINT;
        String build = Build.HARDWARE;
        String host = Build.HOST;
        String buildid = Build.ID;
        //String isDebuggable=  Build.IS ;
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String product = Build.PRODUCT;
        String radio = Build.RADIO;
        String serial = Build.SERIAL;
        //String  Build.SUPPORTED_32_BIT_ABIS = [Ljava.lang.String;@3dd90541
        // Build.SUPPORTED_64_BIT_ABIS = [Ljava.lang.String;@1da4fc3
        //  Build.SUPPORTED_ABIS = [Ljava.lang.String;@525f635
        //  Build.TAGS = release-keys
        String time = String.valueOf(Build.TIME);
        String type = Build.TYPE;
        //  Build.UNKNOWN = unknown
        String user = Build.USER;

        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setBoard(board);
        systemInfo.setBootloader(bootloader);
        systemInfo.setBrand(brand);
        systemInfo.setCpu_abi(cpu_abi);
        systemInfo.setCpu_ab12(cpu_ab12);
        systemInfo.setDevice(device);

        systemInfo.setDiaplay(diaplay);
        systemInfo.setBuild(build);
        systemInfo.setHost(host);
        systemInfo.setBuildid(buildid);
        systemInfo.setManufacturer(manufacturer);
        systemInfo.setModel(model);
        systemInfo.setProduct(product);
        systemInfo.setRadio(radio);
        systemInfo.setSerial(serial);
        systemInfo.setTime(time);
        systemInfo.setType(type);
        systemInfo.setUser(user);
        systemInfo.setFingerprint(fingerprint);


        return systemInfo;
    }

//    public SystemInfo fetchDetails(Context context) {
//        SystemInfo systemInfol = new SystemInfo();
//        DeviceName.with(context).request((info, error) -> {
//            systemInfol.setManufacturer(info.manufacturer);  // "Samsung"
//            systemInfol.setProduct(info.marketName);            // "Galaxy S7 Edge"
//            systemInfol.setModel(info.model);                // "SAMSUNG-SM-G935A"
//            //codename = info.codename;          // "hero2lte"
//            systemInfol.setDevice(info.getName());       // "Galaxy S7 Edge"
//            // FYI: We are on the UI thread.
//        });
//
//        return systemInfol;
//
//    }


}
