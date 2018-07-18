package com.dev.lishaboramobile.Global.Data.Operations.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.lishaboramobile.Farmer.Models.FamerModel;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface FarmersDao {


    @Insert(onConflict = REPLACE)
    void insertMultipleFramers(FamerModel... famerModels);

    @Insert(onConflict = REPLACE)
    void insertMultipleFramers(List<FamerModel> famerModels);

    @Insert(onConflict = REPLACE)
    void insertSingleFramer(FamerModel famerModel);




    @Query("SELECT * FROM FARMERS")
    LiveData<List<FamerModel>> fetchAllData();

    @Query("SELECT * FROM FARMERS WHERE id =:keyid")
    LiveData<FamerModel> getFramerByKeyID(int keyid);

    @Query("SELECT * FROM FARMERS WHERE archived =:archived")
    LiveData<List<FamerModel>> getAllByArchivedStatus(int archived);

    @Query("SELECT * FROM FARMERS WHERE `deleted` =:deleted")
    LiveData<List<FamerModel>> getAllByDeleteStatus(int deleted);

    @Query("SELECT * FROM FARMERS WHERE dummy =:dummy")
    LiveData<List<FamerModel>> getAllByDummyStatus(int dummy);

    @Query("SELECT * FROM FARMERS WHERE status =:status")
    LiveData<List<FamerModel>> getAllByStatus(String status);



    @Query("SELECT * FROM FARMERS WHERE code =:code")
    LiveData<FamerModel> getFramerByCode(String code);

    @Query("SELECT * FROM FARMERS WHERE names =:names")
    LiveData<FamerModel> getFramersByNames(String names);

    @Query("SELECT * FROM FARMERS WHERE route =:route")
    LiveData<FamerModel> getFramersByRoute(String route);


    @Query("SELECT * FROM FARMERS WHERE entitycode =:entitycode")
    LiveData<List<FamerModel>> getFramerByEntityCode(String entitycode);

    @Query("SELECT * FROM FARMERS WHERE names LIKE :names")
    LiveData<List<FamerModel>> searchByNames(String names);

    @Query("SELECT * FROM FARMERS WHERE code LIKE :code")
    LiveData<List<FamerModel>> searchByCode(String code);

    @Query("SELECT * FROM FARMERS WHERE mobile LIKE :mobile")
    LiveData<List<FamerModel>> searchByMobile(String mobile);


    @Query("SELECT * FROM FARMERS WHERE names LIKE :names OR mobile LIKE :moile AND dummy =:dummy AND `deleted` = :deleted AND archived=:archived ")
    LiveData<List<FamerModel>> search(String names, String moile, int dummy, int deleted, int archived);



    @Update
    void updateRecord(FamerModel famerModel);

    @Delete
    void deleteRecord(FamerModel famerModel);


}
