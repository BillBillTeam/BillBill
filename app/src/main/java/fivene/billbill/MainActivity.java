package fivene.billbill;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import bhj.TagGroupProvider;
import bhj.TimePickerFragment;
import bhj.ViewPagerManagement;
import lhq.ie.Expense;
import lz.db.Bill;
import lz.img.IconGetter;
import lz.math.Calculator;
import lz.regex.NumCheck;

/**
 * 主界面
 */

public class MainActivity extends AppCompatActivity {

    private int currentPage=0;

    private int currentSelectedTag_pageN;

    private ViewPager mTagGroupPager = null;

    private ImageView[] mImageViews = null;

    private Calendar mSelectedDate;

    private  int globalHeight;
    private  int globalWidth;
    private Context mContext;

    private Button mTimeButton;

    private EditText remark_text;
    private EditText amount_text;
    private StringBuilder amountTextStringBuilder = new StringBuilder();

    private ScrollView mScrollView;
    private View currentSelectedTag;
    private LinearLayout mNumberKeyboard;
    private LinearLayout mFirstPart;
    private FrameLayout mTagGroupContainer;
    private LinearLayout mAmountLayout;
    private LinearLayout mMarkLayout;
    ViewPagerManagement viewPagerManagement=null;

    private AlertDialog finishDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);

        //获取屏幕信息（不可信）
        DisplayMetrics dm = getResources().getDisplayMetrics();
        globalWidth = dm.widthPixels;
        globalHeight = dm.heightPixels;
        //初始化变量
        init();
        //绑定xml
        initView();
        //初始化标签选择部分
        initViewPager();
        //添加Listener
        initListener();
        // 初始化完成提示框
        initFinishDialog();
        // 初始化快速入口按钮
        initFastButton();
        //添加空白&&添加主页面的上下滑动&&强制回到上半部分
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                remark_text.setFocusable(false);
                amount_text.setFocusable(false);
                adjustSpaceHeight();
                makePageScrollable();
                scrollToUP();

            }
        });

        disableAmountTextShowSoftInput();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    /**
     * 调整空白部分高度适配不同屏幕
     */
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
            case R.id.item_bill:
                Intent intentBill = new Intent(MainActivity.this,BillListActivity.class);
                MainActivity.this.startActivity(intentBill);
                return true;
            case R.id.item_stat:
                Intent intentStat = new Intent(MainActivity.this,StatisticsActivity.class);
                MainActivity.this.startActivity(intentStat);
                return true;
            case R.id.item_about:
                Intent intentAbout = new Intent(MainActivity.this,AboutActivity.class);
                MainActivity.this.startActivity(intentAbout);
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
        // mDefaultImage = (ImageView) findViewById(R.id.home_default_image);
        //标签组
        mTagGroupPager = (ViewPager) findViewById(R.id.tag_group_pager);
        //滑动条
        mScrollView =(ScrollView)findViewById(R.id.mainScrollView);
        //弹出时间选择的按钮
        mTimeButton=(Button)findViewById(R.id.time_button);
        //数字键盘
        mNumberKeyboard=(LinearLayout)findViewById(R.id.table_num);
        //显示钱数的
        amount_text=(EditText)findViewById(R.id.text_Amount) ;
        //显示用户输入的文本的
        remark_text=(EditText)findViewById(R.id.editText2);

        mAmountLayout=(LinearLayout)findViewById(R.id.amount_layout);
        mMarkLayout=(LinearLayout)findViewById(R.id.mark_layout);


        //主界面上半部分
        mFirstPart=(LinearLayout)findViewById(R.id.main_first_part);
        //标签组的外面一层
        mTagGroupContainer=(FrameLayout)findViewById(R.id.tag_group_container);
    }

    private void  initFastButton(){
        findViewById(R.id.btn_bill_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBill = new Intent(MainActivity.this,BillListActivity.class);
                MainActivity.this.startActivity(intentBill);
            }
        });

        findViewById(R.id.btn_bill_stat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentStat = new Intent(MainActivity.this,StatisticsActivity.class);
                MainActivity.this.startActivity(intentStat);
            }
        });

        findViewById(R.id.btn_tag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTag = new Intent(MainActivity.this,TagManageActivity.class);
                MainActivity.this.startActivity(intentTag);
            }
        });
        //findViewById(R.id.btn_next)
    }

    /**
     * 添加主界面的上下滑动响应事件
     */
    private void makePageScrollable(){

        //auto move on the two page
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        Log.i("billbill","up"+globalHeight);
                        Log.i("billbill","up"+mScrollView.getScrollY());
                        if(currentPage==0){
                            if(mScrollView.getScrollY()>mScrollView.getHeight()/8){
                                scrollToDOWN();
                            }
                            else{
                                scrollToUP();
                            }
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
    }

    /**
     * 为主界面添加listener
     */
    private void initListener(){
        //控制
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

        remark_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(v, InputMethod.SHOW_FORCED);
            }
        });

        remark_text.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    //获得用户输入的信息
                    Log.i("KeEvent",event.toString());
                    if(checkInput()){
                        insertNewRecord();
                        finishDialog.show();
                        hideSoftInput(v);
                    }
                    return true;
                }
                return false;
            }
        });

        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftInput(view);
                showTimePopFormBottom(view);
                amount_text.requestFocus();
            }
        });

        // 获得焦点时，强制隐藏当前的系统键盘
        amount_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    hideSoftInput(v);
                }
            }
        });

       addListenerToNumberKeyboard();
    }

    /**
     *  初始化结束询问对话框
     */
    private void initFinishDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("温馨提示");
        builder.setMessage("这笔账我已经记录下来啦，您是不是要再记一笔呢？");
        builder.setPositiveButton("再来一笔",new DialogInterface.OnClickListener() {//添加确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearStatus();
            }
        });
        builder.setNegativeButton("转到账单",new DialogInterface.OnClickListener() {//添加返回按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearStatus();
                Intent intent = new Intent(MainActivity.this,BillListActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                clearStatus();
            }
        });

        finishDialog = builder.create();
    }

    /**
     *  清除记账相关控件的内容，使其恢复初始状态
     *  以便以填写下一项记录
     */
    private void clearStatus(){
        removeCurrentSelect();
        currentSelectedTag = null;
        amountTextStringBuilder = new StringBuilder();
        amount_text.setText("");
        amount_text.setSelection(0);
        remark_text.setText("");
    }

    /**
     *  点击事件监听类
     *  检查输入并将填写的账单存入数据库
     */
    View.OnClickListener listenerWhenFinish = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //获得用户输入的信息
            if(checkInput()){
                boolean isSuccess = insertNewRecord();
                if(isSuccess){
                    finishDialog.show();
                }
            }
        }
    };

    /**
     *  隐藏在控件上的键盘
     * @param view 需要隐藏键盘的控件
     */
    private void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     *  通过界面上的数据，向数据库插入一条记录
     */
    private boolean insertNewRecord() {
        String exp = amountTextStringBuilder.toString();
        double amount;
        try{
            amount = Double.parseDouble(exp);
        }catch (Exception e){
            amount= Calculator.conversion(exp);
        }

        if(amount > 1000000){
            Toast.makeText(this,"单笔最大金额不超过100万，请重新输入",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(amount > 0){
            String remark=remark_text.getText().toString();
            String type=((TextView)currentSelectedTag.findViewById(R.id.tag_name)).getText().toString();
            Bill bill=new Bill(mSelectedDate,type,amount,remark);
            Log.i("billbill","new bill :"+bill.toString());
            Expense ex=new Expense(mContext);
            ex.Insert(bill);
            return true;
        }
        else {
            Toast.makeText(this,"输入的表达式不正确，请重新输入",Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    /**
     * 初始化标签选择部分
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

    /**
     * 刷新标签选择部分
     */
    private void refreshTagGroup(){

       viewPagerManagement.reloadIt(this);

    }


    /**
     * 弹出 日历底部弹窗
     * @param view view
     */
    public void showTimePopFormBottom(View view) {
        TimePickerFragment.Callback callback=new TimePickerFragment.Callback() {
            @Override
            public void setTime(DatePicker datePicker,int i0, int i1, int i2) {
                mSelectedDate=new GregorianCalendar(i0,i1,i2);
                String pat2 = "MM月dd日" ;
                SimpleDateFormat sdf1 = new SimpleDateFormat(pat2, Locale.CHINA) ; // 实例化模板对象

                mTimeButton.setText(sdf1.format(mSelectedDate.getTime()));

                Log.i("billbill",mSelectedDate.toString());
            }
        } ;
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setCallBack(callback);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    /**
     * 给标签组添加监听事件
     * @param layout layout
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
                            removeCurrentSelect();
                        }
                        currentSelectedTag=view;
                        //add selected change
                        int index=((GridLayout)(currentSelectedTag.getParent())).indexOfChild(currentSelectedTag);
                        int currentPage=mTagGroupPager.getCurrentItem();
                        currentSelectedTag_pageN=currentPage;

                        int finIndex=currentPage*TagGroupProvider.COLUMNCOUNT*TagGroupProvider.ROWCOUNT+index;
                        if(finIndex>=TagGroupProvider.getPicRecSize()){//自定义标签//jump

                            jumpToTagManageActivity();
                            currentSelectedTag=null;
                            return;

                        }
                        int picRecID=TagGroupProvider.getPicRecID(finIndex);
                        ((ImageView)layout1.getChildAt(0)).setImageBitmap(IconGetter.getClickedIcon(MainActivity.this,picRecID));

                        if(currentPage==0){
                            scrollToDOWN();
                        }
                        TextView textView=(TextView) layout1.getChildAt(1);
                        Log.i("标签选择", "onClick: "+textView.getText());
                    }
                    amount_text.requestFocus();
                }
            });
        }
    }

    /**
     * 清除标签选择区域的选中状态
     */
    private void removeCurrentSelect() {
        LinearLayout layout2 =(LinearLayout) currentSelectedTag;
        int index=((GridLayout)(currentSelectedTag.getParent())).indexOfChild(currentSelectedTag);
        int currentPage=currentSelectedTag_pageN;
        int finIndex=currentPage* TagGroupProvider.COLUMNCOUNT*TagGroupProvider.ROWCOUNT+index;

        int picRecID=TagGroupProvider.getPicRecID(finIndex);

        ((ImageView)layout2.getChildAt(0)).setImageBitmap(IconGetter.getIcon(MainActivity.this,picRecID));
    }

    /**
     * 跳转到TagManageActivity
     */
    private void jumpToTagManageActivity(){
        Intent intent = new Intent(MainActivity.this,TagManageActivity.class);
        MainActivity.this.startActivityForResult(intent,0);
    }



    /**
     * 小键盘的事件响应
     */
    private void addListenerToNumberKeyboard(){
        int[] keys={R.id.btn_0,R.id.btn_1,R.id.btn_2,
                R.id.btn_3,R.id.btn_4,R.id.btn_5,
                R.id.btn_6,R.id.btn_7,R.id.btn_8,
                R.id.btn_9,};
        for(int i=0;i<keys.length;i++) {
            View view = mNumberKeyboard.findViewById(keys[i]);
            view.setTag(i);
            view.setOnClickListener(NumberClickListener);
        }
        mNumberKeyboard.findViewById(R.id.btn_t).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amountTextStringBuilder.length()<20){
                    if(NumCheck.matchDouble(amountTextStringBuilder+".")){
                        amountTextStringBuilder.append(".");
                        amount_text.setText(amountTextStringBuilder);
                        amount_text.setSelection(amountTextStringBuilder.length());
                    }
                }
            }
        });
        mNumberKeyboard.findViewById(R.id.btn_c).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amountTextStringBuilder.length()>0){
                    amountTextStringBuilder.deleteCharAt(amountTextStringBuilder.length()-1);
                    amount_text.setText(amountTextStringBuilder);
                    amount_text.setSelection(amountTextStringBuilder.length());
                }
            }
        });

        mNumberKeyboard.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remark_text.setFocusable(true);
                remark_text.setFocusableInTouchMode(true);
                remark_text.requestFocus();
            }
        });

        mNumberKeyboard.findViewById(R.id.btn_ok).setOnClickListener(listenerWhenFinish);

        mNumberKeyboard.findViewById(R.id.btn_add).setTag("+");
        mNumberKeyboard.findViewById(R.id.btn_add).setOnClickListener(NumberClickListener);
        mNumberKeyboard.findViewById(R.id.btn_sub).setTag("-");
        mNumberKeyboard.findViewById(R.id.btn_sub).setOnClickListener(NumberClickListener);


    }

    private View.OnClickListener NumberClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(amountTextStringBuilder.length()<20){
                amountTextStringBuilder.append(v.getTag());
                amount_text.setText(amountTextStringBuilder);
                amount_text.setSelection(amountTextStringBuilder.length());
            }
        }
    };

    /**
     * 检查用户输入
     */
    private boolean checkInput(){
        if(currentSelectedTag==null){
            Toast.makeText(this,"请先选择账单类型",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(amount_text.getText().toString().length() == 0){
            Toast.makeText(this,"请输入金额",Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * 滑动到顶部
     */
    private void scrollToUP(){
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
        currentPage=0;
    }

    /**
     * 滑动到底部
     */
    private void scrollToDOWN(){
        amount_text.setFocusable(true);
        amount_text.setFocusableInTouchMode(true);
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        currentPage=1;
    }
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        switch ( resultCode ) {
            case RESULT_OK :
                refreshTagGroup();
                currentSelectedTag=null;
                break;
            default :
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(currentPage==1){
            scrollToUP();
            return;
        }
        super.onBackPressed();
    }

    /**
     *  限制系统键盘弹出
     */
    private void disableAmountTextShowSoftInput() {
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(amount_text, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(amount_text, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}