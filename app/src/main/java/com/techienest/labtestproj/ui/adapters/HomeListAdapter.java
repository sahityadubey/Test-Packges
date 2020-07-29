package com.techienest.labtestproj.ui.adapters;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.techienest.labtestproj.R;
import com.techienest.labtestproj.db.TestInfoDatabase;
import com.techienest.labtestproj.db.dao.TestInfoDao;
import com.techienest.labtestproj.db.entity.TestInfo;

import java.util.List;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {
    private Context mContext;
    private List<TestInfo> testInfoList;
    private TestInfoDao mTestInfoDao;


    public HomeListAdapter(Context mContext, List<TestInfo> testInfoList, Application application) {
        this.mContext = mContext;
        this.testInfoList = testInfoList;

        TestInfoDatabase db = TestInfoDatabase.getDatabase(application);
        this.mTestInfoDao = db.testInfoDaoDao();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(testInfoList.get(position));

        holder.idCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox)view).isChecked()) {
                    TestInfo item = testInfoList.get(position);
                    UpdateDetailInDB updateDetailInDB = new UpdateDetailInDB();
                    updateDetailInDB.execute(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return testInfoList != null ? testInfoList.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        private TextView idSNO;

        @Nullable
        private TextView idTestId;

        @Nullable
        private TextView idTestName;

        @Nullable
        private TextView idPrice;

        @Nullable
        private CheckBox idCheckbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            idTestId = itemView.findViewById(R.id.idTestId);
            idTestName = itemView.findViewById(R.id.idTestName);
            idPrice = itemView.findViewById(R.id.idPrice);
            idSNO = itemView.findViewById(R.id.idsno);
            idCheckbox = itemView.findViewById(R.id.idCheckbox);
        }


        public void bind(TestInfo testInfo) {
            if (idSNO != null) idSNO.setText(Integer.toString(testInfo.getSNO()));
            if (idTestId != null) idTestId.setText(testInfo.getTestId());
            if (idTestName != null) idTestName.setText(testInfo.getTestName());
            if (idPrice != null) idPrice.setText(Integer.toString(testInfo.getTestPrice()));
            if (idCheckbox != null) idCheckbox.setChecked(testInfo.isChecked);
        }
    }

    private class UpdateDetailInDB extends AsyncTask<TestInfo, Void, String> {
        @Override
        protected String doInBackground(TestInfo... testInfos) {
            TestInfo item = testInfos[0];
            item.isChecked  = true;
            mTestInfoDao.UpdateTestsData(item.isChecked, item.getTestId());
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(mContext, "Item Selected!!", Toast.LENGTH_SHORT).show();
        }
    }
}
