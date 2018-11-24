package io.github.boldijar.cosasapp.itecdata;

import com.google.gson.annotations.SerializedName;

public class HumanStats {
    @SerializedName("Start")
    private Integer mStart;
    @SerializedName("End")
    private Integer mEnd;
    @SerializedName("Value")
    private Integer mValue;

    public Integer getStart() {
        return mStart;
    }

    public Integer getEnd() {
        return mEnd;
    }

    public Integer getValue() {
        return mValue;
    }
}
