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

    @Query("SELECT * FROM FAMERSORDERS WHERE id =:keyid")
    LiveData<FarmerOrdersTable> getFarmerOrderById(int keyid);

    @Query("SELECT * FROM FAMERSORDERS WHERE id =:keyid")
    FarmerOrdersTable getFarmerOrderByIdOne(int keyid);


    @Query("SELECT * FROM FAMERSORDERS WHERE timestamp =:date")
    LiveData<List<FarmerOrdersTable>> getFarmerOrderByDate(String date);

    @Query("SELECT * FROM FAMERSORDERS WHERE payoutId =:payoutnumber")
    LiveData<List<FarmerOrdersTable>> getFarmerOrderByPayoutNumber(String payoutnumber);

    @Query("SELECT * FROM FAMERSORDERS WHERE collectionId =:collId")
    LiveData<List<FarmerOrdersTable>> getFarmerOrderByCollections(int collId);

    @Query("SELECT * FROM FAMERSLOANS WHERE collectionId =:collId")
    FarmerOrdersTable getFarmerOrderByCollection(int collId);

    @Query("SELECT * FROM FAMERSLOANS WHERE collectionId =:collId")
    LiveData<FarmerOrdersTable> getFarmerOrderByCollectionLive(int collId);



    @Query("SELECT * FROM FAMERSORDERS WHERE payoutId =:payoutnumber AND farmerCode =:farmer")
    LiveData<List<FarmerOrdersTable>> getFarmerOrderByPayoutNumberByFarmer(String payoutnumber, String farmer);


    @Query("SELECT * FROM FAMERSORDERS WHERE payoutId =:payoutnumber AND farmerCode =:farmer AND status =:status")
    List<FarmerOrdersTable> getFarmerOrderByPayoutNumberByFarmerByStatus(String payoutnumber, String farmer, int status);

    @Query("SELECT * FROM FAMERSORDERS WHERE  farmerCode =:farmer AND status =:status")
    List<FarmerOrdersTable> getFarmerOrderByPayoutNumberByFarmerByStatus(String farmer, int status);

    @Query("SELECT * FROM FAMERSORDERS WHERE  farmerCode =:farmer AND status =:status")
    LiveData<List<FarmerOrdersTable>> getFarmerOrderByFarmerByStatus(String farmer, int status);


    @Query("SELECT * FROM FAMERSORDERS WHERE  farmerCode =:farmer ")
    LiveData<List<FarmerOrdersTable>> getFarmerOrderByFarmer(String farmer);


    @Query("SELECT * FROM FAMERSORDERS WHERE payoutId =:payoutnumber")
    List<FarmerOrdersTable> getFarmerOrderByPayoutNumberListOne(String payoutnumber);

    @Query("UPDATE FAMERSORDERS SET status =:a  WHERE farmerCode =:farmercode AND payoutId =:payoutNumber")
    void approveFarmersPayoutCard(int a, String farmercode, int payoutNumber);


    @Update
    void updateRecord(FarmerOrdersTable farmersOrder);

    @Delete
    void deleteRecord(FarmerOrdersTable farmersOrder);

    @Query("SELECT (SUM(orderAmount)) FROM FAMERSORDERS WHERE farmerCode =:farmercode")
    double getBalanceByFarmerCode(String farmercode);


    @Query("SELECT (SUM(orderAmount)) FROM FAMERSORDERS WHERE payoutId =:payoutid")
    double getBalanceBySumCode(int payoutid);

    @Query("SELECT (SUM(installmentAmount)) FROM FAMERSORDERS WHERE payoutId =:payoutid")
    double getInstallmentSumByPayoutCode(int payoutid);

    @Query("SELECT (SUM(installmentAmount)) FROM FAMERSORDERS WHERE farmerCode =:farmercode")
    double getInstallmentSumByFarmerCode(String farmercode);


    @Query("SELECT * FROM FAMERSORDERS WHERE farmerCode = :code AND timestamp LIKE :date")
    List<FarmerOrdersTable> getFarmerOrderByFarmerByDate(String code, String date);


    @Query("SELECT * FROM FAMERSORDERS WHERE farmerCode = :code  AND timestamp LIKE :today")
    FarmerOrdersTable getFarmerOrderByDateByFarmerByTimeSingle(String code, String today);


    @Query("SELECT * FROM FAMERSORDERS  ORDER BY id DESC LIMIT 1")
    FarmerOrdersTable getLast();


    @Query("UPDATE FAMERSORDERS SET status =:status WHERE  id =:id")
    void updateStatus(int id, int status);

    @Query("SELECT * FROM FAMERSORDERS WHERE timestamp BETWEEN :date1 AND :date2")
    LiveData<List<FarmerOrdersTable>> getFarmerOrdersBetweenDates(Long date1, Long date2);


    @Query("SELECT * FROM FAMERSORDERS WHERE farmerCode = :code AND  timestamp BETWEEN :date1 AND :date2")
    LiveData<List<FarmerOrdersTable>> getFarmerOrdersBetweenDates(Long date1, Long date2, String code);
}
