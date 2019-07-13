package com.group6.choul.adapters;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.group6.choul.fragments.HouseListFragment;
import com.group6.choul.fragments.RoomListFragment;
import com.group6.choul.fragments.SigninFragement;
import com.group6.choul.fragments.SignupFragement;

public class SignupAdapter extends FragmentPagerAdapter {

    int numTab;

    public SignupAdapter(FragmentManager fm, int numTab) {
        super(fm);
        this.numTab = numTab;
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                return new SigninFragement();
            case 1:
                return new SignupFragement();
        }
        return null;
    }

    @Override
    public int getCount() {
        return this.numTab;
    }
}

