package com.dev.lishabora.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.lishabora.Models.Collection;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CollectionsDao {


    @Insert(onConflict = REPLACE)
    void insertMultioleCollections(List<Collection> collections);

    @Insert(onConflict = REPLACE)
    void insertSingleCollection(Collection collection);


    @Query("SELECT * FROM COLLECTIONTRANSACTIONS")
    LiveData<List<Collection>> fetchAllData();

    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE id =:keyid")
    LiveData<Collection> getCollectionById(int keyid);

    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE id =:keyid")
    Collection getCollectionByIdOne(int keyid);



    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE daydate =:date")
    LiveData<List<Collection>> getCollectionByDate(String date);

    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE payoutnumber =:payoutnumber")
    LiveData<List<Collection>> getCollectionByPayoutNumber(String payoutnumber);


    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE payoutnumber =:payoutnumber AND farmerCode =:farmer")
    LiveData<List<Collection>> getCollectionByPayoutNumberByFarmer(String payoutnumber, String farmer);

    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE  farmerCode =:farmer ")
    LiveData<List<Collection>> getCollectionByFarmer(String farmer);



    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE payoutnumber =:payoutnumber")
    List<Collection> getCollectionByPayoutNumberListOne(String payoutnumber);

    @Query("UPDATE COLLECTIONTRANSACTIONS SET approved =:a  WHERE farmerCode =:farmercode AND payoutnumber =:payoutNumber")
    void approveFarmersPayoutCard(int a, String farmercode, int payoutNumber);


    @Update
    void updateRecord(Collection collection);

    @Delete
    void deleteRecord(Collection collection);

    @Query("SELECT (SUM(milkCollectedValueKshAm+milkCollectedValueKshPm) - SUM(loanAmountGivenOutPrice+orderGivenOutPrice)) FROM COLLECTIONTRANSACTIONS WHERE farmerCode =:farmercode")
    double getBalanceByFarmerCode(String farmercode);



    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE farmerCode = :code AND dayDate LIKE :date")
    List<Collection> getCollectionByFarmerByDate(String code, String date);

    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE farmerCode = :code AND  timeOfDay =:ampm AND dayDate LIKE :today")
    List<Collection> getCollectionByDateByFarmerByTime(String code, String today, String ampm);

    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE farmerCode = :code AND  timeOfDay =:ampm AND dayDate LIKE :today")
    Collection getCollectionByDateByFarmerByTimeSingle(String code, String today, String ampm);

    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE farmerCode = :code  AND dayDate LIKE :today")
    Collection getCollectionByDateByFarmerByTimeSingle(String code, String today);



    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE cycleCode = :cycleCode ORDER BY id DESC LIMIT 1")
    Collection getLast(String cycleCode);


    @Query("SELECT SUM(milkCollectedValueLtrsAm+milkCollectedValueLtrsPm) FROM  COLLECTIONTRANSACTIONS  WHERE farmerCode = :farmercode AND payoutnumber = :payoutNumber")
    LiveData<Double> getSumOfMilkFarmerPayoutLtrs(String farmercode, int payoutNumber);

    @Query("SELECT SUM(milkCollectedValueKshAm+milkCollectedValueKshPm) FROM  COLLECTIONTRANSACTIONS  WHERE farmerCode = :farmercode AND payoutnumber = :payoutNumber")
    LiveData<Double> getSumOfMilkFarmerPayoutKsh(String farmercode, int payoutNumber);

    @Query("SELECT SUM(milkCollectedAm+milkCollectedPm) FROM  COLLECTIONTRANSACTIONS  WHERE farmerCode = :farmercode AND payoutnumber = :payoutNumber")
    LiveData<Double> getSumOfMilkFarmerPayout(String farmercode, int payoutNumber);


    @Query("SELECT SUM(loanAmountGivenOutPrice) FROM  COLLECTIONTRANSACTIONS  WHERE farmerCode = :farmercode AND payoutnumber = :payoutNumber")
    LiveData<Double> getSumOfLoanFarmerPayout(String farmercode, int payoutNumber);

    @Query("SELECT SUM(orderGivenOutPrice) FROM  COLLECTIONTRANSACTIONS  WHERE farmerCode = :farmercode AND payoutnumber = :payoutNumber")
    LiveData<Double> getSumOfOrderFarmerPayout(String farmercode, int payoutNumber);

    @Query("UPDATE COLLECTIONTRANSACTIONS SET approved =:status WHERE  payoutnumber =:payoutnumber")
    void approvePayout(int payoutnumber, int status);

}
