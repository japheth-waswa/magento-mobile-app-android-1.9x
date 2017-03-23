package com.japhethwaswa.magentomobileone.custombindings;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.japhethwaswa.magentomobileone.R;


public class CustomPhotoViewBindingAdapter {

    @BindingAdapter("imagePhotoViewUrl")
    public static void setPhoto(ImageView view,String imageUrl){
        Glide
                .with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_loader)
                .error(R.drawable.ic_error_img)
                .into(view);
    }
    @BindingAdapter({"imagePhotoViewUrl","imagePlaceholder"})
    public static void setPhoto(ImageView view,String imageUrl,int imagePlaceholder){
        Glide
                .with(view.getContext())
                .load(imageUrl)
                .placeholder(imagePlaceholder)
                .error(R.drawable.ic_broken_image_black_48dp)
                .into(view);
    }
    @BindingAdapter({"imagePhotoViewUrl","imagePlaceholder","imageError"})
    public static void setPhoto(ImageView view,String imageUrl,int imagePlaceholder,int imageError){
        Glide
                .with(view.getContext())
                .load(imageUrl)
                .placeholder(imagePlaceholder)
                .error(imageError)
                .into(view);
    }

}
