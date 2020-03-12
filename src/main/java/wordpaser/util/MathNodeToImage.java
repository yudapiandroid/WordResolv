package wordpaser.util;


import org.apache.commons.codec.binary.Base64;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import org.w3c.dom.Node;
import wordpaser.entry.math.*;
import wordpaser.util.handler.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

// 数学公式转图片
public class MathNodeToImage {

    static List<BaseHandler> handlers = new ArrayList<BaseHandler>();

    static {
        handlers.add(new MrHandler());
        handlers.add(new MsupHandler());
        handlers.add(new MradHandler());
        handlers.add(new MdenHandler());
        handlers.add(new MfHandler());
        handlers.add(new MnumHandler());
        handlers.add(new MeHandler());
        handlers.add(new MDHandler());
        handlers.add(new MNaryHandler());
        handlers.add(new MSubHandler());
        handlers.add(new MDegHandler());
        handlers.add(new MLimHandler());
        handlers.add(new MLimLowHandler());
    }


    static int MATH_ML_SIZE = 18;


    public static String mathNodeConvertLatexString(Node node) {
        List<Object> nodes = new ArrayList<Object>();
        parseNode(node, node, 1, nodes);
        parseNodeFinish(nodes);
        StringBuffer sb = new StringBuffer();
        genLatexString(nodes, sb);
        return sb.toString();
    }


    private static void parseNodeFinish(List<Object> nodes) {
        for (Object o : nodes) {
            BaseHandler h = parseHandler(null, o.getClass());
            if(h != null){
                h.parseFinish(o);
            }
            if(o instanceof Block){
                parseNodeFinish(((Block) o).getChilds());
            }
        }
    }

    private static void genLatexString(List<Object> nodes, StringBuffer latexStr) {
        for (Object o : nodes) {
            BaseHandler h = parseHandler(null, o.getClass());
            if(h != null){
                latexStr.append(h.toLatexBfore(o));
                latexStr.append(h.toLatexTag(o));
                if(o instanceof Block){
                    genLatexString(((Block) o).getChilds(), latexStr);
                }
                latexStr.append(h.toLatexAfter(o));
            }
        }
    }


    public static String latexToImageBase64(String latex, Rect rect) throws IOException {
        TeXFormula formula = new TeXFormula(latex);
        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, MATH_ML_SIZE);
        // insert a border
        icon.setInsets(new Insets(1, 1, 1, 1));
        // now create an actual image of the rendered equation
        rect.setWidth(icon.getIconWidth());
        rect.setHeight(icon.getIconHeight());
        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.white);
        g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
        JLabel jl = new JLabel();
        jl.setForeground(new Color(0, 0, 0));
        icon.paintIcon(jl, g2, 0, 0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", out);
        g2.dispose();
        return new String(Base64.encodeBase64(out.toByteArray()));
    }

    public static void latexToImage(String latex, String fileName) throws IOException {
        TeXFormula formula = new TeXFormula(latex);
        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, MATH_ML_SIZE);
        // insert a border
        icon.setInsets(new Insets(1, 1, 1, 1));
        // now create an actual image of the rendered equation
        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.white);
        g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
        JLabel jl = new JLabel();
        jl.setForeground(new Color(0, 0, 0));
        icon.paintIcon(jl, g2, 0, 0);
        ImageIO.write(image, "PNG", new File("C:\\Users\\dapi\\Desktop\\demo_png\\demo_" + fileName + ".png"));
        g2.dispose();
    }


    public static void mathNodeToImage(Node mathNode) {
        List<Object> infos = new ArrayList<Object>();
        //loadFont();
        parseNode(mathNode,mathNode, 1,infos);
        print(infos,1);
    }

    private static Font NORMAL_FONT = new Font("黑体", Font.PLAIN, 30);
    private static Font SMALL_FONT = new Font("黑体", Font.PLAIN, 15);

    private static void loadFont() {
        InputStream in = MathNodeToImage.class
                .getClassLoader().getResourceAsStream("fonts/GalvestonTX-Regular.ttf");
        try {
            Font temp = Font.createFont(Font.TRUETYPE_FONT, in);
            NORMAL_FONT = temp.deriveFont(50);
            SMALL_FONT = NORMAL_FONT;
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    private static void print(List<Object> infos, int level) {
        for (Object o : infos) {
            for (int i=0;i < level; i++) {
                System.out.print(" ");
            }
            if(o instanceof M_R){
                M_R m = (M_R) o;
                System.out.println(m.getText());
            } else {
                System.out.println(o);
            }
            if(o instanceof Block){
                print(((Block) o).getChilds(), level + 1);
            }
        }
    }


    private static void parseNode(Node root,Node node, int level,List<Object> infos) {
        Object o = handNode(root, node);
        if(o != null) {
            infos.add(o);
        }
        if(o instanceof Block){
            infos = new ArrayList<Object>();
        }
        if(node.getChildNodes().getLength() > 0){
            for (int i = 0;i < node.getChildNodes().getLength(); i++) {
                parseNode(root,node.getChildNodes().item(i), level + 1,infos);
            }
        }
        if(o instanceof Block){
            handBlock(root, node, infos, o);
        }
    }

    private static void handBlock(Node root, Node node, List<Object> infos, Object obj) {
        BaseHandler h = parseHandler(null, obj.getClass());
        if(h != null){
            h.handBlock(infos,obj);
        }
    }

    private static Object handNode(Node root, Node node) {
        String name = node.getNodeName();
        BaseHandler h = parseHandler(name, null);
        if(h != null){
            return h.hand(root, node);
        }
        return null;
    }


    private static BaseHandler parseHandler(String tagName, Class tagClass) {
        for (BaseHandler h : handlers) {
            if(tagName != null && h.tagName().equals(tagName)){
                return h;
            }
            if(tagClass != null && tagClass == h.tagClass()){
                return h;
            }
        }
        return null;
    }

}
