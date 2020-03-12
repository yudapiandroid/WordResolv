package wordpaser.entry.math;


import java.util.List;

// 标记语句块
public class Block {

    private List<Object> childs;

    public List<Object> getChilds() {
        return childs;
    }

    public void setChilds(List<Object> childs) {
        this.childs = childs;
    }
}
