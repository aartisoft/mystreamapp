package com.domain.mystream.Model;



public class Comment
{
    private User User;

    private String Text;

    private String SystemId;

    private String ParentCommentId;

    private String CommentOn;

    private String ParentComment;

    private String UserId;

    private String CommentId;



    public User getUser ()
    {
        return User;
    }

    public void setUser (User User)
    {
        this.User = User;
    }

    public String getText ()
    {
        return Text;
    }

    public void setText (String Text)
    {
        this.Text = Text;
    }

    public String getSystemId ()
    {
        return SystemId;
    }

    public void setSystemId (String SystemId)
    {
        this.SystemId = SystemId;
    }

    public String getParentCommentId ()
    {
        return ParentCommentId;
    }

    public void setParentCommentId (String  ParentCommentId)
    {
        this.ParentCommentId = ParentCommentId;
    }

    public String getCommentOn ()
    {
        return CommentOn;
    }

    public void setCommentOn (String CommentOn)
    {
        this.CommentOn = CommentOn;
    }

    public String getParentComment ()
    {
        return ParentComment;
    }

    public void setParentComment (String ParentComment)
    {
        this.ParentComment = ParentComment;
    }

    public String getUserId ()
    {
        return UserId;
    }

    public void setUserId (String UserId)
    {
        this.UserId = UserId;
    }

    public String getCommentId ()
    {
        return CommentId;
    }

    public void setCommentId (String CommentId)
    {
        this.CommentId = CommentId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [User = "+User+", Text = "+Text+", SystemId = "+SystemId+", ParentCommentId = "+ParentCommentId+", CommentOn = "+CommentOn+", ParentComment = "+ParentComment+", UserId = "+UserId+", CommentId = "+CommentId+"]";
    }
}