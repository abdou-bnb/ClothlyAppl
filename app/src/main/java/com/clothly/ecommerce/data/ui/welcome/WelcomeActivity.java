package com.clothly.ecommerce.data.ui.welcome;


import android.content.Intent;
import android.os.Handler;

import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.BaseActivity;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.ui.main.MainActivity;

public class WelcomeActivity extends BaseActivity<WelcomeMvpView, WelcomePresenter> {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void startUI() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));

                finish();
            }
        }, Constants.DefaultValue.DELAY_INTERVAL);
    }

    @Override
    protected void stopUI() {

    }

    @Override
    protected WelcomePresenter initPresenter() {
        return new WelcomePresenter();
    }
}
