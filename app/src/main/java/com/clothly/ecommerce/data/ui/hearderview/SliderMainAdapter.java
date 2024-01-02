package com.clothly.ecommerce.data.ui.hearderview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.clothly.ecommerce.data.data.helper.models.SliderMain;

import java.util.ArrayList;
import java.util.List;

public class SliderMainAdapter extends FragmentStatePagerAdapter {

    List<SliderMain> sliderMainList = new ArrayList<>();

    public SliderMainAdapter(FragmentManager fm, List<SliderMain> mList) {
        super(fm);
        this.sliderMainList = mList;
    }

    @Override
    public Fragment getItem(int position) {
        return SliderMainItemFragment.newInstance(sliderMainList.get(position), position);
    }

    @Override
    public int getCount() {
        return sliderMainList.size();
    }

}
