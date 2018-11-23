package io.github.boldijar.cosasapp.parts.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.boldijar.cosasapp.R;
import io.github.boldijar.cosasapp.base.BaseActivity;
import io.github.boldijar.cosasapp.itecdata.Response;
import io.github.boldijar.cosasapp.itecdata.User;
import io.github.boldijar.cosasapp.server.Http;
import io.github.boldijar.cosasapp.util.RxUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.login_email)
    EditText mEmail;
    @BindView(R.id.login_password)
    EditText mPassword;
    @BindView(R.id.login_radius)
    SeekBar mRadius;
    @BindView(R.id.login_name)
    EditText mName;
    @BindView(R.id.login_gender)
    Spinner mSpinner;
    @BindView(R.id.login_location)
    TextView mLocation;
    @BindView(R.id.login_age)
    EditText mAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login_continue)
    void register() {
        String name = mName.getText().toString();
        String email = mEmail.getText().toString();
        int radius = mRadius.getProgress();
        int age;
        try {
            age = Integer.valueOf(mAge.getText().toString());
        } catch (NumberFormatException e) {
            age = 0;
        }
        String password = mPassword.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || age == 0) {
            fillFieldsError();
            return;
        }
        double latitude = 43.2;
        double longitude = 24.2;

        User user = new User(age, email, name, mSpinner.getSelectedItemPosition(), latitude, longitude, password, radius);
        Http.getInstance().getSwaggerService()
                .register(user)
                .compose(RxUtils.applySchedulers())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response response) {
                        Toast.makeText(RegisterActivity.this, "Status: " + response.isSuccess(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RegisterActivity.this, "Error trying to register.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    void fillFieldsError() {
        Toast.makeText(this, "Fix all input values.", Toast.LENGTH_SHORT).show();
    }

}
