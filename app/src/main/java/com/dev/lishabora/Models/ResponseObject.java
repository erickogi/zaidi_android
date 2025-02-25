package com.dev.lishabora.Models;

import com.dev.lishabora.Models.Trader.TraderModel;

import java.io.Serializable;
import java.util.List;

public class ResponseObject implements Serializable {
    private Object Data;
    private int ResultCode;
    private String ResultDescription;
    private int Type = 0;
    private String Code;
//    private List<SyncModel> syncModels;
//


    private List<FamerModel> famerModels;
    private FamerModel famerModel;

    private TraderModel traderModel;
    private List<TraderModel> traderModels;

    private NetworkAnalytics analytics;

//    public List<SyncModel> getSyncModels() {
//        return syncModels;
//    }
//
//    public void setSyncModels(List<SyncModel> syncModels) {
//        this.syncModels = syncModels;
//    }

    public String getCode() {
        if (Code != null) {
            return Code;
        } else {
            return "";
        }
    }

    public NetworkAnalytics getAnalytics() {
        return analytics;
    }

    public void setAnalytics(NetworkAnalytics analytics) {
        this.analytics = analytics;
    }

    public void setCode(String code) {
        Code = code;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
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
}
