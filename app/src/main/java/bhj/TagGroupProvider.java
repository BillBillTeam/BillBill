package bhj;

import android.content.Context;
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
   // private List<String> TagNameList;
    private final int COLUMNCOUNT=5;
    private final int ROWCOUNT=2;
    private int width;
    /**
     * 创建标签组
     * @param context 上下文
     * @param tagList 标签数据
     */
   public TagGroupProvider(Context context, List <CustomType> tagList, int width){
        this.width=width;
        this.TagGroupList = new ArrayList<View>();


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

                View tagWithName = inflater.inflate(R.layout.tag_with_name, null);
                LinearLayout layout1 = (LinearLayout) tagWithName.getRootView();
                //add tagWith name Image and name
                layout1.setMinimumWidth(width/COLUMNCOUNT);
                ImageView img=(ImageView) layout1.getChildAt(0);

                final TextView text=(TextView)layout1.getChildAt(1);
                //set values
                text.setText(tagList.get(j+i*(COLUMNCOUNT*ROWCOUNT)).getType());
                img.setImageBitmap(IconGetter.getIcon(context,tagList.get(j+i*(COLUMNCOUNT*ROWCOUNT)).getRes_ID()));

                //insert tagWithName to gridlayout
                layout.addView(tagWithName);
            }

            TagGroupList.add(layout);
        }


    }
    public void  updataGroupList(List<String> tagNameList){


    }
    public List<View> getTagGroup(){
        return this.TagGroupList;

    }
    public int getGroupSize(){
        return this.TagGroupList.size();
    }

}
