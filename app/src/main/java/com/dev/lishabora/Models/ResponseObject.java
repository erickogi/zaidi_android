package com.dev.lishabora.Models;

import com.dev.lishabora.Models.Trader.TraderModel;

import java.io.Serializable;
import java.util.List;

public class ResponseObject implements Serializable {
    private Object Data;
    private int ResultCode;
    private String ResultDescription;
    private int Type;
    private String Code;


    private List<FamerModel> famerModels;
    private FamerModel famerModel;

    private TraderModel traderModel;
    private List<TraderModel> traderModels;

    public String getCode() {
        return Code;
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
