package com.plantlogger.models;

import com.google.gson.Gson;

import java.io.Serializable;

public class Base implements Serializable
{
    public String toJson()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
    }
}