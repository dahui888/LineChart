# 自定义收益折线图，效果超赞

<image src="./image/line_chart.gif" />

**布局中使用**

```xml
    <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        android:id="@+id/activity_main"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context="com.example.linechart.MainActivity">
    
        <chart.LineChartView
            android:id="@+id/chart_view"
            android:background="#eeeeeeee"
            android:layout_width="400dp"
            android:layout_height="200dp"
            android:layout_centerInParent="true"
            android:text="Hello World!" />
    </RelativeLayout>
```

**代码中使用**
```java
        LineChartView chartView = (LineChartView) findViewById(R.id.chart_view);

        List<String> keys = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            keys.add(i < 10 ? "0" + i : "" + i);
        }

        List<Double> values = new ArrayList<>();
        values.add(Double.valueOf(2334.12));
        values.add(Double.valueOf(833.14));
        values.add(Double.valueOf(1452.56));
        values.add(Double.valueOf(223.14));
        values.add(Double.valueOf(631.14));
        values.add(Double.valueOf(23.14));
        values.add(Double.valueOf(563.14));
        values.add(Double.valueOf(1523.14));
        values.add(Double.valueOf(223.14));
        values.add(Double.valueOf(790.14));
        values.add(Double.valueOf(23.14));
        values.add(Double.valueOf(1934.1342));

        chartView.workWith(keys, values); // 调用workWith方法
```

# 如何联系我：

QQ：129830085，QQ群：83936534