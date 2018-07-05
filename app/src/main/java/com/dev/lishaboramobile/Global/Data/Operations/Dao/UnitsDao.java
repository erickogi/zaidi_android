package com.dev.lishaboramobile.Global.Data.Operations.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.lishaboramobile.Trader.Models.UnitsModel;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UnitsDao {


    @Insert(onConflict = REPLACE)
    void insertMultipleUnits(UnitsModel... UnitsModels);

    @Insert(onConflict = REPLACE)
    void insertMultipleUnits(List<UnitsModel> UnitsModels);

    @Insert(onConflict = REPLACE)
    void insertSingleUnit(UnitsModel UnitsModel);

  


    @Query("SELECT * FROM UNITS")
    LiveData<List<UnitsModel>> fetchAllData();

    @Query("SELECT * FROM UNITS WHERE id =:keyid")
    LiveData<UnitsModel> getUnitByKeyID(int keyid);

  
    @Query("SELECT * FROM UNITS WHERE status =:status")
    LiveData<List<UnitsModel>> getAllByStatus(String status);



    @Query("SELECT * FROM UNITS WHERE code =:code")
    LiveData<UnitsModel> getUnitByCode(String code);

    @Query("SELECT * FROM UNITS WHERE unitprice =:price")
    LiveData<UnitsModel> getUnitByPrice(String price);

    @Query("SELECT * FROM UNITS WHERE transactedby =:transactedby")
    LiveData<List<UnitsModel>> getUnitsByEntityCode(String transactedby);

    @Query("SELECT * FROM UNITS WHERE unit LIKE :unit")
    LiveData<List<UnitsModel>> searchByUnit(String unit);

   


    @Update
    void updateRecord(UnitsModel UnitsModel);

    @Delete
    void deleteRecord(UnitsModel UnitsModel);


}
