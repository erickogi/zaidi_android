package com.dev.zaidi.Models;

public class SyncDownResponse {
    private int ResultCode;
    private String ResultDescription;
    private SyncHolderModel Data;

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

    public SyncHolderModel getData() {
        return Data;
    }

    public void setData(SyncHolderModel data) {
        Data = data;
    }
}
