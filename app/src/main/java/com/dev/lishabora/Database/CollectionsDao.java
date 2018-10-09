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

    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE code =:code")
    LiveData<Collection> getCollectionByCode(String code);

    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE code =:code")
    Collection getCollectionByCodeOne(String code);



    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE daydate =:date")
    LiveData<List<Collection>> getCollectionByDate(String date);

    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE payoutCode =:payoutCode")
    LiveData<List<Collection>> getCollectionByPayoutCode(String payoutCode);


    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE payoutCode =:payoutCode AND farmerCode =:farmer")
    LiveData<List<Collection>> getCollectionByPayoutCodeByFarmer(String payoutCode, String farmer);

    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE  farmerCode =:farmer ")
    LiveData<List<Collection>> getCollectionByFarmer(String farmer);


    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE payoutCode =:payoutCode")
    List<Collection> getCollectionByPayoutCodeListOne(String payoutCode);

    @Query("UPDATE COLLECTIONTRANSACTIONS SET approved =:a  WHERE farmerCode =:farmercode AND payoutCode =:payoutCode")
    void approveFarmersPayoutCard(int a, String farmercode, String payoutCode);


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

    @Query("SELECT SUM(milkCollectedValueKshAm+milkCollectedValueKshPm) FROM  COLLECTIONTRANSACTIONS  WHERE farmerCode = :farmercode AND payoutCode = :payoutcode")
    Double getSumOfLastMilkFarmerPayoutKshD(String farmercode, String payoutcode);


    @Query("SELECT SUM(milkCollectedValueLtrsAm+milkCollectedValueLtrsPm) FROM  COLLECTIONTRANSACTIONS  WHERE farmerCode = :farmercode AND payoutCode = :payoutCode")
    LiveData<Double> getSumOfMilkFarmerPayoutLtrs(String farmercode, String payoutCode);

    @Query("SELECT SUM(milkCollectedValueKshAm+milkCollectedValueKshPm) FROM  COLLECTIONTRANSACTIONS  WHERE farmerCode = :farmercode AND payoutCode = :payoutCode")
    LiveData<Double> getSumOfMilkFarmerPayoutKsh(String farmercode, String payoutCode);

    @Query("SELECT SUM(milkCollectedValueKshAm+milkCollectedValueKshPm) FROM  COLLECTIONTRANSACTIONS  WHERE farmerCode = :farmercode AND payoutCode = :payoutCode")
    Double getSumOfMilkFarmerPayoutKshD(String farmercode, String payoutCode);

    @Query("SELECT SUM(milkCollectedAm+milkCollectedPm) FROM  COLLECTIONTRANSACTIONS  WHERE farmerCode = :farmercode AND payoutCode = :payoutCode")
    LiveData<Double> getSumOfMilkFarmerPayout(String farmercode, String payoutCode);


    @Query("SELECT SUM(loanAmountGivenOutPrice) FROM  COLLECTIONTRANSACTIONS  WHERE farmerCode = :farmercode AND payoutCode = :payoutCode")
    LiveData<Double> getSumOfLoanFarmerPayout(String farmercode, String payoutCode);

    @Query("SELECT SUM(orderGivenOutPrice) FROM  COLLECTIONTRANSACTIONS  WHERE farmerCode = :farmercode AND payoutCode = :payoutCode")
    LiveData<Double> getSumOfOrderFarmerPayout(String farmercode, String payoutCode);

    @Query("UPDATE COLLECTIONTRANSACTIONS SET approved =:status WHERE  payoutCode =:payoutCode")
    void approvePayout(String payoutCode, int status);

    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE dayDateLog BETWEEN :date1 AND :date2")
    LiveData<List<Collection>> getCollectionsBetweenDates(Long date1, Long date2);


    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE farmerCode = :code AND  dayDateLog BETWEEN :date1 AND :date2")
    LiveData<List<Collection>> getCollectionsBetweenDates(Long date1, Long date2, String code);


    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE farmerCode = :code AND  dayDateLog BETWEEN :date1 AND :date2")
    List<Collection> getCollectionsBetweenDatesOne(Long date1, Long date2, String code);
}
