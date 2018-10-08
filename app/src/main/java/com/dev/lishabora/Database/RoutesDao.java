package com.dev.lishabora.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.lishabora.Models.RoutesModel;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface RoutesDao {


    @Insert(onConflict = REPLACE)
    void insertMultipleRoutes(RoutesModel... routesModels);

    @Insert(onConflict = REPLACE)
    void insertMultipleRoutes(List<RoutesModel> routesModels);

    @Insert(onConflict = REPLACE)
    void insertSingleRoute(RoutesModel routesModel);


    @Query("SELECT COUNT(code) FROM routes")
    LiveData<Integer> getNumberOfRows();


    @Query("SELECT * FROM ROUTES")
    LiveData<List<RoutesModel>> fetchAllData();

    @Query("SELECT * FROM ROUTES WHERE code =:code")
    LiveData<RoutesModel> getRouteByKeyCode(String code);

  
    @Query("SELECT * FROM ROUTES WHERE status =:status")
    LiveData<List<RoutesModel>> getAllByStatus(String status);



    @Query("SELECT * FROM ROUTES WHERE code =:code")
    LiveData<RoutesModel> getRouteByCode(String code);

    @Query("SELECT * FROM ROUTES WHERE route =:route")
    LiveData<RoutesModel> getRouteByRoute(String route);

    @Query("SELECT * FROM ROUTES WHERE transactedby =:transactedby")
    LiveData<List<RoutesModel>> getRoutesByEntityCode(String transactedby);

    @Query("SELECT * FROM ROUTES WHERE route LIKE :route")
    LiveData<List<RoutesModel>> searchByRoute(String route);

   


    @Update
    void updateRecord(RoutesModel routesModel);

    @Delete
    void deleteRecord(RoutesModel routesModel);


}
