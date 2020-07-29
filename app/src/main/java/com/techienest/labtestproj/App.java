package com.techienest.labtestproj;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.techienest.labtestproj.db.TestInfoDatabase;
import com.techienest.labtestproj.db.dao.TestInfoDao;
import com.techienest.labtestproj.factory.LiveDataCallAdapterFactory;
import com.techienest.labtestproj.services.GetLabDataService;
import com.techienest.labtestproj.utils.AppExecutors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class App extends Application {

    private static final String TAG = App.class.getSimpleName();
    private static final String BASE_URL = "https://5f1a8228610bde0016fd2a74.mockapi.io/";
    private GetLabDataService mGetLabDataService;
    private static TestInfoDao mTestInfoDao;

    private static App INSTANCE;
    private static AppExecutors mAppExecutors;

    public static App get() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
            INSTANCE = this;

            OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();

            //Gson Builder
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            Timber.plant(new Timber.DebugTree());

            // HttpLoggingInterceptor
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> Timber.i(message));
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // OkHttpClient
            OkHttpClient okHttpClient = new OkHttpClient()
                    .newBuilder()
                    .addInterceptor(httpLoggingInterceptor)
                    .build();


            //Retrofit
            Retrofit mRetrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            //TestsService
            mGetLabDataService = mRetrofit.create(GetLabDataService.class);
            mAppExecutors = new AppExecutors();
        mTestInfoDao = TestInfoDatabase.getDatabase(getApplicationContext()).testInfoDaoDao();
    }

    public GetLabDataService getLabDataService() {
        return mGetLabDataService;
    }


    public TestInfoDao getTestsDao() {
        return mTestInfoDao;
    }

    public AppExecutors getExecutors() {
        return mAppExecutors;
    }

}
