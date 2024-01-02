package com.clothly.ecommerce.data.data.helper.base;

import android.view.View;

/*
* ****************************************************************************
* * Copyright © 2018 W3 Engineers Ltd., All rights reserved.
* *
* * Created by:
* * Name : SUDIPTA KUMAR PAIK
* * Date : 2/7/18
* * Email : sudipta@w3engineers.com
* *
* * Purpose :
* *
* * Last Edited by : SUDIPTA KUMAR PAIK on 2/7/18.
* * History:
* * 1:
* * 2:
* *  
* * Last Reviewed by : SUDIPTA KUMAR PAIK on 2/7/18.
* ****************************************************************************
*/
public interface ICommonInterface {
    void setClickListener(View... views);

    void setSubtitle(String subtitle);

    void setToolbarText(String title, String subtitle);

    void setTitle(String title);

    void startUI();

    void stopUI();

    int getLayoutId();

    //int getToolbarId();

    int getMenuId();
}
