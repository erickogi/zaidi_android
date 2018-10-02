package com.dev.lishabora.Views.Admin;

import com.dev.lishabora.Models.ProductsModel;
import com.dev.lishabora.Models.Trader.TraderModel;

import java.util.List;

public class CreateTraderConstants {
    private static TraderModel traderModel;
    private static List<ProductsModel> productModels;

    public static TraderModel getTraderModel() {
        return traderModel;
    }

    public static void setTraderModel(TraderModel traderModel) {
        CreateTraderConstants.traderModel = traderModel;
    }

    public static List<ProductsModel> getProductModels() {
        return productModels;
    }

    public static void setProductModels(List<ProductsModel> productModels) {
        CreateTraderConstants.productModels = productModels;
    }

    public static void addTraderProducts(List<ProductsModel> selectedProducts) {
        if (productModels != null) {
            productModels.addAll(selectedProducts);
        } else {
            productModels = selectedProducts;
        }
    }

    public class TraderPayload {

    }
}
