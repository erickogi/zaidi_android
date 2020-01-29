package com.dev.zaidi.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.zaidi.Models.SyncDownObserver;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface SyncObserverDao {


    @Insert(onConflict = REPLACE)
    void insertMultipleSyncs(List<SyncDownObserver> SyncDownObservers);

    @Insert(onConflict = REPLACE)
    void insertSingleSync(SyncDownObserver SyncDownObserver);


    @Query("SELECT * FROM SYCNDOWNOBSERVER")
    LiveData<List<SyncDownObserver>> fetchAllData();


    @Query("SELECT * FROM SYCNDOWNOBSERVER ORDER BY id ASC LIMIT 1")
    SyncDownObserver getRecord();

    @Query("SELECT * FROM SYCNDOWNOBSERVER ORDER BY id ASC LIMIT 1")
    LiveData<SyncDownObserver> getRecordObserve();


    @Update
    void updateRecord(SyncDownObserver SyncDownObserver);

    @Delete
    void deleteRecord(SyncDownObserver SyncDownObserver);

    @Query("SELECT COUNT(id) FROM sync ")
    int getCount();


    @Query("SELECT * FROM SYCNDOWNOBSERVER  ORDER BY id ASC LIMIT 1")
    SyncDownObserver getEarliest();

}
