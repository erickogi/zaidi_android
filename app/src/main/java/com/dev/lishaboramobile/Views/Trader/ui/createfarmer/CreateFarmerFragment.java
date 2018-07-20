package com.dev.lishaboramobile.Views.Trader.ui.createfarmer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.lishaboramobile.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


public class CreateFarmerFragment extends Fragment {

    private CreateFarmerViewModel mViewModel;

    public static CreateFarmerFragment newInstance() {
        return new CreateFarmerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_farmer_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CreateFarmerViewModel.class);
        // TODO: Use the ViewModel
    }

}
