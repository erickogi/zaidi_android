package com.dev.lishaboramobile.Trader.Data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dev.lishaboramobile.Global.Data.Operations.Repo.TraderRepo;
import com.dev.lishaboramobile.Trader.Models.TraderModel;

import java.util.List;

public class TradersViewModel extends AndroidViewModel {

    private TraderRepo mRepository;


    private LiveData<List<TraderModel>> traderModels;
    private LiveData<TraderModel> traderModel;

    public TradersViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TraderRepo(application);
        traderModels = mRepository.fetchAllData(false);
    }
    public void insert(TraderModel traderModel) { mRepository.insert(traderModel); }
    public void insert(List<TraderModel> traderModel) { mRepository.insertMultipleTraders(traderModel); }

    public void update(TraderModel traderModel){mRepository.upDateRecord(traderModel);}
    public void delete(TraderModel traderModel){mRepository.deleteRecord(traderModel);}


    public LiveData<TraderModel> getTraderModelById(int id) {
        return mRepository.getTraderByKeyID(id);
    }
    public LiveData<TraderModel> getTraderModelByNames(String name) {
        return mRepository.getTradersByNames(name);
    }
    public LiveData<TraderModel> getTraderModelByCode(String code) {
        return mRepository.getTraderByCode(code);
    }
    public LiveData<List<TraderModel>> getTraderModelsByArchive(int status) {
        return mRepository.getAllByArchivedStatus(status);
    }
    public LiveData<List<TraderModel>> getTraderModelsByDelete(int status) {
        return mRepository.getAllByDeleteStatus(status);
    }
    public LiveData<List<TraderModel>> getTraderModelsByDummyStatus(int status) {
        return mRepository.getAllByDummyStatus(status);
    }
    public LiveData<List<TraderModel>> getTraderModelsByStatus(String status) {
        return mRepository.getAllByStatus(status);
    }
    public LiveData<List<TraderModel>> getTraderModelsByEntityCode(String entity) {
        return mRepository.getTradersByEntityCode(entity);
    }
    public LiveData<List<TraderModel>> getTraderModelsSearchByNames(String names) {
        return mRepository.searchByNames(names);
    }
    public LiveData<List<TraderModel>> getTraderModelsSearchByCode(String code) {
        return mRepository.searchByCode(code);
    }
    public LiveData<List<TraderModel>> getTraderModelsSearchByMobile(String mobile) {
        return mRepository.searchByMobile(mobile);
    }

    public LiveData<List<TraderModel>> getTraderModelsSearch(String names,String moile,int dummy,int deleted,int archived) {
        return mRepository.search(names, moile, dummy, deleted, archived);
    }



}
