package com.clothly.ecommerce.data.ui.onboarding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class OnBoardingPagerAdapter extends android.support.v4.view.PagerAdapter {

    private Context mContext;

    public OnBoardingPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        OnBoardingModel model = OnBoardingModel.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(model.getLayoutResId(), collection, false);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return OnBoardingModel.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        OnBoardingModel customPagerEnum = OnBoardingModel.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }
}
