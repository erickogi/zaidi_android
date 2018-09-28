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

    @Query("SELECT * FROM FAMERSLOANS WHERE id =:keyid")
    LiveData<FarmerLoansTable> getFarmerLoanById(int keyid);

    @Query("SELECT * FROM FAMERSLOANS WHERE id =:keyid")
    FarmerLoansTable getFarmerLoanByIdOne(int keyid);


    @Query("SELECT * FROM FAMERSLOANS WHERE timestamp =:date")
    LiveData<List<FarmerLoansTable>> getFarmerLoanByDate(String date);

    @Query("SELECT * FROM FAMERSLOANS WHERE payoutId =:payoutnumber")
    LiveData<List<FarmerLoansTable>> getFarmerLoanByPayoutNumber(String payoutnumber);

    @Query("SELECT * FROM FAMERSLOANS WHERE collectionId =:collId")
    LiveData<List<FarmerLoansTable>> getFarmerLoansByCollection(int collId);

    @Query("SELECT * FROM FAMERSLOANS WHERE collectionId =:collId")
    FarmerLoansTable getFarmerLoanByCollectionOne(int collId);

    @Query("SELECT * FROM FAMERSLOANS WHERE collectionId =:collId")
    LiveData<FarmerLoansTable> getFarmerLoanByCollection(int collId);


    @Query("SELECT * FROM FAMERSLOANS WHERE payoutId =:payoutnumber AND farmerCode =:farmer")
    LiveData<List<FarmerLoansTable>> getFarmerLoanByPayoutNumberByFarmer(String payoutnumber, String farmer);

    @Query("SELECT * FROM FAMERSLOANS WHERE payoutId =:payoutnumber AND farmerCode =:farmer AND status =:status")
    List<FarmerLoansTable> getFarmerLoanByPayoutNumberByFarmerByStatus(String payoutnumber, String farmer, int status);


    @Query("SELECT * FROM FAMERSLOANS WHERE   farmerCode =:farmer AND status =:status")
    List<FarmerLoansTable> getFarmerLoanByPayoutNumberByFarmerByStatus(String farmer, int status);


    @Query("SELECT * FROM FAMERSLOANS WHERE   farmerCode =:farmer AND status =:status")
    LiveData<List<FarmerLoansTable>> getFarmerLoanByFarmerByStatus(String farmer, int status);


    @Query("SELECT * FROM FAMERSLOANS WHERE  farmerCode =:farmer ")
    LiveData<List<FarmerLoansTable>> getFarmerLoanByFarmer(String farmer);


    @Query("SELECT * FROM FAMERSLOANS WHERE payoutId =:payoutnumber")
    List<FarmerLoansTable> getFarmerLoanByPayoutNumberListOne(String payoutnumber);

    @Query("UPDATE FAMERSLOANS SET status =:a  WHERE farmerCode =:farmercode AND payoutId =:payoutNumber")
    void approveFarmersPayoutCard(int a, String farmercode, int payoutNumber);


    @Update
    void updateRecord(FarmerLoansTable farmersLoan);

    @Delete
    void deleteRecord(FarmerLoansTable farmersLoan);

    @Query("SELECT (SUM(loanAmount)) FROM FAMERSLOANS WHERE farmerCode =:farmercode")
    double getBalanceByFarmerCode(String farmercode);


    @Query("SELECT (SUM(loanAmount)) FROM FAMERSLOANS WHERE payoutId =:payoutid")
    double getBalanceBySumCode(int payoutid);

    @Query("SELECT (SUM(installmentAmount)) FROM FAMERSLOANS WHERE payoutId =:payoutid")
    double getInstallmentSumByPayoutCode(int payoutid);

    @Query("SELECT (SUM(installmentAmount)) FROM FAMERSLOANS WHERE farmerCode =:code")
    double getInstallmentSumByFarmerCode(String code);


    @Query("SELECT * FROM FAMERSLOANS WHERE farmerCode = :code AND timestamp LIKE :date")
    List<FarmerLoansTable> getFarmerLoanByFarmerByDate(String code, String date);


    @Query("SELECT * FROM FAMERSLOANS WHERE farmerCode = :code  AND timestamp LIKE :today")
    FarmerLoansTable getFarmerLoanByDateByFarmerByTimeSingle(String code, String today);


    @Query("SELECT * FROM FAMERSLOANS  ORDER BY id DESC LIMIT 1")
    FarmerLoansTable getLast();


    @Query("UPDATE FAMERSLOANS SET status =:status WHERE  id =:id")
    void updateStatus(int id, int status);

    @Query("SELECT * FROM FAMERSLOANS WHERE timestamp BETWEEN :date1 AND :date2")
    LiveData<List<FarmerLoansTable>> getFarmerLoansBetweenDates(Long date1, Long date2);


    @Query("SELECT * FROM FAMERSLOANS WHERE farmerCode = :code AND  timestamp BETWEEN :date1 AND :date2")
    LiveData<List<FarmerLoansTable>> getFarmerLoansBetweenDates(Long date1, Long date2, String code);
}
