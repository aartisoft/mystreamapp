package com.domain.mystream.Model;

public class Likes
{
    private User User;

    private String SystemId;

    private String PostReactionId;

    private String ReactionTypeId;

    private String ReactionType;

    private String ReactionOn;

    private String InteractionByUserId;

    private String PostTypeId;

    private String ReferenceId;

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

    public String getPostReactionId ()
    {
        return PostReactionId;
    }

    public void setPostReactionId (String PostReactionId)
    {
        this.PostReactionId = PostReactionId;
    }

    public String getReactionTypeId ()
    {
        return ReactionTypeId;
    }

    public void setReactionTypeId (String ReactionTypeId)
    {
        this.ReactionTypeId = ReactionTypeId;
    }

    public String getReactionType ()
    {
        return ReactionType;
    }

    public void setReactionType (String ReactionType)
    {
        this.ReactionType = ReactionType;
    }

    public String getReactionOn ()
    {
        return ReactionOn;
    }

    public void setReactionOn (String ReactionOn)
    {
        this.ReactionOn = ReactionOn;
    }

    public String getInteractionByUserId ()
    {
        return InteractionByUserId;
    }

    public void setInteractionByUserId (String InteractionByUserId)
    {
        this.InteractionByUserId = InteractionByUserId;
    }

    public String getPostTypeId ()
    {
        return PostTypeId;
    }

    public void setPostTypeId (String PostTypeId)
    {
        this.PostTypeId = PostTypeId;
    }

    public String getReferenceId ()
    {
        return ReferenceId;
    }

    public void setReferenceId (String ReferenceId)
    {
        this.ReferenceId = ReferenceId;
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
        return "ClassPojo [User = "+User+", SystemId = "+SystemId+", PostReactionId = "+PostReactionId+", ReactionTypeId = "+ReactionTypeId+", ReactionType = "+ReactionType+", ReactionOn = "+ReactionOn+", InteractionByUserId = "+InteractionByUserId+", PostTypeId = "+PostTypeId+", ReferenceId = "+ReferenceId+", PostType = "+PostType+"]";
    }
}