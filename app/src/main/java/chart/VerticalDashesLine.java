package chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * @author dwj  2017/3/13 14:34
 */

public class VerticalDashesLine {

    private Paint paint;

    private Path path;

    public VerticalDashesLine(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#ff4040"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DensityUtil.dp2px(context, 1f));
        paint.setPathEffect(new DashPathEffect(new float[]{DensityUtil.dp2px(context, 4f), DensityUtil.dp2px(context, 2.5f)}, 0));

        path = new Path();
    }

    public int startX, startY, endX, endY;

    public void setPoints(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        path.moveTo(startX, startY);
        path.lineTo(endX, endY);
    }

    public void draw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }
}
