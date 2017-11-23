package bhj;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fivene.billbill.R;

/**
 * Created by ubuntu on 17-10-20.
 */

import com.appeaser.sublimepickerlibrary.SublimePicker;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeListenerAdapter;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 一个从底部弹出的时间选择窗口
 */
public class TimePopWindow extends PopupWindow {


   // private Context mContext;

    private View view;

  //  private TextView btn_cancel;
    private LinearLayout popupLayout;

    // Date & Time formatter used for formatting
    // text on the switcher button
    private DateFormat mDateFormatter, mTimeFormatter;

    // Picker
    private SublimePicker mSublimePicker;

    // Callback to activity
    private Callback mCallback;

    private SublimeListenerAdapter mListener = new SublimeListenerAdapter() {
        @Override
        public void onCancelled() {
            if (mCallback!= null) {
                mCallback.onCancelled();
            }

            // Should actually be called by activity inside `Callback.onCancelled()`
            dismiss();
        }

        /**
         *
         * @param sublimeMaterialPicker SublimeMaterialPicker view
         * @param selectedDate          The date that was set.
         * @param hourOfDay             The hour of day that was set.
         * @param minute                The minute that was set.
         * @param recurrenceOption      One of the options defined in
         *                              SublimeRecurrencePicker.RecurrenceOption.
         *                              'recurrenceRule' will only be passed if
         *                              'recurrenceOption' is 'CUSTOM'.
         * @param recurrenceRule        The recurrence rule that was set. This will
         *                              be 'null' if 'recurrenceOption' is anything
         */
        @Override
        public void onDateTimeRecurrenceSet(SublimePicker sublimeMaterialPicker,
                                            SelectedDate selectedDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {
            if (mCallback != null) {
                mCallback.onDateTimeRecurrenceSet(selectedDate,
                        hourOfDay, minute, recurrenceOption, recurrenceRule);
            }

            dismiss();
        }
    };

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public TimePopWindow(Context mContext, Bundle arguments) {
        // Initialize formatters
        mDateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        mTimeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        mTimeFormatter.setTimeZone(TimeZone.getTimeZone("GMT+0"));



        this.view = LayoutInflater.from(mContext).inflate(R.layout.pop_up_time, null);
        popupLayout =(LinearLayout)view.findViewById(R.id.pop_layout);
        //添加日历
        System.out.print("creating pop view...");

        popupLayout.addView( CreateView(LayoutInflater.from(mContext),popupLayout,arguments));

            // 设置外部可点击
        this.setOutsideTouchable(true);
            // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
            this.view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {

                    int height = view.findViewById(R.id.pop_layout).getTop();

                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height) {
                            dismiss();
                        }
                    }
                    return true;
                }
            });


    /* 设置弹出窗口特征 */
            // 设置视图
            this.setContentView(this.view);
            // 设置弹出窗体的宽和高
            this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
            this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

            // 设置弹出窗体可点击
            this.setFocusable(true);

            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置弹出窗体的背景
            this.setBackgroundDrawable(dw);

            // 设置弹出窗体显示时的动画，从底部向上弹出
            //this.setAnimationStyle(R.style.pop_window_anim);

    }

    /**
     * 设置日期选择控件的属性
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return 控件
     */
    public View CreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        mSublimePicker = (SublimePicker)inflater.inflate(R.layout.sublime_picker, null);

        // Retrieve SublimeOptions
        Bundle arguments = savedInstanceState;
        SublimeOptions options = null;

        // Options can be null, in which case, default
        // options are used.
        if (arguments != null) {
            options = arguments.getParcelable("SUBLIME_OPTIONS");
        }

        mSublimePicker.initializePicker(options, mListener);

        return mSublimePicker;
    }



    public interface Callback {
        /**
         * 取消选择的回调函数
         */
        void onCancelled();

        /**
         * 用于输出结果的回调函数
         * 只取需要的数据
         * @param selectedDate 选中的时间
         * @param hourOfDay 小时
         * @param minute 分
         * @param recurrenceOption
         * @param recurrenceRule
         */
        void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                     int hourOfDay, int minute,
                                     SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                     String recurrenceRule);
    }
}
