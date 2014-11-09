package com.astra.app.factograph.m_fact;

import android.app.Application;

/**
 * Created by teodor on 31.10.2014.
 */
public class MyGlobalSigns extends Application {

    private String Rights="";

    public String getRights(){
        return Rights;
    }
    public void setRights(String s){
        Rights = s;
    }
}