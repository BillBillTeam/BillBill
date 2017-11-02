package fivene.billbill;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TabHost;

import com.github.mikephil.charting.charts.HorizontalBarChart;

import bhj.MyAsyncHorizontalBarChart;
import bhj.myAsyncTask;
public class StatisticsActivity extends AppCompatActivity {
    //three tab context
    LinearLayout layout1;
    LinearLayout layout2;
    LinearLayout layout3;

    ProgressBar progressBar1;
    ProgressBar progressBar2;
    ProgressBar progressBar3;

    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        mContext=this;
        layout1=(LinearLayout)findViewById(R.id.tab1_content);
        layout2=(LinearLayout)findViewById(R.id.tab2_content);
        layout3=(LinearLayout)findViewById(R.id.tab3_content);
        progressBar1=(ProgressBar)findViewById(R.id.progressBar1);
        progressBar2=(ProgressBar)findViewById(R.id.progressBar2);
        progressBar3=(ProgressBar)findViewById(R.id.progressBar3);
        TabHost host = (TabHost)findViewById(R.id.tabhost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("tabOne");
        spec.setContent(R.id.tab1);
        spec.setIndicator("周统计");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("tabTwo");
        spec.setContent(R.id.tab2);
        spec.setIndicator("月统计");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("tabThree");
        spec.setContent(R.id.tab3);
        spec.setIndicator("年统计");
        host.addTab(spec);

        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                switch (s){
                    case "tabOne":
                        if(progressBar1.getVisibility()!=View.GONE)
                        loadTab1();
                        break;
                    case "tabTwo":
                        if(progressBar2.getVisibility()!=View.GONE)
                        loadTab2();
                        break;
                    case "tabThree":
                        if(progressBar3.getVisibility()!=View.GONE)
                        loadTab3();
                        break;


                }
            }
        });
        loadTab1();
    }

    private void loadTab1(){
        MyAsyncHorizontalBarChart chart=new MyAsyncHorizontalBarChart(this,progressBar1,layout1);
        chart.setValues();
        chart.run();
    }
    private void loadTab2(){
        MyAsyncHorizontalBarChart chart=new MyAsyncHorizontalBarChart(this,progressBar2,layout2);
        chart.setValues();
        chart.run();

    }
    private void loadTab3(){
        MyAsyncHorizontalBarChart chart=new MyAsyncHorizontalBarChart(this,progressBar3,layout3);
        chart.setValues();
        chart.run();

    }
}
