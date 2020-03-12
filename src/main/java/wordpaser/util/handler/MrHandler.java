package wordpaser.util.handler;

import org.w3c.dom.Node;
import wordpaser.entry.math.M_R;
import wordpaser.util.U;



// 处理文本
public class MrHandler extends AbstractHandler<M_R> {

    public Object hand(Node root, Node current) {
        Node node = U.parseChildNodeByNodeName(current, "m:t");
        M_R mr = new M_R();
        mr.setText(node.getTextContent());
        return mr;
    }

    public String tagName() {
        return "m:r";
    }

    public Class tagClass() {
        return M_R.class;
    }

    @Override
    public String toLatexTag(M_R obj) {
        return obj.getText();
    }
}
