package fivene.billbill;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TabHost;

public class StatisticsActivity extends AppCompatActivity {
    //three tab context
    LinearLayout layout1;
    LinearLayout layout2;
    LinearLayout layout3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        layout1=(LinearLayout)findViewById(R.id.tab1_content);
        layout2=(LinearLayout)findViewById(R.id.tab2_content);
        layout3=(LinearLayout)findViewById(R.id.tab3_content);
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
                        loadTab1();
                        break;
                    case "tabTwo":
                        loadTab2();
                        break;
                    case "tabThree":
                        loadTab3();
                        break;


                }
            }
        });
        loadTab1();
    }
    private void loadTab1(){

    }
    private void loadTab2(){

    }
    private void loadTab3(){

    }
}
