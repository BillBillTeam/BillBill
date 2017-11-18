package bhj;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import fivene.billbill.R;
import lhq.ie.ExpenseType;
import lz.db.CustomType;

/**
 * Created by marykt on 17-11-18.
 * 管理标签ui的类
 */

public class ViewPagerManagement {
    private ViewPager mTagGroupPager;
    private TagGroupProvider provider;
    private int globalWidth;
    private Callback mCallback;
    private ViewGroup group ;
    private ImageView mImageView;

    private ImageView[] mImageViews = null;
   public ViewPagerManagement(ViewPager mTagGroupPager, int globalWidth,Callback mCallback,ViewGroup group,ImageView[] mImageViews){
        this.mTagGroupPager=mTagGroupPager;
        this.globalWidth=globalWidth;
        this.mCallback=mCallback;
        this.group=group;
        this.mImageViews=mImageViews;

    }
    public interface Callback{
        void callbackAddListenerToViewPager(GridLayout layout);


    }


    public void createViewPager(Context context){

        mTagGroupPager.setVisibility(View.VISIBLE);


        ExpenseType types=new ExpenseType(context);
        //create TagGroupProvider
        List<CustomType> list=types.getAllShowExpenseType();


        provider=new TagGroupProvider(context,list,globalWidth);

        // group是R.layou.mainview中的负责包裹小圆点的LinearLayout.


        mImageViews = new ImageView[provider.getGroupSize()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(13, 13);
        layoutParams.setMargins(5, 0, 5, 1);

        for (int i = 0; i < provider.getGroupSize(); i++) {
            mImageView = new ImageView(context);
            mImageView.setLayoutParams(layoutParams);
            mImageViews[i] = mImageView;
            if (i == 0) {
                // 默认选中第一张图片
                mImageViews[i].setBackgroundResource(R.drawable.circle_3);
            }
            else {
                mImageViews[i].setBackgroundResource(R.drawable.circle_1);
            }
            group.addView(mImageViews[i]);
        }
        // add listener for tags
        GridLayout layout =(GridLayout)provider.getTagGroup().get(0);
        mCallback.callbackAddListenerToViewPager(layout);

        mTagGroupPager.setAdapter(new TagGroupAdapter(provider.getTagGroup()));
        mTagGroupPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                for(int i=0;i<mImageViews.length;i++){
                    if (i == position) {
                        // 默认选中第一张图片
                        mImageViews[i].setBackgroundResource(R.drawable.circle_3);
                    }
                    else {
                        mImageViews[i].setBackgroundResource(R.drawable.circle_1);
                    }


                }


            }

            @Override
            public void onPageSelected(int position) {

                GridLayout layout =(GridLayout)provider.getTagGroup().get(position);
                mCallback.callbackAddListenerToViewPager(layout);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    private final class TagGroupAdapter extends PagerAdapter {
        private List<View> views = null;
        public TagGroupAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(views.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {

        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position), 0);

            return views.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

    }

    public void reloadIt(Context context){
        group.removeAllViews();

        ExpenseType types=new ExpenseType(context);
        //create TagGroupProvider
        List<CustomType> list=types.getAllShowExpenseType();
        provider=new TagGroupProvider(context,list,globalWidth);

        // group是R.layou.mainview中的负责包裹小圆点的LinearLayout.


        mImageViews = new ImageView[provider.getGroupSize()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(13, 13);
        layoutParams.setMargins(5, 0, 5, 1);

        for (int i = 0; i < provider.getGroupSize(); i++) {
            mImageView = new ImageView(context);
            mImageView.setLayoutParams(layoutParams);
            mImageViews[i] = mImageView;
            if (i == 0) {
                // 默认选中第一张图片
                mImageViews[i].setBackgroundResource(R.drawable.circle_3);
            }
            else {
                mImageViews[i].setBackgroundResource(R.drawable.circle_1);
            }
            group.addView(mImageViews[i]);
        }
        // add listener for tags
        GridLayout layout =(GridLayout)provider.getTagGroup().get(0);
        mCallback.callbackAddListenerToViewPager(layout);

        mTagGroupPager.setAdapter(new TagGroupAdapter(provider.getTagGroup()));
        mTagGroupPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                for(int i=0;i<mImageViews.length;i++){
                    if (i == position) {
                        // 默认选中第一张图片
                        mImageViews[i].setBackgroundResource(R.drawable.circle_3);
                    }
                    else {
                        mImageViews[i].setBackgroundResource(R.drawable.circle_1);
                    }


                }


            }

            @Override
            public void onPageSelected(int position) {

                GridLayout layout =(GridLayout)provider.getTagGroup().get(position);
                mCallback.callbackAddListenerToViewPager(layout);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        GridLayout layoutd =(GridLayout)provider.getTagGroup().get(0);
        mCallback.callbackAddListenerToViewPager(layoutd);
        mTagGroupPager.getAdapter().notifyDataSetChanged();

        // group是R.layou.mainview中的负责包裹小圆点的LinearLayout.
    }

}
