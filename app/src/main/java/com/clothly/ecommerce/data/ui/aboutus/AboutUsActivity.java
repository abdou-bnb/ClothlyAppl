package com.clothly.ecommerce.data.ui.aboutus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.BaseActivity;

public class AboutUsActivity extends BaseActivity<AboutUsMvpView,AboutUsPresenter> implements AboutUsMvpView {

    /**
     * Run Activity
     * @param context mActivity
     */
    public static void runActivity(Context context) {
        Intent intent = new Intent(context, AboutUsActivity.class);
        runCurrentActivity(context, intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void startUI() {

        initToolbar();
    }


    private void initToolbar() {
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(this.getString(R.string.title_about_us));
        toobarTitle.setText("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    protected void stopUI() {

    }

    @Override
    protected AboutUsPresenter initPresenter() {
        return new AboutUsPresenter();
    }
}
