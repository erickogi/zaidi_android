package com.dev.lishaboramobile.Global.Account;

public class EntryModel {
    //private Object DA
    private Object Data;
    private int ResultCode;
    private String ResultDescription;
    private int Type;


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
