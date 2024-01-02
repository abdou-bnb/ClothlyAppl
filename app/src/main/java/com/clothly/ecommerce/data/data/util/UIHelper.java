package com.clothly.ecommerce.data.data.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.ui.userLogin.LoginActivity;


public class UIHelper {

    /**
     * setting thumbnail url to view
     *
     * @param imageView
     * @param imageUri
     */
    @BindingAdapter("setImageUri")
    public static void setThumbImageUriInView(ImageView imageView, String imageUri) {
        String imageUrl = Constants.ServerUrl.THUMB_IMAGE_URL + imageUri;
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .apply(new RequestOptions().placeholder(R.drawable.place_holder).error(R.drawable.place_holder))
                .into(imageView)
        ;
    }

    /**
     * setting full image_url to view
     *
     * @param imageView
     * @param imageUri
     */
    @BindingAdapter("setFullImageUri")
    public static void setFullImageUriInView(ImageView imageView, String imageUri) {
        if (!imageUri.contains("http")) {
            imageUri = Constants.ServerUrl.FULL_IMAGE_URL + imageUri;
        }
        Glide.with(imageView.getContext())
                .load(imageUri)
                .apply(new RequestOptions().placeholder(R.drawable.place_holder).error(R.drawable.place_holder))
                .into(imageView);
    }

    /**
     * setting image url to view
     *
     * @param imageView
     * @param resourceId
     */
    @BindingAdapter("imageResource")
    public static void setImageResource(ImageView imageView, String resourceId) {
        Glide.with(imageView.getContext())
                .load(resourceId)
                .into(imageView);
    }

    /**
     * setting image bitmap to view
     *
     * @param imageView
     * @param bitmap
     */
    @BindingAdapter("imageBitmap")
    public static void setImageBitmap(ImageView imageView, Bitmap bitmap) {
        Glide.with(imageView.getContext())
                .load(bitmap)
                .apply(new RequestOptions().placeholder(R.drawable.place_holder).error(R.drawable.place_holder))
                .into(imageView);
    }

    /**
     * setting image to view
     *
     * @param view
     * @param rating
     */
    @BindingAdapter("setRating")
    public void setRating(RatingBar view, float rating) {
        if (view.getRating() != rating) {
            view.setRating(rating);
        }
    }

    /**
     * if user is not register than this signin pop up will show
     */
    public static void openSignInPopUp(Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage(activity.getString(R.string.need_sign_in))
                .setIcon(R.drawable.ic_logout)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intentLogin = new Intent(activity, LoginActivity.class);
                        activity.startActivity(intentLogin);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

}
