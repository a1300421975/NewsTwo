package com.example.tkw.newstwo.Adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    List<View> relativeLayoutList;

    public ViewPagerAdapter(List<View> relativeLayoutList){
        this.relativeLayoutList = relativeLayoutList;
    }

    @Override
    public int getCount() {
        return relativeLayoutList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
        return arg0 == arg1;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(relativeLayoutList.get(position));
        return relativeLayoutList.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(relativeLayoutList.get(position));
    }
}
