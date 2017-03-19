package com.japhethwaswa.magentomobileone.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableInt;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.databinding.SpinnerCategoryListItemBinding;
import com.japhethwaswa.magentomobileone.model.Category;

public class SpinnerCategoryListAdapter extends BaseAdapter implements ListAdapter,SpinnerAdapter{

    public ObservableArrayList<Category> list;
    private ObservableInt position = new ObservableInt();
    private LayoutInflater layoutInflater;

    public SpinnerCategoryListAdapter(){
        list = new ObservableArrayList<>();
    }

    public SpinnerCategoryListAdapter(ObservableArrayList<Category> itemList) {
        list = itemList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        int id = Integer.valueOf(list.get(position).getEntity_id());
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(layoutInflater == null){
            layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        SpinnerCategoryListItemBinding spinnerCategoryListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.spinner_category_list_item,parent,false);
        spinnerCategoryListItemBinding.setCategory(list.get(position));

        return spinnerCategoryListItemBinding.getRoot();
    }

    //for the spinner
    public int getPosition(Spinner spinner){
        return spinner.getSelectedItemPosition();
    }

    public int getPosition(){
        return position.get();
    }

    public void setPosition(int position){
        this.position.set(position);
    }
}
