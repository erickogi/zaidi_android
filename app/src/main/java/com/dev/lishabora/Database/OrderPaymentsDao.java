package com.dev.lishabora.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.lishabora.Models.Trader.OrderPayments;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

//import com.dev.lishabora.Models.OrderPayments;

@Dao
public interface OrderPaymentsDao {


    @Insert(onConflict = REPLACE)
    void insertMultiple(List<OrderPayments> orderPayments);

    @Insert(onConflict = REPLACE)
    void insertSingle(OrderPayments orderPayments);


    @Query("SELECT * FROM ORDERPAYMENTS")
    LiveData<List<OrderPayments>> fetchAllData();

    @Query("SELECT * FROM ORDERPAYMENTS WHERE code =:code")
    LiveData<OrderPayments> getOrderPaymentByCode(String code);

    @Query("SELECT * FROM ORDERPAYMENTS WHERE code =:code")
    OrderPayments getOrderPaymentByCodeOne(String code);


    @Query("SELECT * FROM ORDERPAYMENTS WHERE timestamp =:date")
    LiveData<List<OrderPayments>> getOrderPaymentByDate(String date);

    @Query("SELECT * FROM ORDERPAYMENTS WHERE orderCode =:orderCode")
    LiveData<List<OrderPayments>> getOrderPaymentByOrderCode(String orderCode);

    @Query("SELECT * FROM ORDERPAYMENTS WHERE orderCode =:orderCode")
    List<OrderPayments> getOrderPaymentByOrderCodeOne(String orderCode);


    @Query("SELECT * FROM ORDERPAYMENTS WHERE payoutCode =:payoutCode")
    LiveData<List<OrderPayments>> getOrderPaymentByPaout(String payoutCode);


    @Query("SELECT * FROM ORDERPAYMENTS WHERE  paymentMethod =:payment ")
    LiveData<List<OrderPayments>> getOrderPaymentByPayment(String payment);

    @Query("SELECT * FROM ORDERPAYMENTS WHERE  refNo =:refNo ")
    LiveData<List<OrderPayments>> getOrderPaymentByRefNo(String refNo);


    @Query("SELECT * FROM ORDERPAYMENTS WHERE refNo =:payoutCode")
    List<OrderPayments> getOrderPaymentByPayoutNumberListOne(String payoutCode);


    @Update
    void updateRecord(OrderPayments loanPayment);

    @Delete
    void deleteRecord(OrderPayments loanPayment);

    @Query("SELECT (SUM(amountPaid)) FROM ORDERPAYMENTS WHERE orderCode =:orderCode")
    double getSumPaid(String orderCode);


    @Query("SELECT (SUM(amountPaid)) FROM ORDERPAYMENTS WHERE payoutCode =:payoutCode")
    double getSumPaidByPayout(String payoutCode);


    @Query("SELECT * FROM ORDERPAYMENTS WHERE orderCode = :orderCode  AND timestamp LIKE :today")
    OrderPayments getOrderPaymentByDateByFarmerByTimeSingle(String orderCode, String today);


    @Query("SELECT * FROM ORDERPAYMENTS  ORDER BY id DESC LIMIT 1")
    OrderPayments getLast();


    @Query("SELECT * FROM ORDERPAYMENTS WHERE timestamp BETWEEN :date1 AND :date2")
    LiveData<List<OrderPayments>> getOrderPaymentsBetweenDates(Long date1, Long date2);


    @Query("SELECT * FROM ORDERPAYMENTS WHERE orderCode = :orderCode AND  timestamp BETWEEN :date1 AND :date2")
    LiveData<List<OrderPayments>> getOrderPaymentsBetweenDates(Long date1, Long date2, String orderCode);

    @Query("SELECT * FROM ORDERPAYMENTS WHERE orderCode = :orderId  AND ( (timestamp BETWEEN :date1 AND :date2) OR (payoutCode =:payoutCode) ) ")
    List<OrderPayments> getOrderPaymentsByOrderIdBetweenDatesorByPayoutCode(Long date1, Long date2, String orderId, String payoutCode);


}
