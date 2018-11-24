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
import android.widget.Toast;

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
import io.github.boldijar.cosasapp.itecdata.VoteModel;
import io.github.boldijar.cosasapp.parts.comment.CommentsActivity;
import io.github.boldijar.cosasapp.server.Http;
import io.github.boldijar.cosasapp.util.Prefs;
import io.github.boldijar.cosasapp.util.RxUtils;
import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
        VoteModel voteModel = item.isLiked() ? VoteModel.createThumbsUp(item.getId()) : VoteModel.createThumbsDown(item.getId());
        Http.getInstance().getSwaggerService().voteIssue(item.getId(), voteModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(IssuesActivity.this, "Vote added", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(IssuesActivity.this, "Vote not added", Toast.LENGTH_SHORT).show();
                    }
                });
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
        @BindView(R.id.issue_comment)
        View mComment;

        public IssueHolder(ViewGroup parent) {
            super(parent, R.layout.item_issue);
        }

        final String[] URLS = new String[]{
                "https://www.telegraph.co.uk/content/dam/Travel/Destinations/Oceania/Australia/Australia-overview-great%20ocean%20road.jpg?imwidth=450",
                "https://www.telegraph.co.uk/content/dam/travel/Spark/tourism-western-australia/western-australia-road-trip.jpg?imwidth=450",
                "https://d1ne1hbcnyoys2.cloudfront.net/imagegen/max/ccr/1088/-/s3/adventures-cougar-assets/travel/2017/03/31/3872/when-is-the-best-time-to-travel-australia.jpg",
                "https://www.lookoutpro.com/wp-content/uploads/2018/04/australia_c.jpg",
                "http://www.statravel.co.uk/static/uk_division_web_live/assets/313_x_210_Blog_Coastal.jpg",
                "https://dynaimage.cdn.cnn.com/cnn/q_auto,w_900,c_fill,g_auto,h_506,ar_16:9/http%3A%2F%2Fcdn.cnn.com%2Fcnnnext%2Fdam%2Fassets%2F180822091052-australia-tourism-instagram---whale-shark.jpg"};

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
            String image = URLS[Math.abs(item.getId().hashCode()) % URLS.length];
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

            mComment.setOnClickListener(v -> startActivityForResult(CommentsActivity.createIntent(item.getId(), item.getCommentModels(), IssuesActivity.this), 2));
        }
    }

    private void addIssue() {
        startActivityForResult(new Intent(this, AddIssueActivity.class), 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK) {
            mAdapter.clear();
            mGoogleMap.clear();
            loadCircle();
            loadIssues();
        }
    }
}
