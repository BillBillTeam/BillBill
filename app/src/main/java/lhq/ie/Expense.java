package lhq.ie;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;

import lz.db.Bill;
import lz.db.DBHelper;
import lz.db.IDBill;

/**
 * 管理用户消费记录的类
 * Created by lihuiqingdediannao on 2017/10/21.
 */

public class Expense {

    DBHelper dbHelper;

    public Expense(Context context)
    {
        this.dbHelper=new DBHelper(context);
    }


    /**
     * 将一条账单记录插入数据库
     * @param bill 待插入账单
     * @return 非负整数表示插入成功，-1表示插入失败
     */
    public long Insert(Bill bill)
    {
       return dbHelper.insertBill(bill);
    }

    /**
     * 对数据库中指定的一条账单记录进行更新
     * @param bill 指定的账单记录
     * @return 0表示更新失败，否则更新成功
     */
    public long Update(IDBill bill)
    {
        return dbHelper.updateBill(bill);
    }

    /**
     * 删除数据库中指定的一项账单
     * @param bill 指定的账单代号
     * @return 0表示删除失败，否则删除成功
     */
    public int Delete(IDBill bill)
    {
        return dbHelper.deleteBill(bill);
    }

    /**
     * 清空账单记录
     */
    public void Clear()
    {
        dbHelper.clearTabBill();
    }

    /**
     * 获取用户所有账单记录
     * @return 保存所有账单记录的数组
     */
    public ArrayList<IDBill> selectAll()
    {
        return dbHelper.selectAllBill();
    }

    /**
     * 获取账单总额
     * @return 账单总额
     */
    public double getTotalExpenses()
    {
        return dbHelper.getTotalBills();
    }



}
