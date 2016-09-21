package com.example.webprog26.support.models;

import android.content.Context;

import com.example.webprog26.support.providers.FamilyClientProvider;
import com.example.webprog26.support.providers.PersonClientProvider;

/**
 * Created by webprog26 on 15.03.2016.
 */
public class Photo {

    private String mFileName;

    public Photo(String mFileName) {
        this.mFileName = mFileName;
    }

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String mFileName) {
        this.mFileName = mFileName;
    }
}
