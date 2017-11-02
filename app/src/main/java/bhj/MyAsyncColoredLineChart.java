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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by ubuntu on 17-11-1.
 * 折线图（异步）
 */

public class MyAsyncColoredLineChart implements  OnChartValueSelectedListener {
    private LineChart mChart;

    private ProgressBar progressBar;
    private LinearLayout layout;
    //add barchart values at here
    //and init values at setValues()
    //and set it on createBarChart()
    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };


    public MyAsyncColoredLineChart(Context context,ProgressBar progressBar,LinearLayout linearLayout){
        this.progressBar=progressBar;
        this.layout=linearLayout;
        this.mChart=new LineChart(context);
    }
    public void setValues(){

    }

    public void run(){
        myAsyncTask.Callback callback=new myAsyncTask.Callback() {

            @Override
            public void CallbackOnPerExecute() {


            }

            @Override
            public void CallbackDoInBackground() {

                //colored line chart
                //horizontal bar chart
                createLineChart();


            }

            @Override
            public void CallbackOnPostExecute() {
                mChart.animateX(1000);
                // get the legend (only possible after setting data)
                Legend l = mChart.getLegend();

                // modify the legend ...
                l.setForm(Legend.LegendForm.LINE);
                progressBar.setVisibility(View.GONE);
                mChart.setMinimumHeight(400);
                layout.addView(mChart);
            }
        };


        myAsyncTask task=new myAsyncTask();
        task.setCallback(callback);
        task.execute();
    }



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
                return "text";
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
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);




        mChart.setMaxVisibleValueCount(60);

        setData(12, 50);



    }
    public LineChart getView(){

        return mChart;
    }

    private void setData    (int count, float range) {

        float barWidth = 9f;
        float spaceForBar = 10f;//
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        //add values at here
        yVals1.add(new Entry( spaceForBar*0,5));
        yVals1.add(new Entry( spaceForBar*1,5));
        yVals1.add(new Entry( spaceForBar*2,5));
        yVals1.add(new Entry( spaceForBar*5,5));

        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        //add values at here
        yVals2.add(new Entry( spaceForBar*0,3));
        yVals2.add(new Entry( spaceForBar*1,3));
        yVals2.add(new Entry( spaceForBar*4,3));
        yVals2.add(new Entry( spaceForBar*5,3));





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
            set1 = new LineDataSet(yVals1, "DataSet 1");
            set1.setDrawIcons(false);
            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

            LineDataSet set2 = new LineDataSet(yVals2, "DataSet 2");
            set2.setColor(Color.rgb(140, 234, 0));

            set2.setDrawIcons(false);


            dataSets.add(set1);
            dataSets.add(set2);

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
