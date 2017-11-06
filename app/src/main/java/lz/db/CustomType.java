package lz.db;

/**
 * Created by LiZec on 2017/10/22.
 * 表示一个系统提供的或者用户自定义的类型
 */

public class CustomType {
    private String type;
    private int index;
    private int res_ID;

    public CustomType(String type, int index) {
        setType(type);
        setIndex(index);
        setRes_ID(-1);
    }

    public CustomType(String type,int index,int res_ID){
        setType(type);
        setIndex(index);
        setRes_ID(res_ID);
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

    public int getRes_ID() {
        return res_ID;
    }

    public void setRes_ID(int res_ID) {
        this.res_ID = res_ID;
    }
}
