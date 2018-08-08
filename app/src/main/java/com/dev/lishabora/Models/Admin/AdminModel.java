package com.dev.lishabora.Models.Admin;

public class AdminModel  {

    private String code;
    private   String names ;
    private   String mobile ;
    private   String email ;
    private   String department ;
    private   String password ;
    private   String apikey ;
    private   String firebasetoken ;
    private   String status ;
    private   String transactiontime ;
    private int passwordstatus;


    public int getPasswordstatus() {
        return passwordstatus;
    }

    public void setPasswordstatus(int passwordstatus) {
        this.passwordstatus = passwordstatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public  String getNames() {
        return names;
    }

    public  void setNames(String names) {
        this.names = names;
    }

    public  String getMobile() {
        return mobile;
    }

    public  void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public  String getEmail() {
        return email;
    }

    public  void setEmail(String email) {
        this.email = email;
    }

    public  String getDepartment() {
        return department;
    }

    public  void setDepartment(String department) {
        this.department = department;
    }

    public  String getPassword() {
        return password;
    }

    public  void setPassword(String password) {
        this.password = password;
    }

    public  String getApikey() {
        return apikey;
    }

    public  void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public  String getFirebasetoken() {
        return firebasetoken;
    }

    public  void setFirebasetoken(String firebasetoken) {
        this.firebasetoken = firebasetoken;
    }

    public  String getStatus() {
        return status;
    }

    public  void setStatus(String status) {
        this.status = status;
    }

    public  String getTransactiontime() {
        return transactiontime;
    }

    public  void setTransactiontime(String transactiontime) {
        this.transactiontime = transactiontime;
    }
}
