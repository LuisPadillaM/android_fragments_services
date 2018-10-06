package com.example.luispadilla.handlerservices.ui.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.luispadilla.handlerservices.ui.Fragments.FragmentBattery;
import com.example.luispadilla.handlerservices.ui.Fragments.FragmentLocation;
import com.example.luispadilla.handlerservices.ui.Fragments.FragmentWifi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter{

    FragmentBattery fragmentBattery;
    FragmentLocation fragmentLocation;
    public FragmentWifi fragmentWifi;
    List<Fragment> appFragments;
    List<String> titles = Arrays.asList("Battery", "Location", "Wifi");

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentBattery = new FragmentBattery();
        fragmentLocation = new FragmentLocation();
        fragmentWifi = new FragmentWifi();
        appFragments = Arrays.asList(fragmentBattery, fragmentLocation, fragmentWifi);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment current = appFragments.get(0); // default fragment;
        switch(position){
            case 0:
                current = appFragments.get(0);
                break;
            case 1:
                current = appFragments.get(1);
                break;
            case 2:
                current = appFragments.get(2); // wifi
                break;
        }
        return current;

    }

    @Override
    public int getCount() {
        return appFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title = titles.get(0);
        switch(position){
            case 0:
                title = titles.get(0);
                break;
            case 1:
                title = titles.get(1);
                break;
            case 2:
                title = titles.get(2);
                break;
        }
        return title;
    }

}
