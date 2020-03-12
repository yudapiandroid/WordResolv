package wordpaser.entry.math;

import java.util.List;

public class DrawInfo {

    private int x;
    private int y;

    private int width;
    private int height;


    private boolean isSmall;

    // 绘制目标对象
    private Object target;


    public boolean isSmall() {
        return isSmall;
    }

    public void setSmall(boolean small) {
        isSmall = small;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public DrawInfo() {
    }

    public DrawInfo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public DrawInfo(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    private List<DrawInfo> childs;

    public List<DrawInfo> getChilds() {
        return childs;
    }

    public void setChilds(List<DrawInfo> childs) {
        this.childs = childs;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
