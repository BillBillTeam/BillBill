package bhj;



import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by mary kt on 17-11-26.
 * 时间选择器
 */
public class TimePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private Callback callback;

    /**
     * 用户选择时间以后的调用
     */
    public interface Callback{
        /**
         * 用户按下确定后的回调函数
         * @param datePicker 控件
         * @param i0 年
         * @param i1 月
         * @param i2 日
         */
        void setTime(DatePicker datePicker,int i0,int i1,int i2);
    }

    /**
     * 设置回调函数(用户选择完时间调用)
     * @param callBack
     */
    public void  setCallBack(Callback callBack){
        this.callback=callBack;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog= new DatePickerDialog(getActivity(),this,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setMaxDate((c.getTime()).getTime());
        dialog.setTitle("");
        return dialog;
    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

       callback.setTime(datePicker,i,i1,i2);
    }
}