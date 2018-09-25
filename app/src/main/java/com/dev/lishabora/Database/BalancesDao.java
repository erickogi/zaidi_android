package com.dev.lishabora.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.lishabora.Models.Trader.FarmerBalance;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface BalancesDao {


    @Insert(onConflict = REPLACE)
    void insertMultiple(FarmerBalance... FarmerBalances);

    @Insert(onConflict = REPLACE)
    void insertMultiple(List<FarmerBalance> FarmerBalances);

    @Insert(onConflict = REPLACE)
    void insertSingle(FarmerBalance FarmerBalance);


    @Query("SELECT * FROM FARMERBALANCE")
    LiveData<List<FarmerBalance>> fetchAllData();

    @Query("SELECT * FROM FARMERBALANCE WHERE id =:keyid")
    LiveData<FarmerBalance> getByKeyID(int keyid);

    @Query("SELECT * FROM FARMERBALANCE WHERE farmerCode =:code")
    LiveData<FarmerBalance> getByFarmerCode(String code);

    @Query("SELECT * FROM FARMERBALANCE WHERE farmerCode =:code")
    FarmerBalance getByFarmerCodeOne(String code);


    @Update
    void updateRecord(FarmerBalance FarmerBalance);

    @Delete
    void deleteRecord(FarmerBalance FarmerBalance);


}
