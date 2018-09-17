package com.dev.lishabora.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.lishabora.Models.SyncModel;

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


    @Query("SELECT * FROM SYNC WHERE syncStatus =:status")
    LiveData<List<SyncModel>> getAllByStatus(int status);


    @Query("SELECT * FROM SYNC WHERE id =:id")
    LiveData<SyncModel> getSyncById(int id);


    @Update
    void updateRecord(SyncModel SyncModel);

    @Delete
    void deleteRecord(SyncModel SyncModel);


}
