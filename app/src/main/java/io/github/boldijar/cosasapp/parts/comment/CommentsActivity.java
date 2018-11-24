package io.github.boldijar.cosasapp.parts.comment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.boldijar.cosasapp.R;
import io.github.boldijar.cosasapp.base.BaseActivity;
import io.github.boldijar.cosasapp.itecdata.CommentModel;
import io.github.boldijar.cosasapp.server.Http;
import io.github.boldijar.cosasapp.util.Prefs;
import io.github.boldijar.cosasapp.util.RxUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author Paul
 * @since 2018.08.18
 */
public class CommentsActivity extends BaseActivity {

    private static final String ARG_EXTRA_ID = "extra_id";
    private static final String ARG_COMMENTS_TYPE = "comments_type";

    @BindView(R.id.comments_list)
    RecyclerView mCommentsRecycler;
    @BindView(R.id.comments_input)
    EditText mInput;
    @BindView(R.id.comments_send)
    View mSend;
    @BindView(R.id.comments_bottom_layout)
    LinearLayout mCommentsRoot;

    private LinearLayoutManager mLinearLayoutManager;
    private CommentsAdapter mCommentsAdapter = new CommentsAdapter();

    public static Intent createIntent(String id, List<CommentModel> comments, Context context) {
        Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("comments", (Serializable) comments);
        return intent;
    }

    private String mId;
    private List<CommentModel> mCommentModels;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);
        mId = getIntent().getStringExtra("id");
        mCommentModels = (List<CommentModel>) getIntent().getSerializableExtra("comments");
        initUi();
        if (Prefs.Token.get() == null) {
            mCommentsRoot.setVisibility(View.GONE);
        }
        mCommentsAdapter.addComments(mCommentModels, false);

    }


    private void initUi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCommentsRoot.setElevation(getResources().getDimension(R.dimen.elevation));
        }
        mLinearLayoutManager = new LinearLayoutManager(this);
        mCommentsRecycler.setLayoutManager(mLinearLayoutManager);
        mCommentsRecycler.setAdapter(mCommentsAdapter);
    }


    @OnClick(R.id.comments_send)
    void sendComment() {
        if (TextUtils.isEmpty(mInput.getText())) {
            return;
        }

        CommentModel commentModel = new CommentModel(mInput.getText().toString(), Prefs.getItecUser().getId(), mId);
        Http.getInstance().getSwaggerService().createComment(commentModel.getId(), commentModel)
                .compose(RxUtils.applySchedulers())
                .subscribe(new Observer<CommentModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CommentModel commentModel) {
                        commentModel.setCreator(Prefs.getItecUser().getFullName());
                        setResult(RESULT_OK);
                        mCommentsAdapter.insertNewComment(commentModel);
                        mInput.setText(null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(CommentsActivity.this, "Try again.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

}
