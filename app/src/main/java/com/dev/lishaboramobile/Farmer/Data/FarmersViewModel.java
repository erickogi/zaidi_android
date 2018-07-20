package com.dev.lishaboramobile.Farmer.Data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dev.lishaboramobile.Farmer.Models.FamerModel;
import com.dev.lishaboramobile.Global.Data.Operations.Repo.FarmerRepo;

import java.util.List;

public class FarmersViewModel extends AndroidViewModel {

    private FarmerRepo mRepository;


    private LiveData<List<FamerModel>> famerModels;
    private LiveData<FamerModel> famerModel;

    public FarmersViewModel(@NonNull Application application) {
        super(application);
        mRepository = new FarmerRepo(application);
        famerModels = mRepository.fetchAllData(false);
    }
    public void insert(FamerModel famerModel) { mRepository.insert(famerModel); }
    public void insert(List<FamerModel> famerModel) { mRepository.insertMultipleTraders(famerModel); }

    public void update(FamerModel famerModel){mRepository.upDateRecord(famerModel);}
    public void delete(FamerModel famerModel){mRepository.deleteRecord(famerModel);}


    public LiveData<FamerModel> getFarmerModelById(int id) {
        return mRepository.getFramerByKeyID(id);
    }
    public LiveData<FamerModel> getFramerModelByNames(String name) {
        return mRepository.getFramersByNames(name);
    }

    public LiveData<List<FamerModel>> getFramerModelByRoute(String route) {
        return mRepository.getFramersByRoute(route);
    }
    public LiveData<FamerModel> getFramerModelByCode(String code) {
        return mRepository.getFramerByCode(code);
    }
    public LiveData<List<FamerModel>> getFramerModelsByArchive(int status) {
        return mRepository.getAllByArchivedStatus(status);
    }
    public LiveData<List<FamerModel>> getFramerModelsByDelete(int status) {
        return mRepository.getAllByDeleteStatus(status);
    }
    public LiveData<List<FamerModel>> getFramerModelsByDummyStatus(int status) {
        return mRepository.getAllByDummyStatus(status);
    }
    public LiveData<List<FamerModel>> getFramerModelsByStatus(String status) {
        return mRepository.getAllByStatus(status);
    }
    public LiveData<List<FamerModel>> getFramerModelsByEntityCode(String entity) {
        return mRepository.getFramerByEntityCode(entity);
    }
    public LiveData<List<FamerModel>> getFramerModelsSearchByNames(String names) {
        return mRepository.searchByNames(names);
    }
    public LiveData<List<FamerModel>> getFramerModelsSearchByCode(String code) {
        return mRepository.searchByCode(code);
    }
    public LiveData<List<FamerModel>> getFramerModelsSearchByMobile(String mobile) {
        return mRepository.searchByMobile(mobile);
    }

    public LiveData<List<FamerModel>> getFramerModelsSearch(String names,String moile,int dummy,int deleted,int archived) {
        return mRepository.search(names, moile, dummy, deleted, archived);
    }



}
