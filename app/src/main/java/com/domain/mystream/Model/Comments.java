package com.domain.mystream.Model;

public class Comments
{
    private String SystemId;

    private String PostCommentId;

    private Comment Comment;

    private String ReferenceTypeId;

    private String CommentId;

    private String ReferenceId;

    public String getSystemId ()
    {
        return SystemId;
    }

    public void setSystemId (String SystemId)
    {
        this.SystemId = SystemId;
    }

    public String getPostCommentId ()
    {
        return PostCommentId;
    }

    public void setPostCommentId (String PostCommentId)
    {
        this.PostCommentId = PostCommentId;
    }

    public Comment getComment ()
    {
        return Comment;
    }

    public void setComment (Comment Comment)
    {
        this.Comment = Comment;
    }

    public String getReferenceTypeId ()
    {
        return ReferenceTypeId;
    }

    public void setReferenceTypeId (String ReferenceTypeId)
    {
        this.ReferenceTypeId = ReferenceTypeId;
    }

    public String getCommentId ()
    {
        return CommentId;
    }

    public void setCommentId (String CommentId)
    {
        this.CommentId = CommentId;
    }

    public String getReferenceId ()
    {
        return ReferenceId;
    }

    public void setReferenceId (String ReferenceId)
    {
        this.ReferenceId = ReferenceId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [SystemId = "+SystemId+", PostCommentId = "+PostCommentId+", Comment = "+Comment+", ReferenceTypeId = "+ReferenceTypeId+", CommentId = "+CommentId+", ReferenceId = "+ReferenceId+"]";
    }
}