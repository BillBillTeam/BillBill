package lz.img;
import android.graphics.Bitmap;

/**
 * 图片块
 * Created by LiZeC on 2017/11/2.
 */
public class ImagePiece {

    // 指示此图片在原有图片中的位置
    public int index = 0;
    // 切割后的图片
    public Bitmap bitmap = null;
}
