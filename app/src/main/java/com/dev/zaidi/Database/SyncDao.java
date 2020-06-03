package com.dev.zaidi.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.zaidi.Models.SyncModel;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface SyncDao {


    @Insert(onConflict = REPLACE)
    void insertMultipleSyncs(List<SyncModel> SyncModels);

    @Insert(onConflict = REPLACE)
    void insertSingleSync(SyncModel SyncModel);


    @Query("SELECT * FROM SYNC")
    LiveData<List<SyncModel>> fetchAllData();

    @Query("SELECT * FROM SYNC WHERE id =:keyid")
    LiveData<SyncModel> getSyncByKeyID(int keyid);


    @Query("SELECT * FROM SYNC ")
    LiveData<List<SyncModel>> getAllByStatus();

    @Query("SELECT * FROM SYNC LIMIT :i")
    List<SyncModel> getAllByStatusRaw(int i);



    @Query("SELECT * FROM SYNC WHERE id =:id")
    LiveData<SyncModel> getSyncById(int id);


    @Update
    void updateRecord(SyncModel SyncModel);

    @Delete
    void deleteRecord(SyncModel SyncModel);

    @Query("SELECT COUNT(id) FROM sync ")
    int getCount();


    @Query("SELECT * FROM SYNC  ORDER BY id ASC LIMIT 1")
    SyncModel getEarliest();

}
