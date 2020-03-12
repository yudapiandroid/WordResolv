package wordpaser.util.handler;


import org.w3c.dom.Node;
import wordpaser.entry.math.M_Deg;
import wordpaser.entry.math.M_Rad;

// 根号
public class MradHandler extends AbstractHandler<M_Rad> {

    public Object hand(Node root, Node current) {
        return new M_Rad();
    }

    @Override
    public void parseFinish(M_Rad obj) {
        boolean haveDeg = false;
        for (Object o : obj.getChilds()) {
            if(o instanceof M_Deg){
                haveDeg = true;
            }
        }
        obj.setHaveDeg(haveDeg);
    }

    public String tagName() {
        return "m:rad";
    }

    public Class tagClass() {
        return M_Rad.class;
    }

    @Override
    public String toLatexBfore(M_Rad obj) {
        if(obj.isHaveDeg()){
            // 有子数
            return "\\sqrt";
        } else {
            return "\\sqrt{";
        }
    }


    @Override
    public String toLatexAfter(M_Rad obj) {
        return "}";
    }
}
