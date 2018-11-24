package io.github.boldijar.cosasapp.parts.home;

import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import io.github.boldijar.cosasapp.R;
import io.github.boldijar.cosasapp.base.BaseActivity;

public class OwnIssuesActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_issues);
        ButterKnife.bind(this);

    }
}
