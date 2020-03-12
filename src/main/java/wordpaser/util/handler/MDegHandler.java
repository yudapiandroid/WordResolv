package wordpaser.util.handler;

import org.w3c.dom.Node;
import wordpaser.entry.math.M_Deg;

public class MDegHandler extends AbstractHandler<M_Deg> {
    public Object hand(Node root, Node current) {
        return new M_Deg();
    }

    public String tagName() {
        return "m:deg";
    }

    public Class tagClass() {
        return M_Deg.class;
    }

    @Override
    public String toLatexBfore(M_Deg obj) {
        return "[";
    }

    @Override
    public String toLatexAfter(M_Deg obj) {
        return "]{";
    }
}


