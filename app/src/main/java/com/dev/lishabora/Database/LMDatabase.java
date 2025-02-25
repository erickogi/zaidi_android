package com.dev.lishabora.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.dev.lishabora.Database.Converters.DateConverter;
import com.dev.lishabora.Models.ApprovalRegisterModel;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.Cycles;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.FarmerBalance;
import com.dev.lishabora.Models.Notifications;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Models.ProductsModel;
import com.dev.lishabora.Models.RoutesModel;
import com.dev.lishabora.Models.SyncDownObserver;
import com.dev.lishabora.Models.SyncModel;
import com.dev.lishabora.Models.Trader.FarmerLoansTable;
import com.dev.lishabora.Models.Trader.FarmerOrdersTable;
import com.dev.lishabora.Models.Trader.LoanPayments;
import com.dev.lishabora.Models.Trader.OrderPayments;
import com.dev.lishabora.Models.Trader.TraderModel;
import com.dev.lishabora.Models.UnitsModel;

@Database(entities = {TraderModel.class, FamerModel.class, RoutesModel.class,
        UnitsModel.class, Cycles.class, ProductsModel.class, Collection.class,
        Payouts.class, SyncModel.class, FarmerLoansTable.class,
        FarmerOrdersTable.class, LoanPayments.class, OrderPayments.class,
        FarmerBalance.class, Notifications.class, SyncDownObserver.class, ApprovalRegisterModel.class}, version = 1)
@TypeConverters(DateConverter.class)

public abstract class LMDatabase extends RoomDatabase {
    //    static final Migration MIGRATION_47_48 = new Migration(47, 48) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE traders  ADD COLUMN synchingStatus INTEGER  ");
//            database.execSQL("ALTER TABLE traders  ADD COLUMN lastsynchingMessage STRING  ");
//            database.execSQL("ALTER TABLE farmerBalance  ADD COLUMN loans STRING  ");
//            database.execSQL("ALTER TABLE farmerBalance  ADD COLUMN orders STRING  ");
//            database.execSQL("ALTER TABLE farmerBalance  ADD COLUMN milk STRING  ");
//        }
//    };
    private static LMDatabase INSTANCE;
    public  static LMDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {
            synchronized (LMDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LMDatabase.class, "zaidi_database")
                            .fallbackToDestructiveMigration()
                            //.addMigrations(MIGRATION_47_48)
                            .allowMainThreadQueries()
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

    public abstract CollectionsDao collectionsDao();

    public abstract PayoutsDao payoutsDao();

    public abstract SyncDao syncDao();

    public abstract FarmersLoansDao farmersLoansDao();

    public abstract FarmersOrdersDao farmersOrdersDao();

    public abstract LoanPaymentsDao loanPaymentsDao();

    public abstract OrderPaymentsDao orderPaymentsDao();


    public abstract BalancesDao balancesDao();

    public abstract NotificationsDao notificationsDao();

    public abstract SyncObserverDao syncObserverDao();

    public abstract ApprovalRegisterDao approvalRegisterDao();

}
