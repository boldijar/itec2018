package io.github.boldijar.cosasapp.parts.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.boldijar.cosasapp.R;
import io.github.boldijar.cosasapp.base.BaseActivity;
import io.github.boldijar.cosasapp.data.RoomResponse;
import io.github.boldijar.cosasapp.leaderboard.LeaderboardActivity;
import io.github.boldijar.cosasapp.parts.history.HistoryActivity;
import io.github.boldijar.cosasapp.parts.login.LoginActivity;
import io.github.boldijar.cosasapp.parts.room.RoomListActivity;
import io.github.boldijar.cosasapp.parts.room.RoomWaitingActivity;
import io.github.boldijar.cosasapp.parts.users.UsersBottomSheet;
import io.github.boldijar.cosasapp.server.Http;
import io.github.boldijar.cosasapp.util.Observatorul;
import io.github.boldijar.cosasapp.util.Prefs;
import io.github.boldijar.cosasapp.util.RxUtils;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

/**
 * @author Paul
 * @since 2018.10.13
 */
public class HomeActivity extends BaseActivity {

    @BindView(R.id.home_image)
    ImageView mImage;
    @BindView(R.id.home_image_layout)
    PulsatorLayout mPulsatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loadUi();
    }

    private void loadUi() {
        ButterKnife.bind(this);
        mPulsatorLayout.start();
    }

    @OnClick(R.id.home_logout)
    void logout() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(Prefs.getUser().mId + "");
        Prefs.User.put(null);
        Prefs.Token.put(null);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.home_top)
    void goToLeaderBoard() {
        startActivity(new Intent(this, LeaderboardActivity.class));
    }

    @OnClick(R.id.home_quiz_history)
    void history() {
        startActivity(new Intent(this, HistoryActivity.class));
    }

    @OnClick(R.id.home_join_room)
    void joinRoom() {
        startActivity(new Intent(this, RoomListActivity.class));
    }

    @OnClick(R.id.home_create_room)
    void createNewRoom() {
        Http.getInstance().getApiService().createRoom()
                .compose(RxUtils.applySchedulers())
                .subscribe(new Observatorul<RoomResponse>() {
                    @Override
                    public void onNext(RoomResponse roomResponse) {
                        startActivity(RoomWaitingActivity.createIntent(HomeActivity.this, roomResponse.mRoom.mId, true));
                    }
                });
    }

    @OnClick(R.id.home_flash_challenge)
    void challange() {
        new UsersBottomSheet().show(getSupportFragmentManager(), "");
    }
}
