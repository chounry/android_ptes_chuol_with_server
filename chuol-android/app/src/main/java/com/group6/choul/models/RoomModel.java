package com.group6.choul.models;

public class RoomModel {

    private String title, price, address, location, img_url;
    private int room_id,estate_id;
    private String text;
    public RoomModel( String title, String price, String address, String location, String img_url, int room_id,int estate_id,String text) {
        this.title = title;
        this.price = price;
        this.address = address;
        this.location = location;
        this.img_url = img_url;
        this.room_id = room_id;
        this.text = text;
        this.estate_id = estate_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public RoomModel() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getEstate_id() {
        return estate_id;
    }

    public void setEstate_id(int estate_id) {
        this.estate_id = estate_id;
    }
}
