package yzj.t;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.widget.LinearLayout;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import lhq.ie.Expense;
import lhq.ie.ExpenseType;
import lz.db.CustomType;
import  lz.db.DBHelper;
import lz.db.IDBill;

/**
 * 折线图组成,主要用来实现一个月度的花销，返回每一天的花销
 * 返回当月每一天的开销
 * Created by ~ on 2017/11/15.
 */

public class Statistics extends CalendarOffset {
    DBHelper mDBHelper;
    Context context;

    public Statistics(Context context) {
        this.mDBHelper = new DBHelper(context);
        this.context=context;
    }
    /**
     * 获得折线图当月统计图数据
     * @return 存储数据的对象
     */
    public LineChartValue showMonthPerDayCost() {
        LineChartValue lineChartValue=new LineChartValue();
        CalendarOffset cal = new CalendarOffset();
        String str = cal.getLocalDate();
        TimeValue v= StringToTime(str);
        int a = v.day;
        int month = v.month;
        int year = v.year;
        ArrayList<IDBill> listCopy = mDBHelper.selectBillByTime(year, month, 1, year, month,a );
        int cur=0;
        for(int i=1;i<=a;i++){
            double temp=0;
            for (int j = cur; j < listCopy.size(); j++) {
                if(listCopy.get(j).getDay()==i) {
                    temp+=listCopy.get(j).getAmount();
                    cur++;
                }
                else break;

            }
            lineChartValue.values.add(temp);
            lineChartValue.x_lineName.add(month+"/"+i);
        }
        lineChartValue.dataSetName="本月消费情况";
        return lineChartValue;
    }
    /**
     * 获得横向柱状图当月统计图数据
     * @return 存储数据的对象
     */
    public BarChartValue showMonthPerDayCost_Bar(){
        Map<String,NameWithNum> result=new HashMap<>();

        CalendarOffset cal = new CalendarOffset();
        String str = cal.getLocalDate();
        TimeValue v= StringToTime(str);
        int a = v.day;
        int month = v.month;
        int year = v.year;
        ArrayList<IDBill> listCopy = mDBHelper.selectBillByTime(year, month, 1, year, month,a );
        for(int i=0;i<listCopy.size();i++){
            String type= listCopy.get(i).getType();
            if(result.containsKey(type)){
                NameWithNum V= result.get(type);
                V.value+=listCopy.get(i).getAmount();
                V.num++;
                result.put(type,V);
            }
            else{
                NameWithNum V=new NameWithNum();
                V.name=type;
                V.value=listCopy.get(i).getAmount();
                V.num=1;
                result.put(type,V);
            }

        }
        Iterator<String> iter = result.keySet().iterator();
        BarChartValue values=new BarChartValue();
        int i=0;
        while (iter.hasNext()) {
            NameWithNum d=result.get(iter.next());
            values.types.add(d);
            values.sum+=d.value;
            i++;
        }
        values.sort();
        return  values;

    }

    /**
     * 获得当前日期是星期几
     * @param year 年
     * @param month 月
     * @param day 日
     * @return String 星期几
     */
    private String getdayOfWeek(int year,int month,int day){
        String[] val={"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
        Calendar calendar=new GregorianCalendar(year,month-1,day);
        int dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);
        return val[dayOfWeek-1];

    }


