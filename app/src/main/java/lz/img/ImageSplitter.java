package lz.img;


import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;


/**
 * 将图片切割成指定的样式
 * Created by LiZeC on 2017/11/2.
 */
class ImageSplitter {

    /**
     * 根据指定的参数切割图片
     * @param bitmap 待切割的图片
     * @param xPiece x轴需要切割的片数
     * @param yPiece y轴需要切割的片数
     * @return 包含 xPiece * yPiece 个子图片的列表
     */
    static List<Bitmap> split(Bitmap bitmap, int xPiece, int yPiece) {

        List<Bitmap> pieces = new ArrayList<>(xPiece * yPiece);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pieceWidth = width / xPiece;
        int pieceHeight = height / yPiece;
        for (int i = 0; i < yPiece; i++) {
            for (int j = 0; j < xPiece; j++) {


                int xValue = j * pieceWidth;
                int yValue = i * pieceHeight;
                Bitmap piece =
                        Bitmap.createBitmap(bitmap, xValue, yValue, pieceWidth, pieceHeight);
                pieces.add(piece);
            }
        }

        return pieces;
    }

}
