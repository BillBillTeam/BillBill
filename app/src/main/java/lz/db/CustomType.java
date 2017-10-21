package lz.db;

/**
 * Created by LiZec on 2017/10/22.
 * 表示一个系统提供的或者用户自定义的类型
 */

public class CustomType {
    private String type;
    private int index;

    public CustomType(String type, int index) {
        this.type = type;
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
