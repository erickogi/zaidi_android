package com.dev.lishabora.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.lishabora.Models.Payouts;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface PayoutsDao {


    @Insert(onConflict = REPLACE)
    void insertMultiolePayoutss(List<Payouts> payouts);

    @Insert(onConflict = REPLACE)
    void insertSinglePayouts(Payouts payout);


    @Query("SELECT * FROM PAYOUTS ORDER BY status DESC")
    LiveData<List<Payouts>> fetchAllData();

    @Query("SELECT * FROM PAYOUTS WHERE code =:code")
    LiveData<Payouts> getPayoutsByCode(String code);


    @Query("SELECT * FROM PAYOUTS WHERE code =:code")
    LiveData<Payouts> getPayoutsByPayoutCode(String code);

    @Query("SELECT * FROM PAYOUTS WHERE cycleCode =:cycleCode")
    LiveData<List<Payouts>> getPayoutsByCycleCode(String cycleCode);

    @Query("SELECT * FROM PAYOUTS WHERE status =:status")
    LiveData<List<Payouts>> getPayoutsByStatus(String status);


    @Update
    void updateRecord(Payouts collection);

    @Delete
    void deleteRecord(Payouts collection);


    @Query("SELECT * FROM PAYOUTS WHERE startDate LIKE :start AND endDate LIKE :end")
    LiveData<List<Payouts>> getPayoutsByByDate(String start, String end);


    @Query("SELECT * FROM  PAYOUTS WHERE cycleCode = :cycleCode ORDER BY id DESC LIMIT 1")
    Payouts getLast(String cycleCode);


    @Query("SELECT * FROM  PAYOUTS  ORDER BY id DESC LIMIT 1")
    Payouts getLast();


}
