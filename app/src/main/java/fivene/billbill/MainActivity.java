package fivene.billbill;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Space;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearSmoothScroller;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import bhj.TagGroupProvider;
import bhj.TimePopWindow;
import bhj.ViewPagerManagement;
import lhq.ie.Expense;
import lhq.ie.ExpenseType;
import lz.db.Bill;
import lz.db.CustomType;
import lz.img.IconGetter;
import lz.regex.NumCheck;

public class MainActivity extends AppCompatActivity {

   // private ImageView mDefaultImage = null;

    private int currentPage=0;

    private int currentSelectedTag_pageN;

    private ViewPager mTagGroupPager = null;

    private ImageView[] mImageViews = null;

    private AtomicInteger what = new AtomicInteger(0);

    private boolean isContinue = true;
    private Calendar mSelectedDate;

    private  int globalHeight;
    private  int globalWidth;
    private Context mContext;
    private Button mButton_ok;
    private Button mTimeButton;
    private Button mbt_jump;
    private Button mbt_jump2;


    private EditText remark_text;
    private TextView amount_text;

    private Button mButton2;
    private Toolbar mToolbar;

    private ScrollView mScrollView;
    private View currentSelectedTag;
    private LinearLayout mNumberKeyboard;
    private LinearLayout mFirstPart;
    private FrameLayout mTagGroupContainer;
    private LinearLayout mAmountLayout;
    private LinearLayout mMarkLayout;

    private TagGroupProvider provider;
    ViewPagerManagement viewPagerManagement=null;
    //tag
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(this.getString(R.string.mainTitle));
        }
        DisplayMetrics dm = getResources().getDisplayMetrics();
        globalWidth = dm.widthPixels;
        globalHeight = dm.heightPixels;
        init();
        initView();
        initViewPager();
        initev();



        mButton2.setClickable(false);
//添加空白&&添加主页面的上下滑动&&强制回到上半部分
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                remark_text.setFocusable(false);
                adjustSpaceHeight();
                makePageScrollable();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {

        super.onResume();
    }


    private void adjustSpaceHeight(){
        Space s1=(Space) findViewById(R.id.space1);
        Space s2=(Space) findViewById(R.id.space2);

        int i_s1=mScrollView.getHeight()-mFirstPart.getHeight()-mTagGroupContainer.getHeight();
        int i_s2=mScrollView.getHeight()-mTagGroupContainer.getHeight()-mAmountLayout.getHeight()-mMarkLayout.getHeight()-mNumberKeyboard.getHeight();
        Log.i("billbill", "adjustSpaceHeight: "+mScrollView.getHeight());
        if(i_s1<0){
            i_s1=0;
        }
        if(i_s2<0){
            i_s2=0;
        }
        s1.setMinimumHeight(i_s1);
        s2.setMinimumHeight(i_s2);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_setting:
                Intent intentSetting = new Intent(MainActivity.this,SettingsActivity.class);
                MainActivity.this.startActivity(intentSetting);
                return true;
            case R.id.item_about:
                Intent intentSearch = new Intent(MainActivity.this,AboutActivity.class);
                MainActivity.this.startActivity(intentSearch);
                return true;
            default:
                return true;
        }

    }
    /**
     * 初始化数据
     */
    private void init() {
        mContext = this;
        mSelectedDate=Calendar.getInstance();
        mSelectedDate.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
    }


    /**
     * 初始化控件
     */
    private void initView() {
        // TODO Auto-generated method stub
        // mDefaultImage = (ImageView) findViewById(R.id.home_default_image);
        mTagGroupPager = (ViewPager) findViewById(R.id.tag_group_pager);

        mScrollView =(ScrollView)findViewById(R.id.mainScrollView);
        mButton_ok=(Button)findViewById(R.id.btn_ok);
        mTimeButton=(Button)findViewById(R.id.time_button);
        mButton2=(Button)findViewById(R.id.Button2);
        mbt_jump=(Button)findViewById(R.id.button_jump);
        mbt_jump2=(Button)findViewById(R.id.button_jump2);
        mNumberKeyboard=(LinearLayout)findViewById(R.id.table_num);
        amount_text=(TextView)findViewById(R.id.text_Amount) ;
        remark_text=(EditText)findViewById(R.id.editText2);
        //test
        mFirstPart=(LinearLayout)findViewById(R.id.main_first_part);

        mTagGroupContainer=(FrameLayout)findViewById(R.id.tag_group_container);
        mAmountLayout=(LinearLayout)findViewById(R.id.amount_layout);
        mMarkLayout=(LinearLayout)findViewById(R.id.mark_layout);

    }


    private void makePageScrollable(){

        //auto move on the two page
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.i("billbill","up"+globalHeight);
                        Log.i("billbill","up"+mScrollView.getScrollY());
                        if(currentPage==0)
                            if(mScrollView.getScrollY()>mScrollView.getHeight()/8){
                                scrollToDOWN();
                            }
                            else{
                                scrollToUP();
                            }
                        else{
                            if(mScrollView.getScrollY()<mScrollView.getHeight()/8*7-mTagGroupContainer.getHeight()){
                                scrollToUP();
                            }
                            else{
                                scrollToDOWN();
                            }

                        }
                        break;
                }
                return false;
            }

        });
