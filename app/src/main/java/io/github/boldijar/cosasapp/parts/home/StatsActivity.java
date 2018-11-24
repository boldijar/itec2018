package io.github.boldijar.cosasapp.parts.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.boldijar.cosasapp.R;
import io.github.boldijar.cosasapp.base.BaseActivity;
import io.github.boldijar.cosasapp.itecdata.Stats;
import io.github.boldijar.cosasapp.server.Http;
import io.github.boldijar.cosasapp.util.RxUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class StatsActivity extends BaseActivity {

    @BindView(R.id.stats_seekbar)
    SeekBar mSeekBar;
    @BindView(R.id.stats_health)
    TextView mHealth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        ButterKnife.bind(this);
        mSeekBar.setFocusableInTouchMode(false);
        mSeekBar.setFocusable(false);
        mSeekBar.setEnabled(false);
        Http.getInstance().getSwaggerService().getStats().compose(RxUtils.applySchedulers()).subscribe(new Observer<Stats>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Stats stats) {
                loadUi(stats);
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

    private void loadUi(Stats stats) {
        mHealth.setText("Health index : " + stats.getHealthIndex());
        mSeekBar.setProgress((int) (stats.getHealthIndex() * 10));
    }
}
