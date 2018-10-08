package com.dev.lishabora.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dev.lishabora.Models.ProductsModel;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ProductsDao {


    @Insert(onConflict = REPLACE)
    void insertMultipleProducts(List<ProductsModel> productsModels);

    @Insert(onConflict = REPLACE)
    void insertSingleProduct(ProductsModel productsModel);

    @Query("SELECT COUNT(code) FROM PRODUCTS")
    LiveData<Integer> getNumberOfRows();


    @Query("SELECT * FROM PRODUCTS")
    LiveData<List<ProductsModel>> fetchAllData();

    @Query("SELECT * FROM PRODUCTS WHERE code =:code")
    LiveData<ProductsModel> getProductByKeyCode(String code);


    @Query("SELECT * FROM PRODUCTS WHERE subscribed =:status")
    LiveData<List<ProductsModel>> getAllByStatus(int status);


    @Query("SELECT * FROM PRODUCTS WHERE code =:code")
    LiveData<ProductsModel> getProductByCode(String code);

    @Query("SELECT * FROM PRODUCTS WHERE names LIKE :names")
    LiveData<List<ProductsModel>> searchByNames(String names);


    @Update
    void updateRecord(ProductsModel UnitsModel);

    @Delete
    void deleteRecord(ProductsModel UnitsModel);


    @Query("SELECT COUNT(id) FROM PRODUCTS ")
    int getCount();


}
