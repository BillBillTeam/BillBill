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
    public String toString() {
        return "IDBill{" +
                "ID=" + ID + " " +super.toString()+
                '}';
    }
}
