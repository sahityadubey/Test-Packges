package com.techienest.labtestproj.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.techienest.labtestproj.db.entity.TestInfo;

import java.util.List;

@Dao
public interface TestInfoDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long saveUser(TestInfo testInfo);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveTestsInfo(List<TestInfo> testInfos);

    @Transaction
    @Query("SELECT * FROM TestsData WHERE id = :testid")
    LiveData<TestInfo> getTests(int testid);


    @Transaction
    @Query("SELECT * from TestsData")
    LiveData<List<TestInfo>> getTests();

    @Transaction
    @Query("DELETE FROM TestsData")
    void deleteTests();

    @Transaction
    @Query("UPDATE TestsData SET isChecked = :isCheckedValue WHERE testId = :getTestId")
    void UpdateTestsData(boolean isCheckedValue, String getTestId);

    @Transaction
    @Query("SELECT * FROM TestsData WHERE isChecked = :isChecked")
    List<TestInfo> GetSelectedItem(boolean isChecked);

}
