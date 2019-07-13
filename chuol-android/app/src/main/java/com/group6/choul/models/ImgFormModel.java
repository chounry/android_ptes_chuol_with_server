package com.group6.choul.models;

import android.graphics.Bitmap;

public class ImgFormModel {
    private Bitmap img;

    public ImgFormModel(Bitmap imgStr) {
        this.img = imgStr;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap imgStr) {
        this.img = imgStr;
    }
}
