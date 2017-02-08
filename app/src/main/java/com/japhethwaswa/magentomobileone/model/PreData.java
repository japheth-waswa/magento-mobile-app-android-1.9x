package com.japhethwaswa.magentomobileone.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Japheth Waswa on 2/7/2017.
 */

public class PreData {

    private String imageUrl;
    private String title;
    private String briefDescription;

    public PreData(){}

    public PreData(String imageUrl,String title,String briefDescription){

        this.imageUrl = imageUrl;
        this.title = title;
        this.briefDescription = briefDescription;

    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

}
