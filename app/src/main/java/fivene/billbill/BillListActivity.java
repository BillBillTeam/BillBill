package fivene.billbill;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class BillListActivity extends AppCompatActivity {
  private   LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_list);
        ll=(LinearLayout) findViewById(R.id.bill_list_layout);
        Button bt = new Button(this);
        ll.addView(bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillListActivity.this,StatisticsActivity.class);
                BillListActivity.this.startActivity(intent);
            }
        });
    }
}
