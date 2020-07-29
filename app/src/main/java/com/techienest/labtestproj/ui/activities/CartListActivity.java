package com.techienest.labtestproj.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import com.techienest.labtestproj.App;
import com.techienest.labtestproj.R;
import com.techienest.labtestproj.db.entity.TestInfo;
import com.techienest.labtestproj.factory.ViewModelFactory;
import com.techienest.labtestproj.repository.GetTestsDataRepository;
import com.techienest.labtestproj.services.GetLabDataService;
import com.techienest.labtestproj.ui.adapters.CartListAdapter;
import com.techienest.labtestproj.utils.AppExecutors;
import com.techienest.labtestproj.viewmodel.CartListViewModel;

import java.util.List;

public class CartListActivity extends AppCompatActivity {
    private CartListViewModel mCartListViewModel;
    private CartListAdapter mHomeListAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        recyclerView = findViewById(R.id.idCartList);
        recyclerView.setHasFixedSize(true);
        GetLabDataService getLabDataService = App.get().getLabDataService();
        GetTestsDataRepository mRepository = new GetTestsDataRepository(getApplication(), CartListActivity.this, getLabDataService, new AppExecutors());
        ViewModelFactory factory = new ViewModelFactory(mRepository);
        mCartListViewModel = ViewModelProviders.of(this, factory).get(CartListViewModel.class);

        new GetData().execute();
    }

    private class GetData extends AsyncTask<Void, Void, List<TestInfo>> {
        @Override
        protected List<TestInfo> doInBackground(Void... voids) {
            return mCartListViewModel.GetDataFromRepo();
        }

        @Override
        protected void onPostExecute(List<TestInfo> result) {
            callAdapter(result);
        }
    }

    private void callAdapter(List<TestInfo> result){
        mHomeListAdapter = new CartListAdapter(CartListActivity.this, result, getApplication());
        recyclerView.setAdapter(mHomeListAdapter);
    }
}