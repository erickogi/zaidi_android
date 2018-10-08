package com.dev.lishabora.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.lishabora.Models.FamerModel;

import java.util.List;

@Dao
public interface FarmersDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMultipleFramers(FamerModel... famerModels);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMultipleFramers(List<FamerModel> famerModels);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertSingleFramer(FamerModel famerModel);




    @Query("SELECT * FROM FARMERS")
    LiveData<List<FamerModel>> fetchAllData();


    @Query("SELECT * FROM FARMERS WHERE routename LIKE :route")
    LiveData<List<FamerModel>> fetchAllData(String route);

    @Query("SELECT * FROM FARMERS ORDER BY id DESC LIMIT 1")
    LiveData<FamerModel> getLastFarmer();


    @Query("SELECT * FROM FARMERS WHERE code =:code")
    LiveData<FamerModel> getFramerByKeyCode(String code);

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

    @Query("SELECT * FROM FARMERS WHERE routecode =:route")
    LiveData<List<FamerModel>> getFramersByRoute(String route);


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

    @Query("SELECT COUNT(routecode) FROM FARMERS WHERE routecode = :route")
    int getNumberOfRows(String route);

    @Update
    void updateRecord(FamerModel famerModel);

    @Delete
    void deleteRecord(FamerModel famerModel);


    @Query("SELECT * FROM FARMERS WHERE routename LIKE :route AND deleted= :deleted AND archived= :archived AND dummy= :dummy")
    LiveData<List<FamerModel>> getFarmerByStatusByRoute(int deleted, int archived, int dummy, String route);

    @Query("SELECT * FROM FARMERS WHERE  deleted= :deleted AND archived= :archived AND dummy= :dummy")
    LiveData<List<FamerModel>> getFarmerByStatus(int deleted, int archived, int dummy);


    //DELETED

    @Query("SELECT * FROM FARMERS WHERE routename LIKE :route AND deleted= :deleted ")
    LiveData<List<FamerModel>> getFarmerByStatusByRouteDeleted(int deleted, String route);

    @Query("SELECT * FROM FARMERS WHERE  deleted= :deleted ")
    LiveData<List<FamerModel>> getFarmerByStatusDeleted(int deleted);


    //ARCHIVED

    @Query("SELECT * FROM FARMERS WHERE routename LIKE :route AND archived= :archived ")
    LiveData<List<FamerModel>> getFarmerByStatusByRouteArchived(int archived, String route);

    @Query("SELECT * FROM FARMERS WHERE   archived= :archived ")
    LiveData<List<FamerModel>> getFarmerByStatusArchived(int archived);

    //DUMMY


    @Query("SELECT * FROM FARMERS WHERE routename LIKE :route AND dummy= :dummy")
    LiveData<List<FamerModel>> getFarmerByStatusByRouteDummy(int dummy, String route);

    @Query("SELECT * FROM FARMERS WHERE  dummy= :dummy")
    LiveData<List<FamerModel>> getFarmerByStatusDummy(int dummy);


    @Query("SELECT * FROM FARMERS WHERE  cyclecode= :code")
    LiveData<List<FamerModel>> getAllByCycleCode(String code);

    @Query("SELECT * FROM FARMERS WHERE  cyclecode= :code")
    List<FamerModel> getAllByCycleCodeOne(String code);



    @Query("SELECT COUNT(id) FROM FARMERS WHERE cyclecode = :code")
    int getFarmersCountByCycle(String code);

}
