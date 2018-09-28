package com.dev.lishabora.Models.Trader;

import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.Cycles;
import com.dev.lishabora.Models.FamerModel;
import com.dev.lishabora.Models.LoanModel;
import com.dev.lishabora.Models.Notifications;
import com.dev.lishabora.Models.OrderModel;
import com.dev.lishabora.Models.Payouts;
import com.dev.lishabora.Models.ProductsModel;
import com.dev.lishabora.Models.RoutesModel;
import com.dev.lishabora.Models.UnitsModel;

import java.util.List;

public class Data {


    private List<RoutesModel> routesModel;
    private List<ProductsModel> productsModel;
    private List<FamerModel> famerModels;


    private List<OrderModel> orderModels;
    private List<LoanModel> loanModels;

    private List<LoanPayments> loanPayments;
    private List<OrderPayments> orderPayments;

    private List<Collection> collections;
    private List<Payouts> payouts;
    private List<Cycles> cycles;
    private List<UnitsModel> unitsModels;
    private List<Notifications> notifications;


    private String ResultCode;
    private String ResultDescription;


}
