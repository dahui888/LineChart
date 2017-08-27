package chart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dwj  2017/3/10 11:48
 */
public class LineChartView extends View {

    /**
     * 横线
     */
    private HLine hLine;

    /**
     * 横坐标，称之为key
     */
    private List<KeyView> keyViewList;

    /**
     * 纵坐标，称之为value
     */
    private List<ValueView> valueViewList;
    /**
     * 连接value的线，称之为line
     */
    private PathView lineView;

    /**
     * 每个value上面显示的悬浮的数值，称之为float value
     */
    private List<FloatValueView> floatValueViewList;

    /**
     * 当前选中的float value，默认选中最后一个
     */
    private FloatValueView clickedValueView;

    /**
     * value 的最小值，最大值
     */
    private double minValue, maxValue;

    private boolean isAllValueSame = false;

    private boolean isReadyForWork = false;

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置key集合
     */
    public void setKeys(List<String> keys) {
        if (keys != null && keys.size() != 0) {
            keyViewList = new ArrayList<>(keys.size());
            final int count = keys.size();
            for (int i = 0; i < count; i++) {
                String key = keys.get(i);
                KeyView keyView = new KeyView(getContext(), key);
                keyViewList.add(keyView);
            }
        }
    }

    /**
     * 设置value集合
     */
    public void setValues(List<Double> values) {
        if (values != null && values.size() != 0) {
            valueViewList = new ArrayList<>(values.size());
            final int count = values.size();
            for (int i = 0; i < count; i++) {
                if (i == 0) {
                    isAllValueSame = true;
                } else {
                    Double value1 = values.get(i - 1);
                    Double value2 = values.get(i);
                    isAllValueSame = value1.doubleValue() == value2.doubleValue();
                    if (!isAllValueSame) {
                        break;
                    } else {
                        continue;
                    }
                }
            }
            for (int i = 0; i < count; i++) {
                Double value = values.get(i);
                if (i == 0) {
                    minValue = value;
                } else {
                    minValue = Math.min(value.doubleValue(), minValue);
                }
                maxValue = Math.max(value.doubleValue(), maxValue);
            }
            for (int i = 0; i < count; i++) {
                Double value = values.get(i);
                ValueView point = new ValueView(getContext(), value);
                valueViewList.add(point);
            }
        }
    }

    /**
     * 开始绘制折线图
     */
    public void work() {
        post(new Runnable() {
            @Override
            public void run() {
                isReadyForWork = true;

                final int width = getMeasuredWidth();  // View的宽度
                final int height = getMeasuredHeight(); // View的高度

                hLine = new HLine(getContext(), width, height);

                final int keyViewCount = keyViewList.size(); // key数量
                int averageWidth = width / keyViewCount; // 每个key可绘制的宽度
                for (int i = 0; i < keyViewCount; i++) {
                    KeyView keyView = keyViewList.get(i);
                    keyView.setTextCenter(averageWidth / 2 + i * averageWidth, height * 7 / 8); // 循环遍历，设置每个key的绘制起点
                }

                /**
                 * View当中，y坐标 是从上往下递增，而Value从下往上递增的
                 * value 越小，y坐标越大，切记，切记!
                 **/
                final int minY = height * 2 / 8; // 最大值所在的y坐标，即最小的y坐标
                final int maxY = height * 6 / 8; // 最小值所在的y坐标，即最大的y坐标
                final int validHeight = maxY - minY; // 可供绘制的有效区域
                final double offsetValue = maxValue - minValue; // value最大值和最小值之差

                final int valueViewCount = valueViewList.size(); // value数量
                floatValueViewList = new ArrayList<>();  // float value数量
                for (int i = 0; i < valueViewCount; i++) {
                    ValueView valueView = valueViewList.get(i);
                    int centerX = averageWidth / 2 + i * averageWidth;
                    // 这里参照最小值所在的y坐标
                    int centerY;
                    if (isAllValueSame) {
                        centerY = height * 4 / 8;
                    } else {
                        centerY = (int) (minY + (maxValue - valueView.getValue()) / offsetValue * validHeight);
                    }
                    valueView.setCenter(centerX, centerY); // 循环遍历，设置每个value的绘制起点

                    FloatValueView valueDetailView = new FloatValueView(getContext(), width, height);
                    valueDetailView.attachValueView(valueView); // 循环遍历，创建并设置每个float value的绘制起点
                    floatValueViewList.add(valueDetailView);
                }

                lineView = new PathView(getContext()); // 创建折线
                lineView.lineValueViews(valueViewList); // 折线连接每个value

                clickedValueView = floatValueViewList.get(valueViewCount - 1); // 默认显示最后一个

                startAnimationLine();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isReadyForWork) {
            if (hLine != null) {
                hLine.draw(canvas);
            }
            for (KeyView key : keyViewList) {
                key.draw(canvas);
            }
            for (ValueView value : valueViewList) {
                value.draw(canvas);
            }
            for (FloatValueView valueDetailView : floatValueViewList) {
                if (valueDetailView == clickedValueView) {
                    valueDetailView.draw(canvas);
                }
            }
            if (lineView != null) {
                lineView.draw(canvas);
            }
        }

    }

    public void startAnimationLine() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                lineView.setPhase(value.floatValue());
                invalidate();
            }
        });
        animator.setDuration(2000);
        animator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        final int valueViewCount = valueViewList.size();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < valueViewCount; i++) {
                    ValueView valueView = valueViewList.get(i);
                    if (valueView.isTouched(x, y)) { // 通过View内触摸点判断点击了哪个value
                        clickedValueView = floatValueViewList.get(i);
                        invalidate();
                        break;
                    }
                }
                break;
        }
        return true;
    }
}
