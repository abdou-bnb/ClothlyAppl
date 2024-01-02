package com.clothly.ecommerce.data.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Point;
import android.support.design.widget.NavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.CustomMenuBaseActivity;
import com.clothly.ecommerce.data.data.helper.models.Featured;
import com.clothly.ecommerce.data.data.helper.models.HomeBanner;
import com.clothly.ecommerce.data.data.helper.models.ProductModel;
import com.clothly.ecommerce.data.data.helper.models.SliderMain;
import com.clothly.ecommerce.data.data.helper.response.MainPageResponse;
import com.clothly.ecommerce.data.data.helper.response.MainProductResponse;
import com.clothly.ecommerce.data.data.helper.response.UserRegistrationResponse;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.Loader;
import com.clothly.ecommerce.data.data.util.SharedPref;
import com.clothly.ecommerce.data.data.util.UIHelper;
import com.clothly.ecommerce.data.data.util.UtilityClass;
import com.clothly.ecommerce.data.databinding.ActivityMainBinding;
import com.clothly.ecommerce.data.ui.aboutus.AboutUsActivity;
import com.clothly.ecommerce.data.ui.category.CategoryActivity;
import com.clothly.ecommerce.data.ui.hearderview.SliderMainAdapter;
import com.clothly.ecommerce.data.ui.myfavourite.UserFavActivity;
import com.clothly.ecommerce.data.ui.ordercomplete.OrderCompleteActivity;
import com.clothly.ecommerce.data.ui.userLogin.LoginActivity;
import com.clothly.ecommerce.data.ui.userProfile.ProfileActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends CustomMenuBaseActivity<MainMvpView, MainPresenter> implements MainMvpView {

    List<SliderMain> sliderMainList = new ArrayList<>();
    ViewPager vpSliderMain;
    private LinearLayout dotsIndicators;

    TextView toobarTitle, tvEmail;
    private ImageView toobarLogo, ivProfileImage;
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView nvMainNav;
    private boolean isRegistered;
    private UserRegistrationResponse currentUser;

    private String userID = "";
    private Loader mLoader;
    private RecyclerView featureRecyclerView, categoryRecyclerView;
    FeatureProductAdapter adapterFeature;
    private CategoryRecylerViewAdapter mCategoryRecylerViewAdapter;

    MainProductResponse productResponse;
    NestedScrollView scrollView;
    ProgressBar progressBar;
    private ProductModel productModel;
    private View views;
    private ActivityMainBinding mBinding;
    boolean isLoadingDataInProgress = false;
    int pageNumber = 1;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRegistered = CustomSharedPrefs.getUserStatus(this);
        if (isRegistered) {
            currentUser = CustomSharedPrefs.getLoggedInUser(this);
        }
        settingNavDrawer();
        updateMenu();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getToolbarId() {
        return R.id.toolbar;
    }

    @Override
    protected void startUI() {
        mBinding = (ActivityMainBinding) getViewDataBinding();
        featureRecyclerView = findViewById(R.id.rv_feature_product);
        categoryRecyclerView = findViewById(R.id.rv_category_product);
        scrollView = findViewById(R.id.scroll_view);
        progressBar = findViewById(R.id.progress_bar);

        mLoader = new Loader(this);
        mLoader.show();

        isRegistered = CustomSharedPrefs.getUserStatus(this);

        initView();
        presenter.getHomeDataFromServer(this, String.valueOf(pageNumber));
        loadMore();

        printKeyHash(this);

    }

    /**
     * initializing views and adapter
     */
    private void initView() {
        settingToolBar();

        initFeatureRecyclerView();
        initCategoryRecyclerView();
        presenter.getAdMobCredential(this);
        presenter.getSettingCredential(this);
        if (isRegistered) {
            userID = CustomSharedPrefs.getLoggedInUserId(this);
        }

        presenter.setInterstitialApp(this);
    }

    /**
     * this api is used for pagination
     */
    private void loadMore() {

        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    if (!isLoadingDataInProgress){
                        // presenter.getHomeDataFromServer(this, String.valueOf(pageNumber));
                    }///Checking if retrofit is busy to load data or not
                       //Now recyclerview is in bottom . So loading more data .
                    //code to fetch more data for endless scrolling
                }
            }
        });
    }


    @Override
    protected void stopUI() {

    }

    /**
     * this method is for sliding latest offer
     */
    void settingMainSlider(List<SliderMain> sliderImageList) {
        int heightPx=220,widthPx=300;
        for (SliderMain sliderMain : sliderImageList) {
            sliderMainList.add(new SliderMain(sliderMain.getId(), sliderMain.getTag(),sliderMain.getImageName(),sliderMain.getResolution()));
            String heightFromServer = sliderMain.getResolution();
            String[] widthHeight = heightFromServer.split(":");
            heightPx=Integer.parseInt(widthHeight[1]);
            widthPx = Integer.parseInt(widthHeight[0]);

        }

        Display display =getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widthScreen = size.x;

        int actualHeight=(int)(heightPx*widthScreen)/widthPx;

        vpSliderMain = findViewById(R.id.vp_slider_main);
        ViewGroup.LayoutParams params = vpSliderMain.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        params.height = actualHeight;
        vpSliderMain.setLayoutParams(params);

        PagerAdapter sliderMainAdapter = new SliderMainAdapter(getSupportFragmentManager(), sliderMainList);
        vpSliderMain.setAdapter(sliderMainAdapter);

        dotsIndicators = findViewById(R.id.layout_slider_main_dots);
        dots(0);

        vpSliderMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                dots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }



    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @Override
    protected MainPresenter initPresenter() {
        return new MainPresenter();
    }

    /**
     * init {@link FeatureProductAdapter} with related recycler view
     */
    private void initFeatureRecyclerView() {
        adapterFeature = new FeatureProductAdapter(new ArrayList<>(), this);
        featureRecyclerView.setAdapter(adapterFeature);
        featureRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    /**
     * init {@link FeatureProductAdapter} with related recycler view
     */
    private void initCategoryRecyclerView() {
        mCategoryRecylerViewAdapter = new CategoryRecylerViewAdapter(new ArrayList<>(), this);
        categoryRecyclerView.setAdapter(mCategoryRecylerViewAdapter);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        categoryRecyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * adding slider dots
     *
     * @param current
     */
    private void dots(int current) {

        dotsIndicators.removeAllViews();
        if (sliderMainList != null) {
            for (int i = 0; i < sliderMainList.size(); i++) {
                TextView dot = new TextView(this);
                dot.setIncludeFontPadding(false);
                dot.setHeight((int) UtilityClass.convertDpToPixel(10, this));
                dot.setWidth((int) UtilityClass.convertDpToPixel(10, this));

                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                int marginAsDp = (int) UtilityClass.convertDpToPixel(4, this);
                params.setMargins(marginAsDp, marginAsDp, marginAsDp, marginAsDp);
                dot.setLayoutParams(params);

                dot.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_border_bg_1));

                if (i == current) {
                    dot.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_bg));
                }
                dotsIndicators.addView(dot);

            }
        }

    }


    /**
     * Data not found
     */
    private void setAllTitleGone() {
        mBinding.textViewFeaturedProducts.setVisibility(View.GONE);
    }


    /**
     * init toolbar with logo image
     */

    public void settingToolBar() {
        toolbar = findViewById(getToolbarId());
        toobarTitle = findViewById(R.id.toolbar_title);
        toobarLogo = findViewById(R.id.toolbar_logo);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.app_name));
        toobarTitle.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * setting navigation drawer
     */
    private void settingNavDrawer() {

        drawerLayout = findViewById(R.id.dl_navigation_drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_main_nav);
        View header = navigationView.getHeaderView(0);
        TextView tvName = header.findViewById(R.id.menu_profile_name);
        tvEmail = header.findViewById(R.id.menu_profile_email);
        UserRegistrationResponse user = CustomSharedPrefs.getLoggedInUser(this);
        String name = "", email = "";

        if (isRegistered) {
            if (user.userRegistrationInfo != null) {
                name = user.userRegistrationInfo.username;
                email = user.userRegistrationInfo.email;
            }
            tvName.setText(name);
            tvEmail.setText(email);

        } else {
            tvName.setText("Sign In");
            tvEmail.setText("");
        }


        ivProfileImage = header.findViewById(R.id.iv_menu_profile_image);
        UIHelper.setThumbImageUriInView(ivProfileImage, SharedPref.getSharedPref(this).read(Constants.Preferences.USER_PROFILE_IMAGE));


        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isRegistered) {
                    Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intentLogin);
                } else {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                overridePendingTransition(R.anim.enter, R.anim.exit);
                drawerLayout.closeDrawers();
            }
        });

        nvMainNav = findViewById(R.id.nv_main_nav);
        setLoginInfo();
        getNavigationItemClick(nvMainNav);

    }

    /**
     * getting click event of navigation drawer
     *
     * @param nvMainNav
     */
    private void getNavigationItemClick(NavigationView nvMainNav) {
        nvMainNav.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawers();
            switch (item.getItemId()) {

                case R.id.nav_category:
                    Intent intentCategory = new Intent(MainActivity.this, CategoryActivity.class);
                    startActivity(intentCategory);
                    break;

                case R.id.nav_orders:

                    if (!isRegistered) {
                        Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intentLogin);
                    } else {
                        Intent intentOrder = new Intent(MainActivity.this, OrderCompleteActivity.class);
                        startActivity(intentOrder);
                    }
                    break;


                case R.id.nav_favourites:
                    if (!isRegistered) {
                        Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intentLogin);
                    } else {
                        Intent intentFav = new Intent(MainActivity.this, UserFavActivity.class);
                        startActivity(intentFav);
                    }
                    break;

                case R.id.nav_logout:
                    if (isRegistered) {
                        showLogoutPopUp();

                    } else {
                        item.setTitle(getString(R.string.string_login));
                        Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intentLogin);
                    }
                    break;

                case R.id.nav_about_us:
                    AboutUsActivity.runActivity(this);
                    break;
            }

            overridePendingTransition(R.anim.enter, R.anim.exit);
            return false;
        });
    }

    /**
     * setting login logout text
     */
    private void setLoginInfo() {
        MenuItem loggedOut = nvMainNav.getMenu().findItem(R.id.nav_logout);
        if (!CustomSharedPrefs.getUserStatus(MainActivity.this))
            loggedOut.setTitle(getString(R.string.string_login));
        else loggedOut.setTitle(getString(R.string.string_logout));
    }

    /**
     * log out pop up alert
     */
    private void showLogoutPopUp() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Logout")
                .setMessage("Do you want to logout?")
                .setIcon(R.drawable.ic_logout)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        setLoginInfo();
                        CustomSharedPrefs.removeLoggedInUser(MainActivity.this);
                        isRegistered = false;
                        SharedPref.getSharedPref(MainActivity.this).write(Constants.Preferences.USER_PROFILE_IMAGE, "");
                        //UtilityClass.signOutGoogle(mGoogleApiClient, MainActivity.this);
                        UtilityClass.signOutFB(MainActivity.this);
                        UtilityClass.signOutEmail(MainActivity.this);
                        Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intentLogin);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    /*
    **
    * Generate Hash Key for facebook log in
    **/
    public void printKeyHash(Activity activity) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    activity.getApplicationContext().getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("FbKeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {

        }
    }


    @Override
    public void onGettingHomePageDataSuccess(MainPageResponse mainPageResponse) {
        isLoadingDataInProgress = true;
        List<Featured> featuredList = mainPageResponse.getData().getFeatured();
        List<HomeBanner> homeBannerList = mainPageResponse.getData().getHomeBanners();
        List<SliderMain> sliderImageList = mainPageResponse.getData().getSliderImages();
        if (sliderImageList != null) {
            settingMainSlider(sliderImageList);
        }


        if (featuredList != null) {
            adapterFeature.addItem(featuredList);
        }
        if (homeBannerList != null) {
            pageNumber = pageNumber + 1;
            mCategoryRecylerViewAdapter.addItem(homeBannerList);

        } else {
            mLoader.stopLoader();
        }

        mLoader.stopLoader();


    }

    @Override
    public void onGettingHomePageDataError(String error) {
        isLoadingDataInProgress = false;
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        mLoader.stopLoader();
    }
}
