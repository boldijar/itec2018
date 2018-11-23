package io.github.boldijar.cosasapp.itecdata;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("Data")
    private User mUser;

    public User getUser() {
        return mUser;
    }
}
