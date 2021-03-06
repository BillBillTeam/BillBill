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
    private ArrayList<CustomType> list;

    public ExpenseType(Context context)
    {
        this.dbHelper=new DBHelper(context);
        systemTypeNames = context.getResources().getStringArray(R.array.systemType);
        list=dbHelper.selectAllCustomType();
    }

    /**
     * 更新数据库
     */
    public void updateDB()
    {
        dbHelper.insertCustomType(list);
    }


    /**
     * 获得所有显示的消费类型，以动态数组形式按位置顺序输出
     * @return 包含所有显示的消费类型的动态数组
     */
    public ArrayList<CustomType> getAllShowExpenseType()
    {
        int showNumber=0;
        int Index=0;
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
                    break;
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
                    break;
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
        int maxIndex=0;
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getType().equals(string))
            {
                throw new Exception("输入类型已经存在");
            }
            if(list.get(i).getIndex()>maxIndex)
            {
                maxIndex=list.get(i).getIndex();
            }
        }
        CustomType expensetype=new CustomType(string,maxIndex+1);
        list.add(expensetype);
    }


    /**
     * 删除用户指定的用户自定义的显示区的消费类型
     * @param Index 用户指定的消费类型的位置
     * @return false表示用户删除类型为系统类型，删除失败；true表示删除成功
     */
    public boolean deleteShowType(int Index)
    {
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getIndex()==Index)
            {
                CustomType customType=list.get(i);
                for(int j=0;j<systemTypeNames.length;j++)
                {
                    if(systemTypeNames[j].equals(list.get(i).getType()))
                    {
                        return false;
                    }
                }
                list.remove(customType);
                break;
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
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getIndex()==Index)
            {
                CustomType customType=list.get(i);
                for(int j=0;j<systemTypeNames.length;j++)
                {
                    if(systemTypeNames[j].equals(list.get(i).getType()))
                    {
                        return false;
                    }
                }
                list.remove(customType);
                break;
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
     * 将用户指定的消费类型移至指定位置
     * @param oldIndex 初始消费类型位置
     * @param newIndex 欲移动到的位置
     */
    public void moveShowType(int oldIndex,int newIndex)
    {
        int tempIndex=oldIndex;
        //从后往前移动
        if(oldIndex>=newIndex)
        {
            for(int i=0;i<list.size();i++)
            {
                if(list.get(i).getIndex()==oldIndex)
                {
                    list.get(i).setIndex(newIndex);
                    tempIndex=i;
                }
            }
            for(int i=0;i<list.size();i++)
            {
                if(list.get(i).getIndex()>=newIndex&&list.get(i).getIndex()<oldIndex&&i!=tempIndex)
                {
                    list.get(i).setIndex(list.get(i).getIndex()+1);
                }
            }
        }
        //从前往后移动
        if(oldIndex<newIndex)
        {
            for(int i=0;i<list.size();i++)
            {
                if(list.get(i).getIndex()==oldIndex)
                {
                    list.get(i).setIndex(newIndex);
                    tempIndex=i;
                }
            }
            for(int i=0;i<list.size();i++)
            {
                if(list.get(i).getIndex()>oldIndex&&list.get(i).getIndex()<=newIndex&&i!=tempIndex)
                {
                    list.get(i).setIndex(list.get(i).getIndex()-1);
                }
            }
        }
    }

    public void moveHideType(int oldIndex,int newIndex)
    {
        int tempIndex=oldIndex;
        //从后往前移动
        if(oldIndex<newIndex)
        {
            for(int i=0;i<list.size();i++)
            {
                if(list.get(i).getIndex()==oldIndex)
                {
                    list.get(i).setIndex(newIndex);
                    tempIndex=i;
                }
            }
            for(int i=0;i<list.size();i++)
            {
                if(list.get(i).getIndex()<=newIndex&&list.get(i).getIndex()>oldIndex&&i!=tempIndex)
                {
                    list.get(i).setIndex(list.get(i).getIndex()-1);
                }
            }
        }
        //从前往后移动
        if(oldIndex>=newIndex)
        {
            for(int i=0;i<list.size();i++)
            {
                if(list.get(i).getIndex()==oldIndex)
                {
                    list.get(i).setIndex(newIndex);
                    tempIndex=i;
                }
            }
            for(int i=0;i<list.size();i++)
            {
                if(list.get(i).getIndex()<oldIndex&&list.get(i).getIndex()>=newIndex&&i!=tempIndex)
                {
                    list.get(i).setIndex(list.get(i).getIndex()+1);
                }
            }
        }
    }

    /**
     * 将用户指定的显示区的消费类型移入隐藏区
     * @param Index 用户指定的消费类型的位置
     */
    public void showToHide(int Index)
    {
        int minIndex=Index;
        int tempIndex=Index;
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getIndex()==Index)
            {
                tempIndex=i;
            }
            if(list.get(i).getIndex()<=minIndex)
            {
                minIndex=list.get(i).getIndex();
            }
            if(list.get(i).getIndex()>Index)
            {
                list.get(i).setIndex(list.get(i).getIndex()-1);
            }
        }

        list.get(tempIndex).setIndex(minIndex-1);
    }

    /**
     * 将用户指定的隐藏区的消费类型加入显示区
     * @param Index 用户指定的消费类型的位置
     */
    public void hideToShow(int Index)
    {
        int maxIndex=Index;
        int tempIndex=Index;
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getIndex()==Index)
            {
                tempIndex=i;
            }
            if(list.get(i).getIndex()>=maxIndex)
            {
                maxIndex=list.get(i).getIndex();
            }
            if(list.get(i).getIndex()<Index)
            {
                list.get(i).setIndex(list.get(i).getIndex()+1);
            }
        }

       list.get(tempIndex).setIndex(maxIndex+1);
    }

    public int searchRes_ID(String string)
    {
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getType().equals(string))
            {
                return list.get(i).getRes_ID();
            }
        }
        return -1;
    }

}
