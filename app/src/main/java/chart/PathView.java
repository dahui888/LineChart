package chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;

import java.util.List;


/**
 * @author dwj  2017/3/10 19:40
 */

public class PathView {

    private Paint paint;

    public Path path = new Path();

    private float pathLength;

    private float phase;

    public PathView(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#ff4040"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DensityUtil.dp2px(context, 0.5f));
    }

    public void lineValueViews(List<ValueView> valueViews) {
        final int size = valueViews.size();
        for (int i = 0; i < size; i++) {
            ValueView valueView = valueViews.get(i);
            if (i == 0) {
                path.moveTo(valueView.getCenter().x, valueView.getCenter().y);
            } else {
                path.lineTo(valueView.getCenter().x, valueView.getCenter().y);
            }
        }

        PathMeasure measure = new PathMeasure(path, false);
        pathLength = measure.getLength();
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPhase(float phase) {
        this.phase = phase;
    }

    public void draw(final Canvas canvas) {
        paint.setPathEffect(new DashPathEffect(new float[]{pathLength, pathLength}, pathLength - pathLength * phase));
        canvas.drawPath(path, paint);
    }
}
