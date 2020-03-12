package wordpaser.util.handler;

import org.w3c.dom.Node;
import wordpaser.entry.math.M_Num;

public class MnumHandler extends AbstractHandler<M_Num> {
    public Object hand(Node root, Node current) {
        return new M_Num();
    }

    public String tagName() {
        return "m:num";
    }

    public Class tagClass() {
        return M_Num.class;
    }

    @Override
    public String toLatexBfore(M_Num obj) {
        if(obj.isNoBar()){
            return "^{";
        }
        return "{";
    }

    @Override
    public String toLatexAfter(M_Num obj) {
        return "}";
    }
}
