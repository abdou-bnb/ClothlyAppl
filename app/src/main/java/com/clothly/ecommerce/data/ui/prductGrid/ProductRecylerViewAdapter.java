package com.clothly.ecommerce.data.ui.prductGrid;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.ItemClickListener;
import com.clothly.ecommerce.data.data.helper.models.ProductModel;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.UIHelper;
import com.clothly.ecommerce.data.data.util.UtilityClass;
import com.clothly.ecommerce.data.ui.productdetails.ProductDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class ProductRecylerViewAdapter extends RecyclerView.Adapter<ProductRecylerViewAdapter.ViewHolder> {

    private ArrayList<ProductModel> productList;
    public Activity mActivity;
    public ItemClickListener<ProductModel> mListener;
    private boolean isFromSearch;


    public ProductRecylerViewAdapter(ArrayList<ProductModel> productList, Activity context, boolean isSearch) {
        this.productList = productList;
        this.mActivity = context;
        this.isFromSearch = isSearch;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recylerview_product, parent, false);

        return new ViewHolder(rootView);
    }

    public void addItem(List<ProductModel> newList) {
        for (ProductModel products : newList) {
            this.productList.add(products);
            notifyItemInserted(productList.size() - 1);
        }
    }

    public void clearList() {
        productList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (productList != null) {
            ProductModel product = productList.get(position);
            if (product != null) {
                UIHelper.setThumbImageUriInView(holder.ivProductImage, product.imageUri);
                holder.btnFavourite.setLiked(product.isFavourite == 1);
                holder.tvProductHeading.setText(product.title);
                holder.tvProductPrice.setText(UtilityClass.getNumberFormat(mActivity, Double.valueOf(product.currentPrice)));

                if (product.previousPrice != 0) {
                    holder.tvGridProductPrevPrice.setVisibility(View.VISIBLE);
                    holder.tvGridProductPrevPrice.setText(UtilityClass.getNumberFormat(mActivity, product.previousPrice));
                } else {
                    holder.tvGridProductPrevPrice.setVisibility(View.GONE);
                }

            }
        }

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setItemClickedListener(ItemClickListener<ProductModel> itemClickedListener) {
        this.mListener = itemClickedListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, OnLikeListener {

        public ImageView ivProductImage;
        public TextView tvProductHeading;
        public TextView tvProductPrice;
        public TextView tvGridProductPrevPrice;
        public LikeButton btnFavourite;
        public TextView favCount;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            ivProductImage = itemView.findViewById(R.id.iv_grid_product_image);
            tvProductHeading = itemView.findViewById(R.id.tv_grid_product_heading);
            tvProductPrice = itemView.findViewById(R.id.tv_grid_product_price);
            tvGridProductPrevPrice = itemView.findViewById(R.id.tv_grid_product_Previous_price);
            favCount = itemView.findViewById(R.id.text_favorite_count);

            btnFavourite = itemView.findViewById(R.id.btn_favourite);
            btnFavourite.setOnLikeListener(this);

        }


        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            ProductModel product = productList.get(position);

            Intent intent = new Intent(mActivity, ProductDetailsActivity.class);
            intent.putExtra(Constants.SharedPrefCredential.PRODUCT_DETAIL_INTENT, "" + product.id);
            mActivity.startActivity(intent);
            if (isFromSearch) {
                mActivity.finish();
            }

        }

        @Override
        public void liked(LikeButton likeButton) {

            if (CustomSharedPrefs.getLoggedInUser(mActivity) != null) {
                int position = getAdapterPosition();
                likeButton.setLiked(false);
                ProductModel currentProduct = productList.get(position);
                mListener.onItemClick(btnFavourite, currentProduct, position);

            } else {
               UIHelper.openSignInPopUp(mActivity);
                likeButton.setLiked(false);
            }
        }

        @Override
        public void unLiked(LikeButton likeButton) {
            int position = getAdapterPosition();
            ProductModel currentProduct = productList.get(position);
            mListener.onItemClick(btnFavourite, currentProduct, position);
        }
    }

}


