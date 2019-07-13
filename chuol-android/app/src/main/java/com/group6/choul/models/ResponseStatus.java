package com.group6.choul.models;


import com.squareup.moshi.Json;

public class ResponseStatus {
    @Json(name = "status")
    String status;

    public String getStatus() {
        return status;
    }
}
