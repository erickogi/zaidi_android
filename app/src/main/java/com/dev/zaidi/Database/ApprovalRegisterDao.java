package com.dev.zaidi.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.zaidi.Models.ApprovalRegisterModel;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ApprovalRegisterDao {


    @Insert(onConflict = REPLACE)
    void insertMultiple(ApprovalRegisterModel... ApprovalRegisterModels);

    @Insert(onConflict = REPLACE)
    void insertMultiple(List<ApprovalRegisterModel> ApprovalRegisterModels);

    @Insert(onConflict = REPLACE)
    void insertSingle(ApprovalRegisterModel ApprovalRegisterModel);


    @Query("SELECT * FROM approvalRegister")
    LiveData<List<ApprovalRegisterModel>> fetchAllData();

    @Query("SELECT * FROM approvalRegister")
    List<ApprovalRegisterModel> fetchAllDataOne();


    @Query("SELECT * FROM approvalRegister WHERE farmerCode =:farmerCode AND payoutCode =:payoutCode")
    LiveData<ApprovalRegisterModel> getByFarmerPayoutCode(String farmerCode, String payoutCode);

    @Query("SELECT * FROM approvalRegister WHERE farmerCode =:farmerCode AND payoutCode =:payoutCode")
    ApprovalRegisterModel getByFarmerPayoutCodeOne(String farmerCode, String payoutCode);


    @Update
    void updateRecord(ApprovalRegisterModel ApprovalRegisterModel);

    @Delete
    void deleteRecord(ApprovalRegisterModel ApprovalRegisterModel);


}
