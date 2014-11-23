package com.astra.app.factograph.m_fact;

/**
 * Created by teodor on 23.11.2014.
 */
public class Item {

    private String title;
    private String description;

    public Item(String title, String description) {
        super();
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
    // getters and setters...
}