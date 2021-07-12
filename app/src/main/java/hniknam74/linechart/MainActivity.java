package hniknam74.linechart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ChartView chartView;
    private ChartControlView chvc;
    private TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chartView = findViewById(R.id.chartView);
        tvDescription = findViewById(R.id.tvDescription);
        chvc = findViewById(R.id.chvc);

        findViewById(R.id.m1).setOnClickListener(this::onClick);
        findViewById(R.id.m3).setOnClickListener(this::onClick);
        findViewById(R.id.m6).setOnClickListener(this::onClick);
        findViewById(R.id.m12).setOnClickListener(this::onClick);

        chartView.touchListener = new ChartView.ChartTouchListener() {
            @Override
            public void onSelectedDateChanged(SampleData data) {
                if (data == null) {
                    tvDescription.setText("");
                } else {
                    tvDescription.setText(
                            String.format("The real value is %d, and the mapped value is: %d for item : %s",
                                    data.getValue(),
                                    data.getMappedValue(),
                                    data.getName())
                    );
                }

            }
        };

        chvc.setUp(chartView);


        //create test data
        ArrayList<SampleData> data = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < 900; i++) {
            SampleData d = new SampleData(i + "", r.nextInt(20) + 100);
            data.add(d);
        }

        chartView.setData(data);
        chvc.setChartTime(ChartView.ChartFrameTiming.month);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.m1) {
            chvc.setChartTime(ChartView.ChartFrameTiming.month);
            chvc.setLimitForChart();
        }
        if (view.getId() == R.id.m3) {
            chvc.setChartTime(ChartView.ChartFrameTiming.threeMonth);
            chvc.setLimitForChart();
        }
        if (view.getId() == R.id.m6) {
            chvc.setChartTime(ChartView.ChartFrameTiming.sixMonth);
            chvc.setLimitForChart();
        }
        if (view.getId() == R.id.m12) {
            chvc.setChartTime(ChartView.ChartFrameTiming.year);
            chvc.setLimitForChart();
        }
    }
}