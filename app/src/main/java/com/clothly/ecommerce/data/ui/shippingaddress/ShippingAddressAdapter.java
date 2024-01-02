package com.clothly.ecommerce.data.ui.shippingaddress;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.ItemClickListener;
import com.clothly.ecommerce.data.data.helper.models.AddressModel;

import java.util.List;


public class ShippingAddressAdapter extends RecyclerView.Adapter<ShippingAddressAdapter.AddressViewHolder> {
    private List<AddressModel> addressList;
    private Context mContext;
    private ItemClickListener<AddressModel> mClickListener;
    private int prevPosition;
    private boolean isFirstClick, isNowCame, firstAlreadyChecked;
    private RadioButton newRadioButton, radioButtonFirst;

    public ShippingAddressAdapter(List<AddressModel> addressList, Context mContext) {
        this.addressList = addressList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_shipping_address, viewGroup, false);
        return new AddressViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder addressViewHolder, int i) {
        AddressModel addressModel = addressList.get(i);
        if (addressModel != null) {
            if (i == 0) {
                firstAlreadyChecked = true;
                radioButtonFirst = addressViewHolder.radioButton;
                addressViewHolder.radioButton.setChecked(true);
                mClickListener.onItemClick(addressViewHolder.constraintLayout, addressModel, i);
            } else if (isNowCame) {
                isNowCame = false;
                if (radioButtonFirst != null)
                    radioButtonFirst.setChecked(false);
                if (newRadioButton != null)
                    newRadioButton.setChecked(false);
                firstAlreadyChecked = true;
                newRadioButton = addressViewHolder.radioButton;
                addressViewHolder.radioButton.setChecked(true);
                mClickListener.onItemClick(addressViewHolder.constraintLayout, addressModel, i);
            }

            addressViewHolder.radioButton.setClickable(false);
            String address = addressModel.addressLine1 + addressModel.addressLine2
                    + ", City : " + addressModel.city + ", zip Code : " + addressModel.zipCode
                    + ", State : " + addressModel.state + ", Country :" + addressModel.country;
            addressViewHolder.textViewAddress.setText(address);

            addressViewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isFirstClick && prevPosition != i) {
                        newRadioButton.setChecked(false);
                    }
                    if (firstAlreadyChecked) {
                        if (i != 0) {
                            firstAlreadyChecked = false;
                            if (radioButtonFirst != null)
                                radioButtonFirst.setChecked(false);
                            if (newRadioButton != null)
                                newRadioButton.setChecked(false);
                        }
                    }
                    newRadioButton = addressViewHolder.radioButton;
                    prevPosition = i;
                    isFirstClick = true;
                    addressViewHolder.radioButton.setChecked(true);
                    mClickListener.onItemClick(addressViewHolder.constraintLayout, addressModel, i);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public void addListItem(List<AddressModel> newList) {
        for (AddressModel model : newList) {
            this.addressList.add(model);
            notifyItemInserted(addressList.size() - 1);
        }
    }

    public void addItem(AddressModel model) {
        if (model != null) {
            isNowCame = true;
            this.addressList.add(model);
            notifyItemInserted(addressList.size() - 1);
        }
    }

    public void setItemClickListener(ItemClickListener<AddressModel> mClickListener) {
        this.mClickListener = mClickListener;
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {

        TextView textViewAddress;
        RadioButton radioButton;
        ConstraintLayout constraintLayout;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAddress = itemView.findViewById(R.id.text_view_address_1);
            radioButton = itemView.findViewById(R.id.radio_current_address);
            constraintLayout = itemView.findViewById(R.id.layout_address);
        }
    }
}
