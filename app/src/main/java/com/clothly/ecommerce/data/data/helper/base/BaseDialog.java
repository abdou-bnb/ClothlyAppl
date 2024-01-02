package com.clothly.ecommerce.data.data.helper.base;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
*  ****************************************************************************
*  * Created by : Md. Azizul Islam on 12/12/2017 at 7:10 PM.
*  * Email : azizul@w3engineers.com
*  * 
*  * Last edited by : Md. Azizul Islam on 12/12/2017.
*  * 
*  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>  
*  ****************************************************************************
*/
public abstract class BaseDialog<V extends MvpView, P extends BasePresenter<V>> extends DialogFragment implements MvpView{
    private P presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        int viewId = getLayoutId();
        presenter = initPresenter();
        presenter.attachView((V)this);
        View view = inflater.inflate(viewId, container, false);
        startUi();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    protected abstract int getLayoutId();
    protected abstract P initPresenter();
    protected abstract void startUi();
}
