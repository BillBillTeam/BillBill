package lz.db;

/**
 * Created by LiZeC on 2017/10/19.
 * 带有ID信息的账单类
 */

public class IDBill extends Bill {
    private int ID;

    public IDBill(int year, int month, int day, String type, double amount, String remark, int ID) {
        super(year, month, day, type, amount, remark);
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    @Override
    public int getYear() {
        return super.getYear();
    }

    @Override
    public int getMonth() {
        return super.getMonth();
    }

    @Override
    public int getDay() {
        return super.getDay();
    }

    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public double getAmount() {
        return super.getAmount();
    }

    @Override
    public String getRemark() {
        return super.getRemark();
    }

    @Override
    public String toString() {
        return "IDBill{" +
                "ID=" + ID + " " +super.toString()+
                '}';
    }
}
