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
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

import yzj.t.statistics;

/**
 * Created by ubuntu on 17-11-1.
 * 横向的柱状图(后台加载)
 * no use see MyAsyncItemLineChart
 */

public class MyAsyncHorizontalBarChart implements  OnChartValueSelectedListener {
    private HorizontalBarChart mChart;
//    protected Typeface mTfRegular;
   //protected Typeface mTfLight;
    private ProgressBar progressBar;
    private LinearLayout layout;
    private statistics.BarChartValue barChartValue;
    private Callback mycallback;
    //add barchart values at here
    //and init values at setValues()
    //and set it on createBarChart()

    public interface Callback{
        statistics.BarChartValue getData();
    }


    public MyAsyncHorizontalBarChart(Context context, ProgressBar progressBar, LinearLayout linearLayout, Callback callback){
        this.progressBar=progressBar;
        this.layout=linearLayout;
        this.mycallback=callback;
        this.mChart=new HorizontalBarChart(context);
    }
    public void setType(int type){

    }

    public void run(){
        final myAsyncTask.Callback callback=new myAsyncTask.Callback() {

            @Override
            public void CallbackOnPerExecute() {


            }

            @Override
            public void CallbackDoInBackground() {

                //colored line chart
                //horizontal bar chart
                barChartValue=mycallback.getData();

                //查询数据库？？


                createBarChart();


            }

            @Override
            public void CallbackOnPostExecute() {
                mChart.animateY(2500);
                progressBar.setVisibility(View.GONE);
                mChart.setMinimumHeight(400);
                layout.addView(mChart);
            }
        };


        myAsyncTask task=new myAsyncTask();
        task.setCallback(callback);
        task.execute();
    }



 private void createBarChart() {


        mChart.setOnChartValueSelectedListener(this);
        // mChart.setHighlightEnabled(false);

        mChart.setMinimumHeight(900);
        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(true);

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
                return barChartValue.types.get((int)(value/10)).name;
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
        mChart.setFitBars(true);



        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
    }
    public HorizontalBarChart getView(){

        return mChart;
    }

    private void setData(int count, float range) {

        float barWidth = 9f;
        float spaceForBar = 10f;//

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();

        for(int i=0;i<barChartValue.types.size();i++){
            ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

            yVals1.add(new BarEntry( spaceForBar*i,barChartValue.types.get(i).value.floatValue()));
            BarDataSet set1;
            set1 = new BarDataSet(yVals1, barChartValue.types.get(i).name);

            dataSets.add(set1);
        }


//        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
//        //add values at here
//        yVals1.add(new BarEntry( spaceForBar*0,5));
//        yVals1.add(new BarEntry( spaceForBar*1,5));
//        yVals1.add(new BarEntry( spaceForBar*2,5));
//        yVals1.add(new BarEntry( spaceForBar*5,5));
//
//        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
//        //add values at here
//        yVals2.add(new BarEntry( spaceForBar*0,3));
//        yVals2.add(new BarEntry( spaceForBar*1,3));
//        yVals2.add(new BarEntry( spaceForBar*4,3));
//        yVals2.add(new BarEntry( spaceForBar*5,3));
//
//        BarDataSet set1;
//
//            //添加表信息
//            set1 = new BarDataSet(yVals1, "DataSet 1");
//            set1.setDrawIcons(false);
//
//
//            BarDataSet set2 = new BarDataSet(yVals2, "DataSet 2");
//            set2.setColor(Color.rgb(140, 234, 0));
//
//            set2.setStackLabels(new String[]{"bian","zz","qq","mm"});
//            set2.setDrawIcons(false);
//
//
//            dataSets.add(set1);
//            dataSets.add(set2);




            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            //data.setValueTypeface(mTfLight);
            data.setBarWidth(barWidth);
            mChart.setData(data);

    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
