package io.github.boldijar.cosasapp.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * This class handles user preferences
 *
 * @author Paul
 * @since 2018.08.15
 */
public enum Prefs {

//    {"Age":21,"Email":"boldijar.paul@gmail.com","FullName":"Paul Boldijar","Gender":0,"Id":
// "65ee1630-ee2c-4c16-aa60-f1d88be11216","Latitude":-33.993213,"Longitude":122.2356088,"Password":"parola","Radius":49}
    User, Token;

    Prefs(String defaultValue) {
        mDefaultValue = defaultValue;
    }

    Prefs() {

    }

    private String mDefaultValue;

    private static SharedPreferences sSharedPreferences;
    private static Gson GSON = new Gson();

    public static void init(Application application) {
        sSharedPreferences = application.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    public static io.github.boldijar.cosasapp.data.User getUser() {
        return User.getFromJson(io.github.boldijar.cosasapp.data.User.class);
    }

    public void put(Object value) {
        if (value != null) {
            sSharedPreferences.edit().putString(name(), value + "").commit();
        } else {
            sSharedPreferences.edit().putString(name(), null).commit();
        }
    }

    public void putAsJson(Object object) {
        put(GSON.toJson(object));
    }

    public <T> T getFromJson(Class<T> type) {
        String json = get();
        if (json == null) {
            return null;
        }
        return GSON.fromJson(json, type);
    }

    public String get() {
        return sSharedPreferences.getString(name(), mDefaultValue);
    }


    public boolean getBoolean(boolean defaultValue) {
        String value = get();
        if (value == null) {
            return defaultValue;
        }
        return Boolean.valueOf(value);
    }

    public int getInteger(int defaultValue) {
        String value = get();
        if (value == null) {
            return defaultValue;
        }
        return Integer.valueOf(value);
    }

    public double getDouble(double defaultValue) {
        String value = get();
        if (value == null) {
            return defaultValue;
        }
        return Double.valueOf(value);
    }
}
