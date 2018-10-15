package com.dev.lishabora.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.lishabora.Models.Trader.FarmerLoansTable;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

//import com.dev.lishabora.Models.FarmerLoansTable;

@Dao
public interface FarmersLoansDao {


    @Insert(onConflict = REPLACE)
    void insertMultioleFarmerLoans(List<FarmerLoansTable> farmersLoans);

    @Insert(onConflict = REPLACE)
    void insertSingleFarmerLoan(FarmerLoansTable farmersLoans);


    @Query("SELECT * FROM FAMERSLOANS")
    LiveData<List<FarmerLoansTable>> fetchAllData();

    @Query("SELECT * FROM FAMERSLOANS WHERE code =:code")
    LiveData<FarmerLoansTable> getFarmerLoanByCode(String code);

    @Query("SELECT * FROM FAMERSLOANS WHERE code =:code")
    FarmerLoansTable getFarmerLoanByCodeOne(String code);


    @Query("SELECT * FROM FAMERSLOANS WHERE timestamp =:date")
    LiveData<List<FarmerLoansTable>> getFarmerLoanByDate(String date);

    @Query("SELECT * FROM FAMERSLOANS WHERE payoutCode =:payoutCode")
    LiveData<List<FarmerLoansTable>> getFarmerLoanByPayoutCode(String payoutCode);

    @Query("SELECT * FROM FAMERSLOANS WHERE payoutCode =:payoutCode")
    List<FarmerLoansTable> getFarmerLoanByPayoutCodeOne(String payoutCode);


    @Query("SELECT * FROM FAMERSLOANS WHERE collectionCode =:collCode")
    LiveData<List<FarmerLoansTable>> getFarmerLoansByCollection(int collCode);

    @Query("SELECT * FROM FAMERSLOANS WHERE collectionCode =:collCode")
    FarmerLoansTable getFarmerLoanByCollectionOne(String collCode);

    @Query("SELECT * FROM FAMERSLOANS WHERE collectionCode =:collCode")
    LiveData<FarmerLoansTable> getFarmerLoanByCollection(String collCode);


    @Query("SELECT * FROM FAMERSLOANS WHERE payoutCode =:payoutCode AND farmerCode =:farmer")
    LiveData<List<FarmerLoansTable>> getFarmerLoanByPayoutCodeByFarmer(String payoutCode, String farmer);

    @Query("SELECT * FROM FAMERSLOANS WHERE payoutCode =:payoutCode AND farmerCode =:farmer")
    List<FarmerLoansTable> getFarmerLoanByPayoutCodeByFarmerOne(String payoutCode, String farmer);

    @Query("SELECT * FROM FAMERSLOANS WHERE payoutCode =:payoutCode AND farmerCode =:farmer AND status =:status")
    List<FarmerLoansTable> getFarmerLoanByPayoutCodeByFarmerByStatus(String payoutCode, String farmer, int status);


    @Query("SELECT * FROM FAMERSLOANS WHERE   farmerCode =:farmer AND status =:status")
    List<FarmerLoansTable> getFarmerLoanByPayoutCodeByFarmerByStatus(String farmer, int status);


    @Query("SELECT * FROM FAMERSLOANS WHERE   farmerCode =:farmer AND status =:status")
    LiveData<List<FarmerLoansTable>> getFarmerLoanByFarmerByStatus(String farmer, int status);


    @Query("SELECT * FROM FAMERSLOANS WHERE  farmerCode =:farmer ")
    LiveData<List<FarmerLoansTable>> getFarmerLoanByFarmer(String farmer);


    @Query("SELECT * FROM FAMERSLOANS WHERE payoutCode =:payoutCode")
    List<FarmerLoansTable> getFarmerLoanByPayoutCodeListOne(String payoutCode);

    @Query("UPDATE FAMERSLOANS SET status =:a  WHERE farmerCode =:farmercode AND payoutCode =:payoutCode")
    void approveFarmersPayoutCard(int a, String farmercode, String payoutCode);


    @Update
    void updateRecord(FarmerLoansTable farmersLoan);

    @Delete
    void deleteRecord(FarmerLoansTable farmersLoan);

    @Query("SELECT (SUM(loanAmount)) FROM FAMERSLOANS WHERE farmerCode =:farmercode")
    double getBalanceByFarmerCode(String farmercode);


    @Query("SELECT (SUM(loanAmount)) FROM FAMERSLOANS WHERE payoutCode =:payoutCode")
    double getBalanceBySumCode(String payoutCode);

    @Query("SELECT (SUM(installmentAmount)) FROM FAMERSLOANS WHERE payoutCode =:payoutCode")
    double getInstallmentSumByPayoutCode(String payoutCode);

    @Query("SELECT (SUM(installmentAmount)) FROM FAMERSLOANS WHERE farmerCode =:code")
    double getInstallmentSumByFarmerCode(String code);


    @Query("SELECT * FROM FAMERSLOANS WHERE farmerCode = :code AND timestamp LIKE :date")
    List<FarmerLoansTable> getFarmerLoanByFarmerByDate(String code, String date);


    @Query("SELECT * FROM FAMERSLOANS WHERE farmerCode = :code  AND timestamp LIKE :today")
    FarmerLoansTable getFarmerLoanByDateByFarmerByTimeSingle(String code, String today);


    @Query("SELECT * FROM FAMERSLOANS  ORDER BY id DESC LIMIT 1")
    FarmerLoansTable getLast();


    @Query("UPDATE FAMERSLOANS SET status =:status WHERE  code =:code")
    void updateStatus(String code, int status);

    @Query("SELECT * FROM FAMERSLOANS WHERE timestamp BETWEEN :date1 AND :date2")
    LiveData<List<FarmerLoansTable>> getFarmerLoansBetweenDates(Long date1, Long date2);


    @Query("SELECT * FROM FAMERSLOANS WHERE farmerCode = :code AND  timestamp BETWEEN :date1 AND :date2")
    LiveData<List<FarmerLoansTable>> getFarmerLoansBetweenDates(Long date1, Long date2, String code);
}
