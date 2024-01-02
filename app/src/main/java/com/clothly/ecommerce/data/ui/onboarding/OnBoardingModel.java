package com.clothly.ecommerce.data.ui.onboarding;


import com.clothly.ecommerce.data.R;

public enum OnBoardingModel {
    RED(R.string.red, R.layout.layout_onboarding_discover),
    BLUE(R.string.blue, R.layout.layout_onboarding_payment),
    GREEN(R.string.green, R.layout.layout_onboarding_shopping);

    private int mTitleResId;
    private int mLayoutResId;

    OnBoardingModel(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
