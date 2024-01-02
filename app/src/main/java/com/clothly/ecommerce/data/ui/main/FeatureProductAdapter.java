package com.clothly.ecommerce.data.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.ItemClickListener;
import com.clothly.ecommerce.data.data.helper.models.Featured;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.ui.productdetails.ProductDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class FeatureProductAdapter extends RecyclerView.Adapter<FeatureProductAdapter.FeatureViewHolder> {
    private List<Featured> productList;
    private Activity context;
    public ItemClickListener<Featured> mListener;

    public FeatureProductAdapter(ArrayList<Featured> categoryList, Activity context) {
        this.productList = categoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_details_interest_product, viewGroup, false);
        return new FeatureViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder featureViewHolder, int i) {
        Featured productModel = productList.get(i);
        if (productModel != null) {

            Glide.with(context).load(Constants.ServerUrl.THUMB_IMAGE_URL + productModel.getImageName()).into(featureViewHolder.imageView);
            if (productModel.getCurrentPrice()!=null){
                featureViewHolder.textViewCurrentPrice.setText(CustomSharedPrefs.getCurrency(context)+"" + productModel.getCurrentPrice()+"");
            }

        }

    }

    public void addItem(List<Featured> newList) {
        for (Featured products : newList) {
            this.productList.add(products);
            notifyItemInserted(productList.size() - 1);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setItemClickedListener(ItemClickListener<Featured> itemClickedListener){
        this.mListener = itemClickedListener;
    }

    public class FeatureViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        ImageView imageView;
        TextView  textViewCurrentPrice;


        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textViewCurrentPrice = itemView.findViewById(R.id.text_view_price);

            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Featured product = productList.get(position);
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra(Constants.SharedPrefCredential.PRODUCT_DETAIL_INTENT, ""+product.getId());
            context.startActivity(intent);
        }

    }
}
