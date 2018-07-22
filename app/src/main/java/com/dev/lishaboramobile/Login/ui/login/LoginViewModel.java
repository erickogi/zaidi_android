package com.dev.lishaboramobile.Login.ui.login;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

public class LoginViewModel extends AndroidViewModel {

    private Context context;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
    }
    // TODO: Implement the ViewModel
}
