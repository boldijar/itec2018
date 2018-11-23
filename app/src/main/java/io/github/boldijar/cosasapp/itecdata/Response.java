package io.github.boldijar.cosasapp.itecdata;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("Success")
    private boolean mSuccess;

    public boolean isSuccess() {
        return mSuccess;
    }
}
