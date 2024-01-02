package com.clothly.ecommerce.data.ui.userLogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.gson.Gson;
import com.clothly.ecommerce.data.data.helper.base.BasePresenter;
import com.clothly.ecommerce.data.data.helper.response.UserRegistrationResponse;
import com.clothly.ecommerce.data.data.provider.retrofit.RetrofitClient;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.NetworkHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginPresenter extends BasePresenter<LoginMvpView> {

    private GoogleSignInClient mGoogleSignInClient;

    /**
     * checking email validation
     */
    public boolean emailValidity(String text) {
        return Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }

    /**
     * checking password validation
     */
    public boolean passwordValidity(String text) {
        return text.length() >= Constants.DefaultValue.MINIMUM_LENGTH_PASS
                && text.length() <= Constants.DefaultValue.MAXIMUM_LENGTH;
    }

    public boolean isEmpty(String text) {
        return text.equals("");
    }

    /**
     * this api is called for logging with email
     *
     * @param email
     * @param password
     * @param context
     */
    public void loginWithEmail(final String email, final String password, final Context context) {
        String JSON_URL = Constants.ServerUrl.ROOT_URL + Constants.ServerUrl.LOGIN_URL;
        RequestQueue queue = Volley.newRequestQueue(context);

        if (NetworkHelper.hasNetworkAccess(context)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    UserRegistrationResponse user = new Gson().fromJson(response, UserRegistrationResponse.class);
                    getMvpView().onEmailSignUpSuccess(user);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("responseV", error.getMessage());
                    getMvpView().onEmainSignUpError(error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
                    params.put("api_token", Constants.ServerUrl.API_TOKEN);
                    return params;
                }
            };

            queue.getCache().clear();
            queue.add(stringRequest);
        } else {
            Toast.makeText(context, "Please check internet connection!", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * this api is used to sign up with fb and google
     *
     * @param name
     * @param email
     * @param password
     * @param type
     * @param socialId
     * @param context
     */
    public void userRegistration(final String name
            , final String email
            , final String password
            , final String type
            , final String socialId
            , final Context context) {

        RetrofitClient.getApiService().socialRegistration(Constants.ServerUrl.API_TOKEN, type, socialId, name, email, password).enqueue(new Callback<UserRegistrationResponse>() {
            @Override
            public void onResponse(Call<UserRegistrationResponse> call, retrofit2.Response<UserRegistrationResponse> response) {
                if (response.body() != null)
                    getMvpView().onSocialSignUpSuccess(response.body());
            }

            @Override
            public void onFailure(Call<UserRegistrationResponse> call, Throwable t) {
                getMvpView().onSocialSignUpError(t.getMessage());
            }
        });

    }


    /**
     * getting all credential for fb log in
     *
     * @param object
     * @param context
     */
    public void getFBData(JSONObject object, Context context) {
        String profileUrl = "";
        try {
            URL profilePicture = new URL("https://graph.facebook.com/" + object.getString("id") + "/picture?width=200&height=200");
            String id = object.getString("id");
            if (profilePicture.toString() != null) {
                profileUrl = profilePicture.toString();
            }
            String firstName = object.getString("first_name");
            String lastName = object.getString("last_name");
            if (id != null) {
                userRegistration(firstName, "a", "a",
                        "" + Constants.RegistrationType.FACEBOOK_SIGN_UP, id, context);


                //  CustomSharedPrefs.setProfileUrl(mActivity, profileUrl);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * getting data for google log in
     *
     * @param googleSignInResult
     * @param context
     */
    public void getGoogleData(GoogleSignInResult googleSignInResult, Context context) {
        if (googleSignInResult.isSuccess()) {
            GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
            String profileUrl = null;
            String id = googleSignInAccount.getId();
            String email = googleSignInAccount.getEmail();
            String name = googleSignInAccount.getDisplayName();
            if (googleSignInAccount.getPhotoUrl() != null) {
                profileUrl = googleSignInAccount.getPhotoUrl().toString();
            }
            if (id != null) {
                userRegistration(name, email, "",
                        "" + Constants.RegistrationType.GOOGLE_SIGN_UP, id, context);


                CustomSharedPrefs.setProfileUrl(context, profileUrl);
            }
        } else {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * initialized Google client for google sign up
     *
     * @param activity
     */
    void initGoogleClient(Activity activity) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, LoginActivity.RC_SIGN_IN);
    }
}
