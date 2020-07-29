package com.techienest.labtestproj.utils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.techienest.labtestproj.services.ApiResponse;

import java.util.Objects;

public abstract class NetworkBoundResourceWithoutRoom<ResultType> {

    private static final String TAG = NetworkBoundResourceWithoutRoom.class.getSimpleName();

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @MainThread
    public NetworkBoundResourceWithoutRoom() {
        //set loading status
        setValue(Resource.<ResultType>loading(null));
        //fetch from network and set value
        fetchFromNetwork();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @MainThread
    private void setValue(Resource<ResultType> newValue) {
        if (!Objects.equals(result.getValue(), newValue)) {
            result.setValue(newValue);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void fetchFromNetwork() {
        //create call to network
        LiveData<ApiResponse<ResultType>> apiResponse = createCall();
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            if (response.isSuccessful()) {
                NetworkBoundResourceWithoutRoom.this.setValue(Resource.success(NetworkBoundResourceWithoutRoom.this.processResponse(response)));

            } else {
                NetworkBoundResourceWithoutRoom.this.onFetchFailed();
                NetworkBoundResourceWithoutRoom.this.setValue(Resource.error(response.errorMessage, null));
            }
        });

    }

    protected void onFetchFailed() {
    }

    public LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }

    @WorkerThread
    protected ResultType processResponse(ApiResponse<ResultType> response) {
        Log.i(TAG, "PROCESSING RESPONSE");
        return response.body;
    }

    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<ResultType>> createCall();
}
