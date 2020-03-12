package wordpaser.util.handler;

import org.w3c.dom.Node;
import wordpaser.entry.math.M_Sup;

public class MsupHandler extends AbstractHandler<M_Sup> {


    public Object hand(Node root, Node current) {
        return new M_Sup();
    }

    public String tagName() {
        return "m:sup";
    }

    public Class tagClass() {
        return M_Sup.class;
    }

    @Override
    public String toLatexTag(M_Sup obj) {
        return "^{";
    }


    @Override
    public String toLatexAfter(M_Sup obj) {
        return "}";
    }
}
