package com.clothly.ecommerce.data.ui.addcart;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.ItemClickListener;
import com.clothly.ecommerce.data.data.helper.database.DatabaseUtil;
import com.clothly.ecommerce.data.data.helper.models.CustomProductInventory;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.UIHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewModel> {

    private List<CustomProductInventory> inventoryList;
    private List<CustomProductInventory> prevCartList;
    ItemClickListener<CustomProductInventory> mListener;
    private Activity mActivity;
    private int productCounter = 0;
    private boolean canIncrease;
    private StringBuilder attTitle;
    private List<String> titleList = new ArrayList<>();

    public CartAdapter(List<CustomProductInventory> inventoryList, Activity mContext) {
        this.inventoryList = inventoryList;
        this.mActivity = mContext;
    }

    @NonNull
    @Override
    public CartViewModel onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list_cart_product, viewGroup, false);
        return new CartViewModel(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewModel cartViewModel, int i) {
        CustomProductInventory productInventory = inventoryList.get(i);
        attTitle = new StringBuilder();
        if (productInventory != null) {

            //cartViewModel.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            //cartViewModel.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, cartViewModel.swipeLayout.findViewById(R.id.bottom_wrapper1));
            cartViewModel.textViewQuantity.setText("" + productInventory.currentQuantity);
            Type listType = new TypeToken<ArrayList<String>>() {
            }.getType();
            titleList = new Gson().fromJson(productInventory.attributeTitle, listType);
            if(productInventory.attributeTitle != null) {
                if (!productInventory.attributeTitle.isEmpty()) {
                    for (String title : titleList) {
                        attTitle.append(title);
                    }
                }
            }
            cartViewModel.textViewSizeLable.setText(attTitle);

            cartViewModel.textViewName.setText(productInventory.productName);
            cartViewModel.textViewPrice.setText(CustomSharedPrefs.getCurrency(mActivity)+ "" + productInventory.price);
            UIHelper.setThumbImageUriInView(cartViewModel.imageViewProductImage, productInventory.imageUri);

            cartViewModel.imageButtonUp.setOnClickListener(v -> {
                AsyncTask.execute(() -> {
                    productCounter=0;
                    prevCartList = DatabaseUtil.on().getAllCodes();
                    for (CustomProductInventory customInventory : prevCartList) {
                        if (productInventory.inventory_id == customInventory.inventory_id) {
                            productCounter = productCounter + customInventory.currentQuantity;
                        }
                    }
                    if (productCounter < productInventory.available_qty) {
                        //   canIncrease = true;
                        productInventory.currentQuantity = productInventory.currentQuantity + 1;
                        mActivity.runOnUiThread(() -> {
                            cartViewModel.textViewQuantity.setText("" + productInventory.currentQuantity);
                            mListener.onItemClick(cartViewModel.textViewQuantity, productInventory, i);
                        });
                        AsyncTask.execute(() -> {
                            DatabaseUtil.on().updateQuantity(productInventory.currentQuantity, productInventory.id);
                        });

                    } else {
                        mActivity.runOnUiThread(() -> Toast.makeText(mActivity, "Inventory exceed. You can't add more!!", Toast.LENGTH_SHORT).show());
                        canIncrease = false;
                    }
                });
            });

            cartViewModel.imageButtonDown.setOnClickListener(v -> {
                if (productInventory.currentQuantity > 1) {
                    productInventory.currentQuantity = productInventory.currentQuantity - 1;
                    cartViewModel.textViewQuantity.setText("" + productInventory.currentQuantity);
                    mListener.onItemClick(cartViewModel.textViewQuantity, productInventory, i);
                    AsyncTask.execute(() -> {
                        DatabaseUtil.on().updateQuantity(productInventory.currentQuantity, productInventory.id);
                    });
                } else {
                    Toast.makeText(mActivity, "Minimum quantity is 1", Toast.LENGTH_SHORT).show();
                }
            });

            cartViewModel.itemView.setOnClickListener(v -> {
                //confirm activity
            });

            cartViewModel.btnDelete.setOnClickListener(onDeleteListener(cartViewModel.getAdapterPosition(), cartViewModel, productInventory));
        }
    }

    private View.OnClickListener onDeleteListener(final int position, final CartViewModel customViewHolder, CustomProductInventory productInventory) {
        return v -> {

            openDeletePopUp(v,position,customViewHolder,productInventory);
            //customViewHolder.swipeLayout.close();
        };

    }


    private void openDeletePopUp( View v,int position, final CartViewModel customViewHolder, CustomProductInventory productInventory) {

        new AlertDialog.Builder(mActivity)
                .setTitle("")
                .setMessage("Do you want to delete the product from the cart?")
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    AsyncTask.execute(() -> {
                        DatabaseUtil.on().deleteEntity(productInventory);
                        inventoryList.remove(position);
                        mListener.onItemClick(v, productInventory, CartActivity.CLICK_CART_DELETE);

                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    });
                    notifyDataSetChanged();
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }

    public void addItem(List<CustomProductInventory> newList) {
        if (newList != null) {
            for (CustomProductInventory list : newList) {
                this.inventoryList.add(list);
                notifyItemInserted(inventoryList.size() - 1);
            }
        }
    }

    public void setItemClickListener(ItemClickListener<CustomProductInventory> listener) {
        this.mListener = listener;
    }

    public List<CustomProductInventory> getItems() {
        return inventoryList;
    }

    public class CartViewModel extends RecyclerView.ViewHolder {
        private TextView btnDelete;
        //private SwipeLayout swipeLayout;
        private ImageView imageViewProductImage;
        private TextView textViewName, textViewSize, textViewColor, textViewPrice, textViewQuantity,textViewSizeLable,textViewColorLable;
        private ImageView imageButtonUp, imageButtonDown;
        //private LinearLayout linearLayoutColorSize;

        public CartViewModel(@NonNull View itemView) {
            super(itemView);
            //swipeLayout = itemView.findViewById(R.id.swipe);
            btnDelete = itemView.findViewById(R.id.btndelete);
            //swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
            imageViewProductImage = itemView.findViewById(R.id.iv_cart_product_image);
            textViewName = itemView.findViewById(R.id.tv_cart_product_heading);
            textViewSizeLable = itemView.findViewById(R.id.tv_cart_product_1);
         /*   textViewColorLable = itemView.findViewById(R.id.tv_cart_product_2);
            textViewSize = itemView.findViewById(R.id.tv_cart_product_size);
            textViewColor = itemView.findViewById(R.id.tv_cart_product_color);*/
            textViewPrice = itemView.findViewById(R.id.tv_cart_product_price);
            imageButtonUp = itemView.findViewById(R.id.image_button_up);
            imageButtonDown = itemView.findViewById(R.id.image_button_down);
            //linearLayoutColorSize = itemView.findViewById(R.id.linear_layout_color_size);
            textViewQuantity = itemView.findViewById(R.id.btn_cart_product_quantity);
        }
    }

}
