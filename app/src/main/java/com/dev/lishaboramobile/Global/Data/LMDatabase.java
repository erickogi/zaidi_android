package com.dev.lishaboramobile.Global.Data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.dev.lishaboramobile.Global.Data.Operations.TradersDao;
import com.dev.lishaboramobile.Trader.Models.TraderModel;

@Database(entities =  {TraderModel.class}, version = 1)
@TypeConverters(DateConverter.class)

public abstract class LMDatabase extends RoomDatabase {

    private static LMDatabase INSTANCE;
    public  static LMDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {
            synchronized (LMDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LMDatabase.class, "lm_database")
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    public abstract TradersDao tradersDao();

}
