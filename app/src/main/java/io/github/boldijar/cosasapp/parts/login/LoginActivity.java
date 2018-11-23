package io.github.boldijar.cosasapp.parts.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.boldijar.cosasapp.R;
import io.github.boldijar.cosasapp.base.BaseActivity;
import io.github.boldijar.cosasapp.itecdata.LoginResponse;
import io.github.boldijar.cosasapp.itecdata.UserResponse;
import io.github.boldijar.cosasapp.parts.home.HomeActivity;
import io.github.boldijar.cosasapp.server.Http;
import io.github.boldijar.cosasapp.util.Prefs;
import io.github.boldijar.cosasapp.util.RxUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_email)
    EditText mEmail;
    @BindView(R.id.login_password)
    EditText mPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Prefs.Token.get() != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login_continue)
    void login() {
        Http.getInstance().getSwaggerService().login(mEmail.getText().toString(),
                mPassword.getText().toString(),
                "password")
                .compose(RxUtils.applySchedulers())
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        Prefs.Token.put(loginResponse.getToken());
                        loadUser();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Error logging in.", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void loadUser() {
        Http.getInstance().getSwaggerService().getUser(mEmail.getText().toString())
                .compose(RxUtils.applySchedulers())
                .subscribe(new Observer<UserResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserResponse userResponse) {
                        userResponse.getUser().setEmail(mEmail.getText().toString());
                        Prefs.User.putAsJson(userResponse.getUser());
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick(R.id.login_create_account)
    void createAccount() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

}
