package io.github.boldijar.cosasapp.parts.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

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

    @OnClick(R.id.login_location)
    void locatinClick() {
        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(data, this);
            mLocation.setTag(place);
            mLocation.setText(place.getName());
        }
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

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || age == 0 || mLocation.getTag() == null) {
            fillFieldsError();
            return;
        }
        Place place = (Place) mLocation.getTag();
        double latitude = place.getLatLng().latitude;
        double longitude = place.getLatLng().longitude;

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
                        if (response.isSuccess()) {
                            Toast.makeText(RegisterActivity.this, "Registering success", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error trying to register.", Toast.LENGTH_SHORT).show();

                        }
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
