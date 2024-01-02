package com.clothly.ecommerce.data.ui.signinresendcode;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.BaseActivity;
import com.clothly.ecommerce.data.data.helper.response.UserRegistrationResponse;
import com.clothly.ecommerce.data.data.util.Loader;
import com.clothly.ecommerce.data.data.util.StatusBarHelper;
import com.clothly.ecommerce.data.ui.signinemailverification.SignInEmailVerificationActivity;

public class SignInEmailSendActivity extends BaseActivity<SignInEmailSendMvpView, SignInEmailSendPresenter> implements SignInEmailSendMvpView {

    private EditText editTextEmail;
    private TextView textViewAlreadyHaveCode, textViewError;
    private Button buttonContinue;
    private String email;
    private Loader mLoader;
    public static String EMAIL_KEY = "email";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_in_email_send;
    }

    @Override
    protected void startUI() {
        initToolbar();
        StatusBarHelper.getStatusBarTransparent(this, R.color.black);
        mLoader = new Loader(this);
        initViews();
    }

    /**
     * view initializing
     */
    private void initViews() {
        editTextEmail = findViewById(R.id.edit_text_email);
        textViewAlreadyHaveCode = findViewById(R.id.text_view_already_have_code);
        textViewError = findViewById(R.id.text_view_email_error);
        buttonContinue = findViewById(R.id.button_sign_in);

        setClickListener(textViewAlreadyHaveCode, buttonContinue);
        checkValidity();
    }

    /**
     * checking validation of field
     */
    private void checkValidity() {
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                textViewError.setVisibility(View.INVISIBLE);
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


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.text_view_already_have_code:
                Intent intent = new Intent(this, SignInEmailVerificationActivity.class);
                intent.putExtra(EMAIL_KEY, "");
                startActivity(intent);
                break;

            case R.id.button_sign_in:
                email = editTextEmail.getText().toString();
                if (presenter.emailValidity(email)) {
                    mLoader.show();
                    presenter.resendCode(email, this);
                } else {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText(getString(R.string.valid_email));
                }
        }
    }

    @Override
    protected void stopUI() {

    }

    @Override
    protected SignInEmailSendPresenter initPresenter() {
        return new SignInEmailSendPresenter();
    }

    @Override
    public void onGetCodeSuccess(UserRegistrationResponse user) {
        if (user != null) {
            if (user.statusCode == 200) {
                Intent intent = new Intent(this, SignInEmailVerificationActivity.class);
                intent.putExtra(EMAIL_KEY, email);
                startActivity(intent);
            } else {
                Toast.makeText(this, user.message, Toast.LENGTH_SHORT).show();
            }
        }
        mLoader.stopLoader();
    }

    @Override
    public void onGetCodeError(String errorMessage) {
        mLoader.stopLoader();
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
