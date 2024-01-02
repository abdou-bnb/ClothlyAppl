package com.clothly.ecommerce.data.ui.userLogin;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.BaseActivity;
import com.clothly.ecommerce.data.data.helper.response.UserRegistrationResponse;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.Loader;
import com.clothly.ecommerce.data.data.util.StatusBarHelper;
import com.clothly.ecommerce.data.ui.forgotpasswordsendemail.SendEmailActivity;
import com.clothly.ecommerce.data.ui.signinresendcode.SignInEmailSendActivity;
import com.clothly.ecommerce.data.ui.userRegistration.RegisterActivity;
import com.clothly.ecommerce.data.ui.welcome.WelcomeActivity;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends BaseActivity<LoginMvpView, LoginPresenter> implements LoginMvpView {

    Toolbar toolbar;
    public static final int RC_SIGN_IN = 111;
    Button btnUserLogin;
    EditText editTextEmail, editTextPassword;
    TextView textViewEmailError, textViewPasswordError, textViewResend, textViewForgotPassword;
    private boolean aBoolean, aBoolean1, aBoolean2, aBoolean3;
    LoginButton fBLogin;
    TextView btnFBLogin;
    CallbackManager callbackManager;
    private Loader mLoader;

    TextView btnGoogleLogin;
    GoogleApiClient googleApiClient;
    public static final int REQ_CODE = 9001;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_in;
    }

    @Override
    protected void startUI() {
        initToolbar();

        initViews();
        registeredToFB();
        printKeyHash(this);
        mLoader = new Loader(this);
    }

    /**
     * register to {@link CallbackManager} to get callback of fb registration
     */
    private void registeredToFB() {
        fBLogin = new LoginButton(this);
        callbackManager = CallbackManager.Factory.create();

        fBLogin.setReadPermissions("public_profile", "email");
        fBLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        presenter.getFBData(object, LoginActivity.this);
                    }
                });

                Bundle bundle = new Bundle();
                bundle.putString("fields", "id, email, first_name, last_name, link, birthday, location");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    /**
     * init views
     */
    private void initViews() {
        StatusBarHelper.getStatusBarTransparent(this, R.color.black);
        TextView textViewSignup = findViewById(R.id.text_view_sign_up);
        btnUserLogin = findViewById(R.id.button_sign_in);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        btnFBLogin = findViewById(R.id.text_view_fb);
        btnGoogleLogin = findViewById(R.id.text_view_google);
        textViewEmailError = findViewById(R.id.text_view_email_error);
        textViewPasswordError = findViewById(R.id.text_view_password_error);
        textViewResend = findViewById(R.id.text_view_resend_code);
        textViewForgotPassword = findViewById(R.id.text_view_forgot_password);

        setClickListener(textViewResend, textViewForgotPassword, btnFBLogin, btnUserLogin, textViewSignup, btnGoogleLogin);
        checkFieldValidity(editTextEmail, editTextPassword);

    }

    /**
     * init toolbar
     */
    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void stopUI() {

    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.text_view_fb:
                fBLogin.performClick();
                break;

            case R.id.text_view_sign_up:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;

            case R.id.button_sign_in:
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                if (!presenter.isEmpty(email) && !presenter.isEmpty(password)) {
                    if (aBoolean && aBoolean1 && aBoolean2 && presenter.emailValidity(email) && presenter.passwordValidity(password)) {
                        mLoader.show();
                        presenter.loginWithEmail(email, password, this);
                    } else {
                        Toast.makeText(this, getString(R.string.check_input_field), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(this, getString(R.string.fiil_field), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.text_view_google:
                // signIn();
                presenter.initGoogleClient(this);
                break;
            case R.id.text_view_resend_code:
                startActivity(new Intent(this, SignInEmailSendActivity.class));
                break;

            case R.id.text_view_forgot_password:
                startActivity(new Intent(this, SendEmailActivity.class));
                break;

        }
    }

    @Override
    protected LoginPresenter initPresenter() {
        return new LoginPresenter();
    }


    /**
     * this method check validation of input fiels
     *
     * @param editTextEmail
     * @param editTextPassword
     */

    private void checkFieldValidity(final EditText editTextEmail, final EditText editTextPassword) {

        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (editTextEmail.getText().toString().equals("")
                            || !presenter.emailValidity(editTextEmail.getText().toString())) {
                        textViewEmailError.setVisibility(View.VISIBLE);
                        textViewEmailError.setText(getString(R.string.valid_email));
                        aBoolean = false;
                    } else {
                        aBoolean = true;
                        textViewEmailError.setVisibility(View.INVISIBLE);
                    }
                } else {
                    aBoolean = !editTextEmail.getText().toString().equals("")
                            && presenter.emailValidity(editTextEmail.getText().toString());
                    textViewEmailError.setVisibility(View.INVISIBLE);
                }
            }
        });
        editTextPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!aBoolean3) {
                if (!hasFocus) {
                    if (editTextPassword.getText().toString().equals("") || !presenter.passwordValidity(editTextPassword.getText().toString())) {
                        textViewPasswordError.setVisibility(View.VISIBLE);
                        textViewPasswordError.setText(getString(R.string.need_6_char));
                        aBoolean1 = false;
                    } else {
                        aBoolean1 = true;
                        textViewPasswordError.setVisibility(View.INVISIBLE);
                    }
                } else {
                    aBoolean1 = !editTextPassword.getText().toString().equals("")
                            && presenter.passwordValidity(editTextPassword.getText().toString());
                    textViewPasswordError.setVisibility(View.INVISIBLE);
                }
            }
        });


        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().contains(" ")) {
                    aBoolean2 = false;
                    aBoolean3 = true;
                    textViewPasswordError.setVisibility(View.VISIBLE);
                    textViewPasswordError.setText(getString(R.string.no_space));
                } else {
                    aBoolean2 = true;
                    aBoolean3 = false;
                    textViewPasswordError.setVisibility(View.INVISIBLE);
                }

                if (presenter.passwordValidity(editTextPassword.getText().toString())) {
                    aBoolean1 = true;
                }
            }
        });

        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (presenter.emailValidity(editTextEmail.getText().toString())) {
                    aBoolean = true;
                }
            }
        });
    }

    /**
     * save logged user credential to shared pref
     * @param user
     */
    private void loginResponse(UserRegistrationResponse user) {
        CustomSharedPrefs.setRegisteredUser(this);
        CustomSharedPrefs.setLoggedInUser(LoginActivity.this, user);
        Intent intentCategory = new Intent(LoginActivity.this, WelcomeActivity.class);
        startActivity(intentCategory);
    }

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    public void signout() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Log.d("emailGoogl", "Loggedout");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            presenter.getGoogleData(googleSignInResult, LoginActivity.this);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * to get debug hash key for fb
     * @param context
     * @return debug hashkey
     */
    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            String packageName = context.getApplicationContext().getPackageName();
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                Log.d("FBKeyHash=", key);
                //Log.d("gogoleKeyHash=", md.toString());
            }
        } catch (PackageManager.NameNotFoundException e1) {
        } catch (NoSuchAlgorithmException e) {
        } catch (Exception e) {
        }
        return key;
    }

    @Override
    public void onEmailSignUpSuccess(UserRegistrationResponse user) {
        if (user != null) {
            if (user.statusCode == 200) {
                loginResponse(user);
            } else {
                Toast.makeText(this, user.message, Toast.LENGTH_SHORT).show();
            }
        }
        mLoader.stopLoader();
    }

    @Override
    public void onEmainSignUpError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        mLoader.stopLoader();
    }

    @Override
    public void onSocialSignUpSuccess(UserRegistrationResponse user) {
        if (user != null) {
            if (user.statusCode == 200) {
                if(user.userRegistrationInfo.email.equals("a")){
                    user.userRegistrationInfo.email = "";
                }
                CustomSharedPrefs.setLoggedInUser(LoginActivity.this, user);
                CustomSharedPrefs.setRegisteredUser(this);
                Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, user.message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSocialSignUpError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
