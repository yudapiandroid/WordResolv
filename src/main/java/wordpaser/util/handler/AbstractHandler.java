package wordpaser.util.handler;

import wordpaser.entry.math.Block;
import java.util.List;

public abstract class AbstractHandler<T> implements BaseHandler<T> {


    public void parseFinish(T obj) {

    }

    public void handBlock(List<Object> infos, Object obj) {
        if(obj instanceof Block){
            Block b = (Block) obj;
            b.setChilds(infos);
        }
    }

    public String toLatexBfore(T obj) {
        return "";
    }

    public String toLatexTag(T obj) {
        return "";
    }

    public String toLatexAfter(T obj) {
        return "";
    }

}
