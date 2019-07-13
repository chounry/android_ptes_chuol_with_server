package com.group6.choul.models;

public class HouseModel {
    private String title, price, address, location, type,img_url, for_sale_rent_status;
    private int estate_id;
    private String text;
    public HouseModel(){

    }

    public HouseModel(String title, String price, String address, String location, String type, String img_url, String sale_rent_status,String text) {
        this.title = title;
        this.price = price;
        this.address = address;
        this.location = location;
        this.type = type;
        this.img_url = img_url;
        this.text = text;
        this.for_sale_rent_status = sale_rent_status;

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getEstate_id() {
        return estate_id;
    }

    public void setEstaeId(int estate_id) {
        this.estate_id = estate_id;
    }

    public String getFor_sale_rent_status() {
        return for_sale_rent_status;
    }

    public void setFor_sale_rent_status(String for_sale_rent_status) {
        this.for_sale_rent_status = for_sale_rent_status;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
