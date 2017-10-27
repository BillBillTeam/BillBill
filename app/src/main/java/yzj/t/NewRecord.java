package yzj.t;
import java.util.Calendar;
import java.util.UUID;
import java.util.Date;
import lz.db.Bill;
import lz.db.DBHelper;
import lz.db.IDBill;
/**
 * Created by ~ on 2017/10/27.
 */

public class NewRecord  {
    Calendar now;
    Date dd;
    private int year,month,date,hour,minute,second;
    private String type;
    private double amount;
    private String remark;
    private UUID mId;



    public NewRecord(String type, double amount, String remark) {

        now = Calendar.getInstance();
        dd =now.getTime();
        year =now.get(Calendar.YEAR);
        month = now.get(Calendar.MONTH);
        date  =now.get(Calendar.DATE);
        hour = now.get(Calendar.HOUR);
        minute  = now.get(Calendar.MINUTE);
        second  = now.get(Calendar.SECOND);
        this.type = type;
        this.amount = amount;
        this.remark = remark;
        mId =UUID.randomUUID();
        String s = UUID.randomUUID().toString();
        IDBill mIDBill =new IDBill(year,month,date,type,amount,remark,mId);
    }
    public Calendar getNow() {
        return now;
    }

    public Date getDd() {
        return dd;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDate() {
        return date;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
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

    public UUID getId() {
        return mId;
    }

}
