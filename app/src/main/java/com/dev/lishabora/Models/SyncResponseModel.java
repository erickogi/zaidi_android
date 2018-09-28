package com.dev.lishabora.Models;

public class SyncResponseModel {

    private int ResultCode;
    private String ResultDescription;
    private String FailureId;

    public SyncResponseModel() {
    }

    public SyncResponseModel(int resultCode, String resultDescription, String failureId) {
        ResultCode = resultCode;
        ResultDescription = resultDescription;
        FailureId = failureId;
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

    public String getFailureId() {
        return FailureId;
    }

    public void setFailureId(String failureId) {
        FailureId = failureId;
    }

    //    public class TransactionResponses{
//        private int ResultCode;
//        private String ResultDescription;
//    }
}
