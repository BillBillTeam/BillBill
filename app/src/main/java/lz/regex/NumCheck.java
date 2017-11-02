package lz.regex;

import android.media.MediaCodec;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检测给定的数字是否符合规则
 * Created by LiZeC on 2017/11/2.
 */

public class NumCheck {
    private static Pattern patternDouble = Pattern.compile("^[1-9][0-9]*([.][0-9]*)?$");
    
    public static boolean matchDouble(String numStr){
        Matcher matcher = patternDouble.matcher(numStr);
        return matcher.find();
    }
}
