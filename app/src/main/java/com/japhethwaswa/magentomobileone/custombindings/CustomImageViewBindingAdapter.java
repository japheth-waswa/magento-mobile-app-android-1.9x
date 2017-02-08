package com.japhethwaswa.magentomobileone.custombindings;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.japhethwaswa.magentomobileone.R;

/**
 * Created by web0002 on 2/8/2017.
 */

public class CustomImageViewBindingAdapter {

    @BindingAdapter("imageUrl")
    public static void setImage(ImageView view,String imageUrl){
        Glide
                .with(view.getContext())
                .load(imageUrl)
                .error(R.drawable.ic_broken_image_black_48dp)
                .into(view);
    }

}
