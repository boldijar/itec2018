package io.github.boldijar.cosasapp.parts.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.boldijar.cosasapp.R;
import io.github.boldijar.cosasapp.base.BaseActivity;
import io.github.boldijar.cosasapp.parts.issues.IssuesActivity;
import io.github.boldijar.cosasapp.parts.login.LoginActivity;
import io.github.boldijar.cosasapp.parts.profile.ProfileActivity;
import io.github.boldijar.cosasapp.util.Prefs;
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

    @OnClick(R.id.home_issues)
    void issues() {
        startActivity(new Intent(this, IssuesActivity.class));
    }

    @OnClick(R.id.home_logout)
    void logout() {
        Prefs.Token.put(null);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.home_stats)
    void stats() {
        startActivity(new Intent(this, StatsActivity.class));
    }

    @OnClick(R.id.home_profile)
    void profileClick() {
        startActivity(new Intent(this, ProfileActivity.class));
    }
}
