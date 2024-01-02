package com.clothly.ecommerce.data.ui.productdetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.ItemClickListener;
import com.clothly.ecommerce.data.data.helper.models.AttributeValueModel;

import java.util.List;

public class ChooseColorAdapter extends RecyclerView.Adapter<ChooseColorAdapter.ColorViewHolder> {
    private List<AttributeValueModel> imageList;
    private Context mContext;
    private ItemClickListener<AttributeValueModel> mListener;
    private int prevPosition;
    private boolean isFirstClick;
    private RadioButton newRadioButton;

    public ChooseColorAdapter(List<AttributeValueModel> imageList, Context mContext) {
        this.imageList = imageList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_choose_color, viewGroup, false);
        return new ColorViewHolder(rootView);
    }

    public void addItem(List<AttributeValueModel> newList) {
        if (newList != null) {
            for (AttributeValueModel list : newList) {
                this.imageList.add(list);
                notifyItemInserted(imageList.size() - 1);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder viewHolder, int i) {
        AttributeValueModel valueModel = imageList.get(i);
        if (valueModel != null) {
            viewHolder.radioButton.setText(valueModel.title);
            viewHolder.radioButton.setClickable(false);
            viewHolder.constraintLayout.setOnClickListener(v -> {
                if (isFirstClick && prevPosition != i) {
                    newRadioButton.setChecked(false);
                }
                newRadioButton = viewHolder.radioButton;
                prevPosition = i;
                isFirstClick = true;
                viewHolder.radioButton.setChecked(true);
                mListener.onItemClick(viewHolder.constraintLayout, valueModel, i);
            });
            if (i == (imageList.size() - 1)) {
                viewHolder.line_view.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void serOnClickListener(ItemClickListener<AttributeValueModel> listener) {
        this.mListener = listener;
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayout;
        RadioButton radioButton;
        View line_view;

        public ColorViewHolder(View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.parent_layout_color);
            radioButton = itemView.findViewById(R.id.color_radio);
            line_view = itemView.findViewById(R.id.view_line);
        }
    }
}