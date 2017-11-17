package fivene.billbill;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

import lhq.ie.ExpenseType;
import lz.db.CustomType;
import lz.img.IconGetter;
import top.lizec.draggablegridview.DraggableGridView;

public class TagManageActivity extends AppCompatActivity {
    static Random random = new Random();
    static String[] words = "the of and a to in is be that was he for it with as his I on have at by not they this had are but from or she an which you one we all were her would there their will when who him been has more if no out do so can what up said about other into than its time only could new them man some these then two first may any like now my such make over our even most me state after also made many did must before back see through way where get much go well your know should down work year because come people just say each those take day good how long Mr own too little use US very great still men here life both between old under last never place same another think house while high right might came off find states since used give against three himself look few general hand school part small American home during number again Mrs around thought went without however govern don't does got public United point end become head once course fact upon need system set every war put form water took".split(" ");
    DraggableGridView dgv;
    Button button1, button2;
    ArrayList<String> poem = new ArrayList<>();


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
                String word = poem.remove(oldIndex);
                if (oldIndex < newIndex)
                    poem.add(newIndex, word);
                else
                    poem.add(newIndex, word);
            }
        });
        dgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                dgv.removeViewAt(arg2);
                poem.remove(arg2);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String word = words[random.nextInt(words.length)];
                ImageView view = new ImageView(TagManageActivity.this);
                view.setImageBitmap(getThumb(word));
                dgv.addView(view);
                poem.add(word);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String finishedPoem = "";
                for (String s : poem)
                    finishedPoem += s + " ";
                new AlertDialog.Builder(TagManageActivity.this)
                        .setTitle("Here's your poem!")
                        .setMessage(finishedPoem).show();
            }
        });
    }

    private Bitmap getThumb(String s)
    {
        Bitmap bmp = Bitmap.createBitmap(150, 150, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();

        paint.setColor(Color.rgb(random.nextInt(128), random.nextInt(128), random.nextInt(128)));
        paint.setTextSize(24);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        canvas.drawRect(new Rect(0, 0, 150, 150), paint);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(s, 75, 75, paint);

        return bmp;
    }

    private void setShow(ExpenseType expenseType){
        ArrayList<CustomType> show = expenseType.getAllShowExpenseType();
        int baseBmpLen = 262;
        int textSize = 60;
        for(CustomType type:show){
            Bitmap bmp = Bitmap.createBitmap(baseBmpLen,baseBmpLen+textSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);

            Bitmap base = IconGetter.getIcon(this,type.getRes_ID());
            String name = type.getType();

            canvas.drawBitmap(base,0,0,null);

            Paint paint = new Paint();
            paint.setFlags(Paint.ANTI_ALIAS_FLAG);
            paint.setTextSize(textSize);

            int begin  = (int)(baseBmpLen/2.0 - name.length()/2.0*textSize);

            canvas.drawText(name,begin, baseBmpLen+textSize-5,paint);

            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(bmp);

            dgv.addView(imageView);

        }
    }
}
