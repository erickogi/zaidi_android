package com.dev.lishaboramobile.Global.Data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.dev.lishaboramobile.Farmer.Models.FamerModel;
import com.dev.lishaboramobile.Global.Data.Operations.Converters.DateConverter;
import com.dev.lishaboramobile.Global.Data.Operations.Dao.CyclesDao;
import com.dev.lishaboramobile.Global.Data.Operations.Dao.FarmersDao;
import com.dev.lishaboramobile.Global.Data.Operations.Dao.ProductsDao;
import com.dev.lishaboramobile.Global.Data.Operations.Dao.RoutesDao;
import com.dev.lishaboramobile.Global.Data.Operations.Dao.TradersDao;
import com.dev.lishaboramobile.Global.Data.Operations.Dao.UnitsDao;
import com.dev.lishaboramobile.Trader.Models.Cycles;
import com.dev.lishaboramobile.Trader.Models.RoutesModel;
import com.dev.lishaboramobile.Trader.Models.TraderModel;
import com.dev.lishaboramobile.Trader.Models.UnitsModel;
import com.dev.lishaboramobile.admin.models.ProductsModel;

@Database(entities = {TraderModel.class, FamerModel.class, RoutesModel.class, UnitsModel.class, Cycles.class, ProductsModel.class}, version = 7)
@TypeConverters(DateConverter.class)

public abstract class LMDatabase extends RoomDatabase {

    private static LMDatabase INSTANCE;
    public  static LMDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {
            synchronized (LMDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LMDatabase.class, "lm_database").fallbackToDestructiveMigration().allowMainThreadQueries()
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    public abstract TradersDao tradersDao();
    public abstract FarmersDao farmersDao();
    public abstract RoutesDao routesDao();
    public abstract UnitsDao unitsDao();

    public abstract CyclesDao cyclesDao();

    public abstract ProductsDao productsDao();

}
