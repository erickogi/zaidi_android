package com.dev.lishabora.Views.Trader;

import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.ProductOrderModel;

import java.util.List;

public class OrderConstants {
    private static List<ProductOrderModel> productOrderModels;
    private static FamerModel famerModel;

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
}
