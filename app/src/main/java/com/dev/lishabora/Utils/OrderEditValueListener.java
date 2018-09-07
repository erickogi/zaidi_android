package com.dev.lishabora.Utils;

import com.dev.lishabora.Models.DayCollectionModel;
import com.dev.lishabora.Models.OrderModel;

public interface OrderEditValueListener {
    void updateCollection(String value, OrderModel orderModel, int time, DayCollectionModel dayCollectionModel, android.support.v7.app.AlertDialog alertDialogAndroid);

}
