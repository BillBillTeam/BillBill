package bhj;



import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.telecom.Call;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by mary kt on 17-11-26.
 */
public class TimePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private Callback callback;
    public interface Callback{

        void setTime(DatePicker datePicker,int i0,int i1,int i2);
    }
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