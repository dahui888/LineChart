package chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * @author dwj  2017/3/10 11:50
 */

public class ValueView {

    private Paint valuePaint = new Paint();

    private Rect rect;

    private Point center;

    private int radius;

    private double value;

    public ValueView(Context context, double value) {

        valuePaint.setAntiAlias(true);
        valuePaint.setColor(Color.parseColor("#ff4040"));
        valuePaint.setTextSize(DensityUtil.dp2px(context, 10));

        radius = DensityUtil.dp2px(context, 4);

        this.value = value;
    }

    public void setPaint(Paint paint) {
        this.valuePaint = paint;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public void setCenter(int centerX, int centerY) {
        center = new Point(centerX, centerY);
        rect = new Rect(-radius, -radius, radius * 2 + radius, radius * 2 + radius);
    }

    public double getValue() {
        return value;
    }

    public boolean isTouched(int x, int y) {
        int innerX = Math.abs(x - center.x);
//        int innerY = Math.abs(y - center.y);
        return rect.left < innerX && innerX < rect.right; // 只判断x坐标范围
//        return rect.contains(innerX, innerY);
    }

    public Point getCenter() {
        return center;
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(center.x, center.y, radius, valuePaint);
    }

}
