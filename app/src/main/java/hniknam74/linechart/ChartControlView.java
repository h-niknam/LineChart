package hniknam74.linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ChartControlView extends View {


    public ChartControlView(Context context) {
        super(context);
        init(context);
    }

    public ChartControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChartControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private Paint paintRect;
    private Paint paintCircle;

    public void init(Context context) {

        paintRect = new Paint();
        paintRect.setColor(Color.parseColor("#F8F8F8"));
        paintRect.setStyle(Paint.Style.FILL);
        paintRect.setAntiAlias(true);

        paintCircle = new Paint();
        paintCircle.setColor(Color.parseColor("#D9D9D9"));
        paintCircle.setStyle(Paint.Style.FILL);
        paintCircle.setAntiAlias(true);

    }

    private ChartView chartView;

    public void setUp(ChartView chartView) {

        this.chartView = chartView;
    }

    int wDevide = 3;
    int numOfData = 90;

    public void setChartTime(ChartView.ChartFrameTiming time) {
        if (time == ChartView.ChartFrameTiming.month) {
            wDevide = 1;
            numOfData = 30;
        }
        if (time == ChartView.ChartFrameTiming.threeMonth) {
            wDevide = 3;
            numOfData = 90;
        }
        if (time == ChartView.ChartFrameTiming.sixMonth) {
            wDevide = 6;
            numOfData = 180;
        }
        if (time == ChartView.ChartFrameTiming.year) {
            wDevide = 6;
            numOfData = 364;
        }

        chartView.chartTime = time;
        chartView.setChartWindowDataSize();
        invalidate();
        chartView.refresh();
    }


    public void refresh() {

        if (chartView != null) {
            chartView.invalidate();
            chartView.requestLayout();
        }

        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int h = canvas.getHeight();
        int w = canvas.getWidth();

        boxWidth = w / wDevide;

        if (touchedX < (boxWidth / 2)) {
            touchedX = (boxWidth / 2);
        } else if (touchedX > w - (boxWidth / 2)) {
            touchedX = w - (boxWidth / 2);
        }

        int centerH = h / 2;
        int boxHeight = ViewHandler.dpToPixel(16);

        canvas.drawRect(touchedX - (boxWidth / 2), centerH - (boxHeight / 2), touchedX + (boxWidth / 2), centerH + (boxHeight / 2), paintRect);

        int circleW = ViewHandler.dpToPixel(16);
        int circleLeft = touchedX - (boxWidth / 2);
        int circleWHalf = circleW / 2;

        if (touchedX - (boxWidth / 2) > circleWHalf)
            canvas.drawOval(circleLeft - circleWHalf, centerH - circleWHalf, circleLeft + circleWHalf, centerH + circleWHalf, paintCircle);

        int circleRight = circleLeft + boxWidth;

        if (touchedX + (boxWidth / 2) < w)
            canvas.drawOval(circleRight - circleWHalf, centerH - circleWHalf, circleRight + circleWHalf, centerH + circleWHalf, paintCircle);

    }

    public int boxWidth = 0;
    public int touchedX = -1;


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchedX = (int) event.getX();

            setLimitForChart();

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

            touchedX = (int) event.getX();
            int w = getWidth();

            if (touchedX - (boxWidth / 2) < 0) {
                touchedX = (boxWidth / 2);
            }
            if (touchedX + (boxWidth / 2) > w) {
                touchedX = w - (boxWidth / 2);
            }

            setLimitForChart();

        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            setLimitForChart();
        }
        return true;
    }

    public void setLimitForChart() {
        int w = getWidth();

        if (chartView.mainData != null && numOfData > chartView.mainData.size()) {
            numOfData = chartView.mainData.size();
        }
        int size = numOfData;
        float partsW = ((float) w) / (float) size;

        float leftIndex = (float) (this.touchedX - (boxWidth / 2)) / (partsW);

        chartView.setLeftLimit((int) leftIndex);

        refresh();
    }


}
