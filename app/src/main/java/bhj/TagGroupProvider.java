package bhj;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import fivene.billbill.R;
import lz.db.CustomType;
import lz.img.IconGetter;

/**
 * Created by ubuntu on 17-10-23.
 */

public class TagGroupProvider {
    private List<View> TagGroupList;

    private static List<Integer>PicPostion= new ArrayList<Integer>();
   // private List<String> TagNameList;
    public final static int COLUMNCOUNT=5;
    public final static int ROWCOUNT=2;

    private int width;
    private Context context;
    /**
     * 创建标签组
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
    public static int getPicRecSize(){
        return PicPostion.size();
    }
    public  static int getPicRecID(int i){
        return PicPostion.get(i);
    }

    private void initData(Context context,List <CustomType> tagList){

        LayoutInflater inflater = LayoutInflater.from(context);
        int n=tagList.size()/(COLUMNCOUNT*ROWCOUNT);
        if(tagList.size()%(COLUMNCOUNT*ROWCOUNT)!=0){
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

    public List<View> getTagGroup(){
        return this.TagGroupList;

    }
    public int getGroupSize(){
        return this.TagGroupList.size();
    }

}
