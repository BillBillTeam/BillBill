package lhq.ie;

import android.content.Context;

import java.util.ArrayList;

import lz.db.CustomType;
import lz.db.DBHelper;

/**
 * 有关用户消费类型的类
 * Created by lihuiqingdediannao on 2017/10/22.
 */

public class ExpenseType {
    DBHelper dbHelper;


    public ExpenseType(Context context)
    {
        this.dbHelper=new DBHelper(context);
    }

    /**
     * 获得所有消费类型，以动态数组形式输出
     * @return 包含所有消费类型的动态数组
     */
    public ArrayList<String> getAllExpenseType()
    {
        ArrayList<CustomType> list= dbHelper.selectAllCustomType();
        ArrayList<String> type=new ArrayList<>();
        for(int i=0;i<list.size();i++)
        {
            type.add(list.get(i).getType());
        }
        return type;
    }

    /**
     * 在数据库尾部插入用户定义的新消费类型
     * @param string 用户定义新消费类型
     * @throws Exception 输入不合法，用户输入类型已存在
     */
    public void InsertType (String string)throws Exception
    {
        ArrayList<CustomType> list = dbHelper.selectAllCustomType();
        for(int i=0;i<=list.size();i++)
        {
            if(list.get(i).getType().equals(string))
            {
                throw new Exception("输入类型已经存在");
            }
        }
        CustomType expensetype=new CustomType(string,list.size()+1);
        list.add(expensetype);
        dbHelper.insertCustomType(list);
    }




}
