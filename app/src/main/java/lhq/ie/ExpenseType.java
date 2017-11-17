package lhq.ie;

import android.content.Context;

//import java.lang.reflect.Array;
import java.util.ArrayList;

import fivene.billbill.R;
import lz.db.CustomType;
import lz.db.DBHelper;

/**
 * 有关用户消费类型的类
 * Created by lihuiqingdediannao on 2017/10/22.
 */

public class ExpenseType {
    private DBHelper dbHelper;
    private String[] systemTypeNames;

    public ExpenseType(Context context)
    {
        this.dbHelper=new DBHelper(context);
        systemTypeNames = context.getResources().getStringArray(R.array.systemType);
    }

    /**
     * 获得所有显示的消费类型，以动态数组形式按位置顺序输出
     * @return 包含所有显示的消费类型的动态数组
     */
    public ArrayList<CustomType> getAllShowExpenseType()
    {
        int showNumber=0;
        int Index=0;
        ArrayList<CustomType> list= dbHelper.selectAllCustomType();
        ArrayList<CustomType> type=new ArrayList<>();

        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getIndex()>=0)
            {
                showNumber++;
            }
        }
        for(int i=0;i<showNumber;i++)
        {
            for(int j=0;j<list.size();j++)
            {
                if(list.get(j).getIndex()==Index)
                {
                    type.add(list.get(j));
                    Index++;
                }
            }

        }
        return type;
    }

    /**
     * 获得所有隐藏的消费类型，以动态数组的形式按位置顺序输出
     * @return 包含所有隐藏的消费类型的动态数组
     */
    public ArrayList<CustomType> getAllHideExpenseType()
    {
        int hideNumber=0;
        int Index=-1;
        ArrayList<CustomType> list= dbHelper.selectAllCustomType();
        ArrayList<CustomType> type=new ArrayList<>();
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getIndex()<0)
            {
                hideNumber++;
            }
        }
        for(int i=0;i<hideNumber;i++)
        {
            for(int j=0;j<list.size();j++)
            {
                if(list.get(j).getIndex()==Index)
                {
                    type.add(list.get(j));
                    Index--;
                }
            }

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
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getType().equals(string))
            {
                throw new Exception("输入类型已经存在");
            }
        }
        CustomType expensetype=new CustomType(string,list.size());
        list.add(expensetype);
        dbHelper.insertCustomType(list);
    }


    /**
     * 删除用户指定的用户自定义的显示区的消费类型
     * @param Index 用户指定的消费类型的位置
     * @return false表示用户删除类型为系统类型，删除失败；true表示删除成功
     */
    public boolean deleteShowType(int Index)
    {
        ArrayList<CustomType> list = dbHelper.selectAllCustomType();
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getIndex()==Index)
            {
                CustomType customType=new CustomType(list.get(i).getType(),list.get(i).getIndex());
                for(int j=0;j<systemTypeNames.length;j++)
                {
                    if(systemTypeNames[j].equals(list.get(i).getType()))
                    {
                        return false;
                    }
                }
                dbHelper.deleteCustomType(customType);
            }
        }
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getIndex()>Index)
            {
                list.get(i).setIndex(list.get(i).getIndex()-1);
            }
        }
        return true;
    }

    /**
     * 删除用户指定的用户自定义的隐藏区的消费类型
     * @param Index 用户指定的消费类型的位置
     * @return false表示用户删除类型为系统类型，删除失败；true表示删除成功
     */
    public boolean deleteHideType(int Index)
    {
        ArrayList<CustomType> list = dbHelper.selectAllCustomType();
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getIndex()==Index)
            {
                CustomType customType=new CustomType(list.get(i).getType(),list.get(i).getIndex());
                for(int j=0;j<systemTypeNames.length;j++)
                {
                    if(systemTypeNames[j].equals(list.get(i).getType()))
                    {
                        return false;
                    }
                }
                dbHelper.deleteCustomType(customType);
            }
        }
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getIndex()<Index)
            {
                list.get(i).setIndex(list.get(i).getIndex()+1);
            }
        }
        return true;
    }
    /**
     * 交换两个消费类型的位置
     * @param Index1 第一个消费类型的位置
     * @param Index2 第二个消费类型的位置
     */
    public void exchange(int Index1,int Index2)
    {
        ArrayList<CustomType> list = dbHelper.selectAllCustomType();
        CustomType customType=new CustomType(list.get(Index1).getType(),list.get(Index1).getIndex());
        list.get(Index1).setType(list.get(Index2).getType());
        list.get(Index1).setIndex(list.get(Index2).getIndex());
        list.get(Index2).setType(customType.getType());
        list.get(Index2).setIndex(customType.getIndex());
    }

    /**
     * 将用户指定的显示区的消费类型移入隐藏区
     * @param Index 用户指定的消费类型的位置
     */
    public void showToHide(int Index)
    {
        int minIndex=Index;
        ArrayList<CustomType> list = dbHelper.selectAllCustomType();
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getIndex()<=minIndex)
            {
                minIndex=list.get(i).getIndex();
            }
            if(list.get(i).getIndex()>Index)
            {
                list.get(i).setIndex(list.get(i).getIndex()-1);
            }
        }

        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getIndex()==Index)
            {
                list.get(i).setIndex(minIndex-1);
            }
        }

    }

    /**
     * 将用户指定的隐藏区的消费类型加入显示区
     * @param Index 用户指定的消费类型的位置
     */
    public void hideToShow(int Index)
    {
        int maxIndex=Index;
        ArrayList<CustomType> list = dbHelper.selectAllCustomType();
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getIndex()>=maxIndex)
            {
                maxIndex=list.get(i).getIndex();
            }
            if(list.get(i).getIndex()<Index)
            {
                list.get(i).setIndex(list.get(i).getIndex()+1);
            }
        }

        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getIndex()==Index)
            {
                list.get(i).setIndex(maxIndex+1);
            }
        }

    }

}
