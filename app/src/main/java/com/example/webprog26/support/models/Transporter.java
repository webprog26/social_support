package com.example.webprog26.support.models;

import android.app.Application;

/**
 * Created by webprog26 on 08.03.2016.
 */
public class Transporter extends Application {

    private Object mObject;

    private static Transporter instance;

    public static Transporter newInstance(){
        if(instance == null)
            instance = new Transporter();
        return instance;
    }


    public Object getObject() {
        return mObject;
    }

    public void setObject(Object object) {
        mObject = object;
    }
}
