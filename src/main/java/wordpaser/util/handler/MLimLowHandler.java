package wordpaser.util.handler;

import org.w3c.dom.Node;
import wordpaser.entry.math.M_Lim;
import wordpaser.entry.math.M_LimLow;

import java.util.ArrayList;
import java.util.List;

public class MLimLowHandler extends AbstractHandler<M_LimLow> {
    public Object hand(Node root, Node current) {
        return new M_LimLow();
    }

    @Override
    public void parseFinish(M_LimLow obj) {
        Object temp = null;
        for (Object o : obj.getChilds()) {
            if(o instanceof M_Lim){
                temp = o;
            }
        }
        List<Object> rs = new ArrayList<Object>();
        if(temp != null){
            rs.add(temp);
        }
        obj.setChilds(rs);
    }

    public String tagName() {
        return "m:limLow";
    }

    public Class tagClass() {
        return M_LimLow.class;
    }

    @Override
    public String toLatexTag(M_LimLow obj) {
        return "\\lim_{";
    }

    @Override
    public String toLatexAfter(M_LimLow obj) {
        return "}";
    }
}
