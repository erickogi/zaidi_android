package com.dev.lishabora.Utils;

import com.dev.lishabora.Models.DayCollectionModel;
import com.dev.lishabora.Models.LoanModel;

public interface LoanEditValueListener {
    void updateCollection(String value, LoanModel loanModel, int time, DayCollectionModel dayCollectionModel, android.support.v7.app.AlertDialog alertDialogAndroid);

}
