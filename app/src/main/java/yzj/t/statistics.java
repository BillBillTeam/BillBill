package yzj.t;
import android.content.Context;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import  lz.db.DBHelper;
import lz.db.IDBill;

/**
 * 折线图组成,主要用来实现一个月度的花销，返回每一天的花销
 * 返回当月每一天的开销
 * Created by ~ on 2017/11/15.
 */

public class statistics extends CalendarOffset {
    DBHelper mDBHelper;

    public statistics(Context context) {
        this.mDBHelper = new DBHelper(context);
    }

    public ArrayList<Double> showMonthPerDayCost() {
        ArrayList<Double> costList = new ArrayList<Double>();
        CalendarOffset cal = new CalendarOffset();
        String str = cal.getLocalDate();
        int n = 2;

        String b = str.substring(str.length() - n, str.length());
        int a = Integer.parseInt(b);
        String aMonth = str.substring(5, 7);
        String sYear = str.substring(0, 4);
        int month = Integer.parseInt(aMonth);
        int year = Integer.parseInt(sYear);
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



}



