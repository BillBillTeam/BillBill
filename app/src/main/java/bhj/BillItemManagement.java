package bhj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import fivene.billbill.R;
import lz.db.Bill;
import lz.img.IconGetter;

/**
 * Created by mary kt on 17-11-19.
 */

public class BillItemManagement {
    private Bill bill;
    private Callback callback;
    private SwipeLayout billItem;
    public BillItemManagement(final Context context, Bill bill,Callback call){
        this.callback=call;
        LayoutInflater inflater = LayoutInflater.from(context);
        billItem = (SwipeLayout) inflater.inflate(R.layout.bill_item_plus, null);
        TextView Type=(TextView) billItem.findViewById(R.id.textView5);
        TextView number=(TextView)billItem.findViewById(R.id.textView7);
        ImageView img=(ImageView)billItem.findViewById(R.id.imageView);


        Type.setText(bill.getType());
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
               callback.onEdit();
            }
        });

        billItem.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Trash Bin", Toast.LENGTH_SHORT).show();
                callback.onDelete();
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
        void onDelete();
        void onEdit();

    }





}
