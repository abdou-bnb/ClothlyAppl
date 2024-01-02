package com.clothly.ecommerce.data.ui.hearderview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.BaseFragment;
import com.clothly.ecommerce.data.data.helper.models.SliderMain;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.ui.offerproduct.OfferProductActivity;


public class SliderMainItemFragment extends BaseFragment<SliderMainMvpView, SliderMainPresenter> implements SliderMainMvpView {

    public static final String SLIDER_MAIN_KEY = "SLIDER_MAIN_KEY";
    public static final String SLIDER_MAIN_KEY_P = "SLIDER_MAIN_KEYs";
    private int positions;
    private String sliderText;

    public static SliderMainItemFragment newInstance(SliderMain sliderMain, int position) {
        Bundle args = new Bundle();
        args.putParcelable(SLIDER_MAIN_KEY, sliderMain);
        args.putInt(SLIDER_MAIN_KEY_P, position);

        SliderMainItemFragment fragment = new SliderMainItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_slider_fragment_main;
    }


    @Override
    protected void startUI() {
        Bundle args = getArguments();
        if (args == null) throw new AssertionError();
        SliderMain sliderMain = args.getParcelable(SLIDER_MAIN_KEY);
        positions = args.getInt(SLIDER_MAIN_KEY_P);
        if (sliderMain == null) throw new AssertionError();

        View view = getRootView();
        if (view != null) {

            ImageView ivSliderImage = view.findViewById(R.id.iv_slider_image);

            String imageUrl = Constants.ServerUrl.FULL_IMAGE_URL + sliderMain.getImageName();
            Picasso.get().load(imageUrl).into(ivSliderImage);



            ivSliderImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), OfferProductActivity.class);
                    intent.putExtra(Constants.IntentKey.INTENT_SLIDER_ID, sliderMain.getTag());
                    startActivity(intent);
                }
            });
        }

    }

    @Override
    protected void stopUI() {

    }

    @Override
    protected SliderMainPresenter initPresenter() {
        return new SliderMainPresenter();
    }

}
