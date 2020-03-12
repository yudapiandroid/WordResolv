package wordpaser.util.handler;

import org.w3c.dom.Node;
import wordpaser.entry.math.M_Sub;

public class MSubHandler extends AbstractHandler<M_Sub> {
    public Object hand(Node root, Node current) {
        return new M_Sub();
    }

    public String tagName() {
        return "m:sub";
    }

    public Class tagClass() {
        return M_Sub.class;
    }

    @Override
    public String toLatexTag(M_Sub obj) {
        return "_{";
    }

    @Override
    public String toLatexAfter(M_Sub obj) {
        return "}";
    }
}
