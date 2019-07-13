package com.group6.choul.models;


import com.squareup.moshi.Json;

public class MessageModel {

    @Json(name = "content")
    String message;
    @Json(name="from_self_user_id")
    int from_self_user_id;
    @Json(name="to_user_user_id")
    int to_user_user_id;
    private MemberDataForChat memberData;
    private boolean belongsToCurrentUser;

    public MessageModel(){}

    public MessageModel(String message, boolean belongsToCurrentUser, MemberDataForChat memberData) {
        this.message = message;
        this.belongsToCurrentUser = belongsToCurrentUser;
        this.memberData = memberData;
    }

    public MemberDataForChat getMemberData() {
        return memberData;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }

    public void setBelongsToCurrentUser(int loginUserId) {
        belongsToCurrentUser = false;
        if(from_self_user_id == loginUserId){
            belongsToCurrentUser = true;
        }
    }

    public void setMemberData(MemberDataForChat memberData) {
        this.memberData = memberData;
    }

    public int getFrom_self_user_id() {
        return from_self_user_id;
    }

    public int getTo_user_user_id() {
        return to_user_user_id;
    }

    public int getToUserId(int myUesrId){
        if(from_self_user_id == myUesrId)
            return to_user_user_id;
        return from_self_user_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
