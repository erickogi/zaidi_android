package com.dev.lishaboramobile.Global.Network

class ApiConstants {


    companion object {
        //KEY VALUES
        var resultCode: String = "ResultCode"
        var resultDescription: String = "ResultDescription"

        var baseUrl: String = "http://lishabora.net/512/APIS/"

        //Accounts
        var Accounts: String = baseUrl + "Accounts/"
        var PhoneAuth: String = Accounts + "AuthPhone.php"
        var PasswordAuth: String = Accounts + "AuthPassword.php"

        //Traders
        var Trader: String = baseUrl + "Traders/"
        var Traders: String = Trader + "List.php"
        var CreateTrader: String = Trader + "Create.php"
        var UpdateTrader: String = Trader + "Update.php"




    }
}
