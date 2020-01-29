package com.dev.zaidi.Utils;

import com.dev.zaidi.Models.DayCollectionModel;
import com.dev.zaidi.Models.OrderModel;

public interface OrderEditValueListener {
    void updateCollection(String value, OrderModel orderModel, int time, DayCollectionModel dayCollectionModel, android.support.v7.app.AlertDialog alertDialogAndroid);

}
