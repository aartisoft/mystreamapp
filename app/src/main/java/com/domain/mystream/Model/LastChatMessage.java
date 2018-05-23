
package com.domain.mystream.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LastChatMessage {

    @SerializedName("ChatMessageId")
    @Expose
    private Integer chatMessageId;
    @SerializedName("ChatId")
    @Expose
    private Integer chatId;
    @SerializedName("SenderId")
    @Expose
    private Integer senderId;
    @SerializedName("ReceiverId")
    @Expose
    private Integer receiverId;
    @SerializedName("MessageBody")
    @Expose
    private String messageBody;
    @SerializedName("IsRead")
    @Expose
    private Boolean isRead;
    @SerializedName("IsRemoved")
    @Expose
    private Boolean isRemoved;
    @SerializedName("SentOn")
    @Expose
    private String sentOn;
    @SerializedName("Identifier")
    @Expose
    private Object identifier;
    @SerializedName("Chat")
    @Expose
    private Chat chat;
    @SerializedName("Sender")
    @Expose
    private Sender sender;
    @SerializedName("Receiver")
    @Expose
    private Receiver receiver;
    @SerializedName("Messages")
    @Expose
    private Object messages;
    @SerializedName("MessageAttachments")
    @Expose
    private Object messageAttachments;

    public Integer getChatMessageId() {
        return chatMessageId;
    }

    public void setChatMessageId(Integer chatMessageId) {
        this.chatMessageId = chatMessageId;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Boolean getIsRemoved() {
        return isRemoved;
    }

    public void setIsRemoved(Boolean isRemoved) {
        this.isRemoved = isRemoved;
    }

    public String getSentOn() {
        return sentOn;
    }

    public void setSentOn(String sentOn) {
        this.sentOn = sentOn;
    }

    public Object getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Object identifier) {
        this.identifier = identifier;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public Object getMessages() {
        return messages;
    }

    public void setMessages(Object messages) {
        this.messages = messages;
    }

    public Object getMessageAttachments() {
        return messageAttachments;
    }

    public void setMessageAttachments(Object messageAttachments) {
        this.messageAttachments = messageAttachments;
    }

}
