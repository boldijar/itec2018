package io.github.boldijar.cosasapp.parts.issues;

import android.content.Context;
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
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditIssueActivity extends BaseActivity {

    @BindView(R.id.add_issue_location)
    TextView mLocation;
    @BindView(R.id.add_issue_message)
    EditText mMessage;
    @BindView(R.id.add_issue_title)
    EditText mTitle;
    private Issue mIssue;

    public static Intent createIntent(Issue issue, Context context) {
        Intent intent = new Intent(context, EditIssueActivity.class);
        intent.putExtra("issue", issue);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_issue);
        ButterKnife.bind(this);
        mIssue = (Issue) getIntent().getSerializableExtra("issue");
        mLocation.setText(mIssue.getLatitude() + ", " + mIssue.getLongitude());
        mTitle.setText(mIssue.getTitle());
        mMessage.setText(mIssue.getDescription());
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
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(message)) {
            Toast.makeText(this, "Fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }
        double latitude;
        double longitude;
        if (mLocation.getTag() != null) {
            Place place = (Place) mLocation.getTag();
            latitude = place.getLatLng().latitude;
            longitude = place.getLatLng().longitude;
        } else {
            latitude = mIssue.getLatitude();
            longitude = mIssue.getLongitude();
        }
        Issue issue = new Issue(title, message, latitude, longitude, Prefs.getItecUser().getId(), mIssue.getId());
        Http.getInstance().getSwaggerService()
                .updateIssue(issue)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(EditIssueActivity.this, "Error updating issue.", Toast.LENGTH_SHORT).show();

                        e.printStackTrace();
                    }
                });
    }
}
