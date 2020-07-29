package com.techienest.labtestproj.services;

import androidx.lifecycle.LiveData;

import com.techienest.labtestproj.db.entity.TestInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetLabDataService {

    @GET("getTestList")
    LiveData<ApiResponse<List<TestInfo>>> getTestList();

    @GET("getTestList")
    Call<List<TestInfo>> fetchTestList();
}
