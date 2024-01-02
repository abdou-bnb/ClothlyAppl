package com.clothly.ecommerce.data.ui.productdetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.models.ProductDetailsImageModel;
import com.clothly.ecommerce.data.data.util.UIHelper;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private List<ProductDetailsImageModel> imgList;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public ViewPagerAdapter(List<ProductDetailsImageModel> arrayList, Context context) {
        this.imgList = arrayList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (ConstraintLayout) o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.item_swipe_image, container, false);
        ImageView imageView = item_view.findViewById(R.id.image_view_details);
        UIHelper.setFullImageUriInView(imageView, imgList.get(position).imageUri);
        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
