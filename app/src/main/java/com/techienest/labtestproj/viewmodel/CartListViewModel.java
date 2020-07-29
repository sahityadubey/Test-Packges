package com.techienest.labtestproj.viewmodel;

import androidx.lifecycle.AndroidViewModel;

import com.techienest.labtestproj.db.entity.TestInfo;
import com.techienest.labtestproj.repository.GetTestsDataRepository;

import java.util.List;

import static com.techienest.labtestproj.utils.Constants.TAG_SYNC_DATA;

public class CartListViewModel extends AndroidViewModel {
    private GetTestsDataRepository mRepository;

    public CartListViewModel(GetTestsDataRepository mRepository) {
        super(mRepository.getApplication());
        this.mRepository = mRepository;
    }

    public List<TestInfo> GetDataFromRepo(){
        return mRepository.GetSelectedItems();
    }
}
