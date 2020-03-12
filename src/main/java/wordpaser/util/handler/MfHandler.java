package wordpaser.util.handler;

import org.w3c.dom.Node;
import wordpaser.entry.math.M_Den;
import wordpaser.entry.math.M_F;
import wordpaser.entry.math.M_Num;
import wordpaser.util.U;

import java.util.List;


public class MfHandler extends AbstractHandler<M_F> {

    public Object hand(Node root, Node current) {
        M_F mf = new M_F();
        Node node = U.parseChildNodeByNodeName(current, "m:fPr");
        if(node != null){
            node = U.parseChildNodeByNodeName(node, "m:type");
            if(node != null){
                node = node.getAttributes().getNamedItem("m:val");
                if(node != null){
                    String val = node.getTextContent();
                    if("noBar".equals(val)){
                        // 没有除法的分界线
                        mf.setNoBar(true);
                    }
                }
            }
        }
        return mf;
    }

    @Override
    public void parseFinish(M_F obj) {
        List<Object> cs = obj.getChilds();
        for (Object o : cs) {
            if(o instanceof M_Num){
                // 分子
                ((M_Num) o).setNoBar(obj.isNoBar());
            }
            if(o instanceof M_Den){
                // 分母
                ((M_Den) o).setNoBar(obj.isNoBar());
            }
        }
    }

    public String tagName() {
        return "m:f";
    }

    public Class tagClass() {
        return M_F.class;
    }

    @Override
    public String toLatexBfore(M_F obj) {
        if(obj.isNoBar()){
            return "";
        } else {
            return "{";
        }
    }

    @Override
    public String toLatexTag(M_F obj) {
        if(obj.isNoBar()){
            return "";
        } else {
            return "\\frac";
        }
    }

    @Override
    public String toLatexAfter(M_F obj) {
        if(obj.isNoBar()){
            return "";
        } else {
            return "}";
        }
    }
}
