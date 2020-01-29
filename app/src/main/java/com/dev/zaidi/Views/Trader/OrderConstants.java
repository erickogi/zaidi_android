package com.dev.zaidi.Views.Trader;

import com.dev.zaidi.Models.FamerModel;
import com.dev.zaidi.Models.OrderModel;
import com.dev.zaidi.Models.ProductOrderModel;

import java.util.List;

public class OrderConstants {
    private static List<ProductOrderModel> productOrderModels;
    private static FamerModel famerModel;


    private static String orderData;
    private static OrderModel orderModel;
    public static FamerModel getFamerModel() {
        return famerModel;
    }

    public static void setFamerModel(FamerModel famerModel) {
        OrderConstants.famerModel = famerModel;
    }

    public static List<ProductOrderModel> getProductOrderModels() {
        return productOrderModels;
    }

    public static void setProductOrderModels(List<ProductOrderModel> productOrderModelss) {
        productOrderModels = productOrderModelss;
    }

    public static void addProductOrders(List<ProductOrderModel> productOrderModelss) {
        if (productOrderModels != null) {
            productOrderModels.addAll(productOrderModelss);
        } else {
            productOrderModels = productOrderModelss;
        }
    }

    public static String getOrderData() {
        return orderData;
    }

    public static void setOrderData(String orderData) {
        OrderConstants.orderData = orderData;
    }

    public static OrderModel getOrderModel() {
        return orderModel;
    }

    public static void setOrderModel(OrderModel orderModel) {
        OrderConstants.orderModel = orderModel;
    }
}
