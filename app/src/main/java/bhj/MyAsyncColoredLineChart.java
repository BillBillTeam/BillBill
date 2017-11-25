package bhj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

import yzj.t.statistics;

/**
 * Created by mary kt on 17-11-1.
 * 折线图（异步）
 */

public class MyAsyncColoredLineChart implements  OnChartValueSelectedListener {
    private LineChart mChart;

    private ProgressBar progressBar;
    private LinearLayout layout;
    private Callback mycallback;
    statistics.LineChartValue lineChartValue;
    private ArrayList<Double> data;
    private ArrayList<String> dataDesc;
    private String dataSetName;

    public interface Callback{
        statistics.LineChartValue getData();
//        ArrayList<String> getDataDesc();
//        String getDataSetName();

    }

    /**
     * 初始化
     * @param context 上下文
     * @param progressBar 界面上的进度条（图表加载完成后要隐藏它）
     * @param linearLayout 包含图表的layout
     * @param callback 回调函数
     */
    public MyAsyncColoredLineChart(Context context,ProgressBar progressBar,LinearLayout linearLayout, Callback callback){
        this.progressBar=progressBar;
        this.layout=linearLayout;
        this.mycallback=callback;
        this.mChart=new LineChart(context);
    }

    /**
     * 运行图表创建过程
     */
    public void run(){
        //创建回调函数
        myAsyncTask.Callback callback=new myAsyncTask.Callback() {

            @Override
            public void CallbackOnPerExecute() {


            }
            @Override
            public void CallbackDoInBackground() {

                //从数据库获得数据
                lineChartValue=mycallback.getData();
                data=lineChartValue.values;
                dataDesc=lineChartValue.x_lineName;
                dataSetName=lineChartValue.dataSetName;
                //通过数据创建表
                createLineChart();

            }

            @Override
            public void CallbackOnPostExecute() {
                mChart.animateX(1000);
                Legend l = mChart.getLegend();

                l.setForm(Legend.LegendForm.LINE);
                if(progressBar.isShown())
                    progressBar.setVisibility(View.GONE);
                mChart.setMinimumHeight(400);
                layout.addView(mChart);
            }
        };


        myAsyncTask task=new myAsyncTask();
        task.setCallback(callback);
        task.execute();
    }


    /**
     * 创建表
     */
    private void createLineChart() {


        mChart.setOnChartValueSelectedListener(this);
        // mChart.setHighlightEnabled(false);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn


        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);


        mChart.setDrawGridBackground(false);

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxisPosition.BOTTOM);
        // xl.setTypeface(mTfLight);
        xl.setDrawLimitLinesBehindData(false);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(10f);

        //set left text value
//    ex:
//     xAxis.setValueFormatter(new IAxisValueFormatter() {
//         @Override
//         public String getFormattedValue(float value, AxisBase axis) {
//             return mMonths[(int) value % mMonths.length];
//         }
//     });
        xl.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return dataDesc.get((int)value/10);
            }
        });

        YAxis yl = mChart.getAxisLeft();
        //   yl.setTypeface(mTfLight);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);


        YAxis yr = mChart.getAxisRight();
        //    yr.setTypeface(mTfLight);
        yr.setDrawAxisLine(false);
        yr.setDrawGridLines(false);
        yr.setDrawLabels(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);




        mChart.setMaxVisibleValueCount(60);

        setData();



    }

    /**
     * 获得表的View
     * @return LineChart
     */
    public LineChart getView(){

        return mChart;
    }

    /**
     * 设置表数据
     */
    private void setData    () {
        float spaceForBar = 10f;//
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        //add values at here
        for(int i=0;i<dataDesc.size();i++) {
            if(i<data.size())
            yVals1.add(new Entry(spaceForBar * i, data.get(i).floatValue()));
        }
        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            //重新更新表信息
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);

            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            //添加表信息
            set1 = new LineDataSet(yVals1, dataSetName);
            set1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return String.valueOf(value);//return your text
                }
            });
            set1.setDrawIcons(false);
            set1.setDrawValues(false);
            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            data.setValueTextSize(10f);
            //data.setValueTypeface(mTfLight);
            mChart.setData(data);
        }
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
