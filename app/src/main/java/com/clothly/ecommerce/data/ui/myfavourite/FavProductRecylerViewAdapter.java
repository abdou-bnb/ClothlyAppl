package com.clothly.ecommerce.data.ui.myfavourite;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.models.ProductModel;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.UIHelper;
import com.clothly.ecommerce.data.data.util.UtilityClass;
import com.clothly.ecommerce.data.ui.productdetails.ProductDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class FavProductRecylerViewAdapter extends RecyclerView.Adapter<FavProductRecylerViewAdapter.ViewHolder> {

    private ArrayList<ProductModel> productList;
    private Context context;

    public FavProductRecylerViewAdapter(ArrayList<ProductModel> productList, Context context) {
        this.productList = productList;
        this.context = context;
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProductModel product = productList.get(position);
        if (product != null) {
            UIHelper.setThumbImageUriInView(holder.ivProductImage, product.imageUri);
            holder.btnFavourite.setLiked(true);
            holder.tvProductHeading.setText(product.title);
            holder.tvProductPrice.setText(UtilityClass.getNumberFormat(context, Double.valueOf(product.currentPrice)));

            if (product.previousPrice != 0) {
                holder.tvGridProductPrevPrice.setText(UtilityClass.getNumberFormat(context, product.previousPrice));
            } else {
                holder.tvGridProductPrevPrice.setVisibility(View.INVISIBLE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivProductImage;
        TextView tvProductHeading;
        TextView tvProductPrice;
        TextView tvGridProductPrevPrice;
        LikeButton btnFavourite;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            ivProductImage = itemView.findViewById(R.id.iv_grid_product_image);
            tvProductHeading = itemView.findViewById(R.id.tv_grid_product_heading);
            tvProductPrice = itemView.findViewById(R.id.tv_grid_product_price);
            tvGridProductPrevPrice = itemView.findViewById(R.id.tv_grid_product_Previous_price);
            btnFavourite = itemView.findViewById(R.id.btn_favourite);
            btnFavourite.setEnabled(false);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ProductModel product = productList.get(position);
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra(Constants.SharedPrefCredential.PRODUCT_DETAIL_INTENT, ""+product.id);
            context.startActivity(intent);

        }

    }

}


