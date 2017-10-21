package lz.db;

import android.content.Context;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by LiZeC on 2017/10/20.
 * 测试数据库的相关方法
 */
@RunWith(AndroidJUnit4.class)
public class DBHelperTest {
    private static Random rand;
    private static DBHelper helper;

    // 数据库中插入的数据会一直保留
    @BeforeClass
    public static void beforeClass(){
        //开始测试前执行，一共只执行一次
        rand = new Random();
    }

    @Before
    public void setUp() throws Exception {
        //执行任意方法之前，会执行此方法，因此此方法负责测试开始前的准备工作
        Context context = InstrumentationRegistry.getTargetContext();
        helper= new DBHelper(context);
    }

    @Test
    public void insertBill() throws Exception {
        final int n = 3;
        for(int i=0;i<n;i++){
            Bill bill = new Bill(2017,rand.nextInt(12),rand.nextInt(30),randomString(4),rand.nextDouble(),randomString(10));
            long r = helper.insertBill(bill);

            // 随机插入一条数据，应该成功插入
            assertNotEquals(-1,r);
        }
    }

    @Test
    public void insertCustomType() throws Exception {
        final int n = 3;

        for(int i=0;i<n;i++){
            String type = randomString(10);

            //任意插入一条数据，应该成功插入
            long r = helper.insertCustomType(type);
            assertNotEquals(-1,r);

            //重复插入同一数据，应该插入失败
            r = helper.insertCustomType(type);
            assertEquals(-1,r);
        }

    }

    @Test
    public void updateBill() throws Exception {

        // 随机选中其中的一条
        ArrayList<IDBill> list = helper.selectAllBill();
        int up = rand.nextInt(list.size());
        IDBill IDbill = list.get(up);

        // 随机修改其中的内容
        double randDouble = rand.nextDouble()+1;
        IDbill.setAmount(randDouble);

        // 更新
        helper.updateBill(IDbill);

        // 更新后的结果应该与之前的随机值一致
        // 由于浮点数不能精确表示，因此当数值小于0.0001时认为相等
        list = helper.selectAllBill();
        assertEquals(randDouble,list.get(up).getAmount(),0.0001);
    }

    @Test
    public void deleteCustomType() throws Exception{
        ArrayList<String> list = helper.selectAllCustomType();
        int de = rand.nextInt(list.size());
        String type = list.get(de);

        long r = helper.deleteCustomType(type);
        assertEquals(r,1);
    }

    @Test
    public void deleteBill() throws Exception {
        ArrayList<IDBill> list = helper.selectAllBill();
        int de = rand.nextInt(list.size());
        IDBill bill = list.get(de);

        // 随机删除一条，应该删除成功
        int r = helper.deleteBill(bill);
        assertEquals(r,1);
    }

    @Test
    public void getTotalBills() throws Exception{
        ArrayList<IDBill> list = helper.selectAllBill();

        double amount = 0;
        for(IDBill bill:list){
            amount += bill.getAmount();
        }

        assertEquals(amount,helper.getTotalBills(),0.001);
    }

    private static String randomString(int length){
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}