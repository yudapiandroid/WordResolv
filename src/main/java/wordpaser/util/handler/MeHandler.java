package wordpaser.util.handler;

import org.w3c.dom.Node;
import wordpaser.entry.math.M_E;

public class MeHandler extends AbstractHandler<M_E> {

    public Object hand(Node root, Node current) {
        return new M_E();
    }

    public String tagName() {
        return "m:e";
    }

    public Class tagClass() {
        return M_E.class;
    }

    @Override
    public String toLatexBfore(M_E obj) {
        return "{";
    }

    @Override
    public String toLatexAfter(M_E obj) {
        return "}";
    }
}
