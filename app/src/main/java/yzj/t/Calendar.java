package yzj.t;

import android.content.Context;
import android.content.Context;
import java.util.ArrayList;
import lz.db.Bill;
import lz.db.DBHelper;
import lz.db.IDBill;
import java.util.*;

/**
 * 有关万年历的类型，注意这里的开始年份是1970年
 * Created by ~ on 2017/10/27.
 */

 class CalendarDemo {
    DBHelper mDBHelper;
    private int year;
    private int month;
    private int day;
    private  static int someday;
    //1970表示起始年份
    static final int N = 1970;
    public CalendarDemo(Context context)
    {
        this.mDBHelper=new DBHelper(context);
    }

    /**
     *
     * @param year 要判断闰年的年份
     * @return 是否为闰年
     */
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
     */


    public int sumDate(int year,int month,int day) {
        //先对输入的年月日进行判断
        for (int i = N; i < year; i++) {
            //判断是否是闰年
            if (leapYear(year))
                someday += 366;
            else
                someday += 365;
        }
        //统计月份的天数
        for (int i = 0; i < month; i++) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    someday += 31;
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    someday += 30;
                    break;
                case 2:
                    if (leapYear(year)) {
                        someday += 29;
                    } else {
                        someday += 28;
                    }
            }
        }
            someday = someday + day;

            return someday;
        }
    /**
     *
     * @param year      表示所要输入的年份
     * @return          返回该年份在数据库里所有的数据
     */
    public ArrayList<IDBill> sumYear(int year){
        this.year =year;
        return mDBHelper.selectBillByTime(year,1,1,year,12,31);
    }

        /**
         *
         * @param year   输入的年份
         * @param month  输入的月份
         * @return   该月的天数
         */
    public  ArrayList<IDBill> sumMonth(int year,int month)
    {
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
                System.out.println("输入月份不正确!!");
                //采用-1来返回错误的月份和日期输入
                lastDay = -1;
        }
        return mDBHelper.selectBillByTime(year,month,1,year,month,lastDay);

    }
    public ArrayList<IDBill> sumDay(int year,int month,int day){
        return  mDBHelper.selectBillByTime(year,month,day,year,month,day);
    }

}