//

    }
    private void initev(){

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
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO:用户没有输入信息导致的error
                //获得用户输入的信息
                double amount=Double.valueOf( amount_text.getText().toString()).doubleValue();
                String remark=remark_text.getText().toString();
                String type=((TextView)currentSelectedTag.findViewById(R.id.tag_name)).getText().toString();
                Bill bill=new Bill(mSelectedDate,type,amount,remark);
                Log.i("billbill","new bill :"+bill.toString());
                Expense ex=new Expense(mContext);
                ex.Insert(bill);
                new AlertDialog.Builder(MainActivity.this).setTitle("温馨提示")//设置对话框标题
                        .setMessage("这笔账我已经记录下来啦，您是不是要再记一笔呢？")//设置显示的内容
                        .setPositiveButton("再来一笔",new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                            }
                        }).setNegativeButton("返回账单",new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        // TODO Auto-generated method stub
                    }

                }).show();//在按键响应事件中显示此对话框
            }
        });


        mbt_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,BillListActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        mbt_jump2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,TagManageActivity.class);
                MainActivity.this.startActivity(intent);
//                refreshTagGroup();
            }
        });

        remark_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!remark_text.isFocusable()) {
                    v.setFocusable(true);
                    v.setFocusableInTouchMode(true);
                    v.requestFocus();
                    InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(v, InputMethod.SHOW_FORCED);
                }

            }
        });
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePopFormBottom(view);
            }
        });
       addListenerToNumberKeyboard();
    }


    /**
     * void
     */
    private void initViewPager() {
        ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
        ViewPagerManagement.Callback callback=new ViewPagerManagement.Callback() {
            @Override
            public void callbackAddListenerToViewPager(GridLayout layout) {
                addListenerToGridLayout(layout);
            }
        };

         viewPagerManagement=new ViewPagerManagement(mTagGroupPager,globalWidth,callback,group,mImageViews);
        viewPagerManagement.createViewPager(this);

    }


    private void refreshTagGroup(){

       viewPagerManagement.reloadIt(this);

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

        // If 'displayOptions' is zero, the chosen options are not valid
        return new Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }

    /**
     * 给标签组添加监听事件
     * @param layout
     */
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
                            int index=((GridLayout)(currentSelectedTag.getParent())).indexOfChild(currentSelectedTag);
                            int currentPage=currentSelectedTag_pageN;
                            int finIndex=currentPage*TagGroupProvider.COLUMNCOUNT*TagGroupProvider.ROWCOUNT+index;

                            int picRecID=TagGroupProvider.getPicRecID(finIndex);

                            ((ImageView)layout2.getChildAt(0)).setImageBitmap(IconGetter.getIcon(MainActivity.this,picRecID));


                        }
                        currentSelectedTag=view;
                        //add selected change
                        int index=((GridLayout)(currentSelectedTag.getParent())).indexOfChild(currentSelectedTag);
                        int currentPage=mTagGroupPager.getCurrentItem();
                        currentSelectedTag_pageN=currentPage;
                        int finIndex=currentPage*TagGroupProvider.COLUMNCOUNT*TagGroupProvider.ROWCOUNT+index;
                        int picRecID=TagGroupProvider.getPicRecID(finIndex);
                        ((ImageView)layout1.getChildAt(0)).setImageBitmap(IconGetter.getClickedIcon(MainActivity.this,picRecID));

                        if(currentPage==0){
                            scrollToDOWN();
                        }
                        TextView textView=(TextView) layout1.getChildAt(1);
                        System.out.print(textView.getText());

                        checkInput();

                    }

                }
            });
        }

    }

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

            if(selectedDate.getFirstDate().getTime()!=Calendar.getInstance().getTime()){//no use need to update

                mSelectedDate = selectedDate.getFirstDate();
                mTimeButton.setText(DateFormat.getDateInstance().format(mSelectedDate.getTime()));

                Log.i("billbill",selectedDate.toString());

            }
            else{
                mTimeButton.setText("今天");
            }

        }
    };

    private void addListenerToNumberKeyboard(){
        int[] keys={R.id.btn_0,R.id.btn_1,R.id.btn_2,
                R.id.btn_3,R.id.btn_4,R.id.btn_5,
                R.id.btn_6,R.id.btn_7,R.id.btn_8,
                R.id.btn_9,};
        final NumCheck check=new NumCheck();
        for(int i=0;i<keys.length;i++) {
            final int k=i;
            mNumberKeyboard.findViewById(keys[i]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s=amount_text.getText().toString();
                    if(s.length()<20)
                        amount_text.setText(s+k);
                    checkInput();
                }
            });
        }
        mNumberKeyboard.findViewById(R.id.btn_t).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s=amount_text.getText().toString();
                if(s.length()<20)
                    if(check.matchDouble(s+"."))
                        amount_text.setText(s+".");
                checkInput();

            }
        });
        mNumberKeyboard.findViewById(R.id.btn_c).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amount_text.getText().length()>0)
                amount_text.setText(amount_text.getText().toString().subSequence(0,amount_text.getText().toString().length()-1));

            }
        });

    }
    private void checkInput(){
        if(currentSelectedTag!=null&&amount_text.getText().toString().length()>0){
            mButton2.setClickable(true);
        }
        else{
            mButton2.setClickable(false);
        }

    }

    private void scrollToUP(){
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
        currentPage=0;
    }
    private void scrollToDOWN(){
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        currentPage=1;
    }
}