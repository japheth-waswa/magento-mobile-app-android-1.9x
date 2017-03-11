package com.japhethwaswa.magentomobileone.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.japhethwaswa.magentomobileone.BR;

public class HomeVars extends BaseObservable{
    private String sectionTwoOne;
    private String sectionTwoTwo;
    private String sectionThree;

    @Bindable
    public String getSectionTwoOne() {
        return sectionTwoOne;
    }

    public void setSectionTwoOne(String sectionTwoOne) {
        this.sectionTwoOne = sectionTwoOne;
        notifyPropertyChanged(BR.sectionTwoOne);
    }

    @Bindable
    public String getSectionTwoTwo() {
        return sectionTwoTwo;
    }

    public void setSectionTwoTwo(String sectionTwoTwo) {
        this.sectionTwoTwo = sectionTwoTwo;
        notifyPropertyChanged(BR.sectionTwoTwo);

    }

    @Bindable
    public String getSectionThree() {
        return sectionThree;
    }

    public void setSectionThree(String sectionThree) {
        this.sectionThree = sectionThree;
        notifyPropertyChanged(BR.sectionThree);
    }

}
