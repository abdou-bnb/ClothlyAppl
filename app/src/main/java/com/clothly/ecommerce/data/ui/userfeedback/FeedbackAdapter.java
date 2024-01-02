package com.clothly.ecommerce.data.ui.userfeedback;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.ItemClickListener;
import com.clothly.ecommerce.data.data.helper.base.ItemLongClickListener;
import com.clothly.ecommerce.data.data.helper.models.ReviewImage;

import java.util.List;

import static com.clothly.ecommerce.data.ui.userfeedback.FeedbackActivity.isAddNeed;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    private ItemClickListener<ReviewImage> imageItemClickListener;
    private ItemLongClickListener<ReviewImage> imageItemLongClickListener;
    private List<ReviewImage> imageList;
    private Context mContext;

    public FeedbackAdapter(List<ReviewImage> imageList, Context context) {
        this.imageList = imageList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_feedback_images, viewGroup, false);
        return new FeedbackViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder feedbackViewHolder, final int position) {
        final ReviewImage reviewImage = imageList.get(position);
        if (reviewImage != null) {
            if (reviewImage.lastIndex == 10) {
                feedbackViewHolder.imageViewHasSource.setVisibility(View.GONE);
                feedbackViewHolder.imageViewAdd.setVisibility(View.VISIBLE);
            } else {
                Glide.with(mContext).load(reviewImage.imageUri).into(feedbackViewHolder.imageViewHasSource);
                feedbackViewHolder.imageViewHasSource.setVisibility(View.VISIBLE);
                feedbackViewHolder.imageViewAdd.setVisibility(View.GONE);
            }
        } else {
            feedbackViewHolder.imageViewHasSource.setVisibility(View.GONE);
            feedbackViewHolder.imageViewAdd.setVisibility(View.GONE);
        }


        feedbackViewHolder.imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageItemClickListener.onItemClick(v, reviewImage, position);
            }
        });

        feedbackViewHolder.imageViewHasSource.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                imageItemLongClickListener.onItemLongClick(v, reviewImage, position, getItemCount());
                return false;
            }
        });
    }

    /**
     * @return current item list
     */
    public List<ReviewImage> getItems() {
        return imageList;
    }

    public void addItemFirst(ReviewImage reviewImage) {

        imageList.add(0, reviewImage);
        if (imageList.size() == 6) {
            imageList.remove(5);
            isAddNeed = false;
        }
        notifyDataSetChanged();
    }

    public void addItemLast(ReviewImage reviewImage) {
        imageList.add(reviewImage);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        imageList.remove(position);
        notifyDataSetChanged();

    }

    public void setClickListener(ItemClickListener<ReviewImage> mClickListener) {
        this.imageItemClickListener = mClickListener;
    }

    public void setImageItemLongClickListener(ItemLongClickListener<ReviewImage> imageItemLongClickListener) {
        this.imageItemLongClickListener = imageItemLongClickListener;
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class FeedbackViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewAdd, imageViewHasSource;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAdd = itemView.findViewById(R.id.image_view_plus);
            imageViewHasSource = itemView.findViewById(R.id.image_view);
        }
    }
}
