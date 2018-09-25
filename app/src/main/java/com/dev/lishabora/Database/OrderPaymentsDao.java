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

    @Query("SELECT * FROM ORDERPAYMENTS WHERE id =:keyid")
    LiveData<OrderPayments> getOrderPaymentById(int keyid);

    @Query("SELECT * FROM ORDERPAYMENTS WHERE id =:keyid")
    OrderPayments getOrderPaymentByIdOne(int keyid);


    @Query("SELECT * FROM ORDERPAYMENTS WHERE timestamp =:date")
    LiveData<List<OrderPayments>> getOrderPaymentByDate(String date);

    @Query("SELECT * FROM ORDERPAYMENTS WHERE orderId =:loanId")
    LiveData<List<OrderPayments>> getOrderPaymentByOrderId(String loanId);

    @Query("SELECT * FROM ORDERPAYMENTS WHERE payoutNo =:payoutNo")
    LiveData<List<OrderPayments>> getOrderPaymentByPaout(int payoutNo);


    @Query("SELECT * FROM ORDERPAYMENTS WHERE  paymentMethod =:payment ")
    LiveData<List<OrderPayments>> getOrderPaymentByPayment(String payment);

    @Query("SELECT * FROM ORDERPAYMENTS WHERE  refNo =:refNo ")
    LiveData<List<OrderPayments>> getOrderPaymentByRefNo(String refNo);


    @Query("SELECT * FROM ORDERPAYMENTS WHERE refNo =:payoutnumber")
    List<OrderPayments> getOrderPaymentByPayoutNumberListOne(String payoutnumber);


    @Update
    void updateRecord(OrderPayments loanPayment);

    @Delete
    void deleteRecord(OrderPayments loanPayment);

    @Query("SELECT (SUM(amountPaid)) FROM ORDERPAYMENTS WHERE orderId =:orderId")
    double getSumPaid(int orderId);


    @Query("SELECT (SUM(amountPaid)) FROM ORDERPAYMENTS WHERE payoutNo =:payoutid")
    double getSumPaidByPayout(int payoutid);


    @Query("SELECT * FROM ORDERPAYMENTS WHERE orderId = :orderId  AND timestamp LIKE :today")
    OrderPayments getOrderPaymentByDateByFarmerByTimeSingle(int orderId, String today);


    @Query("SELECT * FROM ORDERPAYMENTS  ORDER BY id DESC LIMIT 1")
    OrderPayments getLast();


    @Query("SELECT * FROM ORDERPAYMENTS WHERE timestamp BETWEEN :date1 AND :date2")
    LiveData<List<OrderPayments>> getOrderPaymentsBetweenDates(Long date1, Long date2);


    @Query("SELECT * FROM ORDERPAYMENTS WHERE orderId = :orderId AND  timestamp BETWEEN :date1 AND :date2")
    LiveData<List<OrderPayments>> getOrderPaymentsBetweenDates(Long date1, Long date2, int orderId);


}
