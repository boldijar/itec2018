
package io.github.boldijar.cosasapp.itecdata;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class User {

    @SerializedName("Age")
    private Integer mAge;
    @SerializedName("Email")
    private String mEmail;
    @SerializedName("FullName")
    private String mFullName;
    @SerializedName("Gender")
    private Integer mGender;
    @SerializedName("Id")
    private String mId;
    @SerializedName("Latitude")
    private Double mLatitude;
    @SerializedName("Longitude")
    private Double mLongitude;
    @SerializedName("Password")
    private String mPassword;
    @SerializedName("Radius")
    private Integer mRadius;

    public User(Integer age, String email, String fullName, Integer gender, Double latitude, Double longitude, String password, Integer radius) {
        mId = java.util.UUID.randomUUID().toString();
        mAge = age;
        mEmail = email;
        mFullName = fullName;
        mGender = gender;
        mLatitude = latitude;
        mLongitude = longitude;
        mPassword = password;
        mRadius = radius;
    }

    public Integer getAge() {
        return mAge;
    }

    public void setAge(Integer age) {
        mAge = age;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        mFullName = fullName;
    }

    public Integer getGender() {
        return mGender;
    }

    public void setGender(Integer gender) {
        mGender = gender;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double latitude) {
        mLatitude = latitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double longitude) {
        mLongitude = longitude;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public Integer getRadius() {
        return mRadius;
    }

    public void setRadius(Integer radius) {
        mRadius = radius;
    }

}
