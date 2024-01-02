package com.clothly.ecommerce.data.ui.updatepassword;

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

public class UpdatePasswordActivity extends BaseActivity<UpdatePasswordMvpView, UpdatePasswordPresenter> implements UpdatePasswordMvpView {
    private String email, verifyCode;
    private TextView textViewPasswordError, textViewConfirmPasswordError;
    private EditText editTextPassword, editTextConfirmPassword;
    private Button buttonContinue;
    private Loader mLoader;
    private boolean aBoolean3, aBoolean5, aBoolean4, aBoolean2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_password;
    }

    @Override
    protected void startUI() {
        initToolbar();
        StatusBarHelper.getStatusBarTransparent(this, R.color.black);
        getDataFromIntent();
        initView();
        checkValidation();
        mLoader = new Loader(this);
    }

    /**
     * input field validation checking
     */
    private void checkValidation() {
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

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            email = intent.getStringExtra(EmailVerificationActivity.EMAIL_KEY);
            verifyCode = intent.getStringExtra(EmailVerificationActivity.PIN_CODE_KEY);
        }
    }

    /**
     * initializing views
     */
    private void initView() {
        textViewPasswordError = findViewById(R.id.text_view_password_error);
        textViewConfirmPasswordError = findViewById(R.id.text_view_confirm_password_error);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextConfirmPassword = findViewById(R.id.edit_text_confirm_password);
        buttonContinue = findViewById(R.id.button_sign_in);

        setClickListener(buttonContinue);
    }

    /**
     * initializing toolbar
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
        if (view.getId() == R.id.button_sign_in) {
            getDataFromView();
        }
    }

    /**
     * get data from input and hit server
     */
    private void getDataFromView() {
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        if (!verifyCode.equals("") && !email.equals("") && !password.equals("") && !confirmPassword.equals("")) {
            if (presenter.passwordValidity(password)) {
                if (presenter.isConfirmPasswordMatch(password, confirmPassword)) {
                    if (aBoolean2 && aBoolean3 && aBoolean4) {
                        mLoader.show();
                        presenter.updatePassword(email, verifyCode, password, this);
                    } else {
                        Toast.makeText(this, getString(R.string.check_input_field), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.password_mismatch), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, getString(R.string.need_6_char), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.fiil_field), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void stopUI() {

    }

    @Override
    protected UpdatePasswordPresenter initPresenter() {
        return new UpdatePasswordPresenter();
    }

    @Override
    public void onPasswordUpdateSuccess(UserRegistrationResponse user) {
        if (user != null) {
            if (user.statusCode == 200) {
                mLoader.stopLoader();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, user.message, Toast.LENGTH_SHORT).show();
            }
        }
        mLoader.stopLoader();
    }

    @Override
    public void onPasswordUpdateError(String errorMessage) {
        mLoader.stopLoader();
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
