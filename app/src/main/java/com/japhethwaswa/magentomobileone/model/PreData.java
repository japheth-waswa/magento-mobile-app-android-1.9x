package com.japhethwaswa.magentomobileone.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import java.io.Serializable;


public class PreData extends BaseObservable  implements Serializable{

    private String imageUrl;
    private String title;
    private String briefDescription;

    /**public PreData(){}

    public PreData(String imageUrl,String title,String briefDescription){

        this.imageUrl = imageUrl;
        this.title = title;
        this.briefDescription = briefDescription;

    }
**/

    @Bindable
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        notifyPropertyChanged(BR.imageUrl);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getBriefDescription() {
        return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
        notifyPropertyChanged(BR.briefDescription);
    }

}
