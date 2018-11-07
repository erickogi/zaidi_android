package com.dev.lishabora.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.lishabora.Models.Trader.FarmerOrdersTable;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

//import com.dev.lishabora.Models.FarmerOrdersTable;

@Dao
public interface FarmersOrdersDao {


    @Insert(onConflict = REPLACE)
    void insertMultioleFarmerOrders(List<FarmerOrdersTable> farmersOrders);

    @Insert(onConflict = REPLACE)
    void insertSingleFarmerOrder(FarmerOrdersTable farmersOrders);


    @Query("SELECT * FROM FAMERSORDERS")
    LiveData<List<FarmerOrdersTable>> fetchAllData();

    @Query("SELECT * FROM FAMERSORDERS WHERE code =:code")
    LiveData<FarmerOrdersTable> getFarmerOrderByCode(String code);

    @Query("SELECT * FROM FAMERSORDERS WHERE code =:code")
    FarmerOrdersTable getFarmerOrderByCodeOne(String code);


    @Query("SELECT * FROM FAMERSORDERS WHERE timestamp =:date")
    LiveData<List<FarmerOrdersTable>> getFarmerOrderByDate(String date);

    @Query("SELECT * FROM FAMERSORDERS WHERE payoutCode =:payoutCode")
    LiveData<List<FarmerOrdersTable>> getFarmerOrderByPayoutCode(String payoutCode);

    @Query("SELECT * FROM FAMERSORDERS WHERE payoutCode =:payoutCode")
    List<FarmerOrdersTable> getFarmerOrderByPayoutCodeOne(String payoutCode);


    @Query("SELECT * FROM FAMERSORDERS WHERE collectionCode =:colCode")
    LiveData<List<FarmerOrdersTable>> getFarmerOrderByCollectionCode(String colCode);

    @Query("SELECT * FROM FAMERSORDERS WHERE collectionCode =:collId")
    FarmerOrdersTable getFarmerOrderByCollection(String collId);

    @Query("SELECT * FROM FAMERSORDERS WHERE collectionCode =:collId")
    LiveData<FarmerOrdersTable> getFarmerOrderByCollectionLive(String collId);


    @Query("SELECT * FROM FAMERSORDERS WHERE payoutCode =:payoutCode AND farmerCode =:farmer")
    LiveData<List<FarmerOrdersTable>> getFarmerOrderByPayoutCodeByFarmer(String payoutCode, String farmer);

    @Query("SELECT * FROM FAMERSORDERS WHERE payoutCode =:payoutCode AND farmerCode =:farmer")
    List<FarmerOrdersTable> getFarmerOrderByPayoutCodeByFarmerOne(String payoutCode, String farmer);


    @Query("SELECT * FROM FAMERSORDERS WHERE payoutCode =:payoutCode AND farmerCode =:farmer AND status =:status")
    List<FarmerOrdersTable> getFarmerOrderByPayoutCodeByFarmerByStatus(String payoutCode, String farmer, int status);

    @Query("SELECT * FROM FAMERSORDERS WHERE  farmerCode =:farmer AND status =:status")
    List<FarmerOrdersTable> getFarmerOrderByPayoutCodeByFarmerByStatus(String farmer, int status);

    @Query("SELECT * FROM FAMERSORDERS WHERE  farmerCode =:farmer AND status =:status")
    LiveData<List<FarmerOrdersTable>> getFarmerOrderByFarmerByStatus(String farmer, int status);


    @Query("SELECT * FROM FAMERSORDERS WHERE  farmerCode =:farmer ")
    LiveData<List<FarmerOrdersTable>> getFarmerOrderByFarmer(String farmer);

    @Query("SELECT * FROM FAMERSORDERS WHERE  farmerCode =:farmer ")
    List<FarmerOrdersTable> getFarmerOrderByFarmerOne(String farmer);



    @Query("SELECT * FROM FAMERSORDERS WHERE payoutCode =:payoutCode")
    List<FarmerOrdersTable> getFarmerOrderByPayoutCodeListOne(String payoutCode);

    @Query("UPDATE FAMERSORDERS SET status =:a  WHERE farmerCode =:farmercode AND payoutCode =:payoutCode")
    void approveFarmersPayoutCard(int a, String farmercode, String payoutCode);


    @Update
    void updateRecord(FarmerOrdersTable farmersOrder);

    @Delete
    void deleteRecord(FarmerOrdersTable farmersOrder);

    @Query("SELECT (SUM(orderAmount)) FROM FAMERSORDERS WHERE farmerCode =:farmercode")
    double getBalanceByFarmerCode(String farmercode);


    @Query("SELECT (SUM(orderAmount)) FROM FAMERSORDERS WHERE payoutCode =:payoutCode")
    double getBalanceBySumCode(String payoutCode);

    @Query("SELECT (SUM(installmentAmount)) FROM FAMERSORDERS WHERE payoutCode =:payoutCode")
    double getInstallmentSumByPayoutCode(String payoutCode);

    @Query("SELECT (SUM(installmentAmount)) FROM FAMERSORDERS WHERE farmerCode =:farmercode")
    double getInstallmentSumByFarmerCode(String farmercode);


    @Query("SELECT * FROM FAMERSORDERS WHERE farmerCode = :code AND timestamp LIKE :date")
    List<FarmerOrdersTable> getFarmerOrderByFarmerByDate(String code, String date);


    @Query("SELECT * FROM FAMERSORDERS WHERE farmerCode = :code  AND timestamp LIKE :today")
    FarmerOrdersTable getFarmerOrderByDateByFarmerByTimeSingle(String code, String today);


    @Query("SELECT * FROM FAMERSORDERS  ORDER BY id DESC LIMIT 1")
    FarmerOrdersTable getLast();


    @Query("UPDATE FAMERSORDERS SET status =:status WHERE  code =:code")
    void updateStatus(String code, int status);

    @Query("SELECT * FROM FAMERSORDERS WHERE timestamp BETWEEN :date1 AND :date2")
    LiveData<List<FarmerOrdersTable>> getFarmerOrdersBetweenDates(Long date1, Long date2);


    @Query("SELECT * FROM FAMERSORDERS WHERE farmerCode = :code AND  timestamp BETWEEN :date1 AND :date2")
    LiveData<List<FarmerOrdersTable>> getFarmerOrdersBetweenDates(Long date1, Long date2, String code);
}
