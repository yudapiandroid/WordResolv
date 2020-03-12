package wordpaser.entry;

import java.util.ArrayList;
import java.util.List;

public class PNode extends N{

    List<ContentNode> nodes = new ArrayList<ContentNode>();

    public List<ContentNode> getNodes() {
        return nodes;
    }


    public void addTextNode(String text) {
        TextNode n = new TextNode();
        n.setText(text);
        nodes.add(n);
    }

    public void addImageNode(String imgId, int width, int height) {
        ImageNode n = new ImageNode();
        n.setImgId(imgId);
        n.setWidth(width);
        n.setHeight(height);
        nodes.add(n);
    }

    public void addImageNodeBase64(String content, int width, int height) {
        ImageNode n = new ImageNode();
        n.setImageBase64(content);
        n.setWidth(width);
        n.setHeight(height);
        nodes.add(n);
    }


}
