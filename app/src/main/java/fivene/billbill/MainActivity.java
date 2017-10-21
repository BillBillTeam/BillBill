package fivene.billbill;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.renderscript.Sampler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;

import bhj.TimePopWindow;

public class MainActivity extends AppCompatActivity {

    private ImageView mDefaultImage = null;

    private ViewPager mImagePager = null;

    private ImageView[] mImageViews = null;

    private AtomicInteger what = new AtomicInteger(0);

    private boolean isContinue = true;

    private List<View> advPics;

    private Context mContext;

    private ImageView mImageView;

    private Button mButton_pop_time;

    TimePopWindow.Callback mFragmentCallback = new TimePopWindow.Callback() {
        @Override
        public void onCancelled() {
           // rlDateTimeRecurrenceInfo.setVisibility(View.GONE);
        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {
            SelectedDate mSelectedDate;
            mSelectedDate = selectedDate;
            System.out.print(selectedDate.toString());
//            mHour = hourOfDay;
//            mMinute = minute;
//            mRecurrenceOption = recurrenceOption != null ?
//                    recurrenceOption.name() : "n/a";
//            mRecurrenceRule = recurrenceRule != null ?
//                    recurrenceRule : "n/a";
//
//            updateInfoView();
//
//            svMainContainer.post(new Runnable() {
//                @Override
//                public void run() {
//                    svMainContainer.scrollTo(svMainContainer.getScrollX(),
//                            cbAllowDateRangeSelection.getBottom());
//                }
//            });
        }
    };



    //tag
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initView();
        initViewPager();
    }

    /**
     * 初始化数据
     */
    private void init() {
        mContext = this;
        advPics = new ArrayList<View>();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        // TODO Auto-generated method stub
        mDefaultImage = (ImageView) findViewById(R.id.home_default_image);
        mImagePager = (ViewPager) findViewById(R.id.adv_pager);
        mButton_pop_time=(Button) findViewById(R.id.button);
    }

    /**
     * void
     */
    private void initViewPager() {
        // TODO Auto-generated method stub
        mDefaultImage.setVisibility(View.GONE);
        mImagePager.setVisibility(View.VISIBLE);

        //修改循环tag
        ImageView img1 = new ImageView(mContext);
        img1.setBackgroundResource(R.drawable.test);
        advPics.add(img1);

        ImageView img2 = new ImageView(mContext);
        img2.setBackgroundResource(R.drawable.test);
        advPics.add(img2);

        ImageView img3 = new ImageView(mContext);
        img3.setBackgroundResource(R.drawable.test);
        advPics.add(img3);

        ImageView img4 = new ImageView(mContext);
        img4.setBackgroundResource(R.drawable.test);
        advPics.add(img4);

        // group是R.layou.mainview中的负责包裹小圆点的LinearLayout.
        ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
        mImageViews = new ImageView[advPics.size()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(13, 13);
        layoutParams.setMargins(5, 0, 5, 1);

        for (int i = 0; i < advPics.size(); i++) {
            mImageView = new ImageView(this);
            mImageView.setLayoutParams(layoutParams);
            mImageViews[i] = mImageView;
            if (i == 0) {
                // 默认选中第一张图片
                mImageViews[i].setBackgroundResource(R.drawable.test);
            }
            else {
                mImageViews[i].setBackgroundResource(R.drawable.test);
            }
            group.addView(mImageViews[i]);
        }

        mImagePager.setAdapter(new AdvAdapter(advPics));

        mButton_pop_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePopFormBottom(view);
            }
        });

//        mImagePager.setOnPageChangeListener(new GuidePageChangeListener());
//        // 按下时不继续定时滑动,弹起时继续定时滑动
//        mImagePager.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                    case MotionEvent.ACTION_MOVE:
//                        isContinue = false;
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        isContinue = true;
//                        break;
//                    default:
//                        isContinue = true;
//                        break;
//                }
//                return false;
//            }
//        });
        // 定时滑动线程
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                while (true) {
//                    if (isContinue) {
//                        viewHandler.sendEmptyMessage(what.get());
//                        whatOption();
//                    }
//                }
//            }
//
//        }).start();
    }

//    /**
//     * 操作圆点轮换变背景
//     */
//    private void whatOption() {
//        what.incrementAndGet();
//        if (what.get() > mImageViews.length - 1) {
//            what.getAndAdd(-mImageViews.length);
//        }
//        try {
//            if (what.get() == 1) {
//                Thread.sleep(3000);
//            }
//            else {
//                Thread.sleep(2000);
//            }
//        }
//        catch (InterruptedException e) {
//        }
//    }
//
//    /**
//     * 处理定时切换广告栏图片的句柄
//     */
//    @SuppressLint("HandlerLeak")
//    private final Handler viewHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            mImagePager.setCurrentItem(msg.what);
//            super.handleMessage(msg);
//        }
//    };
//
//    /** 指引页面改监听器 */
//    private final class GuidePageChangeListener implements OnPageChangeListener {
//
//        @Override
//        public void onPageScrollStateChanged(int arg0) {
//
//        }
//
//        @Override
//        public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//        }
//
//
//
//
//        //tag
//        @Override
//        public void onPageSelected(int arg0) {
//            arg0 = arg0 % advPics.size();
//            for (int i = 0; i < mImageViews.length; i++) {
//                mImageViews[arg0].setBackgroundResource(R.drawable.test);
//                if (arg0 != i) {
//                    mImageViews[i].setBackgroundResource(R.drawable.test);
//                }
//            }
//            what.set(arg0);
//        }
//    }

    /**
     * @Description: 广告栏PaperView 图片适配器
     */
    private final class AdvAdapter extends PagerAdapter {
        private List<View> views = null;

        public AdvAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {

        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(views.get(arg1), 0);
            return views.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }
    }

    /**
     * 弹出 日历底部弹窗
     * @param view
     */
    public void showTimePopFormBottom(View view) {


        Pair<Boolean, SublimeOptions> optionsPair = getOptions();
        Bundle bundle = new Bundle();
        bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
        TimePopWindow PopWin = new TimePopWindow(this, bundle);
        PopWin.setCallback(mFragmentCallback);
        // Options


//        if (!optionsPair.first) { // If options are not valid
//            Toast.makeText(Sampler.this, "No pickers activated",
//                    Toast.LENGTH_SHORT).show();
//            return;
//        }




//        设置Popupwindow显示位置（从底部弹出）
        PopWin.showAtLocation(findViewById(R.id.main_view), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha=0.7f;
        getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        PopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha=1f;
                getWindow().setAttributes(params);
            }
        });

//        takePhotoPopWin.lis
    }

    /**
     * 设置日历的显示参数
     * @return
     */
    Pair<Boolean, SublimeOptions> getOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;
        displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;
        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
        options.setDisplayOptions(displayOptions);
        // Enable/disable the date range selection feature
        options.setCanPickDateRange(false);

        // Example for setting date range:
        // Note that you can pass a date range as the initial date params
        // even if you have date-range selection disabled. In this case,
        // the user WILL be able to change date-range using the header
        // TextViews, but not using long-press.

        /*Calendar startCal = Calendar.getInstance();
        startCal.set(2016, 2, 4);
        Calendar endCal = Calendar.getInstance();
        endCal.set(2016, 2, 17);

        options.setDateParams(startCal, endCal);*/

        // If 'displayOptions' is zero, the chosen options are not valid
        return new Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }

}