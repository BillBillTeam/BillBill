package fivene.billbill;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

import lhq.ie.ExpenseType;
import lz.db.CustomType;
import lz.img.IconGetter;
import top.lizec.draggablegridview.DraggableGridView;

public class TagManageActivity extends AppCompatActivity {
    DraggableGridView dgv;
    Button button1, button2;
    ArrayList<String> showTypeNames;
    ArrayList<String> hideTypeNames;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_manage);
        //Toolbar toolbar = (Toolbar)findViewById(R.id.)
        ExpenseType expenseType = new ExpenseType(this);

        dgv = ((DraggableGridView)findViewById(R.id.vgv));
        button1 = ((Button)findViewById(R.id.button1));
        button2 = ((Button)findViewById(R.id.button2));

        setListeners();
        setShow(expenseType);
    }
    private void setListeners()
    {
        dgv.setOnRearrangeListener(new DraggableGridView.OnRearrangeListener() {
            public void onRearrange(int oldIndex, int newIndex) {
                String type = showTypeNames.remove(oldIndex);
                if (oldIndex < newIndex)
                    showTypeNames.add(newIndex,type);
                else
                    showTypeNames.add(newIndex,type);
            }
        });
        dgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                dgv.removeViewAt(arg2);
                showTypeNames.remove(arg2);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
//
//                String word = words[random.nextInt(words.length)];
//                ImageView view = new ImageView(TagManageActivity.this);
//                view.setImageBitmap(getThumb(word));
//                dgv.addView(view);
//                poem.add(word);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
//                String finishedPoem = "";
//                for (String s : poem)
//                    finishedPoem += s + " ";
//                new AlertDialog.Builder(TagManageActivity.this)
//                        .setTitle("Here's your poem!")
//                        .setMessage(finishedPoem).show();
            }
        });
    }

    private void setShow(ExpenseType expenseType){
        showTypeNames = new ArrayList<>();
        ArrayList<CustomType> show = expenseType.getAllShowExpenseType();
        int baseBmpLen = 262;
        int textSize = 60;
        for(CustomType type:show){
            Bitmap bmp = Bitmap.createBitmap(baseBmpLen,baseBmpLen+textSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);

            Bitmap base = IconGetter.getIcon(this,type.getRes_ID());
            String name = type.getType();
            showTypeNames.add(name);
            canvas.drawBitmap(base,0,0,null);

            Paint paint = new Paint();
            paint.setFlags(Paint.ANTI_ALIAS_FLAG);
            paint.setTextSize(textSize);
            // 起始位置等于图片的中心位置坐标减去文字长度的一半
            // 从而实现文字居中对齐
            int begin  = (int)(baseBmpLen/2.0 - name.length()/2.0*textSize);
            canvas.drawText(name,begin, baseBmpLen+textSize-5,paint);

            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(bmp);

            dgv.addView(imageView);
        }
    }
}
