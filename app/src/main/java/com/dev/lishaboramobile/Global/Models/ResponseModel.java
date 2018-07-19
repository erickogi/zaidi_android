package com.dev.lishaboramobile.Global.Models;

import android.arch.lifecycle.LiveData;

import java.util.LinkedList;

public class ResponseModel extends LiveData<ResponseModel> {
    //private Object Data;
    private LinkedList<Object> Data;
    private int ResultCode;
    private String ResultDescription;
    private int Type;


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
}
