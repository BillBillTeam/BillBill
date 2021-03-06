package lz.db;

import java.util.Calendar;

/**
 * Created by LiZec on 2017/10/19.
 *
 * 账单类 表示一条账单记录
 */

public class Bill {
    private int year;
    private int month;
    private int day;
    private String type;
    private double amount;
    private String remark;

    public Bill(int year, int month, int day, String type, double amount, String remark) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.type = type;
        this.amount = amount;
        this.remark = remark;
    }
    public Bill (Calendar now, String type, double amount, String remark)
    {
        this.year=now.get(Calendar.YEAR);
        this.month=now.get(Calendar.MONTH)+1;
        this.day=now.get(Calendar.DAY_OF_MONTH);
        this.type = type;
        this.amount = amount;
        this.remark = remark;

    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getRemark() {
        return remark;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", remark='" + remark + '\'' +
                '}';
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
