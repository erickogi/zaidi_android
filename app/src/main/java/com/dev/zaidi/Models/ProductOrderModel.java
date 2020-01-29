package com.dev.zaidi.Models;


import java.io.Serializable;


public class ProductOrderModel implements Serializable {
    private int id;
    private int code;
    private String names;
    private String costprice;
    private String buyingprice;
    private String sellingprice;
    private String allowablediscount;
    private String transactiontime;
    private String transactedby;
    private String subscribed;

    private String quantity;
    private String totalprice;

    private int status;


    private boolean isSelected = false;


    public ProductOrderModel(int id, int code, String names, String costprice, String buyingprice, String sellingprice, String allowablediscount, String transactiontime, String transactedby, String subscribed, String quantity, String totalprice, int status, boolean isSelected) {
        this.id = id;
        this.code = code;
        this.names = names;
        this.costprice = costprice;
        this.buyingprice = buyingprice;
        this.sellingprice = sellingprice;
        this.allowablediscount = allowablediscount;
        this.transactiontime = transactiontime;
        this.transactedby = transactedby;
        this.subscribed = subscribed;
        this.quantity = quantity;
        this.totalprice = totalprice;
        this.status = status;
        this.isSelected = isSelected;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getAllowablediscount() {
        if (allowablediscount != null) {
            return allowablediscount;
        } else {
            return "0.0";
        }
    }

    public void setAllowablediscount(String allowablediscount) {
        this.allowablediscount = allowablediscount;
    }

    public String getBuyingprice() {
        if (buyingprice != null) {
            return buyingprice;
        } else {
            return "20000";
        }
    }

    public void setBuyingprice(String buyingprice) {
        this.buyingprice = buyingprice;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getCostprice() {
        return costprice;
    }

    public void setCostprice(String costprice) {
        this.costprice = costprice;
    }

    public String getSellingprice() {
        if (sellingprice != null && Double.valueOf(sellingprice) > 0.0) {
            return sellingprice;
        } else {

            return getBuyingprice();
        }
    }

    public void setSellingprice(String sellingprice) {
        this.sellingprice = sellingprice;
    }

    public String getTransactiontime() {
        return transactiontime;
    }

    public void setTransactiontime(String transactiontime) {
        this.transactiontime = transactiontime;
    }

    public String getTransactedby() {
        return transactedby;
    }

    public void setTransactedby(String transactedby) {
        this.transactedby = transactedby;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(String subscribed) {
        this.subscribed = subscribed;
    }
}
