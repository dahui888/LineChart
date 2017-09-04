package chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

/**
 * @author dwj  2017/3/10 11:56
 */

public class KeyView {

    private boolean isSelected;

    private Paint keyPaint;

    private Rect rect;

    private int startX, startY;

    private String text;

    public KeyView(Context context, String text) {
        this.text = text;
        rect = new Rect();
        keyPaint = new Paint();
        keyPaint.getTextBounds(text, 0, text.length(), rect);
        keyPaint.setAntiAlias(true);
        keyPaint.setTextSize(DensityUtil.dp2px(context, 12));
        keyPaint.setStyle(Paint.Style.FILL);
    }

    public void setPaint(Paint paint) {
        this.keyPaint = paint;
    }

    public void setTextCenter(int startX, int startY) {
        this.startX = startX - rect.width();
        this.startY = startY;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void draw(Canvas canvas) {
        if (isSelected) {
            keyPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            keyPaint.setColor(Color.parseColor("#ff0000"));
        } else {
            keyPaint.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            keyPaint.setColor(Color.parseColor("#999999"));
        }
        canvas.drawText(text, startX - rect.width() / 2, startY, keyPaint);
    }
}
