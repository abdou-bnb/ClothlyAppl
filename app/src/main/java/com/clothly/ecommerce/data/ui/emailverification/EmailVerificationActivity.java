package com.clothly.ecommerce.data.ui.emailverification;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.BaseActivity;
import com.clothly.ecommerce.data.data.helper.response.UserRegistrationResponse;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.Loader;
import com.clothly.ecommerce.data.data.util.StatusBarHelper;
import com.clothly.ecommerce.data.ui.forgotpasswordsendemail.SendEmailActivity;
import com.clothly.ecommerce.data.ui.updatepassword.UpdatePasswordActivity;
import com.clothly.ecommerce.data.ui.userRegistration.RegisterActivity;
import com.clothly.ecommerce.data.ui.welcome.WelcomeActivity;

public class EmailVerificationActivity extends BaseActivity<EmailVerificationMvpView, EmailVerificationPresenter>
        implements EmailVerificationMvpView {

    private TextView textViewEmail, textViewResendCode, textViewCounter;
    private Button buttonContinue;
    private PinEntryEditText editTextPinCode;
    private String email, pinCode;
    private boolean isFromForgotPassword;
    private Loader mLoader;
    public static String PIN_CODE_KEY = "verify_code";
    public static String EMAIL_KEY = "mail";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_email_verification;
    }

    @Override
    protected void startUI() {
        initToolbar();
        StatusBarHelper.getStatusBarTransparent(this, R.color.black);
        getDataFromIntent();
        initViews();
    }

    /**
     * extracting data from intent
     */
    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            email = intent.getStringExtra(RegisterActivity.EMAIL_INTENT_KEY);
            isFromForgotPassword = intent.getBooleanExtra(SendEmailActivity.IS_FROMFORGOT_PASSWORD, false);
        }
    }

    /**
     * initializing views with data
     */
    private void initViews() {
        textViewEmail = findViewById(R.id.text_view_email);
        textViewResendCode = findViewById(R.id.text_view_resend);
        textViewCounter = findViewById(R.id.text_view_counter);
        editTextPinCode = findViewById(R.id.edit_text_email);
        buttonContinue = findViewById(R.id.button_sign_in);

        if (email != null)
            textViewEmail.setText(email);
        setClickListener(textViewResendCode, buttonContinue);

        mLoader = new Loader(this);
        callCounter();
        getPinCodeFromUser();
    }

    /**
     * getting pin code from edittext
     */
    private void getPinCodeFromUser() {
        editTextPinCode.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                if (str.length() == 4) {
                    pinCode = String.valueOf(str);
                }
            }
        });
    }

    /**
     * init toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * this method start a counter for resend email
     */
    private void callCounter() {
        new CountDownTimer(Constants.DefaultValue.DELAY_INTERVAL_VISIBILITY, Constants.DefaultValue.DELAY_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                textViewCounter.setText("" + millisUntilFinished / Constants.DefaultValue.DELAY_INTERVAL);
            }

            public void onFinish() {
                textViewResendCode.setEnabled(true);
                textViewResendCode.setTextColor(getResources().getColor(R.color.colorWhite));
                textViewCounter.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.button_sign_in:
                if (pinCode.length() == 4 && email != null) {
                    mLoader.show();
                    presenter.doEmailVerification(email, pinCode, this);
                } else {
                    Toast.makeText(this, getString(R.string.check_input_field), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.text_view_resend:
                if (email != null) {
                    presenter.resendCode(email, this);
                    textViewResendCode.setEnabled(false);
                    textViewResendCode.setTextColor(getResources().getColor(R.color.colorGrey));
                    textViewCounter.setVisibility(View.VISIBLE);
                    callCounter();
                }
                break;

        }
    }

    @Override
    protected void stopUI() {

    }

    @Override
    protected EmailVerificationPresenter initPresenter() {
        return new EmailVerificationPresenter();
    }

    @Override
    public void onEmailVerificationSuccess(UserRegistrationResponse user) {
        if (user != null) {
            if (user.statusCode == 200) {
                if (isFromForgotPassword) {
                    mLoader.stopLoader();
                    Intent intent = new Intent(this, UpdatePasswordActivity.class);
                    intent.putExtra(PIN_CODE_KEY, user.userRegistrationInfo.verifyToken);
                    intent.putExtra(EMAIL_KEY, email);
                    startActivity(intent);
                } else {
                    mLoader.stopLoader();
                    CustomSharedPrefs.setLoggedInUser(this, user);
                    CustomSharedPrefs.setRegisteredUser(this);
                    startActivity(new Intent(this, WelcomeActivity.class));
                    finish();
                }

            } else {
                Toast.makeText(this, user.message, Toast.LENGTH_SHORT).show();
                mLoader.stopLoader();
            }
        }

    }

    @Override
    public void onEmailVeridicationError(String errorMessage) {
        mLoader.stopLoader();
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResendCodeSuccess(UserRegistrationResponse user) {
        if (user != null) {
            if (user.statusCode == 200) {
          /*      textViewResendCode.setEnabled(false);
                textViewResendCode.setTextColor(getResources().getColor(R.color.colorGrey));
                textViewCounter.setVisibility(View.VISIBLE);
                callCounter();*/
            } else {
                Toast.makeText(this, user.message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResendCodeError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
