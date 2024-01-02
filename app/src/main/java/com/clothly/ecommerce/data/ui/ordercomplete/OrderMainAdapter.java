package com.clothly.ecommerce.data.ui.ordercomplete;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.models.OrderModel;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;

import java.util.List;

public class OrderMainAdapter extends RecyclerView.Adapter<OrderMainAdapter.MainViewHolder> {
    private List<OrderModel> orderList;
    private Context mContext;

    public OrderMainAdapter(List<OrderModel> orderModels, Context mContext) {
        this.orderList = orderModels;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_main, viewGroup, false);
        return new MainViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder mainHolder, int i) {
        OrderModel orderModel = orderList.get(i);
        if (orderModel != null) {
            String currency = CustomSharedPrefs.getCurrency(mContext);
            mainHolder.textViewId.setText(currency + orderModel.tax);
            mainHolder.textViewMethod.setText(orderModel.method);
            mainHolder.textViewPrice.setText(currency + orderModel.amount);
            mainHolder.textViewStatus.setText(orderModel.status);
            mainHolder.textViewTimeDate.setText(orderModel.DateTime);

            if (orderModel.userOrderLists != null) {
                OrderCompleteAdapter orderCompleteAdapter = new OrderCompleteAdapter(orderModel.userOrderLists, mContext);
                mainHolder.recyclerView.setAdapter(orderCompleteAdapter);
                mainHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            }

        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void addItem(List<OrderModel> newList) {
        if (newList != null) {
            for (OrderModel list : newList) {
                this.orderList.add(list);
                notifyItemInserted(orderList.size() - 1);
            }
        }
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        TextView textViewId, textViewMethod, textViewStatus, textViewTimeDate, textViewPrice;
        RecyclerView recyclerView;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewId = itemView.findViewById(R.id.text_view_order_value);
            textViewMethod = itemView.findViewById(R.id.text_view_bank_name);
            textViewStatus = itemView.findViewById(R.id.text_view_status_condition);
            textViewTimeDate = itemView.findViewById(R.id.text_view_date_time);
            recyclerView = itemView.findViewById(R.id.recycler_view_order_products);
            textViewPrice = itemView.findViewById(R.id.text_view_total_price_value);
        }
    }
}
