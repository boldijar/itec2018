package io.github.boldijar.cosasapp.itecdata;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Stats {

    @SerializedName("HealthIndex")
    private float mHealthIndex;
    @SerializedName("MonthlyData")
    private List<MonthlyData> mMonthlyData;
    @SerializedName("HumanStats")
    private List<HumanStats> mHumanStats;

    public float getHealthIndex() {
        return mHealthIndex;
    }

    public List<MonthlyData> getMonthlyData() {
        return mMonthlyData;
    }

    public List<HumanStats> getHumanStats() {
        return mHumanStats;
    }
}
