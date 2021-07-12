package hniknam74.linechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ChartView extends View {

    public ChartView(Context context) {
        super(context);
        init(context);
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ArrayList<SampleData> list = new ArrayList<>();
    public ArrayList<SampleData> mainData = new ArrayList<>();
    public ArrayList<SampleData> rawData = new ArrayList<>();

    public ChartFrameTiming chartTime = ChartFrameTiming.threeMonth;
    public ChartTouchListener touchListener = null;

    private Paint paintDot;
    private Paint paintLine;
    private Paint paintBottomGrid;
    private Paint paintText;
    private Paint paintGrid;
    private Paint paintTouchLine;
    private Paint paintInfoBack;
    private Paint paintTextWhite;

    private boolean isAllZero = true;
    int margin = ViewHandler.dpToPixel(32);
    boolean isTouch = false;
    int touchedX = 0;
    int lastY = 0, lastX = 0;

    private Paint buildPaint(int color, Paint.Style style){
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(style);
        return paint;
    }

    public void init(Context context) {

        paintDot = buildPaint(Color.RED, Paint.Style.FILL);

        paintLine = buildPaint(Color.parseColor("#67B7DC"), Paint.Style.STROKE);
        paintLine.setStrokeWidth(4);

        paintBottomGrid = buildPaint(Color.BLACK, Paint.Style.STROKE);
        paintBottomGrid.setStrokeWidth(ViewHandler.dpToPixel(3));

        paintText =  buildPaint(Color.BLACK, Paint.Style.FILL);
        paintText.setTextSize(ViewHandler.dpToPixel(8));


        paintGrid = buildPaint(Color.parseColor("#CCE0E0E0"), Paint.Style.STROKE);
        paintGrid.setStrokeWidth(2);

        paintTouchLine = buildPaint(Color.BLACK, Paint.Style.STROKE);
        paintTouchLine.setStrokeWidth(ViewHandler.dpToPixel(2));

        paintInfoBack = buildPaint(Color.BLACK, Paint.Style.FILL);

        paintTextWhite = buildPaint(Color.WHITE, Paint.Style.FILL);
        paintTextWhite.setTextSize(ViewHandler.dpToPixel(8));

        getMainData();
        setChartWindowDataSize();
    }


    public void setData(ArrayList<SampleData> rawData) {

        this.rawData = new ArrayList<>();
        this.rawData.addAll(rawData);

        for (int i = 0; i < rawData.size(); i++) {
            if (rawData.get(i).getValue() != 0) {
                isAllZero = false;
                break;
            }
        }
        getMainData();
        setChartWindowDataSize();
        refresh();
    }

    private void getMainData() {

        int untill = 30;
        if (chartTime == ChartFrameTiming.threeMonth) {
            untill = 90;
        }
        if (chartTime == ChartFrameTiming.sixMonth) {
            untill = 180;
        }
        if (chartTime == ChartFrameTiming.year) {
            untill = 365;
        }

        mainData = new ArrayList<>();
        for (int i = rawData.size() - untill; i < rawData.size(); i++) {

            if (i >= 0 && i < rawData.size()) {

                mainData.add(rawData.get(i));
            }

        }

    }


    public SampleData max() {
        SampleData max = list.get(0);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getValue() > max.getValue()) {
                max = list.get(i);
            }
        }
        return max;
    }

    public SampleData min() {

        SampleData min = list.get(0);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getValue() < min.getValue()) {
                min = list.get(i);
            }
        }
        return min;
    }

    public void refresh() {

        invalidate();
        requestLayout();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int newY = (int) event.getY();
        int newX = (int) event.getX();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {


            isTouch = true;
            touchedX = (int) event.getX();

            if (touchedX > margin && touchedX < getWidth() - margin) {

            }
            refresh();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            isTouch = false;
            refresh();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

            if (Math.abs(newY - lastY) > Math.abs(newX - lastX) + ViewHandler.dpToPixel(1)) {

                isTouch = false;
            } else {
                isTouch = true;

            }
            lastY = newY;
            lastX = newX;

            touchedX = (int) event.getX();
            refresh();
        }

        return true;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int h = canvas.getHeight();
        int w = canvas.getWidth();

        if (list.size() > 0) {

            SampleData min = min();
            SampleData max = max();

            int minValue = (int) min.getValue();
            int diff = (int) (max.getValue() - min.getValue());


            if (isAllZero) {
                for (int i = 0; i < list.size(); i++) {

                    int value = h / 2;
                    list.get(i).setMappedValue(value);
                }
            } else {
                for (int i = 0; i < list.size(); i++) {

                    if (diff == 0) {
                        diff = 1;
                    }
                    list.get(i).setMappedValue((int) ((float) (list.get(i).getValue() - max.getValue()) * (float) ((float) ((h - margin) - (0 + margin)) / (float) (minValue - max.getValue())) + (0 + margin)));

                }
            }


            int marginH = ViewHandler.dpToPixel(54);
            int wSpace = (w - (2 * marginH)) / list.size();

            // for y axix lines
            SampleData min2 = min();
            SampleData max2 = max();

            int startY = margin;
            float startValue = (int) min2.getValue();

            int dif2 = Math.abs(max2.getMappedValue() - min2.getMappedValue());
            int diffValues = (int) Math.abs(max2.getValue() - min2.getValue());

            int numOfYGrid = 3;
            float stepY = (float) dif2 / (float) numOfYGrid;
            float stepValue = (float) diffValues / (float) numOfYGrid;

            for (int i = 0; i < numOfYGrid + 1; i++) {
                canvas.drawLine(0, startY, ViewHandler.dpToPixel(6), startY, paintBottomGrid);
                canvas.drawLine(marginH, startY, w - (marginH), startY, paintGrid);
                String number = new DecimalFormat("##.#").format(startValue);
                canvas.drawText((number + ""), ViewHandler.dpToPixel(10), h - startY, paintText);
                startY += stepY;
                startValue += stepValue;
            }

            int numOfXGrid = 3;
            int startX = marginH;
            int stepX = (int) ((w - (2 * marginH)) / numOfXGrid);

            int indexStep = list.size() / 4;
            for (int i = 0; i < numOfXGrid + 1; i++) {

                SampleData current = list.get(0);
                int index = i * indexStep;
                if (index < list.size() - 1) {
                    current = list.get(index);
                }

                if (i == numOfXGrid) {
                    current = list.get(list.size() - 1);
                }
                canvas.drawLine(startX, margin, startX, h - margin, paintGrid);

                Rect bounds = new Rect();
                paintText.getTextBounds((current.getName() + ""), 0, (current.getName() + "").length(), bounds);
                int textH = bounds.height();
                float textW = bounds.width();

                int s = (int) (startX - (textW / 2));
                int y = h - textH;
                canvas.drawText((current.getName() + ""), s, y, paintText);

                startX += stepX;
            }


            //draw main chart
            for (int i = 0; i < list.size() - 1; i++) {
                canvas.drawLine((wSpace * i) + marginH, list.get(i).getMappedValue(), (wSpace * (i + 1)) + marginH, list.get(i + 1).getMappedValue(), paintLine);
            }
            //draw touched Line
            if (isTouch) {

                if (touchedX >= marginH && touchedX <= w - marginH) {
                    float minXSpace = 10000;
                    int index = -1;

                    for (int i = 0; i < list.size(); i++) {

                        if (Math.abs(((i * wSpace) + marginH) - (float) touchedX) < minXSpace) {
                            minXSpace = Math.abs(((i * wSpace) + marginH) - (float) touchedX);
                            index = i;
                        }
                    }

                    SampleData nearest = list.get(index);

                    if (touchListener != null) {
                        touchListener.onSelectedDateChanged(nearest);
                    }
                    canvas.drawLine(touchedX - ViewHandler.dpToPixel(1), margin, touchedX - ViewHandler.dpToPixel(1), h - margin, paintTouchLine);
                    canvas.drawCircle(touchedX, nearest.getMappedValue() + 10, 10, paintDot);

                }

            } else {
                if (touchListener != null) {
                    touchListener.onSelectedDateChanged(null);
                }
            }


        }

    }


    public void setLeftLimit(int leftIndex) {

        ArrayList<SampleData> temp = new ArrayList<SampleData>();

        for (int i = leftIndex; i < leftIndex + 30; i++) {
            if (i <= mainData.size() - 1 && i >= 0) {
                temp.add(mainData.get(i));
            }
        }

        list = temp;
        refresh();

    }

    public void setChartWindowDataSize() {
        int untill = 7;
        if (chartTime == ChartFrameTiming.month) {
            untill = 30;
        }
        if (chartTime == ChartFrameTiming.threeMonth) {
            untill = 90;
        }
        if (chartTime == ChartFrameTiming.sixMonth) {
            untill = 180;
        }
        if (chartTime == ChartFrameTiming.year) {
            untill = 365;
        }

        list = new ArrayList<>();

        for (int i = mainData.size() - untill; i < mainData.size() - 1; i++) {
            if (i >= 0 && i < mainData.size())
                list.add(mainData.get(i));
        }

        refresh();

    }



    public interface ChartTouchListener {
        void onSelectedDateChanged(SampleData model);
    }

    public enum ChartFrameTiming {
        month(),
        threeMonth(),
        sixMonth(),
        year()
    }


}
