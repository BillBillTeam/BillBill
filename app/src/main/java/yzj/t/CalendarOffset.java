package yzj.t;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * 该类是负责时间的偏移的类
 */
public class CalendarOffset {

    Date d=new Date();
    SimpleDateFormat mDateFormat =new SimpleDateFormat("yyyy-MM-dd");
    GregorianCalendar gc =new GregorianCalendar();

    /**
     * 如果没有调用该函数则返回当前时间
     * @param date  格式如 1999-05-12
     * @return
     * @throws ParseException
     */
    public Date setD(String date) throws ParseException{
        d = mDateFormat.parse(date); // 指定日期
        return d;
    }



    /**
     *
     * @param d    初始化好的日期
     * @param day  需要偏移的时间变量
     * @return
     * @throws ParseException
     */

    public static Date addDate(Date d,long day) throws ParseException {
        long time = d.getTime(); // 得到指定日期的毫秒数
        day = day*24*60*60*1000; // 要加上的天数转换成毫秒数
        time+=day; // 相加得到新的毫秒数
        return new Date(time); // 将毫秒数转换成日期
    }

    /**
     *
     * @return  返回一年前的日期
     */
    public String getYears(int n)
    {
        gc.setTime(d);
        gc.add(Calendar.YEAR,-n);
        gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
        return mDateFormat.format(gc.getTime());
    }
    /**
     *
     * @return  返回当前日期n个半年前的日期
     */
    public String getHalfYear(int n)
    {
        gc.setTime(d);
        gc.add(Calendar.MONTH,-6*n);
        gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
        return mDateFormat.format(gc.getTime());
    }
    /**
     *
     * @return  返回当前日期n个季度前的日期
     */
    public String getQuarters(int n)
    {
        gc.setTime(d);
        gc.add(Calendar.MONTH,-n);
        gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
        return mDateFormat.format(gc.getTime());
    }
    /**
     *
     * @return  返回当前日期
     */
    public String getLocalDate()
    {
        return mDateFormat.format(d);
    }

    /**
     *
     * @return  返回选定日期的n个星期前的日期
     */

    public String getWeek(int n)
    {
        gc.setTime(d);
        gc.add(Calendar.DATE,-7*n);
        gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
        return mDateFormat.format(gc.getTime());
    }
    /**
     *
     * @return  返回当前日期num天后的日期
     */
    public String getDay(int num)
    {
        gc.setTime(d);
        gc.add(Calendar.DATE,num);
        gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),gc.get(Calendar.DATE));
        return mDateFormat.format(gc.getTime());
    }

    /**
     *
     * @return  当前月份初始的第一天
     */
    public String getMonthFirstDay()
    {
        gc.set(gc.get(Calendar.YEAR),gc.get(Calendar.MONTH),1);
        return  mDateFormat.format(gc.getTime());
    }

}
/**
 * 用来实现时间偏移的
 * Created by ~ on 2017/10/28.
 */
/**
 public class CalendarOffset {
 private int year;
 private int month;
 private int day;
 private int offsetDay;
 /**
 *
 * @param year    选定时间的年份
 * @param month   选定时间的月份
 * @param day     选定时间的日期
this.day = day;
}

/**
 *
 * @param year
 * @return true代表该年是闰年，false代表该年不是闰年

static boolean leapYear(int year){
if(year % 400 ==0||year % 4 == 0 && year %100 !=0)
return true;
else
return false;
}


/**
 *
 * @param month     表示输入的月份
 * @param year      表示所要输入的年份
 * @param day       表示所要输入的该月的哪一号
 * @return          这一天相对于1970年一月一日相隔的天数



public int sumMonth(int year,int month) {
//先对输入的年月日进行判断
int lastDay;
this.year = year;
this.month = month;
switch(month) {
case 1:
case 3:
case 5:
case 7:
case 8:
case 10:
case 12:
lastDay = 31;
break;
case 4:
case 6:
case 9:
case 11:
lastDay = 30;
break;
case 2:
if (leapYear(year)) {
lastDay = 29;
} else {
lastDay = 28;
}
break;
default:
//采用-1来返回错误的月份和日期输入
lastDay = -1;
}
return lastDay;
}
public OffsetTime offset(int a){
//默认值
OffsetTime off = new OffsetTime();
int m;
if(a>0){
if(a<sumMonth(year,month)-day){
off.setDay(day+offsetDay);
off.setMonth(month);
off.setYear(year);
}
else if(a>sumMonth(year,month)-day) {
int sum = sumMonth(year, month) - day;
for (int i = month + 1; i <= 12; i++) {
sum += sumMonth(year, i);
}
if (a < sum) {
for (int j = month + 1; j <= 12; j+k,k+) {
sum -= sumMonth(year, j);
off.setMonth(j);
if (a < 0) {
sum += sumMonth(year, j);
j--;
off.setMonth(j);
break;
}z

}
off.setDay(sum);
}
off.setYear(year);

}
else{
while(a>0){
if(leapYear(++year)){
a -= 366;
if(a<0)
a = a+366;
}
else{
a -= 365;
if(a<0)
a = a+365;
}

}
for(int i=1;i<=12;i++) {

a -=sumMonth(year,i);
if(a<0) {
a += sumMonth(year,i); break;
}
}

}
}
else if(offsetDay<0){

}
else{

}
return 0;
}

}


Date d=new Date();
SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
System.out.println("今天的日期："+df.format(d));
System.out.println("两天前的日期：" + df.format(new Date(d.getTime() - 2 * 24 * 60 * 60 * 1000)));
System.out.println("三天后的日期：" + df.format(new Date(d.getTime() + 3 * 24 * 60 * 60 * 1000)));
===============================================================================
GregorianCalendar gc=new GregorianCalendar();
gc.setTime(new Date);
gc.add(field,value);
value为正则往后,为负则往前
field取1加1年,取2加半年,取3加一季度,取4加一周
取5加一天....

/*
 *java中对日期的加减操作
 *gc.add(1,-1)表示年份减一.
 *gc.add(2,-1)表示月份减一.
 *gc.add(3.-1)表示周减一.
 *gc.add(5,-1)表示天减一.
 *以此类推应该可以精确的毫秒吧.没有再试.大家可以试试.
 *GregorianCalendar类的add(int field,int amount)方法表示年月日加减.
 *field参数表示年,月.日等.
 *amount参数表示要加减的数量.
 *
 * UseDate.java 测试如下:
 */




