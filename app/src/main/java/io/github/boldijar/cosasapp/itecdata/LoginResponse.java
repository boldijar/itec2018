package io.github.boldijar.cosasapp.itecdata;

import com.google.gson.annotations.SerializedName;

public class LoginResponse extends Response {

    @SerializedName("access_token")
    private String mToken;

    public String getToken() {
        return mToken;
    }
}
