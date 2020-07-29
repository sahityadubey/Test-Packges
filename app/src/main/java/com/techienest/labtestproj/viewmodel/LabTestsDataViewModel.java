package com.techienest.labtestproj.viewmodel;

import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.techienest.labtestproj.db.entity.TestInfo;
import com.techienest.labtestproj.repository.GetTestsDataRepository;
import com.techienest.labtestproj.utils.Resource;
import com.techienest.labtestproj.worker.GetTestsDataWorker;
import com.techienest.labtestproj.worker.WorkerUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.techienest.labtestproj.utils.Constants.SYNC_DATA_WORK_NAME;
import static com.techienest.labtestproj.utils.Constants.TAG_SYNC_DATA;

public class LabTestsDataViewModel extends AndroidViewModel {
    private GetTestsDataRepository mRepository;
    private WorkManager mWorkManager;
    // New instance variable for the WorkInfo
    private LiveData<List<WorkInfo>> mSavedWorkInfo;

    private static List<TestInfo> testInfoList;

    public LabTestsDataViewModel(GetTestsDataRepository mRepository) {
        super(mRepository.getApplication());
        this.mRepository = mRepository;
        mWorkManager = WorkManager.getInstance(mRepository.getApplication());
        mSavedWorkInfo = mWorkManager.getWorkInfosByTagLiveData(TAG_SYNC_DATA);
    }

    public LiveData<Resource<List<TestInfo>>> loadTests() {
        return mRepository.loadTests();
    }

    public LiveData<Resource<List<TestInfo>>> loadNewTests() {
        return mRepository.loadNewTests();
    }

    public void fetchData() {

        // Create Network constraint
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();


        PeriodicWorkRequest periodicSyncDataWork =
                new PeriodicWorkRequest.Builder(GetTestsDataWorker.class, 5, TimeUnit.SECONDS)
                        .addTag(TAG_SYNC_DATA)
                        .setConstraints(constraints)
                        // setting a backoff on case the work needs to retry
                        .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                        .build();
        mWorkManager.enqueueUniquePeriodicWork(
                SYNC_DATA_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP, //Existing Periodic Work policy
                periodicSyncDataWork //work request
        );

    }

    public LiveData<List<TestInfo>> getTests() {
        return mRepository.getTests();
    }

    public void setOutputData(String outputData) {
        testInfoList = WorkerUtils.fromJson(outputData);
    }

    public List<TestInfo> getOutputData() {
        return testInfoList;
    }

    public LiveData<List<WorkInfo>> getOutputWorkInfo() {
        return mSavedWorkInfo;
    }

    /**
     * Cancel work using the work's unique name
     */
    public void cancelWork() {
        Log.i("VIEWMODEL", "Cancelling work");
        mWorkManager.cancelUniqueWork(SYNC_DATA_WORK_NAME);
    }

}
