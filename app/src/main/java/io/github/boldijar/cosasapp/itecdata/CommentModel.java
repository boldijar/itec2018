
package io.github.boldijar.cosasapp.itecdata;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class CommentModel implements Serializable {

    @SerializedName("Content")
    private String mContent;
    @SerializedName("CreatedBy")
    private String mCreatedBy;
    @SerializedName("Id")
    private String mId;
    @SerializedName("IssueId")
    private String mIssueId;
    @SerializedName("Creator")
    private String mCreator;
    @SerializedName("CreatedAt")
    private long mCreatedAt;

    public CommentModel(String content, String createdBy, String issueId) {
        mId = java.util.UUID.randomUUID().toString();
        mContent = content;
        mCreatedBy = createdBy;
        mIssueId = issueId;

    }

    public long getCreatedAt() {
        return mCreatedAt;
    }

    public String getCreator() {
        return mCreator;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getCreatedBy() {
        return mCreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        mCreatedBy = createdBy;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getIssueId() {
        return mIssueId;
    }

    public void setIssueId(String issueId) {
        mIssueId = issueId;
    }

    public void setCreator(String creator) {
        mCreator = creator;
    }
}
