package lz.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检测给定的数字是否符合规则
 * Created by LiZeC on 2017/11/2.
 */

public class NumCheck {
    private static Pattern patternDouble = Pattern.compile("^[0-9]*([.][0-9]*)?$");

    /**
     *  检查给定的字符串是否满足浮点数格式
     * @param numStr 需要判断的数字字符串
     * @return 如果满足格式，返回true，否则返回false
     */
    public static boolean matchDouble(String numStr){
        Matcher matcher = patternDouble.matcher(numStr);
        return matcher.find();
    }
}
