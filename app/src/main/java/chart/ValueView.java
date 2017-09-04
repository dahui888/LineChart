package chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * @author dwj  2017/3/10 11:50
 */

public class ValueView {

    private Paint valuePaint = new Paint();

    private Rect rect;

    public int centerX, centerY;

    private double value;

    private int radius;

    public ValueView(Context context, double value) {

        valuePaint.setAntiAlias(true);
        valuePaint.setColor(Color.parseColor("#ff4040"));
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
        this.centerX = centerX;
        this.centerY = centerY;
        rect = new Rect(-radius * 2, -radius * 2, radius * 2 + radius * 2, radius * 2 + radius * 2);
    }

    public double getValue() {
        return value;
    }

    public boolean isTouched(int x, int y) {
        int innerX = Math.abs(x - centerX);
//        int innerY = Math.abs(y - centerY);
        return rect.left < innerX && innerX < rect.right; // 只判断x坐标范围
//        return rect.contains(innerX, innerY);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, radius, valuePaint);
    }

}
