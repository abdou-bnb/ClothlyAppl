package com.clothly.ecommerce.data.ui.ordercomplete;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.models.UserOrderList;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.UIHelper;

import java.util.List;

public class OrderCompleteAdapter extends RecyclerView.Adapter<OrderCompleteAdapter.OrderViewHolder> {

    private List<UserOrderList> userOrderLists;
    private Context mContext;

    public OrderCompleteAdapter(List<UserOrderList> orderModels, Context mContext) {
        this.userOrderLists = orderModels;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_complete, viewGroup, false);
        return new OrderViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i) {
        UserOrderList orderList = userOrderLists.get(i);
        if (orderList != null) {
            UIHelper.setThumbImageUriInView(orderViewHolder.imageView, orderList.imageUri);
            orderViewHolder.textViewName.setText(orderList.productTitle);
            if (orderList.attribute!=null){
                orderViewHolder.textViewAttribute.setVisibility(View.VISIBLE);
                orderViewHolder.textViewAttribute.setText("" + orderList.attribute);
            }
            orderViewHolder.textViewQuantity.setText(orderList.quantity);
            orderViewHolder.textViewPrice.setText("" + orderList.price);
            double price = Double.valueOf(orderList.quantity) * Double.valueOf(orderList.price);
            String currency = CustomSharedPrefs.getCurrency(mContext);
            orderViewHolder.textViewTotalAmount.setText(currency + "" + price);
        }
    }


    @Override
    public int getItemCount() {
        return userOrderLists.size();
    }


    public class OrderViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageView;
        TextView textViewName, textViewAttribute, textViewPrice, textViewQuantity, textViewTotalAmount;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.round_image_view_product_image);
            textViewName = itemView.findViewById(R.id.text_view_order_product_name);
            textViewAttribute = itemView.findViewById(R.id.text_view_order_product_color);
            textViewPrice = itemView.findViewById(R.id.text_view_single_order_product_account);
            textViewQuantity = itemView.findViewById(R.id.text_view_order_product_count);
            textViewTotalAmount = itemView.findViewById(R.id.text_view_order_total_price_amount);
        }
    }
}
