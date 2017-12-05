package bhj;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
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

import java.text.DecimalFormat;

import fivene.billbill.R;
import lhq.ie.Expense;
import lhq.ie.ExpenseType;
import lz.db.IDBill;
import lz.img.IconGetter;

/**
 * Created by mary kt on 17-11-19.
 * 每个类对应着billListActivity中的一条数据
 * 主要是完成UI方面的工作
 *
 */

public class BillItemManagement {
    private Callback callback;
    private MySwipeLayout billItem;
    private static MySwipeLayout selectedItem=null;

    /**
     * 初始化
     * @param context 上下文
     * @param bill 一条账单数据
     * @param call 回调函数
     */
    public BillItemManagement(final Context context, final IDBill bill, final Callback call){
        this.callback=call;
        LayoutInflater inflater = LayoutInflater.from(context);
        billItem =  (MySwipeLayout)inflater.inflate(R.layout.bill_item_plus, null);
        //设置回调函数
        //作用：每次只能选中一条账单记录
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
        //数据库初始化
        ExpenseType expenseType=new ExpenseType(context);
        //设置ui上显示的数据
        img.setImageBitmap(IconGetter.getIcon(context,expenseType.searchRes_ID(bill.getType())));
        Type.setText(bill.getType());
        mark.setText(bill.getRemark());
        DecimalFormat df   =     new   DecimalFormat( "################0.00");
        String   temp     =   df.format(bill.getAmount());
        number.setText(temp);

        //编辑账单数据
        billItem.findViewById(R.id.star).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billItem.SimulateScroll(MySwipeLayout.SHRINK);

               callback.onEdit(bill);
            }
        });
        //删除账单数据
        billItem.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int deleteType;
                final LinearLayout layout;
                final View deletedView;
                final int delPostion;
                billItem.SimulateScroll(MySwipeLayout.SHRINK);
                //如果当日账单数据有两条及以上
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
                Expense expense=new Expense(context);
                expense.Delete(bill);
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
                                Expense expense=new Expense(context);
                                expense.Insert(bill);
                                toast.dismiss();
                            }

                        })
                        .setProgressBarColor(Color.WHITE)
                        .setText("您删除了一条记录！")
                        .setDuration(Style.DURATION_LONG)
                        .setFrame(Style.FRAME_LOLLIPOP)
                        .setColor(PaletteUtils.getSolidColor(PaletteUtils.GREY))
                        .setAnimations(Style.ANIMATIONS_POP);
                if(deleteType==0)
                    callback.onDelete(bill.getAmount(),toast);
                toast.cancelAllSuperToasts();
                toast.show();


                //toast.setProgress();
            }
        });



    }

    /**
     * 获得生成好的ui控件
     * @return MySwipeLayout
     */
    public MySwipeLayout getView(){
        return billItem;

    }

    /**
     * 回调函数
     * 1.删除事件
     * 2.编辑事件
     * 3.取消删除事件
     */
    public interface Callback{
        /**
         * 删除账单数据和控件
         * @param amount 账单消费额
         * @param toast 弹出删除提示的提示框
         */
        void onDelete(double amount,SuperActivityToast toast);

        /**
         * 编辑账单数据
         * @param bill 账单数据
         */
        void onEdit(IDBill bill);

        /**
         * 取消删除（发生在删除提示框提示时取消删除）
         * @param amount 账单消费额
         */
        void onCancle(double amount);
    }





}
