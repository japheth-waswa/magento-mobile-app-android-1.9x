package com.japhethwaswa.magentomobileone.model;

/**
 * Created by web0002 on 2/7/2017.
 */

public class PreData {

    private String imageSrc;
    private int imageSrcInt;
    private String title;
    private String briefDescription;

    public PreData(){}

    public PreData(int imageSrcInt,String title,String briefDescription){
        this.imageSrcInt = imageSrcInt;
        this.title = title;
        this.briefDescription = briefDescription;
    }

    public int getImageSrcInt() {
        return imageSrcInt;
    }

    public void setImageSrcInt(int imageSrcInt) {
        this.imageSrcInt = imageSrcInt;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
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
