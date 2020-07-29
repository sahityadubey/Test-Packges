package com.techienest.labtestproj.db.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "TestsData")
public class TestInfo {

    public TestInfo() {
    }

    @Ignore
    public TestInfo(int SNO, String testId, String testName, String testkeyword, int testPrice, boolean isChecked) {
        super();
        this.SNO = SNO;
        this.testId = testId;
        this.testName = testName;
        this.testkeyword = testkeyword;
        this.testPrice = testPrice;
        this.isChecked = isChecked;
    }

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @SerializedName("S.no")
    public int SNO;

    @SerializedName("itemId")
    public String testId;

    @SerializedName("itemName")
    public String testName;

    @SerializedName("Keyword")
    public String testkeyword;

    @SerializedName("minPrice")
    public int testPrice;

    public boolean isChecked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getSNO() {
        return SNO;
    }

    public void setSNO(int SNO) {
        this.SNO = SNO;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestkeyword() {
        return testkeyword;
    }

    public void setTestkeyword(String testkeyword) {
        this.testkeyword = testkeyword;
    }

    public int getTestPrice() {
        return testPrice;
    }

    public void setTestPrice(int testPrice) {
        this.testPrice = testPrice;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", S.no ='" + SNO + '\'' +
                ", testId='" + testId + '\'' +
                ", testName='" + testName + '\'' +
                ", testkeyword='" + testkeyword + '\'' +
                ", testPrice='" + testPrice + '\'' +
                ", isChecked='" + isChecked + '\'' +
                '}';
    }

}