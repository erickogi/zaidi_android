package com.dev.lishabora.ViewModels.Trader;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.dev.lishabora.AppConstants;
import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.Cycles;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Models.ResponseModel;
import com.dev.lishabora.Models.SyncModel;
import com.dev.lishabora.Repos.ProductsRepo;
import com.dev.lishabora.Repos.RoutesRepo;
import com.dev.lishabora.Repos.Trader.CollectionsRepo;
import com.dev.lishabora.Repos.Trader.CyclesRepo;
import com.dev.lishabora.Repos.Trader.FarmerRepo;
import com.dev.lishabora.Repos.Trader.PayoutsRepo;
import com.dev.lishabora.Repos.Trader.SyncRepo;
import com.dev.lishabora.Repos.Trader.UnitsRepo;
import com.dev.lishabora.Utils.DateTimeUtils;
import com.dev.lishabora.Views.Trader.FarmerConst;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;

public class PayoutsVewModel extends AndroidViewModel {
    FarmerRepo farmerRepo;
    RoutesRepo routesRepo;
    UnitsRepo unitsRepo;
    CyclesRepo cyclesRepo;
    ProductsRepo productsRepo;
    CollectionsRepo collectionsRepo;
    PayoutsRepo payoutsRepo;
    SyncRepo syncRepo;

    private MutableLiveData createPayoutSuccess;
    private MutableLiveData updatePayoutSuccess;
    private MutableLiveData deletePayoutSuccess;
    private MutableLiveData createCollectionSuccess;


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
        syncRepo = new SyncRepo(application);

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

    public LiveData<List<SyncModel>> fetchAll() {
        return syncRepo.fetchAllData(false);
    }

    public LiveData<List<SyncModel>> fetchByStatus(int status) {
        return syncRepo.getAllByStatus(status);
    }

    public LiveData<SyncModel> fetchById(int id) {
        return syncRepo.getSynce(id);
    }

    public void createSync(SyncModel model) {
        syncRepo.insert(model);

    }

    public void updateSync(SyncModel syncModel) {
        syncRepo.upDateRecord(syncModel);

    }

    public void deleteSync(SyncModel syncModel) {
        syncRepo.deleteRecord(syncModel);

    }

