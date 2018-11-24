package io.github.boldijar.cosasapp.parts.home;

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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.boldijar.cosasapp.R;
import io.github.boldijar.cosasapp.base.BaseActivity;
import io.github.boldijar.cosasapp.base.FastAdapter;
import io.github.boldijar.cosasapp.itecdata.Issue;
import io.github.boldijar.cosasapp.itecdata.UserResponse;
import io.github.boldijar.cosasapp.parts.comment.CommentsActivity;
import io.github.boldijar.cosasapp.server.Http;
import io.github.boldijar.cosasapp.util.Prefs;
import io.github.boldijar.cosasapp.util.RxUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class OwnIssuesActivity extends BaseActivity {

    @BindView(R.id.own_issues_list)
    RecyclerView mRecyclerView;

    private FastAdapter<Issue, IssueHolder> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_issues);
        ButterKnife.bind(this);

        Http.getInstance().getSwaggerService().getUser(Prefs.getItecUser().getEmail())
                .compose(RxUtils.applySchedulers())
                .subscribe(new Observer<UserResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserResponse userResponse) {
                        showList(userResponse.getUser().getIssueList());
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

    public class IssueHolder extends FastAdapter.AbstractHolder<Issue> {

        @BindView(R.id.issue_image)
        ImageView mImage;
        @BindView(R.id.issue_text)
        TextView mText;
        @BindView(R.id.issue_normal)
        View mNormalIssue;
        @BindView(R.id.issue_comment)
        View mComment;
        @BindView(R.id.issue_text_description)
        TextView mDescription;

        public IssueHolder(ViewGroup parent) {
            super(parent, R.layout.item_issue_own);
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
            mComment.setOnClickListener(v -> startActivityForResult(CommentsActivity.createIntent(item.getId(),
                    item.getCommentModels(), OwnIssuesActivity.this), 2));
            mDescription.setText(item.getDescription());
        }
    }

    private void showList(List<Issue> issueList) {
        mAdapter = new FastAdapter<Issue, IssueHolder>() {
            @Override
            protected AbstractHolder createHolder(ViewGroup parent) {
                return new IssueHolder(parent);
            }
        };
        mAdapter.add(issueList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }
}
