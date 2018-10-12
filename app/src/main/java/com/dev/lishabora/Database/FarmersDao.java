package com.dev.lishabora.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.FarmerRouteBalance;

import java.util.List;

@Dao
public interface FarmersDao {

    @Query("SELECT FARMERS.*,  ROUTES.*, FARMERBALANCE.* FROM FARMERS INNER JOIN ROUTES ON ROUTES.code = FARMERS.routecode INNER JOIN FARMERBALANCE ON FARMERBALANCE.farmerCode = FARMERS.code ")
    LiveData<List<FarmerRouteBalance>> findAllBalRoute();


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMultipleFramers(FamerModel... famerModels);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMultipleFramers(List<FamerModel> famerModels);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertSingleFramer(FamerModel famerModel);


    @Query("SELECT FARMERS.*,  ROUTES.*, FARMERBALANCE.* FROM FARMERS INNER JOIN ROUTES ON ROUTES.code = FARMERS.routecode INNER JOIN FARMERBALANCE ON FARMERBALANCE.farmerCode = FARMERS.code")
    LiveData<List<FarmerRouteBalance>> fetchAllData();


    @Query("SELECT FARMERS.*,  ROUTES.*, FARMERBALANCE.* FROM FARMERS INNER JOIN ROUTES ON ROUTES.code = FARMERS.routecode INNER JOIN FARMERBALANCE ON FARMERBALANCE.farmerCode = FARMERS.code WHERE routename LIKE :route")
    LiveData<List<FarmerRouteBalance>> fetchAllData(String route);

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


    @Query("SELECT FARMERS.*,  ROUTES.*, FARMERBALANCE.* FROM FARMERS INNER JOIN ROUTES ON ROUTES.code = FARMERS.routecode INNER JOIN FARMERBALANCE ON FARMERBALANCE.farmerCode = FARMERS.code WHERE FARMERS.routename LIKE :route AND FARMERS.deleted= :deleted AND FARMERS.archived= :archived AND FARMERS.dummy= :dummy")
    LiveData<List<FarmerRouteBalance>> getFarmerByStatusByRoute(int deleted, int archived, int dummy, String route);

    @Query("SELECT FARMERS.*,  ROUTES.*, FARMERBALANCE.* FROM FARMERS INNER JOIN ROUTES ON ROUTES.code = FARMERS.routecode INNER JOIN FARMERBALANCE ON FARMERBALANCE.farmerCode = FARMERS.code WHERE  FARMERS.deleted= :deleted AND FARMERS.archived= :archived AND FARMERS.dummy= :dummy")
    LiveData<List<FarmerRouteBalance>> getFarmerByStatus(int deleted, int archived, int dummy);


    //DELETED

    @Query("SELECT FARMERS.*,  ROUTES.*, FARMERBALANCE.* FROM FARMERS INNER JOIN ROUTES ON ROUTES.code = FARMERS.routecode INNER JOIN FARMERBALANCE ON FARMERBALANCE.farmerCode = FARMERS.code WHERE routename LIKE :route AND deleted= :deleted ")
    LiveData<List<FarmerRouteBalance>> getFarmerByStatusByRouteDeleted(int deleted, String route);

    @Query("SELECT FARMERS.*,  ROUTES.*, FARMERBALANCE.* FROM FARMERS INNER JOIN ROUTES ON ROUTES.code = FARMERS.routecode INNER JOIN FARMERBALANCE ON FARMERBALANCE.farmerCode = FARMERS.code WHERE  deleted= :deleted ")
    LiveData<List<FarmerRouteBalance>> getFarmerByStatusDeleted(int deleted);


    //ARCHIVED

    @Query("SELECT FARMERS.*,  ROUTES.*, FARMERBALANCE.* FROM FARMERS INNER JOIN ROUTES ON ROUTES.code = FARMERS.routecode INNER JOIN FARMERBALANCE ON FARMERBALANCE.farmerCode = FARMERS.code WHERE routename LIKE :route AND archived= :archived ")
    LiveData<List<FarmerRouteBalance>> getFarmerByStatusByRouteArchived(int archived, String route);

    @Query("SELECT FARMERS.*,  ROUTES.*, FARMERBALANCE.* FROM FARMERS INNER JOIN ROUTES ON ROUTES.code = FARMERS.routecode INNER JOIN FARMERBALANCE ON FARMERBALANCE.farmerCode = FARMERS.code WHERE   archived= :archived ")
    LiveData<List<FarmerRouteBalance>> getFarmerByStatusArchived(int archived);

    //DUMMY


    @Query("SELECT FARMERS.*,  ROUTES.*, FARMERBALANCE.* FROM FARMERS INNER JOIN ROUTES ON ROUTES.code = FARMERS.routecode INNER JOIN FARMERBALANCE ON FARMERBALANCE.farmerCode = FARMERS.code WHERE routename LIKE :route AND dummy= :dummy")
    LiveData<List<FarmerRouteBalance>> getFarmerByStatusByRouteDummy(int dummy, String route);

    @Query("SELECT FARMERS.*,  ROUTES.*, FARMERBALANCE.* FROM FARMERS INNER JOIN ROUTES ON ROUTES.code = FARMERS.routecode INNER JOIN FARMERBALANCE ON FARMERBALANCE.farmerCode = FARMERS.code WHERE  dummy= :dummy")
    LiveData<List<FarmerRouteBalance>> getFarmerByStatusDummy(int dummy);


    @Query("SELECT FARMERS.*,  ROUTES.*, FARMERBALANCE.* FROM FARMERS INNER JOIN ROUTES ON ROUTES.code = FARMERS.routecode INNER JOIN FARMERBALANCE ON FARMERBALANCE.farmerCode = FARMERS.code WHERE  cyclecode= :code")
    LiveData<List<FarmerRouteBalance>> getAllByCycleCode(String code);

    @Query("SELECT * FROM FARMERS WHERE  cyclecode= :code")
    List<FamerModel> getAllByCycleCodeOne(String code);



    @Query("SELECT COUNT(id) FROM FARMERS WHERE cyclecode = :code")
    int getFarmersCountByCycle(String code);

}
