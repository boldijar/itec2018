package io.github.boldijar.cosasapp.parts.profile;

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
import io.github.boldijar.cosasapp.itecdata.User;
import io.github.boldijar.cosasapp.itecdata.UserResponse;
import io.github.boldijar.cosasapp.server.Http;
import io.github.boldijar.cosasapp.util.Prefs;
import io.github.boldijar.cosasapp.util.RxUtils;
import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProfileActivity extends BaseActivity {

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

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        Http.getInstance().getSwaggerService().getUser(Prefs.getItecUser().getEmail())
                .compose(RxUtils.applySchedulers())
                .subscribe(new Observer<UserResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserResponse userResponse) {
                        gotUser(userResponse.getUser());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void gotUser(User user) {
        mUser = user;
        mAge.setText(user.getAge() + "");
        mLocation.setText(user.getLatitude() + ", " + user.getLongitude());
        mName.setText(user.getFullName());
        mEmail.setText(Prefs.getItecUser().getEmail());
        mEmail.setEnabled(false);
        mSpinner.setSelection(user.getGender());
        mRadius.setProgress(user.getRadius());
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

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || age == 0) {
            fillFieldsError();
            return;
        }
        double latitude = 0, longitude = 0;
        if (mLocation.getTag() != null) {
            Place place = (Place) mLocation.getTag();
            latitude = place.getLatLng().latitude;
            longitude = place.getLatLng().longitude;
        } else {
            latitude = mUser.getLatitude();
            longitude = mUser.getLongitude();
        }

        User user = new User(age, email, name, mSpinner.getSelectedItemPosition(), latitude, longitude, null, radius,Prefs.getItecUser().getId());
        Http.getInstance().getSwaggerService()
                .updateProfile(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });

    }

    void fillFieldsError() {
        Toast.makeText(this, "Fix all input values.", Toast.LENGTH_SHORT).show();
    }

}
