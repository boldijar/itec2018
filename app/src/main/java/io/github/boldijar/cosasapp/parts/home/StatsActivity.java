package io.github.boldijar.cosasapp.parts.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.github.anastr.speedviewlib.SpeedView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.boldijar.cosasapp.R;
import io.github.boldijar.cosasapp.base.BaseActivity;
import io.github.boldijar.cosasapp.base.FastAdapter;
import io.github.boldijar.cosasapp.itecdata.Issue;
import io.github.boldijar.cosasapp.itecdata.MonthlyData;
import io.github.boldijar.cosasapp.itecdata.Stats;
import io.github.boldijar.cosasapp.parts.issues.IssuesActivity;
import io.github.boldijar.cosasapp.server.Http;
import io.github.boldijar.cosasapp.util.RxUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class StatsActivity extends BaseActivity {

    @BindView(R.id.stats_recycler)
    RecyclerView mStatsRecycler;
    @BindView(R.id.health_meter)
    PointerSpeedometer healthMeter;
    int gaugeColor = 0;
    private FastAdapter<MonthlyData, StatsActivity.MonthlyDataHolder> mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        ButterKnife.bind(this);
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
        healthMeter.setMinMaxSpeed(0, 10);
        healthMeter.setTrembleDegree(0.1f);
        healthMeter.setUnit("");
        healthMeter.speedTo(stats.getHealthIndex());
        gaugeColor = healthMeter.getBackgroundCircleColor();


        mAdapter = new FastAdapter<MonthlyData, MonthlyDataHolder>() {
            @Override
            protected AbstractHolder createHolder(ViewGroup parent) {
                return new MonthlyDataHolder(parent);
            }
        };
        mStatsRecycler.setAdapter(mAdapter);
        mStatsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAdapter.add(stats.getMonthlyData());
    }

    public class MonthlyDataHolder extends FastAdapter.AbstractHolder<MonthlyData> {
        @BindView(R.id.issues_chart)
        AnyChartView issuesChart;

        public MonthlyDataHolder(ViewGroup parent) {
            super(parent, R.layout.monthly_stats);
        }

        @Override
        public void bind(MonthlyData item) {
            Cartesian cartesian = AnyChart.column();

            List<DataEntry> data = new ArrayList<>();
            data.add(new ValueDataEntry("Created issues", item.getCreatedIssues()));
            data.add(new ValueDataEntry("Solved issues", item.getSolvedIssues()));

            Column column = cartesian.column(data);
            column.color(String.format("#%06X", (0xFFFFFF & gaugeColor)));

            column
                    .tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }");

            cartesian.animation(true);
            cartesian.title(item.getMonthName());
            cartesian.yScale().minimum(0d);

            cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
            cartesian.interactivity().hoverMode(HoverMode.BY_X);

            issuesChart.setChart(cartesian);

        }
    }
}
