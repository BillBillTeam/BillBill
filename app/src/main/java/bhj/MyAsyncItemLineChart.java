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
 * Created by mary kt on 17-11-23.
 * 统计之横向柱状图
 */

public class MyAsyncItemLineChart {
    private LinearLayout mItems;
    private ProgressBar progressBar;
    private Context context;
    private LinearLayout layout;
    private statistics.BarChartValue barChartValue;
    private Callback mycallback;

    public interface Callback{
        /**
         * 通过回调函数获得数据
         * @return BarChartValue 中存储着表需要的各种数据
         */
        statistics.BarChartValue getData();
    }

    /**
     * 初始化
     * @param context 上下文
     * @param progressBar 界面上的进度条（图表加载完成后要隐藏它）
     * @param linearLayout 包含图表的layout
     * @param callback 回调函数
     */
    public MyAsyncItemLineChart(Context context, ProgressBar progressBar, LinearLayout linearLayout, Callback callback){
        this.context=context;
        this.progressBar=progressBar;
        this.layout=linearLayout;
        this.mycallback=callback;
        mItems=new LinearLayout(context);
        mItems.setOrientation(LinearLayout.VERTICAL);
    }

    /**
     *  运行图表创建过程
     */
    public void run(){
        final myAsyncTask.Callback callback=new myAsyncTask.Callback() {

            @Override
            public void CallbackOnPerExecute() {


            }

            @Override
            public void CallbackDoInBackground() {
                //从树据库查询数据
                barChartValue=mycallback.getData();
                //创建表
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


    /**
     * 创建表
     */
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

    /**
     * 获得整个表
     * @return 表View
     */
    public LinearLayout getView(){

        return mItems;
    }



}
