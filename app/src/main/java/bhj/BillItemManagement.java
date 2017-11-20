package bhj;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import fivene.billbill.R;
import lhq.ie.Expense;
import lhq.ie.ExpenseType;
import lz.db.Bill;
import lz.db.IDBill;
import lz.img.IconGetter;

/**
 * Created by mary kt on 17-11-19.
 */

public class BillItemManagement {
    private Bill bill;
    private Callback callback;
    private SwipeLayout billItem;
    public BillItemManagement(final Context context, final IDBill bill, Callback call){
        this.callback=call;
        LayoutInflater inflater = LayoutInflater.from(context);
        billItem = (SwipeLayout) inflater.inflate(R.layout.bill_item_plus, null);
        TextView Type=(TextView) billItem.findViewById(R.id.textView5);
        TextView mark=(TextView)billItem.findViewById(R.id.textView6);
        TextView number=(TextView)billItem.findViewById(R.id.textView7);
        ImageView img=(ImageView)billItem.findViewById(R.id.imageView);
        ExpenseType expenseType=new ExpenseType(context);

        img.setImageBitmap(IconGetter.getIcon(context,expenseType.searchRes_ID(bill.getType())));

        Type.setText(bill.getType());
        mark.setText(bill.getRemark());
        number.setText(String.valueOf((float)bill.getAmount()));
        //TODO:add img recid

        //img.setImageBitmap(IconGetter.getIcon(context,bill.ge));

        billItem.setShowMode(SwipeLayout.ShowMode.LayDown);
        billItem.addDrag(SwipeLayout.DragEdge.Right, billItem.findViewWithTag("Bottom2"));
//        sample2.setShowMode(SwipeLayout.ShowMode.PullOut);
        billItem.findViewById(R.id.star).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Star", Toast.LENGTH_SHORT).show();
               callback.onEdit(bill);
            }
        });

        billItem.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "您删除了一条记录", Toast.LENGTH_SHORT).show();
                Expense expense=new Expense(context);
                expense.Delete(bill);
                if(((LinearLayout)(billItem.getParent())).getChildCount()>2)
                ((LinearLayout)(billItem.getParent())).removeView(billItem);
                else{
                    ((LinearLayout)billItem.getParent().getParent()).removeView((View)billItem.getParent());
                    return;
                }


                //recount amount


                callback.onDelete(bill.getAmount());
            }
        });


        billItem.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Click on surface", Toast.LENGTH_SHORT).show();
            }
        });


    }
    public SwipeLayout getView(){
        return billItem;

    }

    public interface Callback{
        void onDelete(double amount);
        void onEdit(IDBill bill);

    }





}
