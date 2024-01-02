package com.clothly.ecommerce.data.ui.userRegistration;

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
import com.clothly.ecommerce.data.ui.emailverification.EmailVerificationActivity;
import com.clothly.ecommerce.data.ui.userLogin.LoginActivity;


public class RegisterActivity extends BaseActivity<RegisterMvpView, RegisterPresenter> implements RegisterMvpView {

    Button buttonSignUp;
    Toolbar toolbar;
    EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;
    TextView textViewNameError, textViewEmailError, textViewPasswordError, textViewConfirmPasswordError, textViewSignUp;
    private boolean aBoolean, aBoolean1, aBoolean2, aBoolean3, aBoolean4, aBoolean5;
    public static String EMAIL_INTENT_KEY = "email";
    private Loader mLoader;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected void startUI() {
        StatusBarHelper.getStatusBarTransparent(this, R.color.black);
        initToolbar();
        initViews();
        mLoader = new Loader(this);
    }

    /**
     * initializing view with data
     */
    private void initViews() {
        editTextName = findViewById(R.id.edit_text_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextConfirmPassword = findViewById(R.id.edit_text_confirm_password);

        textViewNameError = findViewById(R.id.text_view_name_error);
        textViewEmailError = findViewById(R.id.text_view_email_error);
        textViewPasswordError = findViewById(R.id.text_view_password_error);
        textViewConfirmPasswordError = findViewById(R.id.text_view_confirm_password_error);
        textViewSignUp = findViewById(R.id.text_view_sign_up);


        buttonSignUp = findViewById(R.id.button_sign_in);
        buttonSignUp.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
        checkFieldValidity();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_sign_in:
                getUserInput();
                break;
            case R.id.text_view_sign_up:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }

    @Override
    protected RegisterPresenter initPresenter() {
        return new RegisterPresenter();
    }

    /**
     * this method check validation of input fields
     */
    private void checkFieldValidity() {
        editTextName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (editTextName.getText().toString().equals("")
                            || !presenter.nameValidity(editTextName.getText().toString())) {
                        textViewNameError.setVisibility(View.VISIBLE);
                        textViewNameError.setText(getString(R.string.need_2_char));
                        aBoolean = false;
                    } else {
                        aBoolean = true;
                        textViewNameError.setVisibility(View.INVISIBLE);
                    }
                } else {
                    aBoolean = !editTextName.getText().toString().equals("")
                            && presenter.nameValidity(editTextName.getText().toString());
                    textViewNameError.setVisibility(View.INVISIBLE);
                }
            }
        });
        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (editTextEmail.getText().toString().equals("")
                            || !presenter.emailValidity(editTextEmail.getText().toString())) {
                        textViewEmailError.setVisibility(View.VISIBLE);
                        textViewEmailError.setText(getString(R.string.valid_email));
                        aBoolean1 = false;
                    } else {
                        aBoolean1 = true;
                        textViewEmailError.setVisibility(View.INVISIBLE);
                    }
                } else {
                    aBoolean1 = !editTextEmail.getText().toString().equals("")
                            && presenter.emailValidity(editTextEmail.getText().toString());
                    textViewEmailError.setVisibility(View.INVISIBLE);
                }
            }
        });
        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!aBoolean5) {
                    if (!hasFocus) {
                        if (editTextPassword.getText().toString().equals("")
                                || !presenter.passwordValidity(editTextPassword.getText().toString())) {
                            textViewPasswordError.setVisibility(View.VISIBLE);
                            textViewPasswordError.setText(getString(R.string.need_6_char));
                            aBoolean2 = false;
                        } else {
                            aBoolean2 = true;
                            textViewPasswordError.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        aBoolean2 = !editTextPassword.getText().toString().equals("")
                                && presenter.passwordValidity(editTextPassword.getText().toString());
                        textViewPasswordError.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                 if((editTextPassword.getText().toString().equals("")
                         || !presenter.passwordValidity(editTextPassword.getText().toString()))){
                     aBoolean = false;
                }else {
                     aBoolean = true;
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
                if((editTextPassword.getText().toString().equals("")
                        || !presenter.passwordValidity(editTextPassword.getText().toString()))){
                    aBoolean1 = false;
                }else {
                    aBoolean1 = true;
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
                    aBoolean3 = false;
                    aBoolean5 = true;
                    textViewPasswordError.setVisibility(View.VISIBLE);
                    textViewPasswordError.setText(getString(R.string.no_space));
                } else {
                    aBoolean3 = true;
                    aBoolean5 = false;
                    textViewPasswordError.setVisibility(View.INVISIBLE);
                }
                if (presenter.isConfirmPasswordMatch(editTextPassword.getText().toString(),
                        editTextConfirmPassword.getText().toString())
                        || editTextConfirmPassword.getText().toString().equals("")) {
                    textViewConfirmPasswordError.setVisibility(View.INVISIBLE);
                    aBoolean4 = true;
                } else {
                    aBoolean4 = false;
                    textViewConfirmPasswordError.setVisibility(View.VISIBLE);
                    textViewConfirmPasswordError.setText(getString(R.string.password_mismatch));
                }
            }
        });

        editTextConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!presenter.isConfirmPasswordMatch(editTextPassword.getText().toString()
                        , s.toString())) {
                    aBoolean4 = false;
                    textViewConfirmPasswordError.setVisibility(View.VISIBLE);
                    textViewConfirmPasswordError.setText(getString(R.string.password_mismatch));
                } else {
                    aBoolean4 = true;
                    textViewConfirmPasswordError.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**
     * getdata from input field
     */
    private void getUserInput() {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        if (!name.equals("") && !email.equals("") && !password.equals("") && !confirmPassword.equals("")) {
            if (aBoolean && aBoolean1 && aBoolean2 && aBoolean3 && aBoolean4
                    && presenter.emailValidity(email) && presenter.nameValidity(name)
                    && presenter.passwordValidity(password)) {
                mLoader.show();
                presenter.userRegistration(name, email, password, this);
            } else {
                Toast.makeText(this, getString(R.string.check_input_field), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.fiil_field), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSignUpSuccess(UserRegistrationResponse user) {
        if (user != null) {
            if (user.statusCode == 200) {
                mLoader.stopLoader();
                Intent intent = new Intent(RegisterActivity.this, EmailVerificationActivity.class);
                intent.putExtra(EMAIL_INTENT_KEY, user.userRegistrationInfo.email);
                startActivity(intent);
            } else {
                Toast.makeText(this, user.message, Toast.LENGTH_LONG).show();
            }
        }
        mLoader.stopLoader();
    }

    @Override
    public void onSignUpError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        mLoader.stopLoader();
    }
}
