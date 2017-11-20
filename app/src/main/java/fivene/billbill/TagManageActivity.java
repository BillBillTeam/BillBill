package fivene.billbill;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

import lhq.ie.ExpenseType;
import lz.db.CustomType;
import lz.img.IconGetter;
import top.lizec.draggablegridview.DraggableGridView;

public class TagManageActivity extends AppCompatActivity {
    DraggableGridView dgvShow;
    DraggableGridView dgvHide;
    FloatingActionsMenu fActionsMenu;
    FloatingActionButton fActionNew;
    FloatingActionButton fActionDelete;
    AlertDialog inputDialog;
    ExpenseType expenseType;
    int showCount;
    int hideCount;
    boolean isDelete = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_manage);
        //Toolbar toolbar = (Toolbar)findViewById(R.id.)

        expenseType = new ExpenseType(this);
        dgvShow = ((DraggableGridView)findViewById(R.id.vgvShow));
        dgvHide = ((DraggableGridView)findViewById(R.id.vgvHide));
        fActionsMenu  = (FloatingActionsMenu) findViewById(R.id.fActionMenu);
        fActionNew    = (FloatingActionButton) findViewById(R.id.fActionNew);
        fActionDelete = (FloatingActionButton) findViewById(R.id.fActionDelete);
        setFAction();
        setListeners();
        setAlertDialog();
        setShow(expenseType);
        setHide(expenseType);
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tag_manager,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        expenseType.updateDB();

        Intent in = new Intent();
        in.putExtra( "result", "res" );
        setResult( RESULT_OK, in );

        finish();
        return super.onOptionsItemSelected(item);
    }

    private void setListeners() {
        dgvShow.setOnRearrangeListener(new DraggableGridView.OnRearrangeListener() {
            public void onRearrange(int oldIndex, int newIndex) {
                expenseType.moveShowType(oldIndex,newIndex);
            }
        });
        dgvHide.setOnRearrangeListener(new DraggableGridView.OnRearrangeListener() {
            @Override
            public void onRearrange(int oldIndex, int newIndex) {
                expenseType.moveHideType(-oldIndex-1,-newIndex-1);
            }
        });

        dgvShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                if(isDelete){
                    boolean isSuccess = expenseType.deleteShowType(position);
                    checkAndDelete(dgvShow,isSuccess,position);
                    checkAndPrint(isSuccess);

                    isDelete = false;
                }
                else{
                    if(hideCount < 15){
                        dgvShow.removeViewAt(position);
                        showCount--;
                        expenseType.showToHide(position);
                        dgvHide.addView(view);
                        hideCount++;
                    }
                }

            }
        });

        dgvHide.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isDelete){
                    boolean isSuccess = expenseType.deleteHideType(-position-1);
                    checkAndDelete(dgvHide,isSuccess,position);
                    checkAndPrint(isSuccess);

                    isDelete = false;
                }
                else{
                    if(showCount < 14){
                        dgvHide.removeViewAt(position);
                        hideCount--;
                        expenseType.hideToShow(-position-1);
                        dgvShow.addView(view);
                        showCount++;
                    }
                }
            }
        });

    }

    private void setFAction(){
        fActionNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDialog.show();
                fActionsMenu.collapse();
            }
        });

        fActionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TagManageActivity.this,"现在可以点击删除一个标签",Toast.LENGTH_SHORT).show();
                isDelete = true;
                fActionsMenu.collapse();
            }
        });
    }

    private void setAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(TagManageActivity.this);
        builder.setTitle("请输入自定义类型名");
        final EditText edit = new EditText(TagManageActivity.this);
        builder.setView(edit);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String name = edit.getText().toString();
                    if(!name.isEmpty()){
                        expenseType.InsertType(name);
                        dgvShow.addView(getCustomTypeImageView(name));
                        Toast.makeText(TagManageActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(TagManageActivity.this,"数据库异常，请稍后再试",Toast.LENGTH_SHORT).show();
                }

            }
        });
        inputDialog = builder.create();
    }

    private void setShow(ExpenseType expenseType){
        ArrayList<CustomType> show = expenseType.getAllShowExpenseType();
        showCount = show.size();
        for(CustomType type:show){
            Bitmap base = IconGetter.getIcon(this,type.getRes_ID());
            String name = type.getType();

            Bitmap bmp = getNamedBitmap(base,name);
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(bmp);

            dgvShow.addView(imageView);
        }
    }

    private void setHide(ExpenseType expenseType){
        ArrayList<CustomType> hide = expenseType.getAllHideExpenseType();
        hideCount = hide.size();
        for(CustomType type:hide){
            Bitmap base = IconGetter.getIcon(this,type.getRes_ID());
            String name = type.getType();

            Bitmap bmp = getNamedBitmap(base,name);
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(bmp);

            dgvHide.addView(imageView);
        }
    }

    private Bitmap getNamedBitmap(Bitmap base,String name){
        int textSize = 60;
        int baseBmpHeight = base.getHeight();
        int baseBmpWidth = base.getWidth();
        Bitmap bmp = Bitmap.createBitmap(baseBmpWidth,baseBmpHeight+textSize+5, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);

        canvas.drawBitmap(base,0,0,null);

        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        // 起始位置等于图片的中心位置坐标减去文字长度的一半
        // 从而实现文字居中对齐
        int begin  = (int)(baseBmpWidth/2.0 - name.length()/2.0*textSize);
        canvas.drawText(name,begin, baseBmpHeight+textSize,paint);

        return bmp;
    }

    private ImageView getCustomTypeImageView(String name){
        Bitmap base = IconGetter.getIcon(this,-1);

        Bitmap bmp = getNamedBitmap(base,name);
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bmp);

        return  imageView;
    }

    private void checkAndPrint(boolean isSuccess){
        if(isSuccess){
            Toast.makeText(TagManageActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(TagManageActivity.this,"系统内置类型不能删除！",Toast.LENGTH_SHORT).show();
        }
    }

    private void checkAndDelete(DraggableGridView dgv,boolean isSuccess,int position){
        if(isSuccess){
            dgv.removeViewAt(position);
        }
    }
}
