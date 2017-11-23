package bhj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.ArrayList;

import fivene.billbill.R;
import lhq.ie.ExpenseType;
import lz.img.IconGetter;
import yzj.t.statistics;

/**
 * Created by ubuntu on 17-11-23.
 */

public class MyAsyncItemLineChart {
    private LinearLayout mItems;
    //    protected Typeface mTfRegular;
    //protected Typeface mTfLight;
    private ProgressBar progressBar;
    private Context context;
    private LinearLayout layout;
    private statistics.BarChartValue barChartValue;
    private Callback mycallback;
    //add barchart values at here
    //and init values at setValues()
    //and set it on createBarChart()

    public interface Callback{
        statistics.BarChartValue getData();
    }


    public MyAsyncItemLineChart(Context context, ProgressBar progressBar, LinearLayout linearLayout, Callback callback){
        this.context=context;
        this.progressBar=progressBar;
        this.layout=linearLayout;
        this.mycallback=callback;
        mItems=new LinearLayout(context);
        mItems.setOrientation(LinearLayout.VERTICAL);
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
                if(progressBar.isShown())
                    progressBar.setVisibility(View.GONE);
                layout.addView(mItems);
            }
        };


        myAsyncTask task=new myAsyncTask();
        task.setCallback(callback);
        task.execute();
    }



    private void createBarChart() {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        ExpenseType expenseType=new ExpenseType(context);
        for(int i=0;i<barChartValue.types.size();i++ ) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View Item = inflater.inflate(R.layout.barchart_item, null);
            ProgressBar progressBar1=(ProgressBar) Item.findViewById(R.id.item_progressBar);
            TextView type=(TextView)Item.findViewById(R.id.text_type);
            TextView count=(TextView)Item.findViewById(R.id.text_count);
            TextView per=(TextView)Item.findViewById(R.id.text_per);
            TextView num=(TextView)Item.findViewById(R.id.text_num);
            ImageView img=(ImageView)Item.findViewById(R.id.imageView);

            img.setImageBitmap(IconGetter.getClickedIcon(context,expenseType.searchRes_ID(barChartValue.types.get(i).name)));

            type.setText(barChartValue.types.get(i).name);
            count.setText(String.valueOf(barChartValue.types.get(i).value));
            per.setText(((int)((barChartValue.types.get(i).value/barChartValue.sum)*1000))/10+"%");
            num.setText(barChartValue.types.get(i).num+"笔");
            progressBar1.setProgress((int)(barChartValue.types.get(i).value/barChartValue.sum*1000));




            mItems.addView(Item,p);

        }


    }
    public LinearLayout getView(){

        return mItems;
    }

    private void setData(int count, float range) {

    }

}
