package bhj;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fivene.billbill.R;
import lz.db.CustomType;
import lz.img.IconGetter;

/**
 * Created by mary kt on 17-10-23.
 * 为ViewPagerManagement 提供数据的类
 * 是为了显示在主界面的可左右滑动的标签组
 */

public class TagGroupProvider {
    private List<View> TagGroupList;//存储每页的标签如{第一页视图，第二页视图，第三页视图}
    private static List<Integer>PicPostion= new ArrayList<Integer>();//存储标签对应的图片位置(id)
   // private List<String> TagNameList;
    public final static int COLUMNCOUNT=5;//每页中的gridlayout中的列数
    public final static int ROWCOUNT=2;//每页中的gridlayout中的行数
    private int width;
    private Context context;
    /**
     * 为ViewPagermanagement类提供数据
     * @param context 上下文
     * @param tagList 标签数据
     */
   public TagGroupProvider(Context context, List <CustomType> tagList, int width){
        this.width=width;
        this.TagGroupList = new ArrayList<View>();

        this.context=context;
        if(PicPostion.size()>0){
            PicPostion.clear();
        }
        initData(context,tagList);
   }

    /**
     * 获取存储图片信息的数组大小
     * @return size
     */
    public static int getPicRecSize(){
        return PicPostion.size();
    }

    /**
     * 获取对应位置的图片信息
     * @param i 标签的位置
     * @return 对应图片的ID
     */
    public  static int getPicRecID(int i){
        return PicPostion.get(i);
    }

    /**
     * 初始化数据并创建相关的View
     *
     * @param context 上下文
     * @param tagList 存储（显示的）标签信息的列表
     */
    private void initData(Context context,List <CustomType> tagList){

        LayoutInflater inflater = LayoutInflater.from(context);
        int n=tagList.size()/(COLUMNCOUNT*ROWCOUNT);
        if(tagList.size()%(COLUMNCOUNT*ROWCOUNT)!=0){//因为有个自定义的按钮要占一格
            n=n+1;
        }

        for(int i=0;i<n;i++){
            View view = inflater.inflate(R.layout.tag_group_view, null);
            GridLayout layout=(GridLayout)view.getRootView();
            layout.setColumnCount(COLUMNCOUNT);
            layout.setRowCount(ROWCOUNT);
            for (int j=0;j<(COLUMNCOUNT*ROWCOUNT)&&(j+i*(COLUMNCOUNT*ROWCOUNT))<tagList.size();j++) {

                PicPostion.add( tagList.get(j+i*(COLUMNCOUNT*ROWCOUNT)).getRes_ID());
                //insert tagWithName to gridlayout
                layout.addView(newTagWithName(inflater,tagList.get(j+i*(COLUMNCOUNT*ROWCOUNT)).getType(),IconGetter.getIcon(context,tagList.get(j+i*(COLUMNCOUNT*ROWCOUNT)).getRes_ID())));

            }
            if(i==n-1&&tagList.size()%(COLUMNCOUNT*ROWCOUNT)!=0){
                layout.addView(newTagWithName(inflater,"自定义",IconGetter.getIcon(context,14)));
                TagGroupList.add(layout);
                break;
            }
            else if(i==n-1){
                View view1 = inflater.inflate(R.layout.tag_group_view, null);
                GridLayout layout1=(GridLayout)view1.getRootView();
                layout1.setColumnCount(COLUMNCOUNT);
                layout1.setRowCount(ROWCOUNT);
                layout1.addView(newTagWithName(inflater,"自定义",IconGetter.getIcon(context,14)));
                TagGroupList.add(layout);
                TagGroupList.add(layout1);
                break;
            }
            TagGroupList.add(layout);
        }

    }

    /**
     * 创建新的tag_with_name 实例并初始化相关数据
     * @param inflater 布局注入器
     * @param text 标签名
     * @param bitmap 标签的图片
     * @return 整个视图
     */
    private View newTagWithName(LayoutInflater inflater, String text, Bitmap bitmap){
        View tagWithName = inflater.inflate(R.layout.tag_with_name, null);
        LinearLayout layout1 = (LinearLayout) tagWithName.getRootView();
        //add tagWith name Image and name
        layout1.setMinimumWidth(width/COLUMNCOUNT);
        ImageView img=(ImageView) layout1.getChildAt(0);

        final TextView textView=(TextView)layout1.getChildAt(1);
        //set values
        textView.setText(text);
        img.setImageBitmap(bitmap);
        return tagWithName;

    }

    /**
     * 获得GroupList（结果）
     * @return list数据
     */
    public List<View> getTagGroup(){
        return this.TagGroupList;

    }

    /**
     * 获得实际的分页数
     * @return TagGroupList.size()
     */
    public int getGroupSize(){
        return this.TagGroupList.size();
    }

}
