package navtech.com.rameshordermanagement.ui.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;

import com.jakewharton.rxbinding3.widget.RxTextView;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import navtech.com.rameshordermanagement.R;
import navtech.com.rameshordermanagement.databinding.ActivityLoginBinding;
import navtech.com.rameshordermanagement.models.User;
import navtech.com.rameshordermanagement.presenters.LoginPresenter;
import navtech.com.rameshordermanagement.ui.views.LoginView;
import navtech.com.rameshordermanagement.utils.TinyDB;
import navtech.com.rameshordermanagement.utils.Utils;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements LoginView, View.OnClickListener {
    ActivityLoginBinding binding;
    LoginPresenter loginPresenter;
    private TinyDB tinyDB;
    int logged_user_id;
    Observable<Boolean> observable;
    boolean isRememberChecked = false;
    boolean isuserRemembered = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        tinyDB = new TinyDB(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);
        binding.setUser(new User());
        loginPresenter = new LoginPresenter(this);
        if (!tinyDB.getBoolean("users_exist")) {
            try {
                showProgressDialog();
                loginPresenter.loadJsonFromAssets();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        binding.btnSingin.setOnClickListener(this);
        initRxFormsValidations();
        binding.cbxRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    isRememberChecked = true;
                else isRememberChecked = false;

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        logged_user_id = tinyDB.getInt("user_id");
        isuserRemembered = tinyDB.getBoolean("isuserRemembered");
        if (logged_user_id != 0) {
            if (isuserRemembered) {
                Intent intent = new Intent(LoginActivity.this,
                        MainActivity.class);
                startActivity(intent);
                ComponentName cn = intent.getComponent();
                Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                startActivity(mainIntent);
                finish();
                return;
            } else {
                tinyDB.remove("user");
            }

        }
    }

    @Override
    public void error(String error) {
        hideProgressDialog();
        showToast(error);
    }

    @Override
    public void onLoginSuccess(int user_id, String user_name) {
        tinyDB.putInt("user_id", user_id);
        tinyDB.putString("username", user_name);
        tinyDB.putBoolean("isuserRemembered", isRememberChecked);
        hideProgressDialog();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    @Override
    public void usersSavedinDB(boolean condition) {
        hideProgressDialog();
        if (!condition)
            error("Something went wrong");
        else tinyDB.putBoolean("users_exist", true);
    }

    @Override
    public void areUsersExists(boolean condition) {
        hideProgressDialog();
        if (!condition) {
            showProgressDialog();
            try {
                loginPresenter.loadJsonFromAssets();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        showProgressDialog();
        loginPresenter.login(binding.getUser());
    }

    private void initRxFormsValidations() {
        Observable<String> nameObservable = RxTextView.textChanges(binding.editUsername).skip(1).map(new Function<CharSequence, String>() {
            @Override
            public String apply(CharSequence charSequence) throws Exception {
                return charSequence.toString();
            }
        });
        Observable<String> passwordObservable = RxTextView.textChanges(binding.editPassword).skip(1).map(new Function<CharSequence, String>() {
            @Override
            public String apply(CharSequence charSequence) throws Exception {
                return charSequence.toString();
            }
        });


        observable = Observable.combineLatest(nameObservable, passwordObservable, new BiFunction<String, String, Boolean>() {
            @Override
            public Boolean apply(String s, String s2) throws Exception {
                return isValidForm(s, s2);
            }
        });


        observable.subscribe(new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                updateButton(aBoolean);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });

    }

    public void updateButton(boolean valid) {
        if (valid)
            binding.btnSingin.setEnabled(true);
        else binding.btnSingin.setEnabled(false);
    }

    public boolean isValidForm(String email, String password) {
        boolean validEmail = !email.isEmpty() && Utils.isEmailValid(email);

        if (!validEmail) {
            binding.tiEmail.setError("Please enter valid email");
        } else {
            binding.tiEmail.setError("");
        }

        boolean validPass = !password.isEmpty() && password.length() >= 8 && password.length() <=
                13;
        if (!validPass) {
            binding.tiPassword.setError("Password should be minimun 8 to 13 characters " +
                    "long" +
                    " ");
        } else {
            binding.tiPassword.setError("");
        }
        return validEmail && validPass;
    }


}

