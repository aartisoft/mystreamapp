
package com.domain.mystream.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatType_ {

    @SerializedName("ChatTypeId")
    @Expose
    private Integer chatTypeId;
    @SerializedName("ChatType1")
    @Expose
    private String chatType1;

    public Integer getChatTypeId() {
        return chatTypeId;
    }

    public void setChatTypeId(Integer chatTypeId) {
        this.chatTypeId = chatTypeId;
    }

    public String getChatType1() {
        return chatType1;
    }

    public void setChatType1(String chatType1) {
        this.chatType1 = chatType1;
    }

}
