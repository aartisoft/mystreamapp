package com.domain.mystream.Model;

import java.util.List;

public class PostModel
{
    private User User;
    private Company company;

    public boolean isLikebyme() {
        return likebyme;
    }

    public void setLikebyme(boolean likebyme) {
        this.likebyme = likebyme;
    }
    public boolean isCommentbyme() {
        return commentbyme;
    }

    public void setCOmmentbyme(boolean commentbyme) {
        this.commentbyme = commentbyme;
    }
    private boolean commentbyme=false;
    private boolean likebyme=false;
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    private String SystemId;

    private String CreatedByUserId;

    public List<com.domain.mystream.Model.Comments> getComments() {
        return Comments;
    }

    public void setComments(List<com.domain.mystream.Model.Comments> comments) {
        Comments = comments;
    }

    public List<com.domain.mystream.Model.Likes> getLikes() {
        return Likes;
    }

    public void setLikes(List<com.domain.mystream.Model.Likes> likes) {
        Likes = likes;
    }

    private List<Comments> Comments;

    private String LastUpdatedOnDate;

    private String UserGroupId;

    private List<Likes> Likes;

    private String PostId;

    private String LastUpdatedByUserId;

    private String[] RepliedUsers;

    private String PostName;

    private String IsRemoved;

    private String[] Shares;

    private String PostBody;

    private String PostTypeId;

    private String IsFullPost;

    private String CreatedOnDate;

    private PostType PostType;

    public User getUser ()
    {
        return User;
    }

    public void setUser (User User)
    {
        this.User = User;
    }

    public String getSystemId ()
    {
        return SystemId;
    }

    public void setSystemId (String SystemId)
    {
        this.SystemId = SystemId;
    }

    public String getCreatedByUserId ()
    {
        return CreatedByUserId;
    }

    public void setCreatedByUserId (String CreatedByUserId)
    {
        this.CreatedByUserId = CreatedByUserId;
    }


    public String getLastUpdatedOnDate ()
    {
        return LastUpdatedOnDate;
    }

    public void setLastUpdatedOnDate (String LastUpdatedOnDate)
    {
        this.LastUpdatedOnDate = LastUpdatedOnDate;
    }

    public String getUserGroupId ()
    {
        return UserGroupId;
    }

    public void setUserGroupId (String UserGroupId)
    {
        this.UserGroupId = UserGroupId;
    }


    public String getPostId ()
    {
        return PostId;
    }

    public void setPostId (String PostId)
    {
        this.PostId = PostId;
    }

    public String getLastUpdatedByUserId ()
    {
        return LastUpdatedByUserId;
    }

    public void setLastUpdatedByUserId (String LastUpdatedByUserId)
    {
        this.LastUpdatedByUserId = LastUpdatedByUserId;
    }

    public String[] getRepliedUsers ()
    {
        return RepliedUsers;
    }

    public void setRepliedUsers (String[] RepliedUsers)
    {
        this.RepliedUsers = RepliedUsers;
    }

    public String getPostName ()
    {
        return PostName;
    }

    public void setPostName (String PostName)
    {
        this.PostName = PostName;
    }

    public String getIsRemoved ()
    {
        return IsRemoved;
    }

    public void setIsRemoved (String IsRemoved)
    {
        this.IsRemoved = IsRemoved;
    }

    public String[] getShares ()
    {
        return Shares;
    }

    public void setShares (String[] Shares)
    {
        this.Shares = Shares;
    }

    public String getPostBody ()
    {
        return PostBody;
    }

    public void setPostBody (String PostBody)
    {
        this.PostBody = PostBody;
    }

    public String getPostTypeId ()
    {
        return PostTypeId;
    }

    public void setPostTypeId (String PostTypeId)
    {
        this.PostTypeId = PostTypeId;
    }

    public String getIsFullPost ()
    {
        return IsFullPost;
    }

    public void setIsFullPost (String IsFullPost)
    {
        this.IsFullPost = IsFullPost;
    }

    public String getCreatedOnDate ()
    {
        return CreatedOnDate;
    }

    public void setCreatedOnDate (String CreatedOnDate)
    {
        this.CreatedOnDate = CreatedOnDate;
    }

    public PostType getPostType ()
    {
        return PostType;
    }

    public void setPostType (PostType PostType)
    {
        this.PostType = PostType;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [User = "+User+", SystemId = "+SystemId+", CreatedByUserId = "+CreatedByUserId+", Comments = "+Comments+", LastUpdatedOnDate = "+LastUpdatedOnDate+", UserGroupId = "+UserGroupId+", Likes = "+Likes+", PostId = "+PostId+", LastUpdatedByUserId = "+LastUpdatedByUserId+", RepliedUsers = "+RepliedUsers+", PostName = "+PostName+", IsRemoved = "+IsRemoved+", Shares = "+Shares+", PostBody = "+PostBody+", PostTypeId = "+PostTypeId+", IsFullPost = "+IsFullPost+", CreatedOnDate = "+CreatedOnDate+", PostType = "+PostType+"]";
    }
}