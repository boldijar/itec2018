package io.github.boldijar.cosasapp.parts.issues;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.boldijar.cosasapp.R;
import io.github.boldijar.cosasapp.base.BaseActivity;
import io.github.boldijar.cosasapp.base.FastAdapter;
import io.github.boldijar.cosasapp.itecdata.Issue;
import io.github.boldijar.cosasapp.itecdata.User;
import io.github.boldijar.cosasapp.server.Http;
import io.github.boldijar.cosasapp.util.Prefs;
import io.github.boldijar.cosasapp.util.RxUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class IssuesActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    @BindView(R.id.issues_map)
    MapView mMapView;
    @BindView(R.id.issues_recycler)
    RecyclerView mRecyclerView;

    private GoogleMap mGoogleMap;
    private FastAdapter<Issue, IssueHolder> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);
        ButterKnife.bind(this);
        loadUi();
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
    }

    private void loadUi() {
        mAdapter = new FastAdapter<Issue, IssueHolder>() {
            @Override
            protected AbstractHolder createHolder(ViewGroup parent) {
                return new IssueHolder(parent);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        loadCircle();
        loadIssues();
        mGoogleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(new LatLng(Prefs.getItecUser().getLatitude(), Prefs.getItecUser().getLongitude()), 12.0f))
        ;
    }

    private void loadCircle() {
        User user = Prefs.getItecUser();
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(user.getLatitude(), user.getLongitude()))
                .strokeColor(Color.TRANSPARENT)
                .radius(user.getRadius() * 50)
                .fillColor(0x2000ff00);
        mGoogleMap.addCircle(circleOptions);
    }

    private void loadIssues() {
        Http.getInstance().getSwaggerService().getIssues().compose(RxUtils.applySchedulers())
                .subscribe(new Observer<List<Issue>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Issue> issues) {
                        issues.add(0, Issue.ADD_ISSUE);
                        addMarkers(issues);
                        mAdapter.add(issues);
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

    private void addMarkers(List<Issue> issues) {
        for (Issue issue : issues) {
            if (issue == Issue.ADD_ISSUE) {
                continue;
            }
            MarkerOptions markerOptions = new MarkerOptions().title(issue.getTitle()).position(new LatLng(issue.getLatitude(), issue.getLongitude()));
            Marker marker = mGoogleMap.addMarker(markerOptions);
            marker.setTag(issue);
        }
    }

    private void likeIssue(Issue item, ImageView likeIssue) {
        item.setLiked(!item.isLiked());
        if (item.isLiked()) {
            likeIssue.setImageResource(R.drawable.ic_thumb_down_black_24dp);
        } else {
            likeIssue.setImageResource(R.drawable.ic_thumb_up_black_24dp);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTag() == null) {
            return false;
        }
        Issue issue = (Issue) marker.getTag();
        List<Issue> issues = mAdapter.getItems();
        for (int i1 = 0; i1 < issues.size(); i1++) {
            Issue i = issues.get(i1);
            if (i == issue) {
                mRecyclerView.getLayoutManager().scrollToPosition(i1);
                return false;
            }
        }
        return false;
    }

    public class IssueHolder extends FastAdapter.AbstractHolder<Issue> {

        @BindView(R.id.issue_image)
        ImageView mImage;
        @BindView(R.id.issue_text)
        TextView mText;
        @BindView(R.id.issue_like)
        ImageView mLikeIssue;
        @BindView(R.id.issue_normal)
        View mNormalIssue;
        @BindView(R.id.issue_add)
        View mAddIssue;

        public IssueHolder(ViewGroup parent) {
            super(parent, R.layout.item_issue);
        }

        @Override
        public void bind(Issue item) {
            itemView.setOnClickListener(v -> {
                if (item == Issue.ADD_ISSUE) {
                    addIssue();
                } else {
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(item.getLatitude(), item.getLongitude()), 12.0f));
                }
            });
            if (item == Issue.ADD_ISSUE) {
                mAddIssue.setVisibility(View.VISIBLE);
                mNormalIssue.setVisibility(View.GONE);
                return;
            }
            mAddIssue.setVisibility(View.GONE);
            mNormalIssue.setVisibility(View.VISIBLE);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(),
                    new RoundedCorners(mImage.getContext().getResources().getDimensionPixelSize(R.dimen.elevation)));
            String image = "https://media-cdn.tripadvisor.com/media/photo-s/0e/5b/df/de/kangourou-a-lucky-bay.jpg";
            if (item.getImages() != null && item.getImages().size() > 0) {
                image = item.getImages().get(0);
            }
            Glide.with(mImage.getContext()).load(image).apply(requestOptions).into(mImage);
            mText.setText(item.getTitle());
            mLikeIssue.setOnClickListener(v -> likeIssue(item, mLikeIssue));
            if (item.isLiked()) {
                mLikeIssue.setImageResource(R.drawable.ic_thumb_down_black_24dp);
            } else {
                mLikeIssue.setImageResource(R.drawable.ic_thumb_up_black_24dp);
            }

        }
    }

    private void addIssue() {
        startActivity(new Intent(this, AddIssueActivity.class));
    }

}
