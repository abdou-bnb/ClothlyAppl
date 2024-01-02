package com.clothly.ecommerce.data.data.helper.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/*
*  ****************************************************************************
*  * Created by : Azizul Islam process 16-Oct-17 at 3:23 PM.
*  * Email : azizul@w3engineers.com
*  *
*  * Last edited by : Azizul Islam on 21-Dec-17.
*  *
*  * Last Reviewed by : <Reviewer Name> process <mm/dd/yy>
*  ****************************************************************************
*/
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {
    private final List<T> mItemList;
    protected ItemClickListener mItemClickListener;
    protected ItemLongClickListener mItemLongClickListener;

    public BaseAdapter() {
        mItemList = new ArrayList<T>();
    }

    public abstract boolean isEqual(T left, T right);

    public abstract BaseViewHolder newViewHolder(ViewGroup parent, int viewType);

    /*
    protected abstract int getLayoutIdForPosition(int position);

    protected abstract T getObjForPosition(int position);
    */

    public void setItemLongClickListener(ItemLongClickListener itemLongClickListener) {
        this.mItemLongClickListener = itemLongClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return newViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<T> holder, int position) {
        T itemData = getItem(position);
        holder.bind(itemData);
    }

    public void clear() {
        mItemList.clear();
        notifyDataSetChanged();
    }

    public List<T> getItems() {
        return mItemList;
    }

    public void removeItem(T t) {
        int toIndex = mItemList.indexOf(t);
        if (toIndex < 0 || toIndex >= mItemList.size()) return;
        mItemList.remove(toIndex);
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        if (position < 0 || position >= mItemList.size()) return null;
        return mItemList.get(position);
    }

   /* public void addItemFirst(T item) {

        int position = 0;
        addItemFirst(item, position);

    }*/

    public int addItem(T item) {
        T tItem = findItem(item);

        if (tItem == null) {
            mItemList.add(item);
            notifyItemInserted(mItemList.size() - 1);
            return mItemList.size() - 1;
        }
        return updateItem(item, tItem);
    }

    public void addItem(List<T> items) {
        for (T item : items) {
            addItem(item);
        }
    }

    /**
     * @param item     T type object
     * @param position int value of position where value will be inserted
     */
    public void addAllItemToPosition(List<T> item, int position) {
        mItemList.addAll(position, item);
        notifyItemInserted(position);
    }


    public T findItem(T item) {
        for (T tItem : mItemList) {
            if (isEqual(item, tItem)) {
                return tItem;
            }
        }
        return null;
    }

    public void addItems(List<T> items) {
        for (T item : items) {
            addItem(item);
        }
    }

    public int updateItem(T fromItem, T toItem) {
        int toIndex = mItemList.indexOf(toItem);
        mItemList.set(toIndex, fromItem);
        notifyItemChanged(toIndex);
        return toIndex;
    }

    /*
    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }
    */
    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}