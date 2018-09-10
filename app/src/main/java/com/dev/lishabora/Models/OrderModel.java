package com.dev.lishabora.Models;

import java.util.List;

public class OrderModel {
    private String orderAmount;
    private String installmentAmount;
    private String installmentNo;
    private List<ProductOrderModel> productOrderModels;
    private String productsOrderModels;
    private String collectionId;

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(String installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public String getInstallmentNo() {
        return installmentNo;
    }

    public void setInstallmentNo(String installmentNo) {
        this.installmentNo = installmentNo;
    }

    public List<ProductOrderModel> getProductOrderModels() {
        return productOrderModels;
    }

    public void setProductOrderModels(List<ProductOrderModel> productOrderModels) {
        this.productOrderModels = productOrderModels;
    }

    public String getProductsOrderModels() {
        return productsOrderModels;
    }

    public void setProductsOrderModels(String productsOrderModels) {
        this.productsOrderModels = productsOrderModels;
    }
}
