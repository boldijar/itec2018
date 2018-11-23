package io.github.boldijar.cosasapp.parts.comment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.boldijar.cosasapp.R;
import io.github.boldijar.cosasapp.itecdata.CommentModel;

/**
 * @author Paul
 * @since 2018.08.18
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsHolder> {

    private final List<CommentModel> mCommentModels = new ArrayList<>();

    public void addComments(List<CommentModel> comments, boolean clear) {
        if (clear) {
            mCommentModels.clear();
        }
        int lastCommentIndex = -1;
        if (mCommentModels.size() > 0) {
            lastCommentIndex = mCommentModels.size() - 1;
        }
        mCommentModels.addAll(comments);
        if (clear) {
            notifyDataSetChanged();
        } else {
            int startIndex = mCommentModels.size() - comments.size();
            notifyItemRangeInserted(startIndex, comments.size());
            if (lastCommentIndex != -1 && lastCommentIndex < mCommentModels.size()) {
                // notify last item to remove the bottom padding
                notifyItemChanged(lastCommentIndex);
            }
        }
    }

    @NonNull
    @Override
    public CommentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false));
    }

    private static SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm", Locale.US);

    static String[] IMAGES = new String[]{
            "https://i.imgur.com/bray6Ez.jpg",
            "https://i.imgur.com/IK0L1Zz.jpg",
            "https://i.imgur.com/eGTBriz.jpg",
            "https://i.imgur.com/5T7kqkt.jpg",
            "https://i.imgur.com/160kkDg.jpg",
            "https://i.imgur.com/mlTeaSu.jpg"};

    @Override
    public void onBindViewHolder(@NonNull CommentsHolder holder, int position) {
        CommentModel comment = mCommentModels.get(position);
        holder.mName.setText(comment.getCreator());
        holder.mText.setText(comment.getContent());
        holder.mTime.setText(format.format(new Date(comment.getCreatedAt())));
        int index = ((int) ((Math.random() * 1000)) % IMAGES.length);
        Glide.with(holder.mImage.getContext())
                .load(IMAGES[index])
                .into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return mCommentModels.size();
    }

    public void insertNewComment(CommentModel comment) {
        mCommentModels.add(0, comment);
        notifyItemInserted(0);
    }

    static class CommentsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.comment_name)
        TextView mName;
        @BindView(R.id.comment_profile_picture)
        ImageView mImage;
        @BindView(R.id.comment_text_time)
        TextView mTime;
        @BindView(R.id.comment_text)
        TextView mText;

        CommentsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface UserClickedListener {
        void onUserClicked(int userId);
    }
}
