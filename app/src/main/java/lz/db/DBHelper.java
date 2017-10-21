package lz.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 *  Created by LiZeC on 2017/10/19.
 *  数据库帮助类主类，使用此类完成数据库相关的插入，删除，修改等操作
 */

public class DBHelper extends SQLiteOpenHelper {

    //数据库和表格信息
    private static final String DB_NAME = "db_lty";
    private static final String TAB_BILL_NAME = "bill";
    private static final String TAB_CUSTOM_TYPE_NAME = "customType";
    private static final int DB_VERSION = 1;
    private static final String TAG = "数据库";

    //账单数据库表的列
    private static final String fd_YEAR   = "year";
    private static final String fd_MONTH  = "month";
    private static final String fd_DAY    = "day";
    private static final String fd_TYPE   = "type";
    private static final String fd_AMOUNT = "num";
    private static final String fd_REMARK = "remark";
    private static final String fd_ID     = "ID";



    /**
     * 数据库辅助类构造函数，此对象用于辅助对账单信息的数据库管理
     * @param context 需要访问数据库的上下文
     */
    public DBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String BillSQL = String.format("CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s INTEGER NOT NULL, " +
                        "%s INTEGER NOT NULL, " +
                        "%s INTEGER NOT NULL, " +
                        "%s TEXT NOT NULL, " +
                        "%s REAL NOT NULL, " +
                        "%s TEXT);",
                TAB_BILL_NAME, fd_ID, fd_YEAR, fd_MONTH, fd_DAY,fd_TYPE, fd_AMOUNT,fd_REMARK);
        db.execSQL(BillSQL);

        String TypeSQL = String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY);",TAB_CUSTOM_TYPE_NAME,fd_TYPE);
        db.execSQL(TypeSQL);
    }

    /**
     * 将一个用户自定义数据类型插入数据库中
     * @param type 用户自定义的类型的类型名
     * @return 非负整数表示插入成功，-1表示插入失败
     */
    public long insertCustomType(String type){
        ContentValues cv = new ContentValues();
        cv.put(fd_TYPE,type);
        return getWritableDatabase().insert(TAB_CUSTOM_TYPE_NAME,null,cv);
    }

    /**
     * 删除一个用户自定义类型
     * @param type 待删除的自定义类型
     * @return 删除成功返回受影响的行数(应该为1),否则返回0
     */
    public long deleteCustomType(String type){
        String where = String.format("%s='%s'",fd_TYPE,type);
        return getWritableDatabase().delete(TAB_CUSTOM_TYPE_NAME,where,null);
    }

    public ArrayList<String> selectAllCustomType(){
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(TAB_CUSTOM_TYPE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String type = cursor.getString(cursor.getColumnIndex(fd_TYPE));

            list.add(type);
        }
        cursor.close();
        return list;
    }

    public ArrayList<IDBill> selectAllBill(){
        ArrayList<IDBill> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(TAB_BILL_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            int ID        = cursor.getInt(cursor.getColumnIndex(fd_ID));
            int year      = cursor.getInt(cursor.getColumnIndex(fd_YEAR));
            int month     = cursor.getInt(cursor.getColumnIndex(fd_MONTH));
            int day       = cursor.getInt(cursor.getColumnIndex(fd_DAY));
            String type   = cursor.getString(cursor.getColumnIndex(fd_TYPE));
            double amount = cursor.getDouble(cursor.getColumnIndex(fd_AMOUNT));
            String remark = cursor.getString(cursor.getColumnIndex(fd_REMARK));

            IDBill bill = new IDBill(year,month,day,type,amount,remark,ID);
            list.add(bill);
        }
        cursor.close();
        return list;
    }

    /**
     * 将一条账单记录插入数据库中
     * @param bill 待插入的账单
     * @return 非负整数表示插入成功，-1表示插入失败
     */
    public long insertBill(Bill bill){
        ContentValues cv = bill2Cv(bill);
        Log.i(TAG,"插入一条账单记录");
        return getWritableDatabase().insert(TAB_BILL_NAME,null,cv);
    }

    /**
     * 在数据库中更新一条指定的账单记录
     * @param bill 指定的账单
     * @return 受影响的行数，0表示更新失败
     */
    public long updateBill(IDBill bill){
        ContentValues cv = bill2Cv(bill);
        String where = String.format("%s=%s",fd_ID,bill.getID());
        Log.i(TAG,"更新一条记录");
        return getWritableDatabase().update(TAB_BILL_NAME,cv,where,null);
    }

    /**
     *  删除账单数据库中的一项
     * @param bill 一个带有ID属性的账单类
     * @return 受影响的行数，0表示删除失败
     */
    public int deleteBill(IDBill bill){
        String where = String.format("%s=%s",fd_ID,bill.getID());
        return getWritableDatabase().delete(TAB_BILL_NAME,where,null);
    }


    /**
     *  清空Bill表中的所有数据
     */
    public void clearTabBill(){
        getWritableDatabase().delete(TAB_BILL_NAME,null,null);
    }



    public double getTotalBills(){
        double totalAmount;
        String SQL = String.format("SELECT COUNT(%s) FROM %s", fd_AMOUNT,TAB_BILL_NAME);
        Cursor cursor = getReadableDatabase().rawQuery(SQL,null);
        cursor.moveToNext();
        totalAmount = cursor.getDouble(0);
        cursor.close();
        return totalAmount;
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DropBillSQL    = String.format("DROP TABLE IF EXISTS %s",TAB_BILL_NAME);
        String DropCustomType = String.format("DROP TABLE IF EXISTS %s",TAB_CUSTOM_TYPE_NAME);
        db.execSQL(DropBillSQL);
        db.execSQL(DropCustomType);
        onCreate(db);
        Log.e(TAG,"更新数据库");
    }

    private ContentValues bill2Cv(Bill bill){
        ContentValues cv = new ContentValues();
        cv.put(fd_YEAR,bill.getYear());
        cv.put(fd_MONTH,bill.getMonth());
        cv.put(fd_DAY,bill.getDay());
        cv.put(fd_TYPE,bill.getType());
        cv.put(fd_AMOUNT,bill.getAmount());
        cv.put(fd_REMARK,bill.getRemark());
        return cv;
    }
}
