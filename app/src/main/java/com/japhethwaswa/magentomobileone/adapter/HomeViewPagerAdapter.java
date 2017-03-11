package com.japhethwaswa.magentomobileone.adapter;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.app.HomeActivity;
import com.japhethwaswa.magentomobileone.databinding.FragmentHomePagerBinding;
import com.japhethwaswa.magentomobileone.databinding.HomeViewPagerItemBinding;
import com.japhethwaswa.magentomobileone.databinding.MainViewPagerItemBinding;
import com.japhethwaswa.magentomobileone.db.JumboContract;
import com.japhethwaswa.magentomobileone.fragment.HomeFragmentPager;
import com.japhethwaswa.magentomobileone.model.HomeData;
import com.japhethwaswa.magentomobileone.model.PreData;

import java.lang.ref.WeakReference;

public class HomeViewPagerAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private Cursor cursor;
    private FragmentHomePagerBinding fragmentHomePagerBinding;

    public HomeViewPagerAdapter(Cursor cursor, FragmentHomePagerBinding fragmentHomePagerBinding) {
        this.cursor = cursor;
        this.fragmentHomePagerBinding = fragmentHomePagerBinding;
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
        HomeViewPagerItemBinding itemViewBinding = DataBindingUtil.inflate(inflater, R.layout.home_view_pager_item, container, false);

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
        HomeData homeData = new HomeData();
        cursor.moveToPosition(position);


            homeData.setCategoryId(cursor.getString(cursor.getColumnIndex(JumboContract.MainEntry.COLUMN_CATEGORY_ID)));
            homeData.setProductId(cursor.getString(cursor.getColumnIndex(JumboContract.MainEntry.COLUMN_PRODUCT_ID)));
            homeData.setKeyHome(cursor.getString(cursor.getColumnIndex(JumboContract.MainEntry.COLUMN_KEY_HOME)));
            homeData.setSection(cursor.getString(cursor.getColumnIndex(JumboContract.MainEntry.COLUMN_SECTION)));
            homeData.setUpdatedAt(cursor.getString(cursor.getColumnIndex(JumboContract.MainEntry.COLUMN_UPDATED_AT)));
            homeData.setImageUrl(cursor.getString(cursor.getColumnIndex(JumboContract.MainEntry.COLUMN_IMAGE_URL)));

            itemViewBinding.setHomeData(homeData);
            container.addView(itemViewBinding.getRoot());
            return itemViewBinding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    public void setCursor(Cursor cursor,int initialPosition) {
        this.cursor = cursor;
        notifyDataSetChanged();

        //set the current item in the viewpager
        if (initialPosition != -1) {
            fragmentHomePagerBinding.homeViewPager.setCurrentItem(initialPosition);
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }
}
