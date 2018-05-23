
package com.domain.mystream.Model;

import android.util.Log;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageModel {

    @SerializedName("ChatId")
    @Expose
    private Integer chatId;
    @SerializedName("CreatedOnDate")
    @Expose
    private String createdOnDate;
    @SerializedName("CreatedByUserId")
    @Expose
    private Integer createdByUserId;
    @SerializedName("LastUpdatedOnDate")
    @Expose
    private String lastUpdatedOnDate;
    @SerializedName("LastUpdatedByUserId")
    @Expose
    private Integer lastUpdatedByUserId;
    @SerializedName("ChatTypeId")
    @Expose
    private Integer chatTypeId;
    @SerializedName("Identifier")
    @Expose
    private String identifier;
    @SerializedName("Participants")
    @Expose
    private List<Participant> participants = null;
    @SerializedName("ChatType")
    @Expose
    private ChatType chatType;
    @SerializedName("LastChatMessage")
    @Expose
    private LastChatMessage lastChatMessage;
    @SerializedName("IsRemoved")
    @Expose
    private Boolean isRemoved;

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public String getCreatedOnDate() {
        return createdOnDate;
    }

    public void setCreatedOnDate(String createdOnDate) {
        this.createdOnDate = createdOnDate;
    }

    public Integer getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Integer createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getLastUpdatedOnDate() {
        return lastUpdatedOnDate;
    }

    public void setLastUpdatedOnDate(String lastUpdatedOnDate) {
        this.lastUpdatedOnDate = lastUpdatedOnDate;
    }

    public Integer getLastUpdatedByUserId() {
        return lastUpdatedByUserId;
    }

    public void setLastUpdatedByUserId(Integer lastUpdatedByUserId) {
        this.lastUpdatedByUserId = lastUpdatedByUserId;
    }

    public Integer getChatTypeId() {
        return chatTypeId;
    }

    public void setChatTypeId(Integer chatTypeId) {
        this.chatTypeId = chatTypeId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<Participant> getParticipants() {
        Log.e("val",participants+"");
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
    }

    public LastChatMessage getLastChatMessage() {
        return lastChatMessage;
    }

    public void setLastChatMessage(LastChatMessage lastChatMessage) {
        this.lastChatMessage = lastChatMessage;
    }

    public Boolean getIsRemoved() {
        return isRemoved;
    }

    public void setIsRemoved(Boolean isRemoved) {
        this.isRemoved = isRemoved;
    }

}
