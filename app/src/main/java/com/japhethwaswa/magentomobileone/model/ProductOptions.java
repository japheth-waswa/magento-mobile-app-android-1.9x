package com.japhethwaswa.magentomobileone.model;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.japhethwaswa.magentomobileone.BR;


public class ProductOptions extends BaseObservable{
    private String entity_id;
    private String is_parent;
    private String parent_code;
    private String parent_label;
    private String parent_required;
    private String parent_type;
    private String child_code;
    private String child_label;
    private String child_to_code;

    @Bindable
    public String getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id;
        notifyPropertyChanged(BR.entity_id);
    }

    @Bindable
    public String getIs_parent() {
        return is_parent;
    }

    public void setIs_parent(String is_parent) {
        this.is_parent = is_parent;
        notifyPropertyChanged(BR.is_parent);
    }

    @Bindable
    public String getParent_code() {
        return parent_code;
    }

    public void setParent_code(String parent_code) {
        this.parent_code = parent_code;
        notifyPropertyChanged(BR.parent_code);
    }

    @Bindable
    public String getParent_label() {
        return parent_label;
    }

    public void setParent_label(String parent_label) {
        this.parent_label = parent_label;
        notifyPropertyChanged(BR.parent_label);
    }

    @Bindable
    public String getParent_required() {
        return parent_required;
    }

    public void setParent_required(String parent_required) {
        this.parent_required = parent_required;
        notifyPropertyChanged(BR.parent_required);
    }

    @Bindable
    public String getParent_type() {
        return parent_type;
    }

    public void setParent_type(String parent_type) {
        this.parent_type = parent_type;
        notifyPropertyChanged(BR.parent_type);
    }

    @Bindable
    public String getChild_code() {
        return child_code;
    }

    public void setChild_code(String child_code) {
        this.child_code = child_code;
        notifyPropertyChanged(BR.child_code);
    }

    @Bindable
    public String getChild_label() {
        return child_label;
    }

    public void setChild_label(String child_label) {
        this.child_label = child_label;
        notifyPropertyChanged(BR.child_label);
    }

    @Bindable
    public String getChild_to_code() {
        return child_to_code;
    }

    public void setChild_to_code(String child_to_code) {
        this.child_to_code = child_to_code;
        notifyPropertyChanged(BR.child_to_code);
    }



}
