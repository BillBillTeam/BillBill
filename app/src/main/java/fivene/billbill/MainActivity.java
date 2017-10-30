package fivene.billbill;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;

import bhj.TagGroupProvider;
import bhj.TimePopWindow;

public class MainActivity extends AppCompatActivity {

   // private ImageView mDefaultImage = null;

    private ViewPager mTagGroupPager = null;

    private ImageView[] mImageViews = null;

    private AtomicInteger what = new AtomicInteger(0);

    private boolean isContinue = true;

    private  int globalHeight;
    private  int globalWidth;
    private Context mContext;
    private Button mButton_ok;
    private ImageView mImageView;

    private Button mButton_pop_time;
    private ScrollView mScrollView;
    private View currentSelectedTag;

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

        DisplayMetrics dm = getResources().getDisplayMetrics();
        globalWidth = dm.widthPixels;
        globalHeight = dm.heightPixels;
        init();
        initView();
        initViewPager();
        initev();
    }

    /**
     * 初始化数据
     */
    private void init() {
        mContext = this;

    }
    private void initev(){

        //auto move on the two page
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                           switch (motionEvent.getAction()){
                                case MotionEvent.ACTION_UP:
                                    System.out.print("up");
                                    System.out.print(mScrollView.getScrollY());
                                    if(mScrollView.getScrollY()>globalHeight/4){

                                        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                    else{
                                        mScrollView.fullScroll(ScrollView.FOCUS_UP);

                                    }
                                        break;
                               }
                return false;
            }

        });
//
//        mScrollView.fullScroll(ScrollView.FOCUS_UP);
//        mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        mButton_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //just for test
                Intent intent = new Intent(MainActivity.this,StatisticsActivity.class);
                MainActivity.this.startActivity(intent);

            }
        });

    }


    /**
     * 初始化控件
     */
    private void initView() {
        // TODO Auto-generated method stub
       // mDefaultImage = (ImageView) findViewById(R.id.home_default_image);
        mTagGroupPager = (ViewPager) findViewById(R.id.tag_group_pager);
        mButton_pop_time=(Button) findViewById(R.id.button);
        mScrollView =(ScrollView)findViewById(R.id.mainScrollView);
        mButton_ok=(Button)findViewById(R.id.btn_ok);

    }

    /**
     * void
     */
    private void initViewPager() {
        // TODO Auto-generated method stub
      //  mDefaultImage.setVisibility(View.GONE);
        mTagGroupPager.setVisibility(View.VISIBLE);


        //create TagGroupProvider
        List <String>testList=new ArrayList<String>();
        final TagGroupProvider provider=new TagGroupProvider(this,testList,globalWidth);

        // group是R.layou.mainview中的负责包裹小圆点的LinearLayout.

        ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
        mImageViews = new ImageView[provider.getGroupSize()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(13, 13);
        layoutParams.setMargins(5, 0, 5, 1);

        for (int i = 0; i < provider.getGroupSize(); i++) {
            mImageView = new ImageView(this);
            mImageView.setLayoutParams(layoutParams);
            mImageViews[i] = mImageView;
            if (i == 0) {
                // 默认选中第一张图片
                mImageViews[i].setBackgroundResource(R.drawable.circle_3);
            }
            else {
                mImageViews[i].setBackgroundResource(R.drawable.circle_1);
            }
            group.addView(mImageViews[i]);
        }
        // add listener for tags
        GridLayout layout =(GridLayout)provider.getTagGroup().get(0);
        addListenerToGridLayout(layout);

        mTagGroupPager.setAdapter(new TagGroupAdapter(provider.getTagGroup()));

        mButton_pop_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePopFormBottom(view);
            }
        });
        mTagGroupPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                for(int i=0;i<mImageViews.length;i++){
                    if (i == position) {
                        // 默认选中第一张图片
                        mImageViews[i].setBackgroundResource(R.drawable.circle_3);
                    }
                    else {
                        mImageViews[i].setBackgroundResource(R.drawable.circle_1);
                    }


                }


            }

            @Override
            public void onPageSelected(int position) {

                GridLayout layout =(GridLayout)provider.getTagGroup().get(position);
                addListenerToGridLayout(layout);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }




    private final class TagGroupAdapter extends PagerAdapter {
        private List<View> views = null;
        public TagGroupAdapter(List<View> views) {
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

        //TODO: add listener at here
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position), 0);

            return views.get(position);
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
        PopWin.setHeight((int) (globalHeight/2*1.20));
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
    private void addListenerToGridLayout(final GridLayout layout){
        for(int i=0;i<layout.getChildCount();i++){
            layout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(currentSelectedTag!=view){
                        LinearLayout layout1=(LinearLayout)view;
                        if(currentSelectedTag!=null){
                            //remove selected change
                            LinearLayout layout2 =(LinearLayout) currentSelectedTag;
                            Drawable drawable=getResources().getDrawable(R.drawable.none_drawable);
                            layout2.setBackground(drawable);


                        }
                        currentSelectedTag=view;
                        //add selected change
                        Drawable drawable=getResources().getDrawable(R.drawable.selected_tag_background);
                        layout1.setBackground(drawable);

                        TextView textView=(TextView) layout1.getChildAt(1);
                        System.out.print(textView.getText());



                    }

                }
            });
        }

    }

}