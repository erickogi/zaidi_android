package com.dev.lishabora.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.lishabora.Models.Collection;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CollectionsDao {


    @Insert(onConflict = REPLACE)
    void insertMultioleCollections(List<Collection> collections);

    @Insert(onConflict = REPLACE)
    void insertSingleCollection(Collection collection);


    @Query("SELECT * FROM COLLECTIONTRANSACTIONS")
    LiveData<List<Collection>> fetchAllData();

    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE id =:keyid")
    LiveData<Collection> getCollectionById(int keyid);


    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE dayDate =:date")
    LiveData<List<Collection>> getCollectionByDate(String date);


    @Update
    void updateRecord(Collection collection);

    @Delete
    void deleteRecord(Collection collection);


    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE farmerCode = :code AND dayDate LIKE :date")
    List<Collection> getCollectionByFarmerByDate(String code, String date);

    @Query("SELECT * FROM COLLECTIONTRANSACTIONS WHERE cycleCode = :cycleCode ORDER BY id DESC LIMIT 1")
    Collection getLast(String cycleCode);
}
