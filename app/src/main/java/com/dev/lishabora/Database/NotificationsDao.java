package com.dev.lishabora.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.lishabora.Models.Notifications;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface NotificationsDao {


    @Insert(onConflict = REPLACE)
    void insertMultipleNotifications(Notifications... notifications);

    @Insert(onConflict = REPLACE)
    void insertMultipleNotifications(List<Notifications> notifications);

    @Insert(onConflict = REPLACE)
    void insertSingleNotification(Notifications notification);


    @Query("SELECT COUNT(code) FROM NOTIFICATIONS")
    LiveData<Integer> getNumberOfRows();


    @Query("SELECT * FROM NOTIFICATIONS ORDER BY id")
    LiveData<List<Notifications>> fetchAllData();

    @Query("SELECT * FROM NOTIFICATIONS WHERE viewd =:viewId ORDER BY id")
    LiveData<List<Notifications>> fetchAllData(int viewId);


    @Update
    void updateRecord(Notifications notification);

    @Delete
    void deleteRecord(Notifications notification);


}
