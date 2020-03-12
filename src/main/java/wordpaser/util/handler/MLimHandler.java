package wordpaser.util.handler;

import org.w3c.dom.Node;
import wordpaser.entry.math.M_Lim;

public class MLimHandler extends AbstractHandler<M_Lim> {
    public Object hand(Node root, Node current) {
        return new M_Lim();
    }

    public String tagName() {
        return "m:lim";
    }

    public Class tagClass() {
        return M_Lim.class;
    }

}
