package com.dev.lishabora.Utils.Network

class ApiConstants {


    companion object {


        //KEY VALUES
        var resultCode: String = "ResultCode"
        var resultDescription: String = "ResultDescription"

        var baseUrl1: String = "http://lishabora.net/512/Apis/"
        var baseUrl: String = "http://lishabora.net/Dev/512/Apis/"

        //Accounts
        var Accounts: String = baseUrl + "Accounts/"
        var PhoneAuth: String = Accounts + "AuthPhone.php"
        var PasswordAuth: String = Accounts + "AuthPassword.php"
        var newPassordConfirm: String = Accounts + "ChangePassword.php"
        var otpRequest: String = Accounts + "ForgotPassword.php"

        //Admin
        var Admin: String = baseUrl + "Users/"
        // var Traders: String = Trader + "List.php"
        var CreateAdmin: String = Admin + "Create.php"
        var UpdateAdmin: String = Admin + "Update.php"


        //Traders
        var Trader: String = baseUrl + "Traders/"
        var Traders: String = Trader + "List.php"
        var CreateTrader: String = Trader + "Create.php"
        var UpdateTrader: String = Trader + "Update.php"

        //Products
        var Product: String = baseUrl + "Products/"
        var Products: String = Product + "List.php"
        var CreateProducts: String = Product + "Create.php"
        var UpdateProducts: String = Product + "Update.php"


        //Farmers
        var Farmer: String = baseUrl + "Farmers/"
        var Farmers: String = Farmer + "List.php"
        var CreateFarmer: String = Farmer + "Create.php"
        var UpdateFarmer: String = Farmer + "Update.php"


        //Trader Products
        var TraderProduct: String = baseUrl + "Traders/"
        var TraderProducts: String = TraderProduct + "Products.php"
        var Subscribed: String = TraderProduct + "ProductSubscribe.php"

        //var TraderCreateProducts: String = TraderProduct + "Create.php"
        //var TraderUpdateProducts: String = TraderProduct + "Update.php"


        //Trader FragmentRoutes
        var TraderRoute: String = baseUrl + "Traders/"
        var TraderRoutes: String = TraderRoute + "Routes.php"
        var TraderCreateRoutes: String = TraderRoute + "Create.php"
        var TraderUpdateRoutes: String = TraderRoute + "Update.php"


        //Trader Farmers
        var TraderFarmer: String = baseUrl + "Traders/"
        var TraderFarmers: String = TraderFarmer + "Farmers.php"
        var TraderCreateFarmers: String = TraderFarmer + "Create.php"
        var TraderUpdateFarmers: String = TraderFarmer + "Update.php"


        //Routes
        var Route: String = baseUrl + "Routes/"
        var CreateRoutes: String = Route + "Create.php"
        var UpdateRoutes: String = Route + "Update.php"


        //Cycles
        var Cycle: String = baseUrl + "Cycles/"
        var Cycles: String = Cycle + "List.php"


        //Units
        var Unit: String = baseUrl + "Units/"
        var Units: String = Unit + "List.php"


        //SYNC UP
        var Sync: String = baseUrl + "System/Sync.php"



    }

}
