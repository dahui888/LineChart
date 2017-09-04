package chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * @author dwj  2017/3/11 17:40
 */

public class HorizontalLine {
    private Paint paint;

    private int parentWidth;

    private int parentHeight;

    public HorizontalLine(Context context, int parentWidth, int parentHeight) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#dddddd"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DensityUtil.dp2px(context, 0.5f));
        this.parentWidth = parentWidth;
        this.parentHeight = parentHeight;
    }

    public void draw(Canvas canvas) {
        canvas.drawLine(0, parentHeight * 2 / 8, parentWidth, parentHeight * 2 / 8, paint);
        canvas.drawLine(0, parentHeight * 4 / 8, parentWidth, parentHeight * 4 / 8, paint);
        canvas.drawLine(0, parentHeight * 6 / 8, parentWidth, parentHeight * 6 / 8, paint);
    }
}
