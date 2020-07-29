package com.techienest.labtestproj.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.techienest.labtestproj.db.dao.TestInfoDao;
import com.techienest.labtestproj.db.entity.TestInfo;

@Database(entities = {TestInfo.class}, version = 1, exportSchema = false)
public abstract class TestInfoDatabase extends RoomDatabase {
    private static volatile TestInfoDatabase INSTANCE;

    public abstract TestInfoDao testInfoDaoDao();

    public static TestInfoDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TestInfoDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TestInfoDatabase.class, "test_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}