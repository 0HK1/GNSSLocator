package com.example.GNSSLocator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public class BarChartView extends View {
    private Paint paint;
    private List<Float> values;
    private List<Float> svids;

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBarChart(canvas);
    }

    private void drawBarChart(Canvas canvas) {
        if (values == null || svids == null || values.isEmpty() || svids.isEmpty()) {
            return;
        }

        int width = getWidth();
        int height = getHeight();
        int barWidth = width / (values.size() * 2);
        float maxValue = 0;


        for (float value : values) {
            if (value > maxValue) {
                maxValue = value;
            }
        }


        for (int i = 0; i < values.size(); i++) {
            float barHeight = (values.get(i) / maxValue) * height;
            paint.setColor(Color.BLUE);
            canvas.drawRect(
                    i * (2 * barWidth),
                    height - barHeight,
                    (i * (2 * barWidth)) + barWidth,
                    height,
                    paint
            );

            paint.setColor(Color.BLACK);
            paint.setTextSize(25);
            if (i < svids.size()) {
                Float element = svids.get(i);
                canvas.drawText(String.valueOf(element), i * (2 * barWidth), height - barHeight + 30, paint);
            }
        }
    }

    public void setValues(List<Float> newValues, List<Float> newSvids) {
        this.values = newValues;
        this.svids = newSvids;
        invalidate();
    }
}
