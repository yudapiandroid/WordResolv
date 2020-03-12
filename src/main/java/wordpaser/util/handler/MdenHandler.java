package wordpaser.util.handler;

import org.w3c.dom.Node;
import wordpaser.entry.math.M_Den;

import java.util.List;


// 出发
public class MdenHandler extends AbstractHandler<M_Den> {


    public Object hand(Node root, Node current) {
        return new M_Den();
    }

    public String tagName() {
        return "m:den";
    }

    public Class tagClass() {
        return M_Den.class;
    }

    @Override
    public void handBlock(List<Object> infos, Object obj) {
        super.handBlock(infos, obj);
    }

    @Override
    public String toLatexBfore(M_Den obj) {
        if(obj.isNoBar()){
            return "_{";
        } else {
            return "{";
        }
    }

    @Override
    public String toLatexAfter(M_Den obj) {
        return "}";
    }
}
