package com.japhethwaswa.magentomobileone.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.japhethwaswa.magentomobileone.BR;

public class Product extends BaseObservable{
    private String entity_id;
    private String name;
    private String entity_type;
    private String short_description;
    private String description;
    private String link;
    private String icon;
    private String modification_time;
    private String in_stock;
    private String is_salable;
    private String has_gallery;
    private String has_options;
    private String rating_summary;
    private String reviews_count;
    private String price_regular;
    private String price_special;
    private String category_ids;


    @Bindable
    public String getModification_time() {
        return modification_time;
    }

    public void setModification_time(String modification_time) {
        this.modification_time = modification_time;
        notifyPropertyChanged(BR.modification_time);
    }

    @Bindable
    public String getCategory_ids() {
        return category_ids;
    }

    public void setCategory_ids(String category_ids) {
        this.category_ids = category_ids;
        notifyPropertyChanged(BR.category_ids);
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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getEntity_type() {
        return entity_type;
    }

    public void setEntity_type(String entity_type) {
        this.entity_type = entity_type;
        notifyPropertyChanged(BR.entity_type);
    }

    @Bindable
    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
        notifyPropertyChanged(BR.short_description);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
        notifyPropertyChanged(BR.link);
    }

    @Bindable
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
        notifyPropertyChanged(BR.icon);
    }

    @Bindable
    public String getIn_stock() {
        return in_stock;
    }

    public void setIn_stock(String in_stock) {
        this.in_stock = in_stock;
        notifyPropertyChanged(BR.in_stock);
    }

    @Bindable
    public String getIs_salable() {
        return is_salable;
    }

    public void setIs_salable(String is_salable) {
        this.is_salable = is_salable;
        notifyPropertyChanged(BR.is_salable);
    }

    @Bindable
    public String getHas_gallery() {
        return has_gallery;
    }

    public void setHas_gallery(String has_gallery) {
        this.has_gallery = has_gallery;
        notifyPropertyChanged(BR.has_gallery);
    }

    @Bindable
    public String getHas_options() {
        return has_options;
    }

    public void setHas_options(String has_options) {
        this.has_options = has_options;
        notifyPropertyChanged(BR.has_options);
    }

    @Bindable
    public String getRating_summary() {
        return rating_summary;
    }

    public void setRating_summary(String rating_summary) {
        this.rating_summary = rating_summary;
        notifyPropertyChanged(BR.rating_summary);
    }

    @Bindable
    public String getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(String reviews_count) {
        this.reviews_count = reviews_count;
        notifyPropertyChanged(BR.reviews_count);
    }

    @Bindable
    public String getPrice_regular() {
        return price_regular;
    }

    public void setPrice_regular(String price_regular) {
        this.price_regular = price_regular;
        notifyPropertyChanged(BR.price_regular);
    }

    @Bindable
    public String getPrice_special() {
        return price_special;
    }


    public void setPrice_special(String price_special) {
        this.price_special = price_special;
        notifyPropertyChanged(BR.price_special);
    }

}
