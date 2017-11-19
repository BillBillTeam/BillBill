package lz.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import fivene.billbill.R;

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

    //类型数据库表的列
    private static final String fd_INDEX = "typeIndex";
    private static final String fd_RES_ID = "resID";

    private String[] systemTypeNames;

    /**
     * 数据库辅助类构造函数，此对象用于辅助数据库管理
     * @param context 需要访问数据库的上下文
     */
    public DBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        systemTypeNames = context.getResources().getStringArray(R.array.systemType);
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

        String TypeSQL = String.format("CREATE TABLE %s (" +
                "%s TEXT PRIMARY KEY, " +
                "%s INTEGER, " +
                "%s INTEGER);",
                TAB_CUSTOM_TYPE_NAME,fd_TYPE,fd_INDEX,fd_RES_ID);
        db.execSQL(TypeSQL);

        initSystemType(db);
    }

    /**
     * 清空数据库中全部的数据，并将给定的数据插入
     * @param all 包含全部类型的数组
     * @throws SQLException 如果插入过程存在问题,此异常指出错误的类型名
     */
    public void insertCustomType(ArrayList<CustomType> all) throws SQLException{
        insertCustomType(getWritableDatabase(),all);
    }



    /**
     * 删除一个用户自定义类型
     * @param customType 待删除的自定义类型
     * @return 删除成功返回受影响的行数(应该为1),否则返回0
     */
    public long deleteCustomType(CustomType customType){
        String where = String.format("%s='%s'",fd_TYPE,customType.getType());
        return getWritableDatabase().delete(TAB_CUSTOM_TYPE_NAME,where,null);
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

    /** 根据指定的时间段，返回该时间段的数据，此时间段包含用于指定边界的两天
     * 此函数不检查给定数据的有效性，如果给定数据不符合逻辑或不符合日期表达习惯，则返回空的ArrayList
     * @param begYear 起始年
     * @param begMonth 起始月
     * @param begDay 起始日
     * @param endYear 结束年
     * @param endMonth 结束月
     * @param endDay 结束日
     * @return 在给定范围内的全部数据
     */
    public ArrayList<IDBill> selectBillByTime(int begYear,int begMonth,int begDay,int endYear,int endMonth,int endDay){
        String where = String.format("(%s BETWEEN %s AND %s) AND (%s BETWEEN %s AND %s) AND (%s BETWEEN %s AND %s)",
                fd_YEAR,begYear,endYear,fd_MONTH,begMonth,endMonth,fd_DAY,begDay,endDay);
        Cursor cursor = getReadableDatabase().query(TAB_BILL_NAME,null,where,null,null,null,null);
        ArrayList<IDBill> list = billCursor2List(cursor);
        cursor.close();
        return list;
    }

    /**
     * 获得所有的类型,包括内置类型和用户自定义类型
     * @return 包含所有用户类型的数组
     */
    public ArrayList<CustomType> selectAllCustomType(){
        ArrayList<CustomType> list = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(TAB_CUSTOM_TYPE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            String type = cursor.getString(cursor.getColumnIndex(fd_TYPE));
            int index   = cursor.getInt(cursor.getColumnIndex(fd_INDEX));
            int res_ID  = cursor.getInt(cursor.getColumnIndex(fd_RES_ID));
            CustomType ct = new CustomType(type,index,res_ID);
            list.add(ct);
        }
        cursor.close();
        return list;
    }

    /**
     * 获得用户的所有账单记录
     * @return 包含所有账单记录的数组
     */
    public ArrayList<IDBill> selectAllBill(){
        String orderBy = String.format("%s,%s,%s ASC",fd_YEAR,fd_MONTH,fd_DAY);
        Cursor cursor = getReadableDatabase().query(TAB_BILL_NAME,null,null,null,null,null,orderBy);
        ArrayList<IDBill> list = billCursor2List(cursor);
        cursor.close();
        return list;
    }

    /**
     *  清空Bill表中的所有数据
     */
    public void clearTabBill(){
        getWritableDatabase().delete(TAB_BILL_NAME,null,null);
    }


    /**
     * 获得账单总数量
     * @return 账单总金额
     */
    public double getTotalBills(){
        double totalAmount;
        String SQL = String.format("SELECT SUM(%s) FROM %s", fd_AMOUNT,TAB_BILL_NAME);
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

    //将一个Bill类转化为一个ContentValues类
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

    //将一个Bill表的游标对象转化为一个ArrayList
    private ArrayList<IDBill> billCursor2List(Cursor cursor) {
        ArrayList<IDBill> list = new ArrayList<>();
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
        return list;
    }


    private void insertCustomType(SQLiteDatabase db,ArrayList<CustomType> all) throws SQLException{
        db.delete(TAB_CUSTOM_TYPE_NAME,null,null);
        ContentValues cv = new ContentValues(all.size() * 2);
        for(CustomType ct:all){
            cv.put(fd_TYPE,ct.getType());
            cv.put(fd_INDEX,ct.getIndex());
            cv.put(fd_RES_ID,ct.getRes_ID());
            db.insertOrThrow(TAB_CUSTOM_TYPE_NAME,null,cv);
        }
    }

    /**
     *  将系统提供的自定义类型插入到数据库中
     *
     *  对于默认类型，资源ID按照递增顺序，从0开始，提供20种资源图片
     *  其中默认显示前14个图片，后5个默认隐藏
     */
    private void initSystemType(SQLiteDatabase db){
        int size = systemTypeNames.length;

        ArrayList<CustomType> list = new ArrayList<>(size);


        //可以显示的部分，从0开始排序，资源ID依次递增
        for(int i=0;i<14;i++){
            list.add(new CustomType(systemTypeNames[i],i,i));
        }

        //不显示的部分，从-1开始排序，资源ID依次递增
        //其中14对应的是自定义的图标，跳过此图标
        int idx = -1;
        for(int i=15;i<size;i++){
            list.add(new CustomType(systemTypeNames[i],idx,i));
            idx--;
        }

        insertCustomType(db,list);
    }
}
