package com.dev.lishabora.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.lishabora.Models.Cycles;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CyclesDao {


    @Insert(onConflict = REPLACE)
    void insertMultipleCycles(Cycles... Cycless);

    @Insert(onConflict = REPLACE)
    void insertMultipleCycles(List<Cycles> Cycless);

    @Insert(onConflict = REPLACE)
    void insertSingleCycle(Cycles Cycles);


    @Query("SELECT * FROM CYCLES")
    LiveData<List<Cycles>> fetchAllData();

    @Query("SELECT * FROM CYCLES WHERE id =:keyid")
    LiveData<Cycles> getCycleByKeyID(int keyid);

    @Query("SELECT * FROM CYCLES WHERE code =:code")
    LiveData<Cycles> getCycleByKeyCode(String code);

    @Query("SELECT * FROM CYCLES WHERE code =:code")
    Cycles getCycleByKeyCodeOne(String code);




    @Query("SELECT * FROM CYCLES WHERE status =:status")
    LiveData<List<Cycles>> getAllByStatus(String status);


    @Query("SELECT * FROM CYCLES WHERE transactedby =:transactedby")
    LiveData<List<Cycles>> getCyclesByEntityCode(String transactedby);

    @Query("SELECT * FROM CYCLES WHERE cycle LIKE :cycle")
    LiveData<List<Cycles>> searchByCycle(String cycle);


    @Update
    void updateRecord(Cycles Cycles);

    @Delete
    void deleteRecord(Cycles Cycles);


}