    private void synch(int action, int entity, Object o) {
        SyncModel syncModel = new SyncModel();
        syncModel.setActionType(action);
        syncModel.setObjectData(o);
        syncModel.setObject(new Gson().toJson(o));
        syncModel.setEntityType(entity);
        syncModel.setSyncStatus(0);
        syncModel.setTimeStamp(DateTimeUtils.Companion.getNow());
        syncModel.setSyncTime("");
        switch (action) {
            case AppConstants.INSERT:
                syncModel.setActionTypeName("Insert");
                break;
            case AppConstants.UPDATE:
                syncModel.setActionTypeName("Update");
                break;
            case AppConstants.DELETE:
                syncModel.setActionTypeName("Delete");
                break;

        }
        switch (entity) {
            case AppConstants.ENTITY_FARMER:
                syncModel.setEntityTypeName("Farmer");
                break;
            case AppConstants.ENTITY_PRODUCTS:
                syncModel.setEntityTypeName("Products");
                break;
            case AppConstants.ENTITY_PAYOUTS:
                syncModel.setEntityTypeName("Payout");
                break;
            case AppConstants.ENTITY_COLLECTION:
                syncModel.setEntityTypeName("Collection");
                break;
            case AppConstants.ENTITY_ROUTES:
                syncModel.setEntityTypeName("Route");
                break;
            case AppConstants.ENTITY_TRADER:
                syncModel.setEntityTypeName("Trader");
                break;
        }

        createSync(syncModel);
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

    LiveData<Double> milkTotal;
    LiveData<Double> milkTotalLtrs;
    LiveData<Double> milkTotalKsh;

    public Payouts getLastPayout(String cycleCode) {
        return payoutsRepo.getLast(cycleCode);
    }

    public Payouts getLastPayout() {
        return payoutsRepo.getLast();
    }

    public void updatePayout(Payouts payouts) {
        payoutsRepo.upDateRecord(payouts);
        synch(AppConstants.UPDATE, AppConstants.ENTITY_PAYOUTS, payouts);
        collectionsRepo.updateCollectionsByPayout(payouts.getPayoutnumber(), payouts.getStatus());
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
        Timber.d("Db called ");

        if (farmers == null) {
            farmers = new MutableLiveData();

        }
        farmers = (farmerRepo.getFarmersByCycle(code));

        return farmers;
    }

    LiveData<Double> loanTotal;

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

        Timber.tag("byRouteByStatus").d("Status " + status + "\n Route " + route);

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

    LiveData<Double> orderTotal;

    public LiveData<List<Collection>> getCollectionByDateByPayout(String payoutnumber) {
        if (collections == null) {
            collections = new MutableLiveData<>();
        }
        return collectionsRepo.getCollectionByPayout(payoutnumber);
    }

    public LiveData<List<Collection>> getCollectionsBetweenDates(Long date1, Long date2) {
        return collectionsRepo.getCollectionsBetweenDates(date1, date2);
    }

    public LiveData<List<Collection>> getCollections() {
        return collectionsRepo.fetchAllData(false);
    }

    public LiveData<List<Collection>> getCollectionsBetweenDates(Long date1, Long date2, String code) {
        return collectionsRepo.getCollectionsBetweenDates(date1, date2, code);
    }

    public LiveData<Payouts> getPayoutsByPayoutNumber(String number) {
        return payoutsRepo.getPayoutsByPayout(number);
    }

    public List<Collection> getCollectionByDateByPayoutListOne(String payoutnumber) {
        if (collectionListOne == null) {
            collectionListOne = new LinkedList<>();
        }
        return collectionsRepo.getCollectionByPayoutListOne(payoutnumber);
    }

    public List<FamerModel> getFarmersByCycleONe(String code) {
        Timber.tag("farmersPayouts").d("Db called ");

        if (farmersListOne == null) {
            farmersListOne = new LinkedList<>();

        }
        farmersListOne = (farmerRepo.getFarmersByCycleOne(code));

        return farmersListOne;
    }

    public LiveData<Collection> getCollectionById(int collectionId) {
        if (collection == null) {
            collection = new MutableLiveData<>();

        }
        return collectionsRepo.getCollectionById(collectionId);
    }

    public Collection getCollectionByIdOne(int collectionId) {
        if (collectionOne == null) {
            collectionOne = new Collection();

        }
        return collectionsRepo.getCollectionByIdOne(collectionId);
    }

    public LiveData<List<Collection>> getCollectionByDateByPayoutByFarmer(String payoutnumber, String farmer) {
        if (collections == null) {
            collections = new MutableLiveData<>();
        }
        return collectionsRepo.getCollectionByPayoutByFarmer(payoutnumber, farmer);
    }

    public LiveData<List<Collection>> getCollectionByFarmer(String farmer) {
        if (collections == null) {
            collections = new MutableLiveData<>();
        }
        return collectionsRepo.getCollectionByFarmer(farmer);
    }
    public LiveData<Double> getSumOfMilkForPayout(String farmercode, int payoutNumber) {
        if (milkTotal == null) {
            milkTotal = new MutableLiveData<>();
        }
        milkTotal = collectionsRepo.getSumOfMilkFarmerPayout(farmercode, payoutNumber);

        return milkTotal;
    }

    public LiveData<Double> getSumOfMilkForPayoutLtrs(String farmercode, int payoutNumber) {
        if (milkTotalLtrs == null) {
            milkTotalLtrs = new MutableLiveData<>();
        }
        milkTotalLtrs = collectionsRepo.getSumOfMilkFarmerPayoutLtrs(farmercode, payoutNumber);

        return milkTotalLtrs;
    }

    public LiveData<Double> getSumOfMilkForPayoutKsh(String farmercode, int payoutNumber) {
        if (milkTotalKsh == null) {
            milkTotalKsh = new MutableLiveData<>();
        }
        milkTotalKsh = collectionsRepo.getSumOfMilkFarmerPayoutKsh(farmercode, payoutNumber);

        return milkTotalKsh;
    }

    public LiveData<Double> getSumOfLoansForPayout(String farmercode, int payoutNumber) {
        if (loanTotal == null) {
            loanTotal = new MutableLiveData<>();
        }
        loanTotal = collectionsRepo.getSumOfLoanFarmerPayout(farmercode, payoutNumber);

        return loanTotal;
    }

    public LiveData<Double> getSumOfOrdersForPayout(String farmercode, int payoutNumber) {
        if (orderTotal == null) {
            orderTotal = new MutableLiveData<>();
        }
        orderTotal = collectionsRepo.getSumOfOrderFarmerPayout(farmercode, payoutNumber);

        return orderTotal;
    }


    public void updateCollection(Collection c) {
        synch(AppConstants.UPDATE, AppConstants.ENTITY_COLLECTION, c);
        if (c != null) {
            collectionsRepo.upDateRecord(c);
        }
    }

    public LiveData<ResponseModel> createCollections(Collection collection) {
        if (this.createCollectionSuccess == null) {

            this.createCollectionSuccess = new MutableLiveData();
        }

        synch(AppConstants.INSERT, AppConstants.ENTITY_COLLECTION, collection);

        collectionsRepo.insert(collection);
        ResponseModel responseModel = new ResponseModel();
        responseModel.setResultCode(1);
        responseModel.setResultDescription("Collection Inserted \nExisting payout");
        responseModel.setData(null);
        createCollectionSuccess.setValue(responseModel);


        return createCollectionSuccess;
    }

    public void approveFarmersPayoutCard(String farmercode, int payoutNumber) {

        collectionsRepo.approveFarmersPayoutCard(farmercode, payoutNumber);
    }

    public void cancelFarmersPayoutCard(String farmercode, int payoutNumber) {
        collectionsRepo.cancelFarmersPayoutCard(farmercode, payoutNumber);
    }


//    public LiveData<Integer> getStatusForFarmerPayout(String farmercode, int payoutNumber) {
//
//    }
}
