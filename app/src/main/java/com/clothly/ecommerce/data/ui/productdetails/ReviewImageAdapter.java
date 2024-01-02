package com.clothly.ecommerce.data.ui.productdetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.models.ProductDetailsImageModel;
import com.clothly.ecommerce.data.data.util.UIHelper;
import com.clothly.ecommerce.data.ui.reviewdetails.ReviewDetailsActivity;

import java.util.List;

public class ReviewImageAdapter extends RecyclerView.Adapter<ReviewImageAdapter.ReviewImageViewHolder> {
    //list will be a model type
    private List<ProductDetailsImageModel> imageList;
    private Context mContext;
    private boolean needToFinish;
    private String itemId;
    private int ordered;

    public ReviewImageAdapter(List<ProductDetailsImageModel> imageList, Context mContext) {
        this.imageList = imageList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ReviewImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_details_review_image, viewGroup, false);
        return new ReviewImageViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewImageViewHolder viewHolder, int i) {
        ProductDetailsImageModel imageModel = imageList.get(i);
        if (imageModel != null) {
            if (i == 5) {
                viewHolder.textViewLastCount.setVisibility(View.VISIBLE);
                viewHolder.imageView.setVisibility(View.INVISIBLE);
                int count = getItemCount() - 5;
                viewHolder.textViewLastCount.setText("+" + count);
                needToFinish = true;
            } else {
                if (!needToFinish) {
                    UIHelper.setThumbImageUriInView(viewHolder.imageView, imageModel.imageUri);
                    viewHolder.textViewLastCount.setVisibility(View.GONE);
                }else {
                    viewHolder.textViewLastCount.setVisibility(View.GONE);
                    viewHolder.imageView.setVisibility(View.GONE);
                }
            }
        }
    }

    public void addItem(List<ProductDetailsImageModel> newList) {
        if (newList != null) {
            for (ProductDetailsImageModel list : newList) {
                this.imageList.add(list);
                notifyItemInserted(imageList.size() - 1);
            }
        }
    }

    public void clearItem() {
        imageList.clear();
        notifyDataSetChanged();
    }

    public void addItem(ProductDetailsImageModel reviewImage) {
        imageList.add(reviewImage);
        notifyDataSetChanged();
    }
    public void initItem(String id, int order){
        this.itemId = id;
        this.ordered = order;
    }
    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ReviewImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textViewLastCount;

        public ReviewImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textViewLastCount = itemView.findViewById(R.id.text_view_last_count);
            itemView.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View v) {
            ReviewDetailsActivity.runActivity(mContext, itemId, ordered);
        }
    }
}
