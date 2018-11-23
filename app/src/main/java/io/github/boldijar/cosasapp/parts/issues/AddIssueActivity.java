package io.github.boldijar.cosasapp.parts.issues;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
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
import io.github.boldijar.cosasapp.itecdata.Issue;
import io.github.boldijar.cosasapp.server.Http;
import io.github.boldijar.cosasapp.util.Prefs;
import io.github.boldijar.cosasapp.util.RxUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class AddIssueActivity extends BaseActivity {

    @BindView(R.id.add_issue_location)
    TextView mLocation;
    @BindView(R.id.add_issue_message)
    EditText mMessage;
    @BindView(R.id.add_issue_title)
    EditText mTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_issue);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.add_issue_location)
    void locationClick() {
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

    @OnClick(R.id.issue_add_save)
    void save() {
        String title = mTitle.getText().toString();
        String message = mMessage.getText().toString();
        Place place = (Place) mLocation.getTag();
        if (place == null || TextUtils.isEmpty(title) || TextUtils.isEmpty(message)) {
            Toast.makeText(this, "Fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }
        Issue issue = new Issue(title, message, place.getLatLng().latitude, place.getLatLng().longitude, Prefs.getItecUser().getId());
        Http.getInstance().getSwaggerService()
                .createIssue(issue)
                .compose(RxUtils.applySchedulers())
                .subscribe(new Observer<Issue>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Issue issues) {
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(AddIssueActivity.this, "Error adding issue.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
