package fivene.billbill;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import bhj.BillItemManagement;
import bhj.myAsyncTask;
import lz.db.Bill;

public class BillListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_bill_list);
        //解析
        final LayoutInflater inflater = LayoutInflater.from(this);

         // 获取需要被添加控件的布局
        final LinearLayout lin = (LinearLayout) findViewById(R.id.bill_list_layout);

         // 获取需要添加的布局
        LinearLayout b_c_layout = (LinearLayout) inflater.inflate(
                R.layout.bill_column,null).findViewById(R.id.bill_column_layout);
        LinearLayout b_i_layout = (LinearLayout) inflater.inflate(
                R.layout.bill_item, null).findViewById(R.id.bill_item_layout);

        //create a billitem_plus for test
        BillItemManagement.Callback callback=new BillItemManagement.Callback() {
            @Override
            public void onDelete() {

            }

            @Override
            public void onEdit() {

            }
        };
        Bill b=new Bill(3,2,1,"买游戏",3.3,"hhhhh");
        BillItemManagement M=new BillItemManagement(this,b,callback);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        lin.addView(M.getView(),p);


         // 将布局加入到当前布局中(动态)
        lin.addView(b_c_layout);
        lin.addView(b_i_layout);

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
