package com.domain.mystream.Model;

public class PostType
{
    private String SystemId;

    private String PostType1;

    private String PostTypeId;

    public String getSystemId ()
    {
        return SystemId;
    }

    public void setSystemId (String SystemId)
    {
        this.SystemId = SystemId;
    }

    public String getPostType1 ()
    {
        return PostType1;
    }

    public void setPostType1 (String PostType1)
    {
        this.PostType1 = PostType1;
    }

    public String getPostTypeId ()
    {
        return PostTypeId;
    }

    public void setPostTypeId (String PostTypeId)
    {
        this.PostTypeId = PostTypeId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [SystemId = "+SystemId+", PostType1 = "+PostType1+", PostTypeId = "+PostTypeId+"]";
    }
}