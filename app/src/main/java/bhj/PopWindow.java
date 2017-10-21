package bhj;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fivene.billbill.R;

/**
 * Created by ubuntu on 17-10-20.
 */

public class PopWindow extends PopupWindow {


        private Context mContext;

        private View view;

        private TextView btn_cancel;


        public PopWindow(Context mContext, View.OnClickListener itemsOnClick) {

            this.view = LayoutInflater.from(mContext).inflate(R.layout.pop_up_time, null);

//        btn_take_photo = (Button) view.findViewById(R.id.btn_take_photo);
//        btn_pick_photo = (Button) view.findViewById(R.id.btn_pick_photo);
            btn_cancel = (TextView) view.findViewById(R.id.btn_cancel);
            // 取消按钮
            btn_cancel.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // 销毁弹出框
                    dismiss();
                }
            });
            // 设置按钮监听
//        btn_pick_photo.setOnClickListener(itemsOnClick);
//        btn_take_photo.setOnClickListener(itemsOnClick);

            // 设置外部可点击
            this.setOutsideTouchable(true);
            // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
            this.view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {

                    int height = view.findViewById(R.id.pop_layout).getTop();

                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height) {
                            dismiss();
                        }
                    }
                    return true;
                }
            });


    /* 设置弹出窗口特征 */
            // 设置视图
            this.setContentView(this.view);
            // 设置弹出窗体的宽和高
            this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
            this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

            // 设置弹出窗体可点击
            this.setFocusable(true);

            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置弹出窗体的背景
            this.setBackgroundDrawable(dw);

            // 设置弹出窗体显示时的动画，从底部向上弹出
            this.setAnimationStyle(R.style.pop_window_anim);

        }
    }
