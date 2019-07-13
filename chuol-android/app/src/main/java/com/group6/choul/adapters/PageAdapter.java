package com.group6.choul.adapters;





import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.group6.choul.fragments.HouseListFragment;
import com.group6.choul.fragments.RoomListFragment;

public class PageAdapter extends FragmentPagerAdapter {

    int numTab;

    public PageAdapter(FragmentManager fm, int numTab) {
        super(fm);
        this.numTab = numTab;
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                return new HouseListFragment();
            case 1:
                return new RoomListFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return this.numTab;
    }
}
