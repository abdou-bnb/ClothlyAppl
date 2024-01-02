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
import com.clothly.ecommerce.data.data.helper.models.InterestedProductModel;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.UIHelper;

import java.util.List;

public class InterestProductAdapter extends RecyclerView.Adapter<InterestProductAdapter.ProductViewHolder> {
    private List<InterestedProductModel> imageList;
    private Context mContext;
    private InterestItemClick mClickListener;

    public InterestProductAdapter(List<InterestedProductModel> imageList, Context mContext) {
        this.imageList = imageList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_details_interest_product, viewGroup, false);
        return new ProductViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder viewHolder, int i) {
        InterestedProductModel productModel = imageList.get(i);
        if (productModel != null) {
            UIHelper.setThumbImageUriInView(viewHolder.imageView, productModel.imageUri);
            viewHolder.textViewPrice.setText(CustomSharedPrefs.getCurrency(mContext) + productModel.currentPrice);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onClickListener(productModel, i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void addItem(List<InterestedProductModel> newList) {
        if (newList != null) {
            for (InterestedProductModel list : newList) {
                this.imageList.add(list);
                notifyItemInserted(imageList.size() - 1);
            }
        }
    }

    public void setClickListener(InterestItemClick mClickListener) {
        this.mClickListener = mClickListener;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewPrice;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
        }
    }
}