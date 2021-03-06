package com.japhethwaswa.magentomobileone.adapter;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.app.MainActivity;
import com.japhethwaswa.magentomobileone.databinding.MainViewPagerItemBinding;
import com.japhethwaswa.magentomobileone.db.JumboContract;
import com.japhethwaswa.magentomobileone.model.PreData;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Japheth Waswa on 2/7/2017.
 * me-no*public class MainViewPagerAdapter extends PagerAdapter {
 * <p>
 * //private WeakReference<MainActivity> mainActivityWeakReference;
 * private List<PreData> preDataList;
 * private LayoutInflater inflater;
 * <p>
 * <p>
 * public MainViewPagerAdapter(List<PreData> preDataList) {
 * /**this.mainActivityWeakReference = new WeakReference<>(mainActivity);
 * final MainActivity mainActivity  = mainActivityWeakReference.get();me-no**this.preDataList = preDataList;
 * }
 *
 * @Override public int getCount() {
 * return preDataList.size();
 * }
 * @Override //checks whether view is associated with object or object is associated with page view or not.
 * public boolean isViewFromObject(View view, Object object) {
 * return view == object;
 * }
 * @Override public Object instantiateItem(ViewGroup container, int position) {
 * <p>
 * //get the view of the single view pager item.
 * //View itemView = inflater.inflate(R.layout.main_view_pager_item,container,false);
 * Context context = container.getContext();
 * inflater = LayoutInflater.from(context);
 * MainViewPagerItemBinding itemViewBinding = DataBindingUtil.inflate(inflater,R.layout.main_view_pager_item,container,false);
 * <p>
 * /**ImageView imageView =  (ImageView) itemView.findViewById(R.id.main_view_pager_image);
 * TextView textViewTitle = (TextView) itemView.findViewById(R.id.main_view_pager_title);
 * TextView textViewDescription =  (TextView) itemView.findViewById(R.id.main_view_pager_description);me-no** PreData preData = preDataList.get(position);
 * /**Glide
 * .with(context)
 * .load("http://i2.cdn.cnn.com/cnnnext/dam/assets/160614121003-08-instant-vacation-restricted-super-169.jpg")
 * .into(imageView);imageView.setImageResource(preData.getImageSrcInt());
 * textViewTitle.setText(preData.getTitle());
 * textViewDescription.setText(preData.getBriefDescription());
 **/

/**me-no*public class MainViewPagerAdapter extends PagerAdapter {

 //private WeakReference<MainActivity> mainActivityWeakReference;
 private List<PreData> preDataList;
 private LayoutInflater inflater;


 public MainViewPagerAdapter(List<PreData> preDataList) {
 /**this.mainActivityWeakReference = new WeakReference<>(mainActivity);
 final MainActivity mainActivity  = mainActivityWeakReference.get();**/
/**me-no**this.preDataList = preDataList;
 }

 @Override public int getCount() {
 return preDataList.size();
 }

 @Override //checks whether view is associated with object or object is associated with page view or not.
 public boolean isViewFromObject(View view, Object object) {
 return view == object;
 }

 @Override public Object instantiateItem(ViewGroup container, int position) {

 //get the view of the single view pager item.
 //View itemView = inflater.inflate(R.layout.main_view_pager_item,container,false);
 Context context = container.getContext();
 inflater = LayoutInflater.from(context);
 MainViewPagerItemBinding itemViewBinding = DataBindingUtil.inflate(inflater,R.layout.main_view_pager_item,container,false);

 /**ImageView imageView =  (ImageView) itemView.findViewById(R.id.main_view_pager_image);
 TextView textViewTitle = (TextView) itemView.findViewById(R.id.main_view_pager_title);
 TextView textViewDescription =  (TextView) itemView.findViewById(R.id.main_view_pager_description);**/

/**me-no** PreData preData = preDataList.get(position);
 /**Glide
 .with(context)
 .load("http://i2.cdn.cnn.com/cnnnext/dam/assets/160614121003-08-instant-vacation-restricted-super-169.jpg")
 .into(imageView);**/
/**imageView.setImageResource(preData.getImageSrcInt());
 textViewTitle.setText(preData.getTitle());
 textViewDescription.setText(preData.getBriefDescription());**/

//add main_view_pager_item.xml to viewpager

/**me-no**itemViewBinding.setPreData(preData);
 container.addView(itemViewBinding.getRoot());

 return itemViewBinding.getRoot();
 }

 @Override public void destroyItem(ViewGroup container, int position, Object object) {
 }

 //my impl
 public void updatePagerItems(List<PreData> preDataList){
 this.preDataList = preDataList;
 this.notifyDataSetChanged();
 }

 /**@Override
 public int getItemPosition(Object object) {
 //recreates all views (inefficient for large num of views ie 10>)
 return POSITION_NONE;
 }***/
//me-no//}
public class MainViewPagerAdapter extends PagerAdapter {

    //private WeakReference<MainActivity> mainActivityWeakReference;
    //private List<PreData> preDataList;
    private LayoutInflater inflater;
    private Cursor cursor;


    public MainViewPagerAdapter(Cursor cursor) {
        /**this.mainActivityWeakReference = new WeakReference<>(mainActivity);
         final MainActivity mainActivity  = mainActivityWeakReference.get();**/
        //this.preDataList = preDataList;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return (cursor == null) ? 0 : cursor.getCount();
    }

    @Override
    //checks whether view is associated with object or object is associated with page view or not.
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        //get the view of the single view pager item.
        //View itemView = inflater.inflate(R.layout.main_view_pager_item,container,false);
        Context context = container.getContext();
        inflater = LayoutInflater.from(context);
        MainViewPagerItemBinding itemViewBinding = DataBindingUtil.inflate(inflater, R.layout.main_view_pager_item, container, false);

        /**ImageView imageView =  (ImageView) itemView.findViewById(R.id.main_view_pager_image);
         TextView textViewTitle = (TextView) itemView.findViewById(R.id.main_view_pager_title);
         TextView textViewDescription =  (TextView) itemView.findViewById(R.id.main_view_pager_description);**/

        //PreData preData = preDataList.get(position);
        /**Glide
         .with(context)
         .load("http://i2.cdn.cnn.com/cnnnext/dam/assets/160614121003-08-instant-vacation-restricted-super-169.jpg")
         .into(imageView);**/
        /**imageView.setImageResource(preData.getImageSrcInt());
         textViewTitle.setText(preData.getTitle());
         textViewDescription.setText(preData.getBriefDescription());**/

        //add main_view_pager_item.xml to viewpager
        PreData preData = new PreData();
        cursor.moveToPosition(position);
        preData.setTitle(cursor.getString(cursor.getColumnIndex(JumboContract.PagerEntry.COLUMN_TITLE)));
        preData.setBriefDescription(cursor.getString(cursor.getColumnIndex(JumboContract.PagerEntry.COLUMN_BRIEF_DESCRIPTION)));

        String localUrl = cursor.getString(cursor.getColumnIndex(JumboContract.PagerEntry.COLUMN_IMAGE_URL_LOCAL));
        String remoteUrl = cursor.getString(cursor.getColumnIndex(JumboContract.PagerEntry.COLUMN_IMAGE_URL_REMOTE));

        String imageUrl = localUrl;
        if (localUrl == null) {
            imageUrl = remoteUrl;
        }
        preData.setImageUrl(imageUrl);

        itemViewBinding.setPreData(preData);
        container.addView(itemViewBinding.getRoot());

        return itemViewBinding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    //my impl
    /**public void updatePagerItems(List<PreData> preDataList){
     this.preDataList = preDataList;
     this.notifyDataSetChanged();
     }

     @Override public int getItemPosition(Object object) {
     //recreates all views (inefficient for large num of views ie 10>)
     return POSITION_NONE;
     }***/
}
