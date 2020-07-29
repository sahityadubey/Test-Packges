package com.techienest.labtestproj.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkInfo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.techienest.labtestproj.App;
import com.techienest.labtestproj.R;
import com.techienest.labtestproj.factory.ViewModelFactory;
import com.techienest.labtestproj.repository.GetTestsDataRepository;
import com.techienest.labtestproj.services.GetLabDataService;
import com.techienest.labtestproj.ui.adapters.HomeListAdapter;
import com.techienest.labtestproj.utils.AppExecutors;
import com.techienest.labtestproj.viewmodel.LabTestsDataViewModel;

public class HomeListActivity extends AppCompatActivity {

    private static final String TAG = HomeListActivity.class.getSimpleName();
    private LabTestsDataViewModel mRemoteSyncViewModel;
    private HomeListAdapter mHomeListAdapter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        mProgressBar = findViewById(R.id.progressBar);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        // Get the ViewModel
        GetLabDataService getLabDataService = App.get().getLabDataService();
        GetTestsDataRepository mRepository = new GetTestsDataRepository(getApplication(), HomeListActivity.this, getLabDataService, new AppExecutors());
        ViewModelFactory factory = new ViewModelFactory(mRepository);
        mRemoteSyncViewModel = ViewModelProviders.of(this, factory).get(LabTestsDataViewModel.class);

        mRemoteSyncViewModel.fetchData();

        // Show work info, goes inside onCreate()
        mRemoteSyncViewModel.getOutputWorkInfo().observe(this, listOfWorkInfo -> {

            // If there are no matching work info, do nothing
            if (listOfWorkInfo == null || listOfWorkInfo.isEmpty()) {
                return;
            }

            // We only care about the first output status.
            // Every continuation has only one worker tagged TAG_SYNC_DATA
            WorkInfo workInfo = listOfWorkInfo.get(0);
            Log.i(TAG, "WorkState: " + workInfo.getState());
            if (workInfo.getState() == WorkInfo.State.ENQUEUED) {
                showWorkFinished();

                //observe Room db
                mRemoteSyncViewModel.getTests().observe(this, tests -> {
                    mHomeListAdapter = new HomeListAdapter(HomeListActivity.this, tests, getApplication());
                    recyclerView.setAdapter(mHomeListAdapter);

                });


            } else {
                showWorkInProgress();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void showWorkInProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void showWorkFinished() {
        mProgressBar.setVisibility(View.GONE);
    }

    public void openCartActivity(View view){
        Intent intent = new Intent(this, CartListActivity.class);
        startActivity(intent);
    }
}