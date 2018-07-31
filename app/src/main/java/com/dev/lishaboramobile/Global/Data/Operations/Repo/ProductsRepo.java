package com.dev.lishaboramobile.Global.Data.Operations.Repo;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dev.lishaboramobile.Global.Data.LMDatabase;
import com.dev.lishaboramobile.Global.Data.Operations.Dao.ProductsDao;
import com.dev.lishaboramobile.admin.models.ProductsModel;

import java.util.List;

public class ProductsRepo {
    private ProductsDao productsDao;
    private LiveData<List<ProductsModel>> products;
    private LiveData<ProductsModel> product;


    private LMDatabase db;


    public ProductsRepo(Application application) {
        db = LMDatabase.getDatabase(application);
        productsDao = db.productsDao();
    }


    public LiveData<List<ProductsModel>> fetchAllData(boolean isOnline) {
        if (isOnline) {
            return null;
        } else {
            productsDao = db.productsDao();
            return productsDao.fetchAllData();
        }
    }

    public void insert(ProductsModel productsModel) {
        productsDao = db.productsDao();

        new insertUnitAsyncTask(productsDao).execute(productsModel);
    }

    public void insert(List<ProductsModel> productsModels) {
        productsDao = db.productsDao();

        new insertProductsAsyncTask(productsDao).execute(productsModels);
    }

    public void upDateRecord(ProductsModel productsModel) {
        new updateProductAsyncTask(db.productsDao()).execute(productsModel);
    }

    public void deleteRecord(ProductsModel productsModel) {
        new deleteProductAsyncTask(db.productsDao()).execute(productsModel);
    }

    public void insertMultipleUnits(List<ProductsModel> traderModels) {
        productsDao = db.productsDao();

        new insertProductsAsyncTask(productsDao).execute(traderModels);
    }

    public void insertSingleTrader(ProductsModel unitsModel, boolean isOnline) {
        if (isOnline) {
            insertSingleProduct(unitsModel);

        } else {
            db.productsDao().insertSingleProduct(unitsModel);
        }
    }

    private void insertSingleProduct(ProductsModel productsModel) {


    }

    public LiveData<ProductsModel> getProductByKeyID(int key) {
        return db.productsDao().getProductByKeyID(key);
    }

    public LiveData<List<ProductsModel>> getAllByStatus(String status) {
        return db.productsDao().getAllByStatus(status);
    }

    public LiveData<ProductsModel> getUnitByCode(String code) {
        return db.productsDao().getProductByCode(code);
    }

    public LiveData<List<ProductsModel>> searchByNames(String names) {
        return db.productsDao().searchByNames(names);
    }

    private static class insertUnitAsyncTask extends AsyncTask<ProductsModel, Void, Boolean> {

        private ProductsDao mAsyncTaskDao;

        insertUnitAsyncTask(ProductsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final ProductsModel... params) {
            mAsyncTaskDao.insertSingleProduct(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class insertProductsAsyncTask extends AsyncTask<List<ProductsModel>, Void, Boolean> {

        private ProductsDao mAsyncTaskDao;

        insertProductsAsyncTask(ProductsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final List<ProductsModel>... params) {
            mAsyncTaskDao.insertMultipleProducts(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class updateProductAsyncTask extends AsyncTask<ProductsModel, Void, Boolean> {

        private ProductsDao mAsyncTaskDao;

        updateProductAsyncTask(ProductsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final ProductsModel... params) {
            mAsyncTaskDao.updateRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }

    private static class deleteProductAsyncTask extends AsyncTask<ProductsModel, Void, Boolean> {

        private ProductsDao mAsyncTaskDao;

        deleteProductAsyncTask(ProductsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final ProductsModel... params) {
            mAsyncTaskDao.deleteRecord(params[0]);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


        }
    }


}
