package com.dev.zaidi.Utils;

import com.dev.zaidi.Models.DayCollectionModel;
import com.dev.zaidi.Models.LoanModel;

public interface LoanEditValueListener {
    void updateCollection(String value, LoanModel loanModel, int time, DayCollectionModel dayCollectionModel, android.support.v7.app.AlertDialog alertDialogAndroid);

}
