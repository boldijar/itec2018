
package io.github.boldijar.cosasapp.itecdata;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class Issue {

    public final static Issue ADD_ISSUE = new Issue();

    @SerializedName("CreatedAt")
    private Long mCreatedAt;
    @SerializedName("CreatedBy")
    private String mCreatedBy;
    @SerializedName("Creator")
    private String mCreator;
    @SerializedName("Description")
    private String mDescription;
    @SerializedName("DownVotes")
    private Long mDownVotes;
    @SerializedName("Id")
    private String mId;
    @SerializedName("Images")
    private List<String> mImages;
    @SerializedName("Latitude")
    private Double mLatitude;
    @SerializedName("Longitude")
    private Double mLongitude;
    @SerializedName("Title")
    private String mTitle;
    @SerializedName("UpVotes")
    private Long mUpVotes;

    private boolean mLiked;

    public void setLiked(boolean liked) {
        mLiked = liked;
    }

    public boolean isLiked() {
        return mLiked;
    }

    public Long getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Long createdAt) {
        mCreatedAt = createdAt;
    }

    public String getCreatedBy() {
        return mCreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        mCreatedBy = createdBy;
    }

    public String getCreator() {
        return mCreator;
    }

    public void setCreator(String creator) {
        mCreator = creator;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Long getDownVotes() {
        return mDownVotes;
    }

    public void setDownVotes(Long downVotes) {
        mDownVotes = downVotes;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public List<String> getImages() {
        return mImages;
    }

    public void setImages(List<String> images) {
        mImages = images;
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

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Long getUpVotes() {
        return mUpVotes;
    }

    public void setUpVotes(Long upVotes) {
        mUpVotes = upVotes;
    }

}
