package com.dev.lishabora.ViewModels.Trader;

import android.app.Application;

public class CollectMilkBuilder {
    private Application application;

    public CollectMilkBuilder setApplication(Application application) {
        this.application = application;
        return this;
    }

    public TraderViewModel createTraderViewModel() {
        return new TraderViewModel(application);
    }
}