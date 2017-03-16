package com.japhethwaswa.magentomobileone.model;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.japhethwaswa.magentomobileone.BR;


public class Category extends BaseObservable{
    private String label;
    private String entity_id;
    private String content_type;
    private String parent_id;
    private String my_parent_id;
    private String icon;
    private String modification_time;

    @Bindable
    public String getModification_time() {
        return modification_time;
    }

    public void setModification_time(String modification_time) {
        this.modification_time = modification_time;
        notifyPropertyChanged(BR.modification_time);
    }

    @Bindable
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        notifyPropertyChanged(BR.label);
    }

    @Bindable
    public String getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id;
        notifyPropertyChanged(BR.entity_id);
    }

    @Bindable
    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
        notifyPropertyChanged(BR.content_type);
    }

    @Bindable
    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
        notifyPropertyChanged(BR.parent_id);
    }

    @Bindable
    public String getMy_parent_id() {
        return my_parent_id;
    }

    public void setMy_parent_id(String my_parent_id) {
        this.my_parent_id = my_parent_id;
        notifyPropertyChanged(BR.parent_id);
    }

    @Bindable
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
        notifyPropertyChanged(BR.icon);
    }

}
