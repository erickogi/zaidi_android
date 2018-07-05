package com.dev.lishaboramobile.Global.Data.Operations.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dev.lishaboramobile.Global.Data.Operations.Repo.UnitsRepo;
import com.dev.lishaboramobile.Global.Data.Operations.Repo.UnitsRepo;
import com.dev.lishaboramobile.Trader.Models.UnitsModel;

import java.util.List;

public class UnitsViewModel extends AndroidViewModel {

    private UnitsRepo mRepository;


    private LiveData<List<UnitsModel>> unitModels;
    private LiveData<UnitsModel> unitModel;

    public UnitsViewModel(@NonNull Application application) {
        super(application);
        mRepository = new UnitsRepo(application);
        unitModels = mRepository.fetchAllData(false);
    }
    public void insert(UnitsModel unitModel) { mRepository.insert(unitModel); }
    public void insert(List<UnitsModel> unitModels) { mRepository.insert(unitModels); }

    public void update(UnitsModel unitModel){mRepository.upDateRecord(unitModel);}
    public void delete(UnitsModel unitModel){mRepository.deleteRecord(unitModel);}


    public LiveData<UnitsModel> getUnitModelByKeyID(int id) {
        return mRepository.getUnitByKeyID(id);
    }

    public LiveData<UnitsModel> getUNitModelByCode(String code) {
        return mRepository.getUnitByCode(code);
    }
    public LiveData<List<UnitsModel>> getUnitModelsByEntityCode(String entity) {
        return mRepository.getUnitsByEntityCode(entity);
    }
    public LiveData<List<UnitsModel>> getRoutesModelsByStatus(String status) {
        return mRepository.getAllByStatus(status);
    }
    public LiveData<List<UnitsModel>> searchByUnit(String unit) {
        return mRepository.searchByUnit(unit);
    }
    


}
