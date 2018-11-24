package io.github.boldijar.cosasapp.itecdata;

import com.google.gson.annotations.SerializedName;

public class MonthlyData {
    @SerializedName("MonthNumber")
    private Integer mMonthNumber;
    @SerializedName("MonthName")
    private String mMonthName;
    @SerializedName("CreatedIssues")
    private Integer mCreatedIssues;
    @SerializedName("SolvedIssues")
    private Integer mSolvedIssues;

    public Integer getMonthNumber() {
        return mMonthNumber;
    }

    public String getMonthName() {
        return mMonthName;
    }

    public Integer getCreatedIssues() {
        return mCreatedIssues;
    }

    public Integer getSolvedIssues() {
        return mSolvedIssues;
    }
}
