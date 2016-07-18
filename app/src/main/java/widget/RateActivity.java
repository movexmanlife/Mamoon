package widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jcodecraeer.linkedviewpager.LinkedViewPager.MyPagerAdapter;
import com.jcodecraeer.linkedviewpager.LinkedViewPager.ViewPager;

import java.util.ArrayList;
import java.util.List;

import io.lahphim.mamoon.R;

/**
 * Created by Administrator on 2016/7/15.
 */
public class RateActivity extends Activity {
    private Button btn;
    private Button btnArcRateView;
    private ArcRateChart arcRate;
    private ArcRateChartView arcRateView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc_rate);

        btn = (Button)findViewById(R.id.btn);
        btnArcRateView = (Button)findViewById(R.id.btnArcRateView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
        btnArcRateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arcRateView();
            }
        });
        arcRate = (ArcRateChart) findViewById(R.id.arcRate);
        arcRateView = (ArcRateChartView) findViewById(R.id.arcRateView);
    }

    private void test() {
        arcRate.startAnimation(new ArcRateChart.ArcInfo(23, android.R.color.holo_blue_dark),
                new ArcRateChart.ArcInfo(70, android.R.color.holo_red_light));
    }

    private void arcRateView() {
        List<ArcRateChartView.ArcInfo> list = new ArrayList<>();

        list.add(new ArcRateChartView.ArcInfo(0.01, R.color.total_color));
        list.add(new ArcRateChartView.ArcInfo(796080, R.color.wait_color));
        list.add(new ArcRateChartView.ArcInfo(2653600, R.color.balance_color));
        list.add(new ArcRateChartView.ArcInfo(3184320, R.color.withdraw_color));

//        list.add(new ArcRateChartView.ArcInfo(3, R.color.total_color));
//        list.add(new ArcRateChartView.ArcInfo(796080, R.color.wait_color));
//        list.add(new ArcRateChartView.ArcInfo(2653600, R.color.balance_color));
//        list.add(new ArcRateChartView.ArcInfo(1, R.color.withdraw_color));

//        list.add(new ArcRateChartView.ArcInfo(3, R.color.total_color));
//        list.add(new ArcRateChartView.ArcInfo(0.9, R.color.wait_color));
//        list.add(new ArcRateChartView.ArcInfo(4, R.color.balance_color));
//        list.add(new ArcRateChartView.ArcInfo(1, R.color.withdraw_color));
        arcRateView.startAnimation(list);
    }
}
