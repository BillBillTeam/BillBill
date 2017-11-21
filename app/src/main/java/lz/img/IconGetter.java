package lz.img;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.List;

/**
 * 根据给定的资源ID，获得相应的图标资源
 * Created by LiZeC 2017/11/6.
 */

public class IconGetter {
    private static List<Bitmap> imgList;
    private static List<Bitmap> clickedImgList;
    private static Bitmap customIcon;
    private static Bitmap clickedCustomIcon;

    /**
     * 获得系统内置的图标
     * @param context 当前进程上下文
     * @param res_ID 图标资源的内置ID
     * @return  图标的Bitmap
     */
    public static Bitmap getIcon(Context context,int res_ID){
        //使用单例模式确保只加载一次资源
        if(imgList == null){
            synchronized (IconGetter.class){
                if(imgList == null){
                    initIcons(context);
                }
            }
        }
        return getBitmapByIdx(res_ID);
    }

    /**
     * 获得系统内置的图标(选中状态)
     * @param context 当前进程上下文
     * @param res_ID 图标资源的内置ID
     * @return 图标的Bitmap
     */
    public static Bitmap getClickedIcon(Context context,int res_ID) {
        if(clickedImgList == null){
            synchronized (IconGetter.class){
                if(clickedImgList == null){
                    initClickedIcons(context);
                }
            }
        }
        return getClickedBitmapByIdx(res_ID);
    }

    /**
     *  初始化图标
     */
    private static void initIcons(Context context){
        String iconName = "icon";
        ApplicationInfo appInfo = context.getApplicationInfo();
        //得到该图片的id(name 是该图片的名字，"drawable" 是该图片存放的目录，appInfo.packageName是应用程序的包)
        int resID = context.getResources().getIdentifier(iconName, "drawable", appInfo.packageName);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resID);
        imgList = ImageSplitter.split(bitmap,5,4);

        String customIconName = "custom_type";
        resID = context.getResources().getIdentifier(customIconName,"drawable",appInfo.packageName);
        customIcon = BitmapFactory.decodeResource(context.getResources(),resID);
    }

    private static void initClickedIcons(Context context){
        String clickedIconName = "clicked_icon";
        ApplicationInfo appInfo = context.getApplicationInfo();
        //得到该图片的id(name 是该图片的名字，"drawable" 是该图片存放的目录，appInfo.packageName是应用程序的包)
        int resID = context.getResources().getIdentifier(clickedIconName, "drawable", appInfo.packageName);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resID);
        clickedImgList = ImageSplitter.split(bitmap,5,4);

        String clickedCustomIconName = "clicked_custom_type";
        resID = context.getResources().getIdentifier(clickedCustomIconName,"drawable",appInfo.packageName);
        clickedCustomIcon =BitmapFactory.decodeResource(context.getResources(),resID);
    }

    private static Bitmap getBitmapByIdx(int index){
        if(index == -1){
            return customIcon;
        }
        else{
            return imgList.get(index);
        }
    }

    private static Bitmap getClickedBitmapByIdx(int index){
        if(index == -1){
            return clickedCustomIcon;
        }
        else{
            return clickedImgList.get(index);
        }
    }

    private IconGetter(){ }
}
