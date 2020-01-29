package com.dev.zaidi.Utils;

import com.dev.zaidi.Models.DayCollectionModel;

public interface MilkEditValueListener {
    void updateCollection(String value, int adapterPosition, int time, int type, DayCollectionModel dayCollectionModel, android.support.v7.app.AlertDialog alertDialogAndroid);

}
