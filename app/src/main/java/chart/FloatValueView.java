package chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * @author dwj  2017/3/10 15:44
 */

public class FloatValueView {

    private Context context;

    private Paint floatViewPaint;

    private final int BORDER_COLOR = Color.parseColor("#ffb2b4"); // 边界线颜色
    private final int BACKGROUND = Color.parseColor("#ffe5e5"); // 填充色
    private final int TEXT_COLOR = Color.parseColor("#ff4040"); // 文本颜色

    private int parentWidth, parentHeight, triangleWidth, triangleHeight;

    private String value; // 文本信息

    private Path path = new Path();
    private Rect rect = new Rect();
    private RectF rectF = new RectF();

    private Paint.FontMetricsInt fontMetricsInt;

    public FloatValueView(Context context, int parentWidth, int parentHeight) {
        this.context = context;

        floatViewPaint = new Paint();
        floatViewPaint.setAntiAlias(true);
        floatViewPaint.setTextSize(DensityUtil.dp2px(context, 15));

        fontMetricsInt = floatViewPaint.getFontMetricsInt();

        this.parentWidth = parentWidth; // View宽度，用来判断是否超出绘制边界
        this.parentHeight = parentHeight; // View高度，同上
        this.triangleWidth = DensityUtil.dp2px(context, 6); // 三角形的宽度
        this.triangleHeight = DensityUtil.dp2px(context, 4); // 三角形的高度
    }

    public void attachValueView(ValueView valueView) {
        value = String.valueOf(valueView.getValue());
        floatViewPaint.getTextBounds(value, 0, value.length(), rect); // 计算所绘制文本的长宽信息

        int centerX = valueView.centerX; // 圆点圆心坐标 (x, y)
        int centerY = valueView.centerY;

        float left = centerX // 圆点圆心x坐标
                - rect.width() / 2 // 文字一半宽度
                - DensityUtil.dp2px(context, 8); // 文本信息周围的空白区域

        float top = centerY // 圆点圆心y坐标
                - valueView.getRadius() // 圆点半径
                - DensityUtil.dp2px(context, 4) // 三角形尖端距离圆点的距离
                - triangleHeight // 三角形高度
                - DensityUtil.dp2px(context, 8) // 文本信息周围的空白区域
                - rect.height() // 文字高度
                - DensityUtil.dp2px(context, 8); // 文本信息周围的空白区域

        float right = centerX // 圆点圆心x坐标
                + rect.width() / 2 // 文字一半宽度
                + DensityUtil.dp2px(context, 8); // 文本信息周围的空白区域

        float bottom = centerY // 圆点圆心y坐标
                - valueView.getRadius() // 圆点半径
                - DensityUtil.dp2px(context, 4) // 三角形尖端距离圆点的距离
                - triangleHeight; // 三角形高度

        rectF.set(left, top, right, bottom);

        int rightEdge = parentWidth - DensityUtil.dp2px(context, 4);
        int leftEdge = DensityUtil.dp2px(context, 4);
        if (right >= rightEdge) { // 右边宽度超出
            rectF.set(left - (right - rightEdge), top, rightEdge, bottom);
        } else if (left <= leftEdge) { // 左边宽度超出
            rectF.set(leftEdge, top, right + (leftEdge - left), bottom);
        }

        final int ROUND = DensityUtil.dp2px(context, 4);

        path.addRoundRect(rectF, new float[]{ROUND, ROUND, ROUND, ROUND, ROUND, ROUND, ROUND, ROUND}, Path.Direction.CW);
        path.moveTo(centerX - triangleWidth / 2, bottom);
        path.lineTo(centerX + triangleWidth / 2, bottom);
        path.lineTo(centerX, bottom + triangleHeight);
        path.close();
    }

    public void draw(Canvas canvas) {
        floatViewPaint.setColor(BORDER_COLOR);
        floatViewPaint.setStyle(Paint.Style.STROKE);
        floatViewPaint.setStrokeWidth(DensityUtil.dp2px(context, 2));
        canvas.drawPath(path, floatViewPaint);

        floatViewPaint.setColor(BACKGROUND);
        floatViewPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, floatViewPaint);

        floatViewPaint.setColor(TEXT_COLOR);
        floatViewPaint.setStyle(Paint.Style.FILL);
        floatViewPaint.setTextAlign(Paint.Align.CENTER);

        canvas.drawText(value,
                rectF.left + rectF.width() / 2,
                rectF.top + rectF.height() / 2 - (fontMetricsInt.top + fontMetricsInt.bottom) / 2,
                floatViewPaint);
    }
}
