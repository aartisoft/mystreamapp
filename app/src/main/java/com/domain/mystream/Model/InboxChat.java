
package com.domain.mystream.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InboxChat {

    @SerializedName("Identifier")
    @Expose
    private String identifier;
    @SerializedName("IsRemoved")
    @Expose
    private Boolean isRemoved;
    @SerializedName("ChatId")
    @Expose
    private Integer chatId;
    @SerializedName("ChatMessageId")
    @Expose
    private Integer chatMessageId;
    @SerializedName("IsRead")
    @Expose
    private Boolean isRead;
    @SerializedName("MessageBody")
    @Expose
    private String messageBody;
    @SerializedName("ReceiverId")
    @Expose
    private Integer receiverId;
    @SerializedName("SenderId")
    @Expose
    private String senderId;
    @SerializedName("SentOn")
    @Expose
    private String sentOn;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Boolean getIsRemoved() {
        return isRemoved;
    }

    public void setIsRemoved(Boolean isRemoved) {
        this.isRemoved = isRemoved;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public Integer getChatMessageId() {
        return chatMessageId;
    }

    public void setChatMessageId(Integer chatMessageId) {
        this.chatMessageId = chatMessageId;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSentOn() {
        return sentOn;
    }

    public void setSentOn(String sentOn) {
        this.sentOn = sentOn;
    }

}
