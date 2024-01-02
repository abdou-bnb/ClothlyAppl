package com.clothly.ecommerce.data.data.helper.database;

import android.content.Context;

import com.clothly.ecommerce.data.data.helper.models.CustomProductInventory;

import java.util.List;


public class DatabaseUtil {
    /**
     * Fields
     */
    private static DatabaseUtil sInstance;
    private UniqaCustomDao mCodeDao;

    private DatabaseUtil() {
        setCodeDao(UniqaDatabase.on().codeDao());
    }

    /**
     * This method builds an instance
     */
    public static void init(Context context) {
        UniqaDatabase.init(context);

        if (sInstance == null) {
            sInstance = new DatabaseUtil();
        }
    }

    public static DatabaseUtil on() {
        if (sInstance == null) {
            sInstance = new DatabaseUtil();
        }

        return sInstance;
    }

    private UniqaCustomDao getCodeDao() {
        return mCodeDao;
    }

    private void setCodeDao(UniqaCustomDao codeDao) {
        mCodeDao = codeDao;
    }

    public long[] insertItem(CustomProductInventory productInventory) {
        return getCodeDao().insert(productInventory);
    }

    public List<CustomProductInventory> getAllCodes() {
        return getCodeDao().getAllFlowableCodes();
    }

    public int deleteEntity(CustomProductInventory code) {
        return getCodeDao().delete(code);
    }

    public int getItemCount() {
        return getCodeDao().getRowCount();
    }

    public void deleteAll() {
        getCodeDao().nukeTable();
    }

    public void updateQuantity(int quantity, int id) {
        getCodeDao().update(quantity, id);
    }
}
