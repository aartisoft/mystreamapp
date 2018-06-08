
package com.domain.mystream.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentGson {

    @SerializedName("CommentId")
    @Expose
    private Integer commentId;
    @SerializedName("Text")
    @Expose
    private String text;
    @SerializedName("ParentCommentId")
    @Expose
    private Integer parentCommentId;
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("CommentOn")
    @Expose
    private String commentOn;
    @SerializedName("ParentComment")
    @Expose
    private ParentComment parentComment;
    @SerializedName("User")
    @Expose
    private User_ user;
    @SerializedName("SystemId")
    @Expose
    private Integer systemId;

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getParentCommentId() {
        if(String.valueOf(parentCommentId).equals(null))
            return 0;
        else
        return parentCommentId;
    }

    public void setParentCommentId(Integer parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCommentOn() {
        return commentOn;
    }

    public void setCommentOn(String commentOn) {
        this.commentOn = commentOn;
    }

    public ParentComment getParentComment() {
        return parentComment;
    }

    public void setParentComment(ParentComment parentComment) {
        this.parentComment = parentComment;
    }

    public User_ getUser() {
        return user;
    }

    public void setUser(User_ user) {
        this.user = user;
    }

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

}
