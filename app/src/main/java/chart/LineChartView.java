package chart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dwj  2017/3/10 11:48
 */
public class LineChartView extends View {

    /**
     * 横线
     */
    private HorizontalLine hLine;

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
    private PathView pathView;

    /**
     * 垂直虚线
     */
    private VerticalDashesLine verticalDashesLine;

    /**
     * 每个value上面显示的悬浮的数值，称之为float value
     */
    private FloatValueView floatValueView;

    /**
     * value 的最小值，最大值
     */
    private double minValue, maxValue;

    private boolean isAllValueSame = false;

    private boolean isReadyForWork = false, isReadyForDrawAfterAnimation = false;

    /**
     * 当前选中那个 value view 的 index
     */
    private int mSelectedIndex;

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

                hLine = new HorizontalLine(getContext(), width, height);

                final int keyViewCount = keyViewList.size(); // key数量
                int averageWidth = width / keyViewCount; // 每个key可绘制的宽度
                for (int i = 0; i < keyViewCount; i++) {
                    KeyView keyView = keyViewList.get(i);
                    keyView.setTextCenter(averageWidth / 2 + i * averageWidth, getMeasuredHeight() - DensityUtil.dp2px(getContext(), 4)); // 循环遍历，设置每个key的绘制起点
                }

                /**
                 * View当中，y坐标 是从上往下递增，而Value从下往上递增的
                 * value 越小，y坐标越大，切记，切记!
                 **/
                final int minY = getMeasuredHeight() * 2 / 8; // 最大值所在的y坐标，即最小的y坐标
                final int maxY = getMeasuredHeight() * 6 / 8; // 最小值所在的y坐标，即最大的y坐标
                final int validHeight = maxY - minY; // 可供绘制的有效区域
                final double offsetValue = maxValue - minValue; // value最大值和最小值之差
                final int valueViewCount = valueViewList.size(); // value数量
                for (int i = 0; i < valueViewCount; i++) {
                    ValueView valueView = valueViewList.get(i);
                    int centerX = averageWidth / 2 + i * averageWidth;
                    // 这里参照最小值所在的y坐标
                    int centerY;
                    if (isAllValueSame) {
                        centerY = getMeasuredHeight() * 4 / 8;
                    } else {
                        centerY = (int) (minY + (maxValue - valueView.getValue()) / offsetValue * validHeight);
                    }
                    valueView.setCenter(centerX, centerY); // 循环遍历，设置每个value的绘制起点
                }

                final int last = keyViewCount - 1;

                ValueView valueView = valueViewList.get(last);
                verticalDashesLine = new VerticalDashesLine(getContext());
                verticalDashesLine.setPoints(valueView.centerX,
                        valueView.centerY,
                        valueView.centerX,
                        getMeasuredHeight() - DensityUtil.dp2px(getContext(), 20));

                floatValueView = new FloatValueView(getContext(), getWidth(), getHeight());
                floatValueView.attachValueView(valueView);

                pathView = new PathView(getContext()); // 创建折线
                pathView.lineValueViews(valueViewList); // 折线连接每个value

                mSelectedIndex = last;

                startAnimationPath();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isReadyForWork) {
            if (hLine != null) {
                hLine.draw(canvas);
            }

            final int keyViewCount = keyViewList.size(); // key数量
            for (int i = 0; i < keyViewCount; i++) {
                KeyView key = keyViewList.get(i);
                key.setSelected(mSelectedIndex == i);
                key.draw(canvas);
            }

            for (ValueView value : valueViewList) {
                value.draw(canvas);
            }

            if (pathView != null) {
                pathView.draw(canvas);
            }

            if (isReadyForDrawAfterAnimation) {
                floatValueView.draw(canvas);
                verticalDashesLine.draw(canvas);
            }
        }
    }

    private void startAnimationPath() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                pathView.setPhase(value.floatValue());
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isReadyForDrawAfterAnimation = true;
                invalidate();
            }
        });
        animator.setDuration(2000);
        animator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isReadyForWork) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    final int valueViewCount = valueViewList.size();
                    for (int i = 0; i < valueViewCount; i++) {
                        ValueView valueView = valueViewList.get(i);
                        if (valueView.isTouched(x, y) && mSelectedIndex != i) {
                            mSelectedIndex = i;

                            verticalDashesLine = new VerticalDashesLine(getContext());
                            verticalDashesLine.setPoints(valueView.centerX,
                                    valueView.centerY,
                                    valueView.centerX,
                                    getMeasuredHeight() - DensityUtil.dp2px(getContext(), 20));

                            floatValueView = new FloatValueView(getContext(), getWidth(), getHeight());
                            if (mSelectedIndex == valueViewCount - 1) {
                                floatValueView.attachValueView(valueView);
                            } else {
                                floatValueView.attachValueView(valueView);
                            }

                            invalidate();
                            break;
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:

                    break;
            }
        }
        return true;
    }
}
