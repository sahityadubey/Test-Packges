package com.techienest.labtestproj.repository;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.techienest.labtestproj.db.TestInfoDatabase;
import com.techienest.labtestproj.db.dao.TestInfoDao;
import com.techienest.labtestproj.db.entity.TestInfo;
import com.techienest.labtestproj.services.ApiResponse;
import com.techienest.labtestproj.services.GetLabDataService;
import com.techienest.labtestproj.utils.AppExecutors;
import com.techienest.labtestproj.utils.NetworkBoundResource;
import com.techienest.labtestproj.utils.NetworkBoundResourceWithoutRoom;
import com.techienest.labtestproj.utils.Resource;

import java.util.List;

import timber.log.Timber;

public class GetTestsDataRepository {
    private static TestInfoDao mTestInfoDao;
    private static GetLabDataService getLabDataService;
    private final AppExecutors appExecutors;
    private final Activity activity;
    private final android.app.Application application;
    private static String LOG_TAG = GetTestsDataRepository.class.getSimpleName();


    public GetTestsDataRepository(android.app.Application application, Activity activity, GetLabDataService getLabDataService,
                          AppExecutors appExecutors) {
        this.application = application;
        this.activity = activity;
        TestInfoDatabase db = TestInfoDatabase.getDatabase(application);
        mTestInfoDao = db.testInfoDaoDao();
        GetTestsDataRepository.getLabDataService = getLabDataService;
        this.appExecutors = appExecutors;
    }

    public LiveData<List<TestInfo>> getTests() {
        return mTestInfoDao.getTests();
    }

    public LiveData<Resource<List<TestInfo>>> loadTests() {

        return new NetworkBoundResource<List<TestInfo>, List<TestInfo>>(appExecutors) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void saveCallResult(@NonNull List<TestInfo> testInfoList) {
                Timber.d("call to delete tests in db");
                mTestInfoDao.deleteTests();
                Timber.d("call to insert results to db");
                mTestInfoDao.saveTestsInfo(testInfoList);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected boolean shouldFetch(@Nullable List<TestInfo> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<TestInfo>> loadFromDb() {
                Timber.d(" call to load from db");
                return mTestInfoDao.getTests();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<TestInfo>>> createCall() {
                Timber.d("creating a call to network");
                return getLabDataService.getTestList();
            }

            @Override
            protected List<TestInfo> processResponse(ApiResponse<List<TestInfo>> response) {
                return super.processResponse(response);
            }
        }.asLiveData();
    }


    public LiveData<Resource<List<TestInfo>>> loadNewTests() {
        return new NetworkBoundResourceWithoutRoom<List<TestInfo>>() {
            @NonNull
            @Override
            protected LiveData<ApiResponse<List<TestInfo>>> createCall() {
                return getLabDataService.getTestList();
            }
        }.asLiveData();
    }

    public List<TestInfo> GetSelectedItems(){
        return mTestInfoDao.GetSelectedItem(true);
    }


    public android.app.Application getApplication() {
        return application;
    }
}
