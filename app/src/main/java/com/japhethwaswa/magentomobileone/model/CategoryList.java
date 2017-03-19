package com.japhethwaswa.magentomobileone.model;

import android.databinding.ObservableArrayList;

import java.io.Serializable;

public class CategoryList implements Serializable{
    public final ObservableArrayList<Category> ItemList;

    public CategoryList() {
        ItemList = new ObservableArrayList<>();
    }
    public CategoryList(ObservableArrayList<Category> itemList) {
        ItemList = itemList;
    }
}
