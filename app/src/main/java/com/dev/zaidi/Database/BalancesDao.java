package com.dev.zaidi.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.zaidi.Models.FarmerBalance;

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

    @Query("SELECT * FROM FARMERBALANCE")
    List<FarmerBalance> fetchAllDataOne();


    @Query("SELECT * FROM FARMERBALANCE WHERE code =:code")
    LiveData<FarmerBalance> getByKeyCode(String code);

    @Query("SELECT * FROM FARMERBALANCE WHERE farmerCode =:code")
    LiveData<List<FarmerBalance>> getByFarmerCode(String code);

    @Query("SELECT * FROM FARMERBALANCE WHERE farmerCode =:code")
    List<FarmerBalance> getByFarmerCodeOne(String code);


    @Query("SELECT * FROM FARMERBALANCE WHERE farmerCode =:code AND payoutCode =:payoutCode")
    LiveData<FarmerBalance> getByFarmerCodeByPayout(String code, String payoutCode);

    @Query("SELECT * FROM FARMERBALANCE WHERE farmerCode =:code AND payoutCode =:payoutCode")
    FarmerBalance getByFarmerCodeByPayoutOne(String code, String payoutCode);


    @Update
    void updateRecord(FarmerBalance FarmerBalance);

    @Delete
    void deleteRecord(FarmerBalance FarmerBalance);


}
