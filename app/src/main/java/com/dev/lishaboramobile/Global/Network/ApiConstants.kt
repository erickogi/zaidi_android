package com.dev.lishaboramobile.Global.Network

class ApiConstants {


    companion object {


        //KEY VALUES
        var resultCode: String = "ResultCode"
        var resultDescription: String = "ResultDescription"

        var baseUrl: String = "http://lishabora.net/512/Apis/"

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




    }
}
