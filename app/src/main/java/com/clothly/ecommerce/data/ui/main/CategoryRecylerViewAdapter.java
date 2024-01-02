package com.clothly.ecommerce.data.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.models.HomeBanner;
import com.clothly.ecommerce.data.data.helper.models.HomeImage;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.ui.offerproduct.OfferProductActivity;
import com.clothly.ecommerce.data.ui.prductGrid.ProductGridActivity;

import java.util.ArrayList;
import java.util.List;

public class CategoryRecylerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<HomeBanner> categoryList;
    private Context context;
    private final int FIRST_ITEM = 0;//41
    private final int SECOND_ITEM = 1;//14
    private final int THIRD_ITEM = 2;//11

    public CategoryRecylerViewAdapter(ArrayList<HomeBanner> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == FIRST_ITEM) {//41
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recylerview_category, parent, false);
            return new ViewHolderOne(view);

        } else if (viewType == SECOND_ITEM) {//14
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_cat_another, parent, false);
            return new ViewHolderTwo(view);
        } else if (viewType == THIRD_ITEM) {//11
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_category_one_value, parent, false);
            return new ViewHolderThree(view);
        } else {
            throw new RuntimeException("The type has to be ONE or TWO or three");
        }
    }


    @Override
    public int getItemViewType(int position) {
        HomeBanner item = categoryList.get(position);
        List<HomeImage> category1ImgList = item.getCategory1().getImages();
        List<HomeImage> category2ImgList = item.getCategory2().getImages();

        if (category1ImgList.size() == 1 && category2ImgList.size() == 1) {
            return THIRD_ITEM;
        } else if (category1ImgList.size() == 1 && category2ImgList.size() == 4){
            return SECOND_ITEM;
        }else if (category1ImgList.size() == 4 && category2ImgList.size() == 1){
            return FIRST_ITEM;
        }else {
            return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case FIRST_ITEM:
                initLayoutOne((ViewHolderOne) holder, position);
                break;
            case SECOND_ITEM:
                initLayoutTwo((ViewHolderTwo) holder, position);
                break;
            case THIRD_ITEM:
                initLayoutThird((ViewHolderThree) holder, position);
                break;
            default:
                break;
        }
    }

    /**
     * category 1 are 4 items and category 2 is 1 item.
     * @param holder holder
     * @param position position
     */
    private void initLayoutOne(ViewHolderOne holder, int position) {

        HomeBanner productCategory = categoryList.get(position);

        String categoryBanner = Constants.ServerUrl.FULL_IMAGE_URL + productCategory.getImageName();
        Picasso.get().load(categoryBanner).into(holder.category_banner);

        List<HomeImage> category1ImgList = productCategory.getCategory1().getImages();
        if (category1ImgList != null) {
                holder.text_title_category1.setText(productCategory.getCategory1().getTitle());

                String cat1Img = Constants.ServerUrl.THUMB_IMAGE_URL + category1ImgList.get(0).getImageName();
                Picasso.get().load(cat1Img).into(holder.image_cat1_1);

                String cat2Img = Constants.ServerUrl.THUMB_IMAGE_URL + category1ImgList.get(1).getImageName();
                Picasso.get().load(cat2Img).into(holder.image_cat1_2);

                String cat3Img = Constants.ServerUrl.THUMB_IMAGE_URL + category1ImgList.get(2).getImageName();
                Picasso.get().load(cat3Img).into(holder.image_cat1_3);

                String cat4Img = Constants.ServerUrl.THUMB_IMAGE_URL + category1ImgList.get(3).getImageName();
                Picasso.get().load(cat4Img).into(holder.image_cat1_4);
        }


        List<HomeImage> homeImageCategory2List = productCategory.getCategory2().getImages();
        if (homeImageCategory2List != null) {
            if (homeImageCategory2List.size() <= 1) {
                holder.text_title_category2.setText(productCategory.getCategory2().getTitle());
                String category1Img = Constants.ServerUrl.THUMB_IMAGE_URL + homeImageCategory2List.get(0).getImageName();
                Picasso.get().load(category1Img).into(holder.image_view_category2);
            }
        }

    }

    /**
     * category 1 are 1 item and category 2 is 4 items.
     * @param holder holder
     * @param position position
     */
    private void initLayoutTwo(ViewHolderTwo holder, int position) {
        HomeBanner productCategory = categoryList.get(position);

        String categoryBanner = Constants.ServerUrl.FULL_IMAGE_URL + productCategory.getImageName();
        Picasso.get().load(categoryBanner).into(holder.category_banner);


        List<HomeImage> category1ImgList = productCategory.getCategory1().getImages();
        if (category1ImgList != null) {
            if (category1ImgList.size() <= 1) {
                holder.text_title_category1.setText(productCategory.getCategory1().getTitle());

                String category1Img = Constants.ServerUrl.THUMB_IMAGE_URL + category1ImgList.get(0).getImageName();
                Picasso.get().load(category1Img).into(holder.image_view_category1);
            }
        }


        List<HomeImage> homeImageCategory2List = productCategory.getCategory2().getImages();

        if (homeImageCategory2List != null) {

                holder.text_title_category2.setText(productCategory.getCategory2().getTitle());

                String cat1Img = Constants.ServerUrl.THUMB_IMAGE_URL + homeImageCategory2List.get(0).getImageName();
                Picasso.get().load(cat1Img).into(holder.image_cat2_1);

                String cat2Img = Constants.ServerUrl.THUMB_IMAGE_URL + homeImageCategory2List.get(1).getImageName();
                Picasso.get().load(cat2Img).into(holder.image_cat2_2);

                String cat3Img = Constants.ServerUrl.THUMB_IMAGE_URL + homeImageCategory2List.get(2).getImageName();
                Picasso.get().load(cat3Img).into(holder.image_cat2_3);

                String cat4Img = Constants.ServerUrl.THUMB_IMAGE_URL + homeImageCategory2List.get(3).getImageName();
                Picasso.get().load(cat4Img).into(holder.image_cat2_4);

        }

    }

    /**
     * category 1 are 1 item and category 2 is 1 item.
     * @param holder holder
     * @param position position
     */
    private void initLayoutThird(ViewHolderThree holder, int position) {
        HomeBanner productCategory = categoryList.get(position);

        String categoryBanner = Constants.ServerUrl.FULL_IMAGE_URL + productCategory.getImageName();
        Picasso.get().load(categoryBanner).into(holder.category_banner);


        List<HomeImage> category1ImgList = productCategory.getCategory1().getImages();
        if (category1ImgList != null) {
                holder.text_title_category1.setText(productCategory.getCategory1().getTitle());
                String category1Img = Constants.ServerUrl.THUMB_IMAGE_URL + category1ImgList.get(0).getImageName();
                Picasso.get().load(category1Img).into(holder.image_view_category1);
        }


        List<HomeImage> homeImageCategory2List = productCategory.getCategory2().getImages();
        if (homeImageCategory2List != null) {
                holder.text_title_category2.setText(productCategory.getCategory2().getTitle());
                String category1Img = Constants.ServerUrl.THUMB_IMAGE_URL + homeImageCategory2List.get(0).getImageName();
                Picasso.get().load(category1Img).into(holder.image_cat2_1);
        }

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


    public void addItem(List<HomeBanner> newList) {
        for (HomeBanner products : newList) {
            this.categoryList.add(products);
            notifyItemInserted(categoryList.size() - 1);
        }
    }

    public class ViewHolderOne extends RecyclerView.ViewHolder {

        ImageView category_banner, image_view_category2, image_cat1_1, image_cat1_2, image_cat1_3, image_cat1_4;
        TextView text_title_category2, text_title_category1;
        ConstraintLayout constraint_category1;

        public ViewHolderOne(View itemView) {
            super(itemView);
            category_banner = itemView.findViewById(R.id.category_banner);
            constraint_category1= itemView.findViewById(R.id.constraint_category1);
            image_view_category2 = itemView.findViewById(R.id.image_view_category2);
            image_cat1_1 = itemView.findViewById(R.id.image_cat1_1);
            image_cat1_2 = itemView.findViewById(R.id.image_cat1_2);
            image_cat1_3 = itemView.findViewById(R.id.image_cat1_3);
            image_cat1_4 = itemView.findViewById(R.id.image_cat1_4);
            text_title_category2 = itemView.findViewById(R.id.text_title_category2);
            text_title_category1 = itemView.findViewById(R.id.text_title_category1);

            category_banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeBanner productCategory = categoryList.get(getAdapterPosition());
                    Intent intent = new Intent(context, OfferProductActivity.class);
                    intent.putExtra(Constants.IntentKey.INTENT_SLIDER_ID, productCategory.getTag());
                    context.startActivity(intent);
                }
            });

            constraint_category1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeBanner productCategory = categoryList.get(getAdapterPosition());
                    List<HomeImage> category1ImgList = productCategory.getCategory1().getImages();
                    if (category1ImgList != null) {
                            Intent intentDetails = new Intent(context, ProductGridActivity.class);
                            intentDetails.putExtra(Constants.SharedPrefCredential.INTENT_CATEGORY_ID, "" + productCategory.getCategory1().getId());
                            context.startActivity(intentDetails);
                    }
                }
            });


            image_view_category2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeBanner productCategory = categoryList.get(getAdapterPosition());
                    List<HomeImage> homeImageCategory2List = productCategory.getCategory2().getImages();

                    if (homeImageCategory2List != null) {
                        if (homeImageCategory2List.size() <= 1) {

                                Intent intentDetails = new Intent(context, ProductGridActivity.class);
                                intentDetails.putExtra(Constants.SharedPrefCredential.INTENT_CATEGORY_ID, "" + productCategory.getCategory2().getId());
                                context.startActivity(intentDetails);

                        }
                    }
                }
            });


        }


    }

    //14
    public class ViewHolderTwo extends RecyclerView.ViewHolder {

        ImageView category_banner, image_view_category1, image_cat2_1, image_cat2_2, image_cat2_3, image_cat2_4;
        TextView text_title_category2, text_title_category1;
        ConstraintLayout constraint_category2;

        public ViewHolderTwo(View itemView) {
            super(itemView);
            category_banner = itemView.findViewById(R.id.category_banner);
            constraint_category2 = itemView.findViewById(R.id.constraint_category2);
            image_view_category1 = itemView.findViewById(R.id.image_view_category1);
            image_cat2_1 = itemView.findViewById(R.id.image_cat2_1);
            image_cat2_2 = itemView.findViewById(R.id.image_cat2_2);
            image_cat2_3 = itemView.findViewById(R.id.image_cat2_3);
            image_cat2_4 = itemView.findViewById(R.id.image_cat2_4);
            text_title_category2 = itemView.findViewById(R.id.text_title_category2);
            text_title_category1 = itemView.findViewById(R.id.text_title_category1);

            category_banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeBanner productCategory = categoryList.get(getAdapterPosition());
                    Intent intent = new Intent(context, OfferProductActivity.class);
                    intent.putExtra(Constants.IntentKey.INTENT_SLIDER_ID, productCategory.getTag());
                    context.startActivity(intent);
                }
            });

            constraint_category2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeBanner productCategory = categoryList.get(getAdapterPosition());
                    List<HomeImage> category2ImgList = productCategory.getCategory2().getImages();
                    if (category2ImgList != null) {
                            Intent intentDetails = new Intent(context, ProductGridActivity.class);
                            intentDetails.putExtra(Constants.SharedPrefCredential.INTENT_CATEGORY_ID, "" + productCategory.getCategory2().getId());
                            context.startActivity(intentDetails);
                        }
                    }
            });


            image_view_category1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeBanner productCategory = categoryList.get(getAdapterPosition());
                    List<HomeImage> category1ImgList = productCategory.getCategory1().getImages();
                    if (category1ImgList != null) {
                        if (category1ImgList.size() <= 1) {
                            Intent intentDetails = new Intent(context, ProductGridActivity.class);
                            intentDetails.putExtra(Constants.SharedPrefCredential.INTENT_CATEGORY_ID, "" + productCategory.getCategory1().getId());
                            context.startActivity(intentDetails);
                        }
                    }
                }
            });


        }


    }

    public class ViewHolderThree extends RecyclerView.ViewHolder {

        ImageView category_banner, image_view_category1, image_cat2_1;
        TextView text_title_category2, text_title_category1;
        ConstraintLayout constraint_category2;

        public ViewHolderThree(View itemView) {
            super(itemView);
            category_banner = itemView.findViewById(R.id.category_banner);
            constraint_category2 = itemView.findViewById(R.id.constraint_category2);
            image_view_category1 = itemView.findViewById(R.id.image_view_category1);
            image_cat2_1 = itemView.findViewById(R.id.image_cat2_1);

            text_title_category2 = itemView.findViewById(R.id.text_title_category2);
            text_title_category1 = itemView.findViewById(R.id.text_title_category1);

            category_banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeBanner productCategory = categoryList.get(getAdapterPosition());
                    Intent intent = new Intent(context, OfferProductActivity.class);
                    intent.putExtra(Constants.IntentKey.INTENT_SLIDER_ID, productCategory.getTag());
                    context.startActivity(intent);
                }
            });

            constraint_category2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeBanner productCategory = categoryList.get(getAdapterPosition());
                    List<HomeImage> category2ImgList = productCategory.getCategory2().getImages();
                    if (category2ImgList != null) {
                            Intent intentDetails = new Intent(context, ProductGridActivity.class);
                            intentDetails.putExtra(Constants.SharedPrefCredential.INTENT_CATEGORY_ID, "" + productCategory.getCategory2().getId());
                            context.startActivity(intentDetails);
                    }
                }
            });


            image_view_category1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeBanner productCategory = categoryList.get(getAdapterPosition());
                    List<HomeImage> category1ImgList = productCategory.getCategory1().getImages();
                    if (category1ImgList != null) {
                            Intent intentDetails = new Intent(context, ProductGridActivity.class);
                            intentDetails.putExtra(Constants.SharedPrefCredential.INTENT_CATEGORY_ID, "" + productCategory.getCategory1().getId());
                            context.startActivity(intentDetails);
                    }
                }
            });
        }

    }
}
