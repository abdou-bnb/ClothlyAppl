package com.clothly.ecommerce.data.ui.reviewdetails;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.makeramen.roundedimageview.RoundedImageView;
import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.models.ShowReviewImage;
import com.clothly.ecommerce.data.data.util.UIHelper;

import java.util.List;

public class ReviewImageAdapter extends RecyclerView.Adapter<ReviewImageAdapter.ImageViewHolder> {
    private List<ShowReviewImage> imageList;

    public ReviewImageAdapter(List<ShowReviewImage> imageList) {
        this.imageList = imageList;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_review_up_images, viewGroup, false);
        return new ImageViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        if (imageList!=null){
            ShowReviewImage showReviewImage=imageList.get(i);
            UIHelper.setThumbImageUriInView(imageViewHolder.mRoundedImageView , showReviewImage.getProfileImageOfReviewer());
        }
    }

    @Override
    public int getItemCount() {
        if (imageList !=null){
            return imageList.size();
        }else {
            return 0;
        }

    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView mRoundedImageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            mRoundedImageView=itemView.findViewById(R.id.image_view_images);
        }
    }
}
