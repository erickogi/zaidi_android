package com.dev.lishabora.Models.Admin;

import com.dev.lishabora.Models.ProductsModel;
import com.dev.lishabora.Models.Trader.TraderModel;

import java.util.List;

public class TraderPayload {
    private TraderModel traderModel;
    private List<ProductsModel> productModels;

    public TraderPayload() {
    }

    public TraderModel getTraderModel() {
        return traderModel;
    }

    public void setTraderModel(TraderModel traderModel) {
        this.traderModel = traderModel;
    }

    public List<ProductsModel> getProductModels() {
        return productModels;
    }

    public void setProductModels(List<ProductsModel> productModels) {
        this.productModels = productModels;
    }
}
