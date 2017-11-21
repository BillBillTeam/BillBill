package bhj;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;

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
    private MySwipeLayout billItem;
    static MySwipeLayout selectedItem=null;
    public BillItemManagement(final Context context, final IDBill bill, final Callback call){
        this.callback=call;
        LayoutInflater inflater = LayoutInflater.from(context);
        billItem =  (MySwipeLayout)inflater.inflate(R.layout.bill_item_plus, null);
        MySwipeLayout.Callback callback1=new MySwipeLayout.Callback() {
            @Override
            public void OnExpend() {
                if(selectedItem!=null)
                    selectedItem.SimulateScroll(MySwipeLayout.SHRINK);
                selectedItem=billItem;
            }

            @Override
            public void OnShrink() {
                selectedItem=null;
            }
        };



        billItem.setCallback(callback1);
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

   //     billItem.setShowMode(SwipeLayout.ShowMode.LayDown);
   //     billItem.addDrag(SwipeLayout.DragEdge.Right, billItem.findViewWithTag("Bottom2"));
//        sample2.setShowMode(SwipeLayout.ShowMode.PullOut);
        billItem.findViewById(R.id.star).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billItem.SimulateScroll(MySwipeLayout.SHRINK);

               callback.onEdit(bill);
            }
        });

        billItem.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int deleteType;
                final LinearLayout layout;
                final View deletedView;
                final int delPostion;
                billItem.SimulateScroll(MySwipeLayout.SHRINK);
                if(((LinearLayout)(billItem.getParent())).getChildCount()>2) {
                    layout=((LinearLayout) (billItem.getParent()));
                    deletedView=billItem;
                    delPostion=layout.indexOfChild(deletedView);
                    layout.removeView(billItem);

                    deleteType=0;
                }

                else{
                    layout=((LinearLayout)billItem.getParent().getParent());
                    deletedView=(View)billItem.getParent();
                    delPostion=layout.indexOfChild(deletedView);
                    layout.removeView((View)billItem.getParent());

                        deleteType=1;

                }
                if(deleteType==0)
                callback.onDelete(bill.getAmount());


                //superToast
                final SuperActivityToast toast= SuperActivityToast.create(context, new Style(), Style.TYPE_BUTTON);
                toast.setButtonText("撤销")
                        //.setButtonIconResource(R.drawable.ic_undo)
                        .setOnButtonClickListener("good_tag_name", null, new SuperActivityToast.OnButtonClickListener() {
                            @Override
                            public void onClick(View view, Parcelable token) {
                                layout.addView(deletedView,delPostion);
                                if(deleteType==0){//re add value to timeline
                                    callback.onCancle(bill.getAmount());

                                }
                                toast.setOnDismissListener(null);
                                toast.dismiss();
                            }

                        })
                        .setProgressBarColor(Color.WHITE)
                        .setText("您删除了一条记录！")
                        .setDuration(Style.DURATION_LONG)
                        .setFrame(Style.FRAME_LOLLIPOP)
                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.GREY))
                        .setAnimations(Style.ANIMATIONS_POP)
                        .setOnDismissListener(new SuperToast.OnDismissListener() {
                            @Override
                            public void onDismiss(View view, Parcelable token) {
                                Expense expense=new Expense(context);
                                expense.Delete(bill);
                            }
                        })
                        .show();
//
//                Toast.makeText(context, "您删除了一条记录", Toast.LENGTH_SHORT).show();

            }
        });


//        billItem.getSurfaceView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "Click on surface", Toast.LENGTH_SHORT).show();
//            }
//        });


    }
    public MySwipeLayout getView(){
        return billItem;

    }

    public interface Callback{
        void onDelete(double amount);
        void onEdit(IDBill bill);
        void onCancle(double amount);
    }





}
