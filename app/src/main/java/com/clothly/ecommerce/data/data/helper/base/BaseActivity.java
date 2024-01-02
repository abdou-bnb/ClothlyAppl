package com.clothly.ecommerce.data.data.helper.base;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;



/**
 * Abstract activity that every other Activity in this application must implement.
 */
public abstract class BaseActivity<V extends MvpView, P extends BasePresenter<V>>
        extends AppCompatActivity implements MvpView, View.OnClickListener {

    /**
     * LifecycleRegistry is an implementation of Lifecycle that can handle multiple observers.
     * It is used by Fragments and Support Library Activities.
     * You can also directly use it if you have a custom LifecycleOwner.
     */
    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    protected P presenter;

    private ViewDataBinding viewDataBinding;
    private Menu menu;
    private int mDefaultValue = -1;

    /**
     * its built in method in Fragment Activity
     * that is extends by AppCompatActivity
     *
     * @return
     */
    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    protected abstract int getLayoutId();

    protected int getToolbarId() {
        return mDefaultValue;
    }

    protected int getMenuId() {
        return mDefaultValue;
    }

    protected int statusBarColor() {
        return mDefaultValue;
    }

    @SuppressWarnings("unchecked")
    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        int layoutId = getLayoutId();
        if (layoutId > mDefaultValue) {

            try {
                viewDataBinding = DataBindingUtil.setContentView(this, layoutId);
            } catch (Exception e) {
                if (viewDataBinding == null) {
                    setContentView(layoutId);
                    //ButterKnife.bind(this);
                }
            }

            BaseViewModel<V, P> viewModel = ViewModelProviders.of(this).get(BaseViewModel.class);
            boolean isPresenterCreated = false;

            if (viewModel.getPresenter() == null) {
                viewModel.setPresenter(initPresenter());
                isPresenterCreated = true;
            }

            presenter = viewModel.getPresenter();
            presenter.attachLifecycle(getLifecycle());
            presenter.attachView((V) this);
            if (isPresenterCreated) {
                presenter.onPresenterCreated();
            }

            int toolbarId = getToolbarId();

         //   setStatusBarColor();

            if (toolbarId > mDefaultValue) {
                Toolbar toolbar = findViewById(toolbarId);

                if (toolbar != null) {
                    setSupportActionBar(toolbar);
                }

                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    actionBar.setDisplayShowHomeEnabled(true);
                }
            }
        }

        startUI();
    }

    /*FixMe: have to enable for on Resume state
    @Override
    protected void onResume() {
        super.onResume();
        onResumeUI();
    }
    protected abstract void onResumeUI();
    */

    protected abstract void startUI();

    protected abstract void stopUI();

    protected ViewDataBinding getViewDataBinding() {
        return viewDataBinding;
    }

    protected static void runCurrentActivity(Context context, Intent intent) {
        context.startActivity(intent);
    }

    private void setStatusBarColor() {

        int statusBarColor = statusBarColor();

        if (statusBarColor > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = this.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                }
                window.setStatusBarColor(ContextCompat.getColor(this, statusBarColor));
            }
        }
    }

    @Override
    public void onClick(View view) {

    }

    protected Menu getMenu() {
        return menu;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getMenuId() > mDefaultValue) {
            getMenuInflater().inflate(getMenuId(), menu);
            this.menu = menu;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void refreshMenu() {
        supportInvalidateOptionsMenu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stopUI();
        presenter.detachLifecycle(getLifecycle());
        presenter.detachView();
    }

    protected abstract P initPresenter();

    protected void setTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void setClickListener(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

  /*  protected void setAnimation(View... views) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.grow_effect);

        for (View view : views) {
            view.startAnimation(animation);
        }
    }
*/
    protected void setToolbarText(String title, String subtitle) {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setSubtitle(subtitle);
        }
    }

    protected void setSubtitle(String subtitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(subtitle);
        }
    }

    //FixMe: have to enable for commit Fragment in Activity
    protected BaseFragment mBaseCurrentFragment;

    protected void commitFragment(int parentId, BaseFragment baseFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(parentId, baseFragment, baseFragment.getClass().getName())
                .commit();

        setCurrentFragment(baseFragment);
    }

    protected void setCurrentFragment(BaseFragment baseFragment) {
        this.mBaseCurrentFragment = baseFragment;
    }

    protected BaseFragment getBaseCurrentFragment() {
        return mBaseCurrentFragment;
    }
}
