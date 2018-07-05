package com.dev.lishaboramobile.Global.Data.Operations.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dev.lishaboramobile.Global.Data.Operations.Repo.RoutesRepo;
import com.dev.lishaboramobile.Trader.Models.RoutesModel;

import java.util.List;

public class RoutesViewModel extends AndroidViewModel {

    private RoutesRepo mRepository;


    private LiveData<List<RoutesModel>> routesModels;
    private LiveData<RoutesModel> routesModel;

    public RoutesViewModel(@NonNull Application application) {
        super(application);
        mRepository = new RoutesRepo(application);
        routesModels = mRepository.fetchAllData(false);
    }
    public void insert(RoutesModel routesModel) { mRepository.insert(routesModel); }
    public void insert(List<RoutesModel> routesModel) { mRepository.insertMultipleRoutes(routesModel); }

    public void update(RoutesModel routesModel){mRepository.upDateRecord(routesModel);}
    public void delete(RoutesModel routesModel){mRepository.deleteRecord(routesModel);}


    public LiveData<RoutesModel> getRouteModelByKeyID(int id) {
        return mRepository.getRouteByKeyID(id);
    }
    public LiveData<RoutesModel> getRouteModelByRoute(String route) {
        return mRepository.getRouteByRoute(route);
    }
    public LiveData<RoutesModel> getRouteModelByCode(String code) {
        return mRepository.getRouteByCode(code);
    }
    public LiveData<List<RoutesModel>> getRoutesModelsByEntityCode(String entity) {
        return mRepository.getRoutesByEntityCode(entity);
    }
    public LiveData<List<RoutesModel>> getRoutesModelsByStatus(String status) {
        return mRepository.getAllByStatus(status);
    }
    public LiveData<List<RoutesModel>> searchByRoute(String route) {
        return mRepository.searchByRoute(route);
    }



}
