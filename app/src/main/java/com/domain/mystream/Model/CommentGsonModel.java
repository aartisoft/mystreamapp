
package com.domain.mystream.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentGsonModel {

    @SerializedName("PostCommentId")
    @Expose
    private Integer postCommentId;
    @SerializedName("ReferenceId")
    @Expose
    private Integer referenceId;
    @SerializedName("ReferenceTypeId")
    @Expose
    private Integer referenceTypeId;
    @SerializedName("CommentId")
    @Expose
    private Integer commentId;
    @SerializedName("Comment")
    @Expose
    private CommentGson comment;
    @SerializedName("SystemId")
    @Expose
    private Integer systemId;

    public Integer getPostCommentId() {
        return postCommentId;
    }

    public void setPostCommentId(Integer postCommentId) {
        this.postCommentId = postCommentId;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public Integer getReferenceTypeId() {
        return referenceTypeId;
    }

    public void setReferenceTypeId(Integer referenceTypeId) {
        this.referenceTypeId = referenceTypeId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public CommentGson getComment() {
        return comment;
    }

    public void setComment(CommentGson comment) {
        this.comment = comment;
    }

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

}
