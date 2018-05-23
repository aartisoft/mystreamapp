
package com.domain.mystream.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParentComment_ {

    @SerializedName("CommentId")
    @Expose
    private Integer commentId;
    @SerializedName("Text")
    @Expose
    private Object text;
    @SerializedName("ParentCommentId")
    @Expose
    private Object parentCommentId;
    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("CommentOn")
    @Expose
    private String commentOn;
    @SerializedName("ParentComment")
    @Expose
    private Object parentComment;
    @SerializedName("User")
    @Expose
    private Object user;
    @SerializedName("SystemId")
    @Expose
    private Integer systemId;

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Object getText() {
        return text;
    }

    public void setText(Object text) {
        this.text = text;
    }

    public Object getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Object parentCommentId) {
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

    public Object getParentComment() {
        return parentComment;
    }

    public void setParentComment(Object parentComment) {
        this.parentComment = parentComment;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

}
