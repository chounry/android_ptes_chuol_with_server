package com.group6.choul.models;


import com.squareup.moshi.Json;

public class MemberDataForChat {
    private String name;
    private String img;
    private int user_id;

    public MemberDataForChat(String name, String img,int user_id) {
        this.name = name;
        this.img = img;
        this.user_id = user_id;
    }

    // Add an empty constructor so we can later parse JSON into MemberData using Jackson
    public MemberDataForChat() {
    }

    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }

    public int getUser_id() {
        return user_id;
    }
}
