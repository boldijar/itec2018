package io.github.boldijar.cosasapp.itecdata;

import com.google.gson.annotations.SerializedName;

public class Stats {

    @SerializedName("HealthIndex")
    private float mHealthIndex;

    public float getHealthIndex() {
        return mHealthIndex;
    }
}
