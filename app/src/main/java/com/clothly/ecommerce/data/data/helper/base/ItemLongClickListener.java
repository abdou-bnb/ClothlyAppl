package com.clothly.ecommerce.data.data.helper.base;

/*
*  ****************************************************************************
*  * Created by : Md. Azizul Islam on 1/22/2018 at 2:56 PM.
*  * Email : azizul@w3engineers.com
*  * 
*  * Last edited by : Md. Azizul Islam on 1/22/2018.
*  * 
*  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>  
*  ****************************************************************************
*/

import android.view.View;

public interface ItemLongClickListener<T> {
    void onItemLongClick(View v, T item);

    void onItemLongClick(View v, T item, int position, int listSize);
}