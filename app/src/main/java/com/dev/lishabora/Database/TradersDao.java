package com.dev.lishabora.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.lishabora.Models.Trader.TraderModel;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TradersDao {


    @Insert(onConflict = REPLACE)
    void insertMultipleTraders(TraderModel... traderModels);

    @Insert(onConflict = REPLACE)
    void insertMultipleTraders(List<TraderModel> traderModels);

    @Insert(onConflict = REPLACE)
    void insertSingleTrader(TraderModel traderModel);




    @Query("SELECT * FROM TRADERS")
    LiveData<List<TraderModel>> fetchAllData();

    @Query("SELECT * FROM TRADERS WHERE id =:keyid")
    LiveData<TraderModel> getTraderByKeyID(int keyid);

    @Query("SELECT * FROM TRADERS WHERE archived =:archived")
    LiveData<List<TraderModel>> getAllByArchivedStatus(int archived);

    @Query("SELECT * FROM TRADERS WHERE deleted =:deleted")
    LiveData<List<TraderModel>> getAllByDeleteStatus(int deleted);

    @Query("SELECT * FROM TRADERS WHERE dummy =:dummy")
    LiveData<List<TraderModel>> getAllByDummyStatus(int dummy);

    @Query("SELECT * FROM TRADERS WHERE status =:status")
    LiveData<List<TraderModel>> getAllByStatus(String status);



    @Query("SELECT * FROM TRADERS WHERE code =:code")
    LiveData<TraderModel> getTraderByCode(String code);

    @Query("SELECT * FROM TRADERS WHERE code =:code")
    TraderModel getTraderByCodeOne(String code);

    @Query("SELECT * FROM TRADERS WHERE names =:names")
    LiveData<TraderModel> getTradersByNames(String names);

    @Query("SELECT * FROM TRADERS WHERE entitycode =:entitycode")
    LiveData<List<TraderModel>> getTradersByEntityCode(String entitycode);

    @Query("SELECT * FROM TRADERS WHERE names LIKE :names")
    LiveData<List<TraderModel>> searchByNames(String names);

    @Query("SELECT * FROM TRADERS WHERE code LIKE :code")
    LiveData<List<TraderModel>> searchByCode(String code);

    @Query("SELECT * FROM TRADERS WHERE mobile LIKE :mobile")
    LiveData<List<TraderModel>> searchByMobile(String mobile);


    @Query("SELECT * FROM TRADERS WHERE names LIKE :names OR mobile LIKE :moile AND dummy =:dummy AND deleted = :deleted AND archived=:archived ")
    LiveData<List<TraderModel>> search(String names,String moile,int dummy,int deleted,int archived);



    @Update
    void updateRecord(TraderModel traderModel);

    @Delete
    void deleteRecord(TraderModel traderModel);


}
