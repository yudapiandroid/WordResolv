package wordpaser.util;

import com.alibaba.fastjson.JSON;
import wordpaser.entry.*;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import wordpaser.entry.math.Rect;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class U {


    public static void guestQuestionType(List<Question> qs, List<Answer> as) {
        for (Question q : qs) {
            Answer answer = loadAnswerByIndex(as, q.getIndex());
            if(answer != null){
                handPureAnswer(answer);
                if(q.getChoses() != null && q.getChoses().size() > 0){
                    // 选择
                    if(answer.getPureAnswer() != null) {
                        q.setType(answer.getPureAnswer().length() > 1 ? Question.TYE_MUTIL : Question.TYPE_SINGLE);
                    }
                }else{
                    // 判断
                    if(answer.getPureAnswer() != null
                            && answer.getPureAnswer().equals("A")
                            || answer.getPureAnswer().equals("B")) {
                        q.setType(Question.TYPE_JUM);
                    }
                }
            }
        }
    }



    private static void handPureAnswer(Answer answer) {
        String result = "";
        for (int i=0;i < answer.getAnswer().length();i++) {
            char c = answer.getAnswer().charAt(i);
            if(c >= 'A' && c <= 'Z'){
                result += c;
            }
        }
        answer.setPureAnswer(result);
    }


    public static String genJson(List<Question> qs, List<Answer> as) {
        guestQuestionType(qs, as);
        for (Question q : qs) {
            int index = q.getIndex();
            String qc = "";
            int tempIndex = 0;
            Answer answer = loadAnswerByIndex(as, q.getIndex());
            q.setAnswer(answer);
        }
        return JSON.toJSONString(qs);
    }

    public static String genViewHtml(List<Question> qs, List<Answer> as, String key) throws IOException {
        String Q_CHOSE_ITEM = new String(loadResFromClassPath("static/component/q_chose_item.html"));
        String Q_ITEM = new String(loadResFromClassPath("static/component/q_item.html"));
        String TEMPL = new String(loadResFromClassPath("static/paper_view.html"));
        String T_INFO = new String(loadResFromClassPath("static/component/t_info.html"));
        String T_INFO_ITEM = new String(loadResFromClassPath("static/component/t_info_item.html"));

        guestQuestionType(qs, as);
        // 没有匹配到解析
        List<Integer> noAnswer = new ArrayList<Integer>();
        // 选项存在空的情况
        List<Integer> choseEmpty = new ArrayList<Integer>();
        // 索引重复
        List<Integer> indexRepeat = new ArrayList<Integer>();
        Set<Integer> indexSet = new HashSet<Integer>();

        // 选项可能存在问题
        List<Integer> chosePro = new ArrayList<Integer>();


        String qHtml = "";
        int index = 1;
        for (Question q : qs) {

            boolean repeatFlag = false;
            boolean emptyFlag = false;
            boolean noAnswerFlag = false;

            index = q.getIndex();
            if(indexSet.contains(index)){
                repeatFlag = true;
                indexRepeat.add(index);
            } else {
                indexSet.add(index);
            }
            String qc = "";
            int tempIndex = 0;
            if(q.getChoses() != null && q.getChoses().size() % 2 == 1 && q.getChoses().size() < 5){
                chosePro.add(q.getIndex());
            }
            for (String c : q.getChoses()) {
                c = c.trim();
                if(c == null || c.length() == 0){
                    choseEmpty.add(q.getIndex());
                    emptyFlag = true;
                }
                char cc = (char)(tempIndex + 'A');
                qc += Q_CHOSE_ITEM.replaceAll("::chose", c).replaceAll("::c_number",  cc + "");
                tempIndex++;
            }

            Answer answer = loadAnswerByIndex(as, q.getIndex());
            if(answer == null){
                noAnswerFlag = true;
                noAnswer.add(q.getIndex());
            }
            String cssClass = repeatFlag ? "item_status_index"
                    :  emptyFlag ? "item_status_answer_empty"
                    :  noAnswerFlag ? "item_status_answer_no" : "";
            qHtml += Q_ITEM.replaceAll("::chose", qc)
                    .replaceAll("::title", q.getTitle())
                    .replaceAll("::answer_title", answer == null ? " " : answer.getAnswer())
                    .replaceAll("::answer_content", answer == null ? " " : answer.getContent())
                    .replaceAll("::status_class", cssClass)
                    .replaceAll("::number", index + ".");
        }

        String resultInfo = "";
        if(noAnswer.size() > 0){
            resultInfo += "没有匹配到解析的题目: " + genIndexInfo(noAnswer) + "<br>";
        }
        if(choseEmpty.size() > 0){
            resultInfo += "选项存在空的题目: " + genIndexInfo(choseEmpty) + "<br>";
        }

        if(indexRepeat.size() > 0){
            resultInfo += "题目编号重复: " + genIndexInfo(indexRepeat) + "<br>";
        }

        if(chosePro.size() > 0){
            resultInfo += "选项可能存在问题的题目: " + genIndexInfo(chosePro) + "<br>";
        }

        String noTypeQuestionHtml = "";
        String jumTypeQuestionHtml = "";
        String singleTypeQuestionHtml = "";
        String mutilTypeQuestionHtml = "";
        for (Question q : qs) {
            if(q.getType() == Question.TYPE_JUM){
                // panduan
                jumTypeQuestionHtml += T_INFO_ITEM.replaceAll("::number", q.getIndex() + "");
            }else if(q.getType() == Question.TYPE_SINGLE) {
                // 单选
                singleTypeQuestionHtml += T_INFO_ITEM.replaceAll("::number", q.getIndex() + "");
            } else if(q.getType() == Question.TYE_MUTIL){
                // 多选
                mutilTypeQuestionHtml += T_INFO_ITEM.replaceAll("::number", q.getIndex() + "");
            } else {
                // 没有类型
                noTypeQuestionHtml += T_INFO_ITEM.replaceAll("::number", q.getIndex() + "");
            }
        }

        String questionTypeHtml = T_INFO.replaceAll("::title", "判断题")
                .replaceAll("::items", jumTypeQuestionHtml);
        questionTypeHtml += T_INFO.replaceAll("::title", "单选题")
                .replaceAll("::items", singleTypeQuestionHtml);
        questionTypeHtml += T_INFO.replaceAll("::title", "多选题")
                .replaceAll("::items", mutilTypeQuestionHtml);
        questionTypeHtml += T_INFO.replaceAll("::title", "未知类型")
                .replaceAll("::items", noTypeQuestionHtml);

        // 避免内存泄漏
        idToFile.clear();
        return TEMPL.replaceAll("::questions", qHtml)
                .replaceAll("::key", key)
                .replaceAll("::total_number", String.valueOf(qs.size()))
                .replaceAll("::total_info_", questionTypeHtml)
                .replaceAll("::result_info", resultInfo);
    }


    private static String genIndexInfo(List<Integer> indexs) {
        String result = "";
        for (Integer index : indexs) {
            result += index + ", ";
        }
        if(result.endsWith(", ")){
            return result.substring(0, result.length() - 2);
        } else {
            return result;
        }
    }


    private static Answer loadAnswerByIndex(List<Answer> answers, int index) {
        for (Answer a : answers) {
            if(a.getIndex() == index){
                return a;
            }
        }
        return null;
    }




    public static byte[] loadResFromClassPath(String path) throws IOException {
        URL url = U.class.getClassLoader().getResource(path);
        InputStream in = url.openStream();
        byte[] data = loadFromStream(in);
        in.close();
        return data;
    }



    private static final int ANSWER_TITLE = 0x1100;
    private static final int ANSWER_CONTENT = 0x1200;

    // 解析答案
    public static List<Answer> parseAnswerByNodes(List<N> nodes) {
        List<Answer> answers = new ArrayList<Answer>();
        int step = -1;
        String title = "";
        List<String> content = new ArrayList<String>();
        for (int i = 0;i < nodes.size(); i++) {
            String msg = genPNodeToString(nodes.get(i),false);
            msg = msg.trim();
            if(isAnswerTitle(msg)){
                if(title.length() > 0){
                    Answer a = new Answer();
                    a.setAnswer(filterStringFirstNumber(title));
                    a.setContent(genHtmlByStrs(content));
                    a.setIndex(parseFirstNumberFromStr(title));
                    answers.add(a);
                    title = "";
                    content = new ArrayList<String>();
                }
                step = ANSWER_TITLE;
            }
            if(isAnswerContent(msg)){
                step = ANSWER_CONTENT;
            }
            if(step == ANSWER_TITLE){
                title += msg;
            }
            if(step == ANSWER_CONTENT){
                content.add(msg);
            }
        }
        return answers;
    }

    private static boolean isAnswerContent(String msg) {
        return msg.indexOf("解析") >= 0;
    }

    private static boolean isAnswerTitle(String msg) {
        return msg.indexOf("答案") >= 0 && parseFirstNumberFromStr(msg) >= 0;
    }


    private static int parseFirstNumberFromStr(String msg) {
        msg = msg.trim();
        String numberStr = "";
        for (int i=0;i<msg.length();i++) {
            char c = msg.charAt(i);
            if(c >= '0' && c <= '9'){
                numberStr += c;
            } else {
                break;
            }
        }
        return numberStr.length() > 0 ? Integer.parseInt(numberStr) : -1;
    }


    private static final int PARSE_TITLE = 0x0011;
    private static final int PARSE_CHOSE = 0x0012;

    public static List<Question> parseQuestionByNodes(List<N> nodes) {
        List<Question> qs = new ArrayList<Question>();
        int step = -1;
        int questionIndex = -1;
        List<String> title = new ArrayList<String>();
        String chose = "";
        for (int i = 0;i < nodes.size(); i++) {
            String current = genPNodeToString(nodes.get(i), true);
            if(isTitle(current)){
                if(title.size() > 0) {
                    Question q = new Question();
                    q.setTitle(genHtmlByStrs(title));
                    q.setChoses(handChoseByString(chose));
                    q.setIndex(questionIndex);
                    qs.add(q);
                    title = new ArrayList<String>();
                    chose = "";
                }
                step = PARSE_TITLE;
                questionIndex = parseFirstNumberFromStr(current);
            }
            if(isChoser(current)){
                step = PARSE_CHOSE;
            }
            if(PARSE_TITLE == step){
                title.add(current);
            } else if(step == PARSE_CHOSE) {
                chose += current;
            }
        }

        // 处理标题和选项
        int index = 1;
        for (Question q : qs) {
            handQuestionTitle(q, index);
            handQuestionChose(q, index);
            index++;
        }

        return qs;
    }


    private static String genHtmlByStrs(List<String> ss) {
        String result = "";
        for (String s : ss) {
            result += "<div>"+s+"</div>";
        }
        return handTabelHtml(result);
    }

    private static String handTabelHtml(String result) {
        Iterator<Map.Entry<String, TableNode>> it = idToTable.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, TableNode> next = it.next();
            String id = next.getKey();
            TableNode tn = next.getValue();
            String thtml = genTableHtmlByNode(tn);
            result = result.replaceAll(id, thtml);
        }
        return result;
    }

    private static String genTableHtmlByNode(TableNode tn) {
        String h = "<table style='margin: 12px 0;' align='center' class='_w_table' border='0' cellspacing='1' cellpadding='0'>";
        for (int i=0;i < tn.getCc().length; i++) {
            String row = "<tr>";
            for (int j = 0;j < tn.getWidths().length; j++) {
                String m = genPNodeToString(tn.getCc()[i][j],false);
                row += "<td align='center' valign='middle'>" + m + "</td>";
            }
            row += "</tr>";
            h += row;
        }
        h += "</table>";
        return h;
    }

    //
    private static void handQuestionChose(Question q, int index) {
        for (int i=0;i<q.getChoses().size(); i++) {
            for (String t : NUMBER_SUFFIX) {
                String choseSuffix = (char)((i) + 'A') + t;
                q.getChoses().set(i, handHookImage(q.getChoses().get(i)));
                if(q.getChoses().get(i).startsWith(choseSuffix)){
                    q.getChoses().set(i, q.getChoses().get(i).substring(choseSuffix.length()));
                }
            }
        }
    }

    private static String filterStringFirstNumber(String msg) {
        int number = parseFirstNumberFromStr(msg);
        if(number > -1) {
            for (String t : NUMBER_SUFFIX) {
                String s = number + t;
                if(msg.startsWith(s)){
                    return msg.substring(s.length());
                }
            }
        }
        return msg;
    }

    private static void handQuestionTitle(Question q, int index) {
       for (String t : NUMBER_SUFFIX) {
           String suffixStr = "<div>"+ q.getIndex() + t;
           if(q.getTitle().startsWith(suffixStr)){
               q.setTitle(handHookImage("<div>" + q.getTitle().substring(suffixStr.length())));
           }
       }
    }

    // a.xxx b.xcc -->  List
    private static List<String> handChoseByString(String chose) {
        List<String> data = new ArrayList<String>();
        int index = 0; // 0 -> a   1 -> b
        for (int i = 0;i < chose.length(); i++) {
            int pos = chose.indexOf((char) ('A' + index), i);
            int posNext = chose.indexOf((char)('A' + (index + 1)), i + 1);
            if(pos >= 0 && posNext > pos){
                index++;
                data.add(chose.substring(pos, posNext));
                i = posNext - 1;
            } else if (pos >= 0 && posNext < 0) {
                index++;
                data.add(chose.substring(pos));
            } else {
                break;
            }
        }
        return data;
    }


    // 判断是否是选项
    public static boolean isChoser(String current) {
        String temp = current.trim();
        boolean flag = false;
        for (String s: NUMBER_SUFFIX) {
            if(temp.contains(s)){
                int index = temp.indexOf(s);
                int cIndex = index - 1;
                if(cIndex >= 0 && cIndex < s.length()){
                    char c = temp.charAt(cIndex);
                    if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')){
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }


    private static final String[] NUMBER_SUFFIX = new String[] {
            ",", "." , "," , "，" , "。","．"
    };

    private static boolean isTitle(String txt) {
        String temp = txt.trim();
        boolean flag = false;
        for (String s : NUMBER_SUFFIX) {
            if(temp.contains(s)){
                boolean b = isNumber(temp.substring(0, temp.indexOf(s)));
                if(b){
                    flag = b;
                    break;
                }
            }
        }
        return flag;
    }


    private static boolean isNumber(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (Exception e) {
            return false;
        }

    }


    private static ConcurrentHashMap<String, String> idToFile = new ConcurrentHashMap();
    private static ConcurrentHashMap<String, TableNode> idToTable = new ConcurrentHashMap<String, TableNode>();

    private static String handHookImage(String content) {
        Iterator<Map.Entry<String, String>> it = idToFile.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> next = it.next();
            String id = next.getKey();
            String dom = next.getValue();
            content = content.replaceAll(id, dom);
        }
        return content;
    }

    private static String genPNodeToString(N n, boolean hookImage) {
        if(n instanceof PNode){
            PNode node = (PNode) n;
            StringBuffer sb = new StringBuffer();
            for (ContentNode cn : node.getNodes()) {
                if(cn instanceof TextNode){
                    TextNode tn = (TextNode) cn;
                    sb.append(tn.getText());
                }
                if(cn instanceof ImageNode){
                    ImageNode in = (ImageNode) cn;
                    String imageDom = "<img style='width:"+in.getWidth()+"px;height:"+in.getHeight()+"px' " +
                            "src='data:image/png;base64,"+in.getImageBase64()+"'/>";
                    if(hookImage){
                        String id = genID();
                        id = "---->" + id + "<-----";
                        sb.append(id);
                        idToFile.put(id, imageDom);
                    } else {
                        sb.append(imageDom);
                    }
                }
            }
            return sb.toString();
        } else if (n instanceof TableNode) {
            String id = genID();
            id = "---> table " + id + "<---";
            idToTable.put(id, (TableNode) n);
            return id;
        } else {
            return "";
        }
    }


    private static String genID() {
        return String.valueOf(System.currentTimeMillis() + new Random().nextInt(10000));
    }


    public static void writeToFile(String cotent, String file) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        out.write(cotent.getBytes());
        out.flush();
        out.close();
    }



    public static byte[] loadFromStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[10 * 1024];
        int length = 0;
        while ((length = in.read(buf)) != -1) {
            out.write(buf, 0, length);
        }
        return out.toByteArray();
    }

    public static byte[] loadFile(String file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[10 * 1024];
        int length = 0;
        while ((length = in.read(buf)) != -1) {
            out.write(buf, 0, length);
        }
        in.close();
        out.close();
        return out.toByteArray();
    }


    public static byte[] parseDocXML(byte[] content) throws IOException {
        ZipInputStream zipIn = new ZipInputStream(new ByteArrayInputStream(content));
        ZipEntry entry = zipIn.getNextEntry();
        while (entry != null) {
            System.out.println(entry.getName());
            entry = zipIn.getNextEntry();
        }
        return null;
    }


    public static Doc parseDoc(byte[] content) throws IOException {
        ZipInputStream zipIn = new ZipInputStream(new ByteArrayInputStream(content));

        Doc doc = new Doc();
        Map<String, byte[]> files = new HashMap<String, byte[]>();

        ZipEntry entry = zipIn.getNextEntry();
        while (entry != null) {

            String name = entry.getName();
            if(name.equals("word/_rels/document.xml.rels")){
                // 资源
                doc.setResXML(loadFromStream(zipIn));
            }

            if(name.equals("word/document.xml")){
                // 主体
                doc.setWordXML(loadFromStream(zipIn));
            }

            if(name.startsWith("word/media")){
                // 文件
                files.put(name, loadFromStream(zipIn));
            }
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
        doc.setFiles(files);
        return doc;
    }



    public static List<N> parsePNodeByDoc(Doc docE) throws ParserConfigurationException, IOException, SAXException {
        List<N> nodes = new ArrayList<N>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(docE.getWordXML()));
        NodeList nl = doc.getElementsByTagName("w:body");
        if(nl.getLength() > 0){
            NodeList wp = nl.item(0).getChildNodes();
            for (int i = 0;i < wp.getLength();i++) {
                // parsePNode(wp.item(i), temp);
                N n = parseNode(wp.item(i));
                if(n != null){
                    nodes.add(n);
                }
            }
        }

        // 解析资源
        Document resDoc = builder.parse(new ByteArrayInputStream(docE.getResXML()));
        NodeList rss = resDoc.getElementsByTagName("Relationship");
        Map<String, String> resMap = new HashMap<String, String>();
        for (int i = 0;i < rss.getLength(); i++) {
            Node n = rss.item(i);
            resMap.put(
                    n.getAttributes().getNamedItem("Id").getTextContent(),
                    n.getAttributes().getNamedItem("Target").getTextContent());
        }
        // 处理资源
        for (N n : nodes) {
            if(n instanceof PNode){
                PNode pn = (PNode) n;
                loadImage(pn, resMap, docE);
            } else if (n instanceof TableNode) {
                TableNode tn = (TableNode) n;
                for (int i = 0;i < tn.getCc().length; i++) {
                    for (int j = 0;j < tn.getWidths().length; j++) {
                        loadImage(tn.getCc()[i][j], resMap,docE);
                    }
                }
            }

        }
        // 处理完成
        return nodes;
    }


    private static void loadImage(PNode pn, Map<String, String> resMap, Doc docE) {
        if(pn.getNodes() != null){
            for (ContentNode cn : pn.getNodes()) {
                if(cn instanceof ImageNode){
                    ImageNode in = (ImageNode) cn;
                    String id = in.getImgId();
                    if(resMap.containsKey(id) && in.getImageBase64() == null){
                        String image = resMap.get(id);
                        image = "word/" + image;
                        in.setImageBase64(new String(Base64.encodeBase64(docE.getFiles().get(image))));
                    }
                }
            }
        }
    }

    private static N parseNode(Node item) {
        String name = item.getNodeName();
        if(name.equals("w:p")){
            PNode pn = new PNode();
            parsePNode(item, pn);
            return pn;
        } else if (name.equals("w:tbl")) {
            return parseTableNode(item);
        }
        return null;
    }

    private static N parseTableNode(Node item) {
        TableNode tn = new TableNode();
        Node gridInfo = parseChildNodeByNodeName(item, "w:tblGrid");
        List<Node> trNodes = parseChildNodesByNodeNameAndLoop(item, "w:tr");
        PNode[][] cc = new PNode[trNodes.size()][gridInfo.getChildNodes().getLength()];
        int[] widths = new int[gridInfo.getChildNodes().getLength()];
        for (int i = 0;i < gridInfo.getChildNodes().getLength(); i++) {
            Node temp = gridInfo.getChildNodes().item(i);
            widths[i] = Integer.valueOf(temp.getAttributes().getNamedItem("w:w").getTextContent()) / 9525;
        }
        for (int i = 0;i < trNodes.size(); i++ ) {
            for (int j = 0; j < gridInfo.getChildNodes().getLength();j++) {
                Node tcNode = parseChildNodesByNodeNameAndLoop(trNodes.get(i), "w:tc").get(j);
                Node pNode = parseChildNodeByNodeName(tcNode, "w:p");
                PNode pn = new PNode();
                parsePNode(pNode, pn);
                cc[i][j] = pn;
            }
        }
        tn.setCc(cc);
        tn.setWidths(widths);
        return tn;
    }


    private static void parsePNode(Node wpNode, PNode temp) {
        List<Node> allc = new ArrayList<Node>();
        parseAllChildNode(allc, wpNode);
        for (int i=0;i< allc.size(); i++) {
            Node item = allc.get(i);
            if(item.getNodeName().equals("w:r")){
                // 处理
                Node node = parseChildNodeByNodeName(item, "w:t");
                if(node != null){
                    // 文本
                    temp.addTextNode(node.getTextContent());
                }
                node = parseChildNodeByNodeName(item, "w:drawing");
                if(node != null){
                    // image
                    Node tn = parseChildNodeByNodeNameAndLoop(node, "a:blip");
                    Node exN = parseChildNodeByNodeNameAndLoop(node, "wp:extent");
                    temp.addImageNode(
                            tn.getAttributes().getNamedItem("r:embed").getTextContent(),
                            Integer.valueOf(exN.getAttributes().getNamedItem("cx").getTextContent()) / 9525,
                            Integer.valueOf(exN.getAttributes().getNamedItem("cy").getTextContent()) / 9525
                            );
                }
            }

            // 数学公式
            if(item.getNodeName().equals("m:oMath")){
                try {
                    String latexStr = MathNodeToImage.mathNodeConvertLatexString(item);
                    Rect r = new Rect();
                    String image = MathNodeToImage.latexToImageBase64(latexStr, r);
                    temp.addImageNodeBase64(image, r.getWidth(), r.getHeight());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static void parseAllChildNode(List<Node> temp, Node parent) {
        temp.add(parent);
        for (int i=0;i < parent.getChildNodes().getLength(); i++) {
            parseAllChildNode(temp, parent.getChildNodes().item(i));
        }
    }

    public static Node parseChildNodeByNodeNameAndLoop(Node n, String name) {
        List<Node> temp = parseChildNodesByNodeNameAndLoop(n, name);
        return temp.size() > 0 ? temp.get(0) : null;
    }


    public static List<Node> parseChildNodesByNodeNameAndLoop(Node n, String name) {
        List<Node> temp = new ArrayList<Node>();
        List<Node> allNode = new ArrayList<Node>();
        parseAllChildNode(allNode, n);
        for (Node tn : allNode) {
            if(tn.getNodeName().equals(name)) {
                temp.add(tn);
            }
        }
        return temp;
    }

    public static Node parseChildNodeByNodeName(Node n, String name) {
        for (int i=0;i<n.getChildNodes().getLength(); i++) {
            Node temp = n.getChildNodes().item(i);
            if(name.equals(temp.getNodeName())) {
                return temp;
            }
        }
        return null;
    }

}
