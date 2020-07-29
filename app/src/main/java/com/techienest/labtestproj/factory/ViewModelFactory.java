package com.techienest.labtestproj.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.techienest.labtestproj.repository.GetTestsDataRepository;
import com.techienest.labtestproj.viewmodel.CartListViewModel;
import com.techienest.labtestproj.viewmodel.LabTestsDataViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
private final GetTestsDataRepository mGetTestsDataRepository;

    public ViewModelFactory(GetTestsDataRepository mGetTestsDataRepository) {
            this.mGetTestsDataRepository = mGetTestsDataRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(LabTestsDataViewModel.class))
            return (T) new LabTestsDataViewModel(mGetTestsDataRepository);

            else if (modelClass.isAssignableFrom(CartListViewModel.class))
                return (T) new CartListViewModel(mGetTestsDataRepository);
            throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
