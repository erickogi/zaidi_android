package com.dev.lishabora.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.lishabora.Models.Trader.LoanPayments;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

//import com.dev.lishabora.Models.LoanPayments;

@Dao
public interface LoanPaymentsDao {


    @Insert(onConflict = REPLACE)
    void insertMultiple(List<LoanPayments> loanPayments);

    @Insert(onConflict = REPLACE)
    void insertSingle(LoanPayments loanPayments);


    @Query("SELECT * FROM LOANPAYMENTS")
    LiveData<List<LoanPayments>> fetchAllData();

    @Query("SELECT * FROM LOANPAYMENTS WHERE id =:keyid")
    LiveData<LoanPayments> getLoanPaymentById(int keyid);

    @Query("SELECT * FROM LOANPAYMENTS WHERE id =:keyid")
    LoanPayments getLoanPaymentByIdOne(int keyid);


    @Query("SELECT * FROM LOANPAYMENTS WHERE timestamp =:date")
    LiveData<List<LoanPayments>> getLoanPaymentByDate(String date);

    @Query("SELECT * FROM LOANPAYMENTS WHERE loanId =:loanId")
    LiveData<List<LoanPayments>> getLoanPaymentByLoanId(String loanId);

    @Query("SELECT * FROM LOANPAYMENTS WHERE payoutNo =:payoutNo")
    LiveData<List<LoanPayments>> getLoanPaymentByPaout(int payoutNo);


    @Query("SELECT * FROM LOANPAYMENTS WHERE  paymentMethod =:payment ")
    LiveData<List<LoanPayments>> getLoanPaymentByPayment(String payment);

    @Query("SELECT * FROM LOANPAYMENTS WHERE  refNo =:refNo ")
    LiveData<List<LoanPayments>> getLoanPaymentByRefNo(String refNo);


    @Query("SELECT * FROM LOANPAYMENTS WHERE refNo =:payoutnumber")
    List<LoanPayments> getLoanPaymentByPayoutNumberListOne(String payoutnumber);


    @Update
    void updateRecord(LoanPayments loanPayment);

    @Delete
    void deleteRecord(LoanPayments loanPayment);

    @Query("SELECT (SUM(amountPaid)) FROM LOANPAYMENTS WHERE loanId =:loanId")
    double getSumPaid(int loanId);


    @Query("SELECT (SUM(amountPaid)) FROM LOANPAYMENTS WHERE payoutNo =:payoutid")
    double getSumPaidByPayout(int payoutid);


    @Query("SELECT * FROM LOANPAYMENTS WHERE loanId = :loanId  AND timestamp LIKE :today")
    LoanPayments getLoanPaymentByDateByFarmerByTimeSingle(int loanId, String today);


    @Query("SELECT * FROM LOANPAYMENTS  ORDER BY id DESC LIMIT 1")
    LoanPayments getLast();


    @Query("SELECT * FROM LOANPAYMENTS WHERE timestamp BETWEEN :date1 AND :date2")
    LiveData<List<LoanPayments>> getLoanPaymentsBetweenDates(Long date1, Long date2);


    @Query("SELECT * FROM LOANPAYMENTS WHERE loanId = :loanId AND  timestamp BETWEEN :date1 AND :date2")
    LiveData<List<LoanPayments>> getLoanPaymentsBetweenDates(Long date1, Long date2, int loanId);


}
