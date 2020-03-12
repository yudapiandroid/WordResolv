package wordpaser.util.handler;

import org.w3c.dom.Node;
import wordpaser.entry.math.M_D;

public class MDHandler extends AbstractHandler<M_D> {


    public Object hand(Node root, Node current) {
        return new M_D();
    }

    public String tagName() {
        return "m:d";
    }

    public Class tagClass() {
        return M_D.class;
    }

    @Override
    public String toLatexBfore(M_D obj) {
        return "(";
    }

    @Override
    public String toLatexAfter(M_D obj) {
        return ")";
    }
}
