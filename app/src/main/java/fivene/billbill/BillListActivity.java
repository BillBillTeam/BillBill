package fivene.billbill;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.library.SuperActivityToast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import bhj.BillItemManagement;
import lhq.ie.Expense;
import lhq.ie.ExpenseType;
import lz.db.CustomType;
import lz.db.IDBill;

/**
 * 账单显示界面
 */
public class BillListActivity extends AppCompatActivity {
    private LinearLayout lin;
    private SuperActivityToast currentToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("消费记录");
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_bill_list);

        final LayoutInflater inflater = LayoutInflater.from(this);
        lin = (LinearLayout) findViewById(R.id.bill_list_layout);
        createViews();

    }

    /**
     * 初始化消费记录
     */
    private void createViews(){
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
                //设置回调函数
                BillItemManagement.Callback callback=new BillItemManagement.Callback() {


                    @Override
                    public void onDelete(double amount, SuperActivityToast toast) {
                        if (currentToast!=null&&currentToast.isShowing()){
                            toast.dismiss();
                        }
                        currentToast=toast;
                        TextView view=(TextView)TimeLine.findViewById(R.id.textView4);
                        view.setText(String.valueOf((float)(Double.valueOf(view.getText().toString())-amount)));
                    }

                    @Override
                    public void onEdit(IDBill bill) {
                        showItemEidtor(bill);

                    }

                    @Override
                    public void onCancle(double amount) {

                        TextView view=(TextView)TimeLine.findViewById(R.id.textView4);
                        view.setText(String.valueOf((float)(Double.valueOf(view.getText().toString())+amount)));

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

    /**
     * 显示编辑账单界面
     * @param bill 被编辑的数据
     */
    private void showItemEidtor(final IDBill bill){
        final View dialgoView = View.inflate(this, R.layout.bill_eidtor, null); //初始化输入对话框的布局

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
      //  builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("请输入您想要的记录");
        //    设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(dialgoView);
        final EditText amount = (EditText)dialgoView.findViewById(R.id.editText_amount);
        final EditText mark = (EditText)dialgoView.findViewById(R.id.editText_mark);
        final Spinner sp=(Spinner) dialgoView.findViewById(R.id.spinner);
        final DatePicker datePicker=(DatePicker)dialgoView.findViewById(R.id.datePicker2);

        amount.setText(String.valueOf( bill.getAmount()));
        amount.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        mark.setText(bill.getRemark());
        List<String> list = new ArrayList<String>();
        list.add(bill.getType());
        ExpenseType expenseType =new ExpenseType(this);
        ArrayList<CustomType> typeArrayList=expenseType.getAllShowExpenseType();
        for(int i=0;i<typeArrayList.size();i++){
            if(!typeArrayList.get(i).getType().equals(  bill.getType())){

                list.add(typeArrayList.get(i).getType());
            }

        }
        typeArrayList=expenseType.getAllHideExpenseType();
        for(int i=0;i<typeArrayList.size();i++){
            if(!typeArrayList.get(i).getType().equals( bill.getType())){

                list.add(typeArrayList.get(i).getType());
            }

        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        datePicker.init(bill.getYear(), bill.getMonth()-1, bill.getDay(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {


            }
        });



        builder.setPositiveButton("确定", null);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        final AlertDialog  pwdDialog=builder.create();
        pwdDialog.show();
        pwdDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double a;
                try {
                    a = Double.valueOf(amount.getText().toString());
                }
                catch (Exception e){
                    amount.setTextColor(Color.RED);
                    return ;

                }

                String b = mark.getText().toString();
                String type=sp.getSelectedItem().toString();
                int year=datePicker.getYear();
                int month=datePicker.getMonth()+1;
                int day=datePicker.getDayOfMonth();
                Expense expense=new Expense(BillListActivity.this);
                IDBill newBill=new IDBill(year,month,day,type,a,b,bill.getID());
                expense.Update(newBill);

                pwdDialog.cancel();
                lin.removeAllViews();
                createViews();
            }
        }
    );


    }

    @Override
    protected void onDestroy() {
        if(currentToast!=null &&currentToast.isShowing())
            currentToast.dismiss();
        super.onDestroy();
    }

    /**
     * 通过日期获得星期几
     * @param year 年
     * @param month 月
     * @param day 日
     * @return String 日期
     */
    private String getdayOfWeek(int year,int month,int day){
        String[] val={"周日","周一","周二","周三","周四","周五","周六"};
        Calendar calendar=new GregorianCalendar(year,month-1,day);
        int dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);
        return val[dayOfWeek-1];

    }

    /**
     * 创建时间条
     * @param year 年
     * @param month 月
     * @param day 日
     * @param amounts 总支出
     * @return 创建好的VIEW
     */
    private View createTimeLine(int year, int month, int day, double amounts){
        final LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout b_c_layout = (LinearLayout) inflater.inflate(
                R.layout.bill_column,null).findViewById(R.id.bill_column_layout);
        TextView t_day=(TextView) b_c_layout.findViewById(R.id.textView2);
        TextView dayOfWeek=(TextView)b_c_layout.findViewById(R.id.textView3);
        dayOfWeek.setText(getdayOfWeek(year,month,day));
        t_day.setText(month+"月"+day+"日");

        TextView t_amount=(TextView) b_c_layout.findViewById(R.id.textView4);
        t_amount.setText(String.valueOf( amounts));
        return b_c_layout;
    }
}
