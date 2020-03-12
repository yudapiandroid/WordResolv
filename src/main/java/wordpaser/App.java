package wordpaser;

import org.w3c.dom.Document;
import wordpaser.entry.*;
import wordpaser.util.MathNodeToImage;
import wordpaser.util.U;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class App {

    private static List<PNode> nodes = new ArrayList<PNode>();


//    public static void main__(String[] args) {
//        SpringApplication app = new SpringApplication(App.class);
//        app.run(args);
//    }


    public static void main2(String[] args) throws IOException {
        String latex = "$x^2_1$f(x)^{\\frac{x+1}{y+z}}\\sum_{k-1}^nf(x)\\int_a^b\\sqrt{x+2+ab+c^2}";

    }


    public static void main3(String[] args) throws IOException, ParserConfigurationException, SAXException {
        Doc doc = U.parseDoc(U.loadFile("C:\\Users\\dapi\\Desktop\\各种题型的文档\\test.docx"));
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document d = builder.parse(new ByteArrayInputStream(doc.getWordXML()));
        NodeList nl = d.getElementsByTagName("m:oMathPara");
        for(int i=0;i < nl.getLength();i++) {
            Node item = nl.item(i);
            String s = MathNodeToImage.mathNodeConvertLatexString(item);
            MathNodeToImage.latexToImage(s,String.valueOf(i));
        }
    }


    public static void main1(String[] args) throws IOException {
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(new Color(246, 96, 0));
        g.drawString("TEST", 100, 100);
        ImageIO.write(image, "PNG", new File("C:\\Users\\dapi\\Desktop\\demo.png"));
        g.dispose();
    }


    public static void main12(String[] args) throws IOException, ParserConfigurationException, SAXException {

        Doc doc = U.parseDoc(U.loadFile("C:\\Users\\dapi\\Desktop\\各种题型的文档\\事业单位综合岗题本- 有判断题.docx"));
        List<N> pNodes = U.parsePNodeByDoc(doc);

        Doc anDoc = U.parseDoc(U.loadFile("C:\\Users\\dapi\\Desktop\\各种题型的文档\\事业单位综合岗解析- 有判断题.docx"));
        List<N> anNodes = U.parsePNodeByDoc(anDoc);
        List<Answer> answers = U.parseAnswerByNodes(anNodes);
        List<Question> qs = U.parseQuestionByNodes(pNodes);

        //U.writeToFile(U.genViewHtml(qs,answers),"C:\\Users\\dapi\\Desktop\\demo.html");
    }


    private static void printQuestions(List<Question> qs) {
        for (Question q : qs) {
            System.out.println(q);
        }
    }


    private static void printPNodes(List<PNode> nodes) {
        for (int i=0;i < nodes.size();i++) {
            PNode pNode = nodes.get(i);
            printPNode(pNode);
        }
    }

    private static void printPNode(PNode pNode) {
        for (int i=0;i<pNode.getNodes().size(); i++) {
            ContentNode node = pNode.getNodes().get(i);
            if(node instanceof TextNode){
                TextNode textNode = (TextNode) node;
                System.out.printf(textNode.getText().replaceAll("%", "  "));
            }

            if(node instanceof ImageNode){
                ImageNode imageNode = (ImageNode) node;
                System.out.println("[image] " + imageNode.getImgId() + "  " + imageNode.getImageBase64());
            }
        }
        System.out.println("");
    }


    public static void parseNode(NodeList ns, int suffix) {
        for (int i=0;i<ns.getLength();i++) {
            Node item = ns.item(i);
            for (int j=0;j < suffix;j++){
                System.out.printf(" ");
            }
            if(item.getNodeName().equals("w:t")){
                String t = item.getTextContent();
                //System.out.println("[w:t] " + t);
                System.out.println(t);
            } else {
                // System.out.println(item.getNodeName());
            }

            NodeList cn = item.getChildNodes();
            if(cn != null && cn.getLength() > 0){
                parseNode(cn, suffix + 2);
            }
        }
    }

}
