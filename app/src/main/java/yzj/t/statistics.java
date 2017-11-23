package yzj.t;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

public class statistics extends CalendarOffset {
    DBHelper mDBHelper;
    Context context;

    public statistics(Context context) {
        this.mDBHelper = new DBHelper(context);
        this.context=context;
    }

    /**
     * 折线图一月统计图
     * @return
     */
    public ArrayList<Double> showMonthPerDayCost() {
        ArrayList<Double> costList = new ArrayList<Double>();
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
            costList.add(temp);

        }
        return costList;
    }

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
     * 折线图7天统计数据
     * @return
     */
    public ArrayList<Double> showWeekPerDayCost() {
        ArrayList<Double> costList = new ArrayList<Double>();
        CalendarOffset cal = new CalendarOffset();
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
            costList.add(sum);
        }
        return costList;
    };

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



}



