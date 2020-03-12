package wordpaser.util.handler;

import org.w3c.dom.Node;
import java.util.List;

public interface BaseHandler<T> {

    void parseFinish(T obj);

    Object hand(Node root, Node current);

    String tagName();

    Class tagClass();

    void handBlock(List<Object> infos, Object obj);


    String toLatexBfore(T obj);

    String toLatexTag(T obj);

    String toLatexAfter(T obj);

}
