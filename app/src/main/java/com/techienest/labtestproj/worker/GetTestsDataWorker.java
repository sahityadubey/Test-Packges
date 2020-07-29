package com.techienest.labtestproj.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.techienest.labtestproj.App;
import com.techienest.labtestproj.R;
import com.techienest.labtestproj.db.dao.TestInfoDao;
import com.techienest.labtestproj.db.entity.TestInfo;
import com.techienest.labtestproj.services.GetLabDataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class GetTestsDataWorker extends Worker {
    private GetLabDataService getLabDataService;
    private TestInfoDao testInfoDao;

    private static final String TAG = GetTestsDataWorker.class.getSimpleName();

    public GetTestsDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        getLabDataService = App.get().getLabDataService();
        testInfoDao = App.get().getTestsDao();
    }

    @NonNull
    @Override
    public Result doWork() {
        Context applicationContext = getApplicationContext();
        //simulate slow work
        // WorkerUtils.makeStatusNotification("Fetching Data", applicationContext);
        Log.i(TAG, "Fetching Data from Remote host");
        WorkerUtils.sleep();
        try {
            //create a call to network
            Call<List<TestInfo>> call = getLabDataService.fetchTestList();
            Response<List<TestInfo>> response = call.execute();

            if (response.isSuccessful() && response.body() != null && !response.body().isEmpty() && response.body().size() > 0) {

                String data = WorkerUtils.toJson(response.body());
                Log.i(TAG, "data fetched from network successfully");

                //delete existing tests data
                testInfoDao.deleteTests();

                testInfoDao.saveTestsInfo(response.body());

                WorkerUtils.makeStatusNotification(applicationContext.getString(R.string.new_data_available), applicationContext);

                return Result.success();
            } else {
                return Result.retry();
            }


        } catch (Throwable e) {
            e.printStackTrace();
            // Technically WorkManager will return Result.failure()
            // but it's best to be explicit about it.
            // Thus if there were errors, we're return FAILURE
            Log.e(TAG, "Error fetching data", e);
            return Result.failure();
        }
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.i(TAG, "OnStopped called for this worker");
    }
}
