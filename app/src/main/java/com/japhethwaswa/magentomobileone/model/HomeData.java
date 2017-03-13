package com.japhethwaswa.magentomobileone.model;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;


public class HomeData extends BaseObservable{
    private String categoryId;
    private String productId;
    private String imageUrl;
    private String keyHome;
    private String section;
    private String updatedAt;
    private String title;

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }


    @Bindable
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {

        this.categoryId = categoryId;
        notifyPropertyChanged(BR.categoryId);
    }

    @Bindable
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
        notifyPropertyChanged(BR.productId);
    }

    @Bindable
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        notifyPropertyChanged(BR.imageUrl);
    }

    @Bindable
    public String getKeyHome() {
        return keyHome;
    }

    public void setKeyHome(String keyHome) {
        this.keyHome = keyHome;
        notifyPropertyChanged(BR.keyHome);
    }

    @Bindable
    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
        notifyPropertyChanged(BR.section);
    }

    @Bindable
    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        notifyPropertyChanged(BR.updatedAt);
    }

}
