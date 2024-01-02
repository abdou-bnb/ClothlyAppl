package com.clothly.ecommerce.data.ui.productdetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.ItemClickListener;
import com.clothly.ecommerce.data.data.helper.models.AttributeValueModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChooseSizeAdapter extends RecyclerView.Adapter<ChooseSizeAdapter.SizeViewHolder> {

    private List<AttributeValueModel> imageList;
    private Context mContext;
    private ItemClickListener<AttributeValueModel> mListener;
    private String prevPosition;

    public ChooseSizeAdapter(List<AttributeValueModel> imageList, Context mContext) {
        this.imageList = imageList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SizeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_choose_size, viewGroup, false);
        return new SizeViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeViewHolder viewHolder, int i) {
        AttributeValueModel valueModel = imageList.get(i);
        if (valueModel != null) {
            viewHolder.textViewSize.setText(valueModel.title);
            viewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyDataSetChanged();
                    viewHolder.circleImageView.setCircleBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                    mListener.onItemClick(viewHolder.circleImageView, valueModel, i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void addItem(List<AttributeValueModel> newList) {
        if (newList != null) {
            for (AttributeValueModel list : newList) {
                this.imageList.add(list);
                notifyItemInserted(imageList.size() - 1);
            }
        }
    }

    public void serOnClickListener(ItemClickListener<AttributeValueModel> listener) {
        this.mListener = listener;
    }

    public class SizeViewHolder extends RecyclerView.ViewHolder {

        TextView textViewSize;
        CircleImageView circleImageView;
        ConstraintLayout constraintLayout;

        public SizeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSize = itemView.findViewById(R.id.text_view_XS);
            circleImageView = itemView.findViewById(R.id.size_XS);
            constraintLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
