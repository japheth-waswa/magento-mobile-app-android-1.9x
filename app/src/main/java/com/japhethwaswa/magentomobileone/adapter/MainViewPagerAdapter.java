package com.japhethwaswa.magentomobileone.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.japhethwaswa.magentomobileone.R;
import com.japhethwaswa.magentomobileone.model.PreData;

import java.util.List;

/**
 * Created by web0002 on 2/7/2017.
 */

public class MainViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<PreData> preDataList;
    private LayoutInflater inflater;

    public MainViewPagerAdapter(Context context, List<PreData> preDataList) {
        this.context = context;
        this.preDataList = preDataList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return preDataList.size();
    }

    @Override //checks whether view is associated with object or object is associated with page view or not.
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        //get the view of the single view pager item.
        View itemView = inflater.inflate(R.layout.main_view_pager_item,container,false);

        ImageView imageView =  (ImageView) itemView.findViewById(R.id.main_view_pager_image);
        TextView textViewTitle = (TextView) itemView.findViewById(R.id.main_view_pager_title);
        TextView textViewDescription =  (TextView) itemView.findViewById(R.id.main_view_pager_description);

        PreData preData = preDataList.get(position);
        imageView.setImageResource(preData.getImageSrcInt());
        textViewTitle.setText(preData.getTitle());
        textViewDescription.setText(preData.getBriefDescription());

        //add main_view_pager_item.xml to viewpager
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}
