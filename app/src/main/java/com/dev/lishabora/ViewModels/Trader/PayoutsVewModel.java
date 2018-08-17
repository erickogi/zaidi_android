package com.dev.lishabora.ViewModels.Trader;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.Cycles;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Repos.ProductsRepo;
import com.dev.lishabora.Repos.RoutesRepo;
import com.dev.lishabora.Repos.Trader.CollectionsRepo;
import com.dev.lishabora.Repos.Trader.CyclesRepo;
import com.dev.lishabora.Repos.Trader.FarmerRepo;
import com.dev.lishabora.Repos.Trader.PayoutsRepo;
import com.dev.lishabora.Repos.Trader.UnitsRepo;
import com.dev.lishabora.Views.Trader.FarmerConst;

import java.util.LinkedList;
import java.util.List;

public class PayoutsVewModel extends AndroidViewModel {
    FarmerRepo farmerRepo;
    RoutesRepo routesRepo;
    UnitsRepo unitsRepo;
    CyclesRepo cyclesRepo;
    ProductsRepo productsRepo;
    CollectionsRepo collectionsRepo;
    PayoutsRepo payoutsRepo;

    private MutableLiveData createPayoutSuccess;
    private MutableLiveData updatePayoutSuccess;
    private MutableLiveData deletePayoutSuccess;


    private LiveData<List<Payouts>> payouts;
    private LiveData<Payouts> payout;
    private Payouts payoutOne;

    private LiveData<List<Collection>> collections;
    private LiveData<Collection> collection;
    private List<Collection> collectionListOne;
    private Collection collectionOne;


    private LiveData<List<Cycles>> cycles;
    private LiveData<Cycles> cycle;
    private Cycles cycleOne;

    private LiveData<List<FamerModel>> farmers;
    private List<FamerModel> farmersListOne;
    private LiveData<FamerModel> farmer;
    private FamerModel farmerOne;

    private Application application;


    public PayoutsVewModel(@NonNull Application application) {
        super(application);
        this.application = application;

        farmerRepo = new FarmerRepo(application);
        unitsRepo = new UnitsRepo(application);
        routesRepo = new RoutesRepo(application);
        cyclesRepo = new CyclesRepo(application);
        productsRepo = new ProductsRepo(application);
        collectionsRepo = new CollectionsRepo(application);
        payoutsRepo = new PayoutsRepo(application);

//
        payouts = new MutableLiveData<>();
        payout = new MutableLiveData<>();
        payoutOne = new Payouts();

        collections = new MutableLiveData<>();
        collection = new MutableLiveData<>();
        collectionOne = new Collection();


        farmers = new MutableLiveData<>();
        farmer = new MutableLiveData<>();
        farmerOne = new FamerModel();

        cycles = new MutableLiveData<>();
        cycle = new MutableLiveData<>();
        cycleOne = new Cycles();


    }

    public LiveData<List<Payouts>> fetchAll(boolean isOnline) {
        return payoutsRepo.fetchAllData(false);
    }

    public LiveData<Payouts> getPayoutById(int id) {
        return payoutsRepo.getPayoutById(id);
    }

    public LiveData<List<Payouts>> getPayoutsByCycleCode(String code) {
        return payoutsRepo.getPayoutsByCycleCode(code);
    }

    public LiveData<List<Payouts>> getPayoutsByStatus(String status) {
        return payoutsRepo.getPayoutsByStatus(status);
    }

    public LiveData<List<Payouts>> getPayoutsByPayoutsByDates(String startDate, String endDate) {
        return payoutsRepo.getPayoutsByPayoutsByDate(startDate, endDate);
    }

    public LiveData<List<Payouts>> getPayoutsByPayoutNumber(String number) {
        return payoutsRepo.getPayoutsByPayout(number);
    }

    public Payouts getLastPayout(String cycleCode) {
        return payoutsRepo.getLast(cycleCode);
    }

    public Payouts getLastPayout() {
        return payoutsRepo.getLast();
    }

    public void updatePayout(Payouts payouts) {
        payoutsRepo.upDateRecord(payouts);
    }

    public void deletePayout(Payouts payouts) {
        payoutsRepo.deleteRecord(payouts);
    }

    public void insertPayout(Payouts payouts) {
        payoutsRepo.insert(payouts);
    }

    public void insertPayout(List<Payouts> payouts) {
        payoutsRepo.insert(payouts);
    }

    public LiveData<List<FamerModel>> getFarmersByCycle(String code) {
        Log.d("farmersPayouts", "Db called ");

        if (farmers == null) {
            farmers = new MutableLiveData();

        }
        farmers = (farmerRepo.getFarmersByCycle(code));

        return farmers;
    }

    public LiveData<List<FamerModel>> getFarmers() {
        if (farmers == null) {
            farmers = new MutableLiveData();


        }

        farmers = (farmerRepo.fetchAllData(false));
        return farmers;
    }

    public LiveData<FamerModel> getFarmerByCode(String code) {
        if (farmer == null) {
            farmer = new MutableLiveData();


        }

        farmer = (farmerRepo.getFramerByCode(code));
        return farmer;
    }

    public int getFarmersCountByCycle(String code) {
        return (farmerRepo.getFarmersCountByCycle(code));


    }

    public LiveData<List<FamerModel>> getFarmerByStatusRoute(int status, String route) {

        Log.d("byRouteByStatus", "Status " + status + "\n Route " + route);

        if (farmers == null) {
            farmers = new MutableLiveData();
        }

        switch (status) {
            case FarmerConst.ACTIVE:

                farmers = (farmerRepo.getFarmersByStatusRoute(0, 0, 0, route));
                break;
            case FarmerConst.DELETED:
                farmers = (farmerRepo.getFarmersByStatusRouteDeleted(1, route));
                break;
            case FarmerConst.DUMMY:
                farmers = (farmerRepo.getFarmersByStatusRouteDummy(1, route));
                break;
            case FarmerConst.ARCHIVED:
                farmers = (farmerRepo.getFarmersByStatusRouteArchived(1, route));
                break;
            case FarmerConst.ALL:
                farmers = (farmerRepo.fetchAllData(false, route));
                break;
            default:
                farmers = (farmerRepo.fetchAllData(false));


        }


        return farmers;
    }

    public LiveData<FamerModel> getLastFarmer() {
        if (farmer == null) {
            farmer = new MutableLiveData();
            farmer = (farmerRepo.getLastFarmer());
        }

        return farmer;
    }

    public LiveData<List<Cycles>> getCycles(boolean isOnline) {
        if (cycles == null) {
            cycles = new MutableLiveData();
        }

        cycles = (cyclesRepo.fetchAllData(false));


        return cycles;
    }

    public LiveData<Cycles> getCycle(String cycleCode) {
        if (cycle == null) {
            cycle = new MutableLiveData();
        }

        cycle = (cyclesRepo.getCycleByKeyCode(cycleCode, false));


        return cycle;
    }

    public LiveData<List<Collection>> getCollectionByDateByPayout(String payoutnumber) {
        if (collections == null) {
            collections = new MutableLiveData<>();
        }
        return collectionsRepo.getCollectionByPayout(payoutnumber);
    }

    public List<Collection> getCollectionByDateByPayoutListOne(String payoutnumber) {
        if (collectionListOne == null) {
            collectionListOne = new LinkedList<>();
        }
        return collectionsRepo.getCollectionByPayoutListOne(payoutnumber);
    }


}
