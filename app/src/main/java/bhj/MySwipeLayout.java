package bhj;

import android.content.Context;
import android.util.AttributeSet;

import cn.fanrunqi.swipelayoutlibrary.SwipeLayout;

/**
 * Created by ubuntu on 17-11-21.
 */

public class MySwipeLayout extends SwipeLayout{

    Callback callback;
    public MySwipeLayout(Context context, AttributeSet attrs) {

        super(context, attrs);
    }
    public interface Callback{


    }


}
