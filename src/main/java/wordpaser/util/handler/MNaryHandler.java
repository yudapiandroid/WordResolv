package wordpaser.util.handler;

import org.w3c.dom.Node;
import wordpaser.entry.math.M_Nary;
import wordpaser.util.U;

public class MNaryHandler extends AbstractHandler<M_Nary> {
    public Object hand(Node root, Node current) {
        M_Nary m_nary = new M_Nary();
        Node node = U.parseChildNodeByNodeName(current, "m:naryPr");
        if(node != null){
            node = U.parseChildNodeByNodeName(node, "m:chr");
            if(node != null){
                node = node.getAttributes().getNamedItem("m:val");
                if(node != null){
                    m_nary.setTag(node.getTextContent());
                }
            }
        }
        return m_nary;
    }

    public String tagName() {
        return "m:nary";
    }

    public Class tagClass() {
        return M_Nary.class;
    }

    @Override
    public String toLatexBfore(M_Nary obj) {
        if("∑".equals(obj.getTag())){
            return "\\sum";
        }
        return "";
    }
}
