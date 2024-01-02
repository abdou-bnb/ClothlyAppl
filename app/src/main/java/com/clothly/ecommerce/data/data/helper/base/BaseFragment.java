package com.clothly.ecommerce.data.data.helper.base;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;


/*
 *  ****************************************************************************
 *  * Created by : Sudipta K Paik on 26-Aug-17 at 4:02 PM.
 *  * Email : sudipta@w3engineers.com
 *  *
 *  * Responsibility: Abstract fragment that every other fragment in this application must implement.
 *  *
 *  * Last edited by : Sudipta on 02-11-17.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */
public abstract class BaseFragment<V extends MvpView, P extends BasePresenter<V>>
        extends Fragment implements MvpView, View.OnClickListener {

    /**
     * LifecycleRegistry is an implementation of Lifecycle that can handle multiple observers.
     * It is used by Fragments and Support Library Activities.
     * You can also directly use it if you have a custom LifecycleOwner.
     */
    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    protected P presenter;
    private int mDefaultValue = -1;
    private ViewDataBinding viewDataBinding;
    private View rootView;

    protected abstract int getLayoutId();

    protected int getMenuId() {
        return mDefaultValue;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getMenuId() > mDefaultValue) {
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getMenuId() > mDefaultValue) {
            inflater.inflate(getMenuId(), menu);
            super.onCreateOptionsMenu(menu, inflater);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int layoutId = getLayoutId();
        if (layoutId <= mDefaultValue) { // if default or invalid layout id, then no possibility to create view
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        if (viewDataBinding == null) {
            rootView = inflater.inflate(layoutId, container, false);
        } else {
            viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false);
        }


        return viewDataBinding == null ? rootView : viewDataBinding.getRoot();
    }

    public View getRootView() {
        if (viewDataBinding != null) {
            return viewDataBinding.getRoot();
        } else {
            if (rootView != null) {
                return rootView;
            } else {
                return getView();
            }
        }
    }

    @SuppressWarnings("unchecked")
    @CallSuper
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseViewModel<V, P> viewModel = ViewModelProviders.of(this).get(BaseViewModel.class);
        boolean isPresenterCreated = false;
        if (viewModel.getPresenter() == null) {
            viewModel.setPresenter(initPresenter());
            isPresenterCreated = true;
        }
        presenter = viewModel.getPresenter();
        presenter.attachLifecycle(getLifecycle());
        presenter.attachView((V) this);
        if (isPresenterCreated)
            presenter.onPresenterCreated();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        startUI();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    protected abstract void startUI();

    protected abstract void stopUI();

    protected ViewDataBinding getViewDataBinding() {
        return viewDataBinding;
    }

    @CallSuper
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (presenter != null) {
            presenter.detachLifecycle(getLifecycle());
            presenter.detachView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (presenter != null) {
            presenter.detachLifecycle(getLifecycle());
            presenter.detachView();
        }

        stopUI();
    }

    private BaseActivity getBaseActivity() {
        return ((BaseActivity) getActivity());
    }

    private boolean isBaseActivityInstance() {
        return BaseActivity.class.isInstance(getActivity());
    }

    protected void setTitle(String title) {
        if (isBaseActivityInstance()) {
            getBaseActivity().setTitle(title);
        }
    }

    public void setToolbarText(String title, String subtitle) {
        if (isBaseActivityInstance()) {
            getBaseActivity().setToolbarText(title, subtitle);
        }
    }

    protected void setSubtitle(String subtitle) {
        if (isBaseActivityInstance()) {
            getBaseActivity().setSubtitle(subtitle);
        }
    }

    protected void setClickListener(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

   /* protected void setAnimation(View... views) {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.grow_effect);

        for (View view : views) {
            view.startAnimation(animation);
        }
    }*/

    protected abstract P initPresenter();
}