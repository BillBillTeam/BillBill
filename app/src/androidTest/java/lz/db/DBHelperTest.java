package lz.db;

import android.content.Context;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
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
    private Random rand = new Random();
    private DBHelper helper= new DBHelper(InstrumentationRegistry.getTargetContext());

    @Before
    public void setUp() throws Exception {
        //执行任意方法之前，会执行此方法，因此此方法负责测试开始前的准备工作
    }

    @Test
    public void insertBill() throws Exception {
        Bill bill = new Bill(2017,1,1,"F",rand.nextDouble(),"FFF");

        long r = helper.insertBill(bill);
        assertNotEquals(-1,r);
    }

    @Test
    public void insertCustomType() throws Exception {

    }

    @Test
    public void updateBill() throws Exception {
        Bill bill = new Bill(2017,1,1,"F",rand.nextDouble(),"FFF");
        helper.insertBill(bill);

        ArrayList<IDBill> list = helper.selectAllBill();
        int up = list.size()/2;

        IDBill IDbill = list.get(up);
        double randDouble = rand.nextDouble();
        IDbill.setAmount(randDouble);

        helper.updateBill(IDbill);

        list = helper.selectAllBill();

        assertEquals(randDouble,list.get(up).getAmount(),0.01);
    }

    @Test
    public void deleteBill() throws Exception {
        ArrayList<IDBill> list = helper.selectAllBill();
        int de = list.size()/2;
        IDBill bill = list.get(de);

        int r = helper.deleteBill(bill);
        assertEquals(r,1);
    }

}