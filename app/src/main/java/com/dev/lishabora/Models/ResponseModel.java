package com.dev.lishabora.Models;

import android.arch.lifecycle.LiveData;

import com.dev.lishabora.Models.Trader.TraderModel;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ResponseModel extends LiveData<ResponseModel> implements Serializable {
    //private Object Data;
    private LinkedList<Object> Data;
    private int ResultCode;
    private String ResultDescription;
    private int Type;
    private List<FamerModel> famerModels;
    private FamerModel famerModel;

    private TraderModel traderModel;
    private List<TraderModel> traderModels;

    private int payoutkey;

    public int getPayoutkey() {
        return payoutkey;
    }

    public void setPayoutkey(int payoutkey) {
        this.payoutkey = payoutkey;
    }

    public LinkedList<Object> getData() {
        return Data;
    }

    public void setData(LinkedList<Object> data) {
        Data = data;
    }

    public int getResultCode() {
        return ResultCode;
    }

    public void setResultCode(int resultCode) {
        ResultCode = resultCode;
    }

    public String getResultDescription() {
        return ResultDescription;
    }

    public void setResultDescription(String resultDescription) {
        ResultDescription = resultDescription;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public void setFarmers(List<FamerModel> value) {
        Data.add(value);
    }

    public List<FamerModel> getFamerModels() {
        return famerModels;
    }

    public void setFamerModels(List<FamerModel> famerModels) {
        this.famerModels = famerModels;
    }

    public FamerModel getFamerModel() {
        return famerModel;
    }

    public void setFamerModel(FamerModel famerModel) {
        this.famerModel = famerModel;
    }

    public TraderModel getTraderModel() {
        return traderModel;
    }

    public void setTraderModel(TraderModel traderModel) {
        this.traderModel = traderModel;
    }

    public List<TraderModel> getTraderModels() {
        return traderModels;
    }

    public void setTraderModels(List<TraderModel> traderModels) {
        this.traderModels = traderModels;
    }
}
