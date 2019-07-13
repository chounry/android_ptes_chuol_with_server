package com.group6.choul.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.group6.choul.R;
import com.group6.choul.models.ImageModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SlideAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<ImageModel> images;

    public SlideAdapter(Context context,ArrayList<ImageModel> images){
        this.context = context;
        this.images = images;
    }
    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject( View view,  Object obj) {
        return view == obj;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_slide_layout,null);
        ImageView imageView = view.findViewById(R.id.imageView);
        Picasso.get().load(images.get(position).getImg()).into(imageView);

        ViewPager vp = (ViewPager) container;
        vp.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem( ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View)object;
        vp.removeView(view);
    }
}
