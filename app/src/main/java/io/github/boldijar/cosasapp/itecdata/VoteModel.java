package io.github.boldijar.cosasapp.itecdata;

import com.google.gson.annotations.SerializedName;

public class VoteModel {
    @SerializedName("IssueId")
    private String mIssueId;
    @SerializedName("VoteType")
    private int mVoteType;

    public static VoteModel createThumbsUp(String id) {
        VoteModel voteModel = new VoteModel();
        voteModel.mIssueId = id;
        voteModel.mVoteType = 0;
        return voteModel;
    }

    public static VoteModel createThumbsDown(String id) {
        VoteModel voteModel = new VoteModel();
        voteModel.mIssueId = id;
        voteModel.mVoteType = 1;
        return voteModel;
    }
}
