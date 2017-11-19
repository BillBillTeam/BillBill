package fivene.billbill;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import bhj.BillItemManagement;
import bhj.myAsyncTask;
import lhq.ie.Expense;
import lz.db.Bill;
import lz.db.IDBill;

public class BillListActivity extends AppCompatActivity {


    private View createTimeLine(int year,int month,int day,double amounts){
        final LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout b_c_layout = (LinearLayout) inflater.inflate(
                R.layout.bill_column,null).findViewById(R.id.bill_column_layout);
        TextView t_day=(TextView) b_c_layout.findViewById(R.id.textView2);
        t_day.setText(month+"月"+day+"日");
        TextView t_amount=(TextView) b_c_layout.findViewById(R.id.textView4);
        t_amount.setText(""+amounts);
        return b_c_layout;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_bill_list);

        final LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout lin = (LinearLayout) findViewById(R.id.bill_list_layout);
        //从树据库查询
        Expense expense=new Expense(this);
        ArrayList<IDBill> idBills=expense.selectAll();


        int year,month,day;
        double amounts=0;
        ArrayList<IDBill> idBills_oneDay=new ArrayList<>();
        for(int i=0;i<idBills.size();i++){//处理所有的数据
            year=idBills.get(i).getYear();
            month=idBills.get(i).getMonth();
            day=idBills.get(i).getDay();
            LinearLayout layout=new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            for(int j=i;j<idBills.size();j++){//把同一天的放到一起并统计总金额
                if(idBills.get(j).getYear()==year&&idBills.get(j).getMonth()==month&&idBills.get(j).getDay()==day){
                    idBills_oneDay.add(idBills.get(j));
                    amounts=amounts+idBills.get(j).getAmount();
                }
            }
            //添加日期条
            final View TimeLine=createTimeLine(year,month,day,amounts);
            layout.addView(TimeLine);
            //添加账单信息
            for(int j=0;j<idBills_oneDay.size();j++){
                BillItemManagement.Callback callback=new BillItemManagement.Callback() {
                    @Override
                    public void onDelete(double amount) {
//                        TextView view=(TextView) ((LinearLayout)((LinearLayout)billItem.getParent()).getChildAt(0)).getChildAt(3);
//                        view.setText(String.valueOf((float)(Double.valueOf(view.getText().toString())-bill.getAmount())));
                        TextView view=(TextView)TimeLine.findViewById(R.id.textView4);
                        view.setText(String.valueOf((float)(Double.valueOf(view.getText().toString())-amount)));
                    }

                    @Override
                    public void onEdit() {

                    }
                };

                BillItemManagement M=new BillItemManagement(this,idBills_oneDay.get(j),callback);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layout.addView(M.getView(),p);

            }
            lin.addView(layout);

            i=i+idBills_oneDay.size()-1;
            amounts=0;
            idBills_oneDay.clear();

        }

//         // 日期信息
//        LinearLayout b_c_layout = (LinearLayout) inflater.inflate(
//                R.layout.bill_column,null).findViewById(R.id.bill_column_layout);
//
//
//        //create a billitem_plus for test
//        BillItemManagement.Callback callback=new BillItemManagement.Callback() {
//            @Override
//            public void onDelete() {
//
//            }
//
//            @Override
//            public void onEdit() {
//
//            }
//        };
//        IDBill b=new IDBill(3,2,1,"买游戏",3.3,"hhhhh",-1);
//        BillItemManagement M=new BillItemManagement(this,b,callback);
//        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//
//        lin.addView(M.getView(),p);
//
//
//         // 将布局加入到当前布局中(动态)
//        lin.addView(b_c_layout);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