    /**
     * 获得折线图七天统计图数据
     * @return 存储数据的对象
     */
    public LineChartValue  showWeekPerDayCost() {

        CalendarOffset cal = new CalendarOffset();
        LineChartValue values=new LineChartValue();
        String str;
        int n = 2;
        for(int i=6;i>=0;i--) {
            str=cal.getDay(-i);
            String b = str.substring(str.length() - n, str.length());
            String aMonth = str.substring(5, 7);
            String sYear = str.substring(0, 4);
            int a = Integer.parseInt(b);
            int month = Integer.parseInt(aMonth);
            int year = Integer.parseInt(sYear);
            ArrayList<IDBill> listCopy = mDBHelper.selectBillByTime(year,month,a,year,month,a);
            double sum=0;
            for(int j=0;j<listCopy.size();j++){
                sum+=listCopy.get(j).getAmount();


            }
            values.x_lineName.add(getdayOfWeek(year,month,a));
            values.values.add(sum);
        }

        values.dataSetName="最近七天消费";
        return values;
    };
    /**
     * 获得横向柱状图七天统计图数据
     * @return 存储数据的对象
     */
    public BarChartValue showWeekPerDayCost_Bar(){
        Map<String,NameWithNum> result=new HashMap<>();

        CalendarOffset cal = new CalendarOffset();
        String str2 = cal.getLocalDate();
        String str1=cal.getDay(-6);
        TimeValue v1=StringToTime(str1);
        TimeValue v2=StringToTime(str2);

        ArrayList<IDBill> listCopy = mDBHelper.selectBillByTime(v1.year, v1.month, v1.day, v2.year, v2.month,v2.day );
        for(int i=0;i<listCopy.size();i++){
           String type= listCopy.get(i).getType();
            if(result.containsKey(type)){
                NameWithNum V= result.get(type);
                V.value+=listCopy.get(i).getAmount();
                V.num++;
                result.put(type,V);
            }
            else{
                NameWithNum V=new NameWithNum();
                V.name=type;
                V.value=listCopy.get(i).getAmount();
                V.num=1;
                result.put(type,V);
            }

        }
        Iterator<String> iter = result.keySet().iterator();
        BarChartValue values=new BarChartValue();
        int i=0;
        while (iter.hasNext()) {
            NameWithNum d=result.get(iter.next());
            values.types.add(d);
            values.sum+=d.value;
            i++;
        }
        values.sort();
      return  values;

    }
    /**
     * 获得折线图总统计图数据
     * @return 存储数据的对象
     */
    public LineChartValue showGlobalPerMonthCost() {

        LineChartValue lineChartValue=new LineChartValue();
        CalendarOffset cal = new CalendarOffset();
        String str = cal.getLocalDate();
        TimeValue v= StringToTime(str);
        int a = v.day;
        int month = v.month;
        int year = v.year;
        ArrayList<IDBill> listCopy = mDBHelper.selectBillByTime(0, 0, 0, year, month,a );
        if(listCopy.size()==0){
            return lineChartValue;
        }
        int f_year=listCopy.get(0).getYear();
        int f_month=listCopy.get(0).getMonth();

        int g_month_count=(year-f_year)*12+month-f_month+1;
        int cur=0;
        for(int i=0;i<g_month_count;i++){
            double count=0;
            while (cur<listCopy.size()&&listCopy.get(cur).getYear()==f_year&&listCopy.get(cur).getMonth()==f_month){
                count+=listCopy.get(cur).getAmount();
                cur++;
            }
            lineChartValue.values.add(count);
            lineChartValue.x_lineName.add(year+"/"+f_month);


            f_month++;
            if(f_month>12){
                f_month=1;
                f_year++;
            }
        }
        lineChartValue.x_lineName.add(lineChartValue.x_lineName.get(lineChartValue.x_lineName.size()-1)+"  -");
        lineChartValue.x_lineName.remove(lineChartValue.x_lineName.size()-2);
        lineChartValue.dataSetName="历月消费情况";

        return lineChartValue;
    };
    /**
     * 获得横向柱状图总统计图数据
     * @return 存储数据的对象
     */
    public BarChartValue showGlobalPerMonthCost_Bar(){
        Map<String,NameWithNum> result=new HashMap<>();

        CalendarOffset cal = new CalendarOffset();
        String str = cal.getLocalDate();
        TimeValue v= StringToTime(str);
        int a = v.day;
        int month = v.month;
        int year = v.year;
        ArrayList<IDBill> listCopy = mDBHelper.selectBillByTime(0, 0, 0, year, month,a );
        for(int i=0;i<listCopy.size();i++){
            String type= listCopy.get(i).getType();
            if(result.containsKey(type)){
                NameWithNum V= result.get(type);
                V.value+=listCopy.get(i).getAmount();
                V.num++;
                result.put(type,V);
            }
            else{
                NameWithNum V=new NameWithNum();
                V.name=type;
                V.value=listCopy.get(i).getAmount();
                V.num=1;
                result.put(type,V);
            }

        }
        Iterator<String> iter = result.keySet().iterator();
        BarChartValue values=new BarChartValue();
        int i=0;
        while (iter.hasNext()) {
            NameWithNum d=result.get(iter.next());
            values.types.add(d);
            values.sum+=d.value;
            i++;
        }
        values.sort();
        return  values;

    }





    private TimeValue StringToTime(String str){
        int n = 2;
        String b = str.substring(str.length() - n, str.length());
       int day = Integer.parseInt(b);
        String aMonth = str.substring(5, 7);
        String sYear = str.substring(0, 4);
       int month = Integer.parseInt(aMonth);
       int  year = Integer.parseInt(sYear);
        TimeValue value=new TimeValue();
        value.day=day;
        value.month=month;
        value.year=year;
        return  value;
    }
    private class TimeValue{
        public int year;
        public int month;
        public int day;
    }
    public class NameWithNum implements Comparable<NameWithNum>{
        public String name;
        public Double value;//总钱数
        public int num;//笔数
        NameWithNum(){
            name="";
            value=0.0;
            num=0;
        }
        @Override
        public int compareTo(@NonNull NameWithNum nameWithNum) {
            if(this.value>nameWithNum.value){
                return -1;
            }
            return 1;
        }
    }

    public class BarChartValue{
        public ArrayList<NameWithNum> types;
        public double sum;
        public BarChartValue(){
            types=new ArrayList<>();

        }
        public void sort(){
            Collections.sort(types);
        }
    }
    public class LineChartValue{
        public ArrayList<Double> values;//值
        public String dataSetName;
        public ArrayList<String> x_lineName;//x轴各个项名字
        float f;//x轴名字倾斜角度
        public LineChartValue(){
            values=new ArrayList<>();
            x_lineName=new ArrayList<>();
            f=0;
        }
    }


}



