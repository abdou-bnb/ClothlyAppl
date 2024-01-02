package com.clothly.ecommerce.data.ui.reviewdetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.models.ReviewImage;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.UIHelper;

import java.util.ArrayList;
import java.util.List;

public class ReviewDetailsAdapter extends RecyclerView.Adapter<ReviewDetailsAdapter.ReviewViewHolder> {
    private List<ReviewImage> mReviewImageList;
    private Context mContext;

    public ReviewDetailsAdapter(Context mContext) {
        mReviewImageList = new ArrayList<>();
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_review_details, viewGroup, false);
        return new ReviewViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int i) {
        if (mReviewImageList != null) {

            ReviewImage reviewImage = mReviewImageList.get(i);
            if (reviewImage != null) {
                UIHelper.setThumbImageUriInView(reviewViewHolder.imageViewProfile,reviewImage.getImageName() );

                reviewViewHolder.textViewName.setText(reviewImage.getUserName());
                reviewViewHolder.textViewDate.setText(reviewImage.getTime());
                reviewViewHolder.textViewDescription.setText(reviewImage.getReview());
                reviewViewHolder.ratingBar.setRating(Float.parseFloat(reviewImage.getRating()));

                if (reviewImage.getShowReviewImageList() != null) {
                    ReviewImageAdapter imageAdapter = new ReviewImageAdapter(reviewImage.getShowReviewImageList());
                    reviewViewHolder.recyclerViewImage.setAdapter(imageAdapter);
                    reviewViewHolder.recyclerViewImage.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                }
                if (String.valueOf(reviewImage.getUserId()).equals(CustomSharedPrefs.getLoggedInUserId(mContext))){
                    ReviewDetailsActivity.isAlreadyReviewed=true;
                }
            }

        }


    }

    /**
     * @param item     item type object
     * @param position int value of position where value will be inserted
     */
    public void addAllItemToPosition(List<ReviewImage> item, int position) {
        mReviewImageList.addAll(position, item);
        notifyItemInserted(position);
    }

    public void addItem(List<ReviewImage> newList) {
        for (ReviewImage reviewImage : newList) {
            this.mReviewImageList.add(reviewImage);
            notifyItemInserted(mReviewImageList.size() - 1);
        }
    }
    public void clearList() {
        mReviewImageList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mReviewImageList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProfile;
        TextView textViewName, textViewDate, textViewDescription;
        RatingBar ratingBar;
        RecyclerView recyclerViewImage;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewProfile = itemView.findViewById(R.id.image_view_profile);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            textViewDescription = itemView.findViewById(R.id.text_view_message);
            ratingBar = itemView.findViewById(R.id.rating_bar_avg_rating);
            recyclerViewImage = itemView.findViewById(R.id.recycler_view_up_images);
        }
    }
}
