package chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * @author dwj  2017/3/10 11:56
 */

public class KeyView {

    private Paint keyPaint = new Paint();

    private Rect rect;

    private int startX, startY;

    private String text;

    public KeyView(Context context, String text) {
        this.text = text;
        rect = new Rect();
        keyPaint.getTextBounds(text, 0, text.length(), rect);
        keyPaint.setAntiAlias(true);
        keyPaint.setColor(Color.parseColor("#ff0000"));
        keyPaint.setTextSize(DensityUtil.dp2px(context, 12));
    }

    public void setPaint(Paint paint) {
        this.keyPaint = paint;
    }

    public void setTextCenter(int startX, int startY) {
        this.startX = startX - rect.width();
        this.startY = startY;
    }

    public void draw(Canvas canvas) {
        canvas.drawText(text, startX - rect.width() / 2, startY, keyPaint);
    }
}
