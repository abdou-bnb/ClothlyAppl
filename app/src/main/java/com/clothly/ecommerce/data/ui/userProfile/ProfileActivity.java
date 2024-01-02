package com.clothly.ecommerce.data.ui.userProfile;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.CustomMenuBaseActivity;
import com.clothly.ecommerce.data.data.helper.base.ItemClickListener;
import com.clothly.ecommerce.data.data.helper.models.AddressModel;
import com.clothly.ecommerce.data.data.helper.response.UploadImageResponse;
import com.clothly.ecommerce.data.data.helper.response.UserAddressResponse;
import com.clothly.ecommerce.data.data.helper.response.UserMultipleAddressResponse;
import com.clothly.ecommerce.data.data.helper.response.UserRegistrationResponse;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.Loader;
import com.clothly.ecommerce.data.data.util.PermissionUtil;
import com.clothly.ecommerce.data.data.util.SharedPref;
import com.clothly.ecommerce.data.data.util.UIHelper;
import com.clothly.ecommerce.data.ui.myfavourite.UserFavActivity;
import com.clothly.ecommerce.data.ui.ordercomplete.OrderCompleteActivity;

import java.net.HttpURLConnection;
import java.util.ArrayList;


public class ProfileActivity extends CustomMenuBaseActivity<ProfileMvpView, ProfilePresenter>
        implements ProfileMvpView, ItemClickListener<AddressModel> {

    private Toolbar toolbar;
    private Button btnMyOrders, btnMyFavourites, btnCompleteAddress, buttonAddAddress;
    private TextView tvUserName, textViewEmail, textViewAddressHeader;
    boolean isFieldEmpty, isFromEdit;
    private ImageView ivProfileImage;
    private Dialog dialog;
    private Loader mLoader;
    private UserAddressAdapter mAdapter;
    private RecyclerView recyclerViewAddress;
    private LinearLayout linearLayout;
    private UserRegistrationResponse loggedInUser;
    private EditText etAddress1, etAddress2, etCity, etZip, etState, etCountry;
    private String address1 = "", address2 = "", city = "", zip = "", state = "", country = "";
    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 2;
    private PopupWindow mDropdown;
    private int adapterPosition = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_profile;
    }

    @Override
    protected void startUI() {
        initToolbar();
        initViews();
    }

    /**
     * initializing views
     */
    private void initViews() {
        if (CustomSharedPrefs.getUserStatus(ProfileActivity.this)) {
            mLoader = new Loader(this);
            recyclerViewAddress = findViewById(R.id.recycler_view_address);
            linearLayout = findViewById(R.id.layout_no_data);
            buttonAddAddress = findViewById(R.id.button_add_address);
            buttonAddAddress.setOnClickListener(this);
            textViewAddressHeader = findViewById(R.id.text_view_address_header);
            ivProfileImage = findViewById(R.id.iv_profile_image);
            ivProfileImage.setOnClickListener(this);

            btnMyOrders = findViewById(R.id.btn_my_orders);
            btnMyOrders.setOnClickListener(this);

            btnMyFavourites = findViewById(R.id.btn_my_favourite);
            btnMyFavourites.setOnClickListener(this);

            loggedInUser = CustomSharedPrefs.getLoggedInUser(ProfileActivity.this);
            tvUserName = findViewById(R.id.tv_user_name);
            textViewEmail = findViewById(R.id.text_view_email);

            mAdapter = new UserAddressAdapter(new ArrayList<>(), this);
            mAdapter.setItemClickListener(this);
            recyclerViewAddress.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerViewAddress.setAdapter(mAdapter);
            setUserInfo();
        } else {
            Toast.makeText(ProfileActivity.this, "Please login to view your profile", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * set user infor from shared pref
     */
    private void setUserInfo() {
        UserRegistrationResponse loggedInUser = CustomSharedPrefs.getLoggedInUser(this);
        if (loggedInUser != null) {
            mLoader.show();
            presenter.getAllAddress(this, loggedInUser.userRegistrationInfo.id);
            tvUserName.setText(loggedInUser.userRegistrationInfo.username);
            textViewEmail.setText(loggedInUser.userRegistrationInfo.email);
            UIHelper.setThumbImageUriInView(ivProfileImage, SharedPref.getSharedPref(this).read(Constants.Preferences.USER_PROFILE_IMAGE));
        }
    }

    /**
     * init toolbar
     */
    private void initToolbar() {
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText(getString(R.string.user));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void stopUI() {

    }

    /**
     * checking field validation
     */
    private void validateAddress() {

        address1 = etAddress1.getText().toString().trim();
        address2 = etAddress2.getText().toString().trim();
        city = etCity.getText().toString().trim();
        zip = etZip.getText().toString().trim();
        state = etState.getText().toString().trim();
        country = etCountry.getText().toString().trim();
        if (presenter.isEmpty(address1)) {
            isFieldEmpty = true;
            etAddress1.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etAddress1.setBackgroundResource(R.drawable.edittext_round);
        }
        if (presenter.isEmpty(city)) {
            isFieldEmpty = true;
            etCity.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etCity.setBackgroundResource(R.drawable.edittext_round);
        }
        if (presenter.isEmpty(zip)) {
            isFieldEmpty = true;
            etZip.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etZip.setBackgroundResource(R.drawable.edittext_round);
        }
        if (presenter.isEmpty(state)) {
            isFieldEmpty = true;
            etState.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etState.setBackgroundResource(R.drawable.edittext_round);
        }
        if (presenter.isEmpty(country)) {
            isFieldEmpty = true;
            etCountry.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etCountry.setBackgroundResource(R.drawable.edittext_round);
        }
    }

    /**
     * open address pop up to update address
     */
    private void openAddressPopUp(boolean isEdit, AddressModel item) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_alert_address, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);
        dialog = alertDialogBuilder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();

        etAddress1 = dialog.findViewById(R.id.et_shipping_address_1);
        etAddress2 = dialog.findViewById(R.id.et_shipping_address_2);
        etCity = dialog.findViewById(R.id.et_shipping_city);
        etZip = dialog.findViewById(R.id.et_shipping_zip);
        etState = dialog.findViewById(R.id.et_shipping_state);
        etCountry = dialog.findViewById(R.id.et_shipping_country);
        btnCompleteAddress = dialog.findViewById(R.id.btn_complete_address_inner);

        if (isEdit) {
            etAddress1.setText(item.addressLine1);
            etAddress2.setText(item.addressLine2);
            etCity.setText(item.city);
            etZip.setText(item.zipCode);
            etState.setText(item.state);
            etCountry.setText(item.country);
            btnCompleteAddress.setText(getString(R.string.update));
        } else btnCompleteAddress.setText(getString(R.string.add_address));

        btnCompleteAddress.setOnClickListener(v -> {
            validateAddress();
            if (!isFieldEmpty) {
                mLoader.show();
                if (isEdit) {
                    presenter.updateAddress(ProfileActivity.this, address1, address2, city, zip, state, country, ""+item.id);
                } else
                    presenter.updateAddress(ProfileActivity.this, address1, address2, city, zip, state, country, "");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_my_orders:
                Intent intentOrder = new Intent(ProfileActivity.this, OrderCompleteActivity.class);
                startActivity(intentOrder);
                break;

            case R.id.btn_my_favourite:
                Intent intentFav = new Intent(ProfileActivity.this, UserFavActivity.class);
                startActivity(intentFav);
                break;
            case R.id.iv_profile_image:
                if (PermissionUtil.on(this).request(PermissionUtil.REQUEST_CODE_PERMISSION_CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
                    presenter.selectImage(this);
                }
                break;

            case R.id.button_add_address:
                isFromEdit = false;
                openAddressPopUp(false, null);
                break;
        }
    }

    @Override
    protected ProfilePresenter initPresenter() {
        return new ProfilePresenter();
    }

    @Override
    public void onSetAddressSuccess(UserAddressResponse response) {
        dialog.dismiss();
        mLoader.stopLoader();
        if (response != null) {
            if (response.statusCode == HttpURLConnection.HTTP_OK) {
                recyclerViewAddress.setVisibility(View.VISIBLE);
                if (isFromEdit) {
                    mAdapter.removeItem(adapterPosition);
                    mAdapter.addItem(response.addressModel, adapterPosition);
                } else {
                    mAdapter.addItem(response.addressModel);
                }
                textViewAddressHeader.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show();
            }
        } else {
            if (mAdapter.getItemCount() <= 0) {
                recyclerViewAddress.setVisibility(View.GONE);
                textViewAddressHeader.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRemoveAddressSuccess(UserAddressResponse address) {
        mLoader.stopLoader();
        if (address != null && address.statusCode == HttpURLConnection.HTTP_OK) {
            if (adapterPosition != -1) {
                recyclerViewAddress.setVisibility(View.VISIBLE);
                mAdapter.removeItem(adapterPosition);
                if (mAdapter.getItemCount() <= 0) {
                    recyclerViewAddress.setVisibility(View.GONE);
                    textViewAddressHeader.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onSetAddressError(String errorMessage) {
        dialog.dismiss();
        mLoader.stopLoader();
        if (mAdapter.getItemCount() <= 0) {
            textViewAddressHeader.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                presenter.onSelectFromGalleryResult(data, this);
            else if (requestCode == REQUEST_CAMERA)
                presenter.onCaptureImageResult(data, this);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.REQUEST_CODE_PERMISSION_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    presenter.selectImage(this);
                } else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onGettingImageSuccess(UploadImageResponse response) {
        if (response.profileImageModel != null) {
            SharedPref.getSharedPref(this).write(Constants.Preferences.USER_PROFILE_IMAGE, response.profileImageModel.imageUrl);
            UIHelper.setThumbImageUriInView(ivProfileImage, response.profileImageModel.imageUrl);
            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onGettingImageError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGettingAllAddressSuccess(UserMultipleAddressResponse response) {
        mLoader.stopLoader();
        if (response != null && response.statusCode == HttpURLConnection.HTTP_OK) {
            if (!response.addressModel.isEmpty()) {
                textViewAddressHeader.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                recyclerViewAddress.setVisibility(View.VISIBLE);
                mAdapter.addListItem(response.addressModel);
            } else {
                recyclerViewAddress.setVisibility(View.GONE);
                textViewAddressHeader.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        } else {
            textViewAddressHeader.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            recyclerViewAddress.setVisibility(View.GONE);
        }
    }


    @Override
    public void onItemClick(View view, AddressModel item, int i) {
        if (item != null) {
            openItemMenu((ImageView) view, item);
            adapterPosition = i;
        }
    }

    private void openItemMenu(View view, AddressModel item) {
        LayoutInflater mInflater;
        try {
            mInflater = (LayoutInflater) getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = mInflater.inflate(R.layout.alert_address_alert, null);
            final TextView textViewEdit = layout.findViewById(R.id.text_view_edit);
            final TextView textViewDelete = layout.findViewById(R.id.text_view_delete);

            textViewEdit.setOnClickListener(v -> {
                isFromEdit = true;
                mDropdown.dismiss();
                openAddressPopUp(true,  item);
            });

            textViewDelete.setOnClickListener(v -> {
                openDeletePopUp(item);
            });

            layout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            mDropdown = new PopupWindow(layout,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT, true);
            Drawable background = getResources()
                    .getDrawable(R.drawable.bg_address);
            mDropdown.setBackgroundDrawable(background);
            mDropdown.showAsDropDown(view, -160, -80);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openDeletePopUp(AddressModel item) {
        mDropdown.dismiss();
        new AlertDialog.Builder(ProfileActivity.this)
                .setTitle("")
                .setMessage(getString(R.string.want_to_delete))
                .setIcon(R.drawable.ic_logout)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    mLoader.show();
                    presenter.removeAddress(ProfileActivity.this, "" + item.id);
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
}