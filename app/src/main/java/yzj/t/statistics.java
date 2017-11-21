package yzj.t;
import android.content.Context;

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
        int n = 8;
        String b = str.substring(str.length() - n, str.length());
        int a = Integer.parseInt(b);
        String aMonth = str.substring(6, 7);
        String sYear = str.substring(0, 3);
        int month = Integer.parseInt(aMonth);
        int year = Integer.parseInt(sYear);
        for (int j = 0; j < a; j++) {
            ArrayList<IDBill> listCopy = (ArrayList<IDBill>) mDBHelper.selectBillByTime(year, month, a - j, year, month, a - j).clone();
            for (int i = 0; i < 100; i++) {
                double temp = 0;
                temp = +listCopy.get(i).getAmount();
                if (temp == 0) {
                    costList.add(temp);
                }
            }
        }
        return costList;
    }

    public ArrayList<Double> showMonthPerMonthCost() {
        ArrayList<Double> monthCost = new ArrayList<Double>();
        CalendarOffset cal = new CalendarOffset();
        String str = cal.getLocalDate();
        int n = 8;
        String b = str.substring(str.length() - n, str.length());
        int a = Integer.parseInt(b);
        String aMonth = str.substring(6, 7);
        String sYear = str.substring(0, 3);
        int month = Integer.parseInt(aMonth);
        int year = Integer.parseInt(sYear);
        ArrayList<IDBill> listCopy = (ArrayList<IDBill>) mDBHelper.selectBillByTime(year, month, 1, year, month, a).clone();
        for (int i = 0; i < 30; i++) {
            double temp = 0;
            temp = +listCopy.get(i).getAmount();
            if (temp == 0) {
                monthCost.add(temp);
                break;
            }
        }
        return monthCost;
    }
}



