package com.clothly.ecommerce.data.data.helper.base;

import android.view.View;

/**
 * Created by W3E16 on 10/20/2017.
 */

public interface ItemClickListener<T>{
    void onItemClick(View view, T item, int i);
}
