package com.clothly.ecommerce.data.ui.category;

import com.clothly.ecommerce.data.data.helper.base.MvpView;
import com.clothly.ecommerce.data.data.helper.response.AllCategoryResponse;

public interface CategoryMvpView extends MvpView {
    void onCategoryListSuccess(AllCategoryResponse productCategories);
    void onCategoryListError(String message);
}
