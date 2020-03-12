package wordpaser.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import wordpaser.entry.Answer;
import wordpaser.entry.Doc;
import wordpaser.entry.N;
import wordpaser.entry.Question;
import wordpaser.util.CacheManager;
import wordpaser.util.IO;
import wordpaser.util.U;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/wp/api")
public class ApiController {


    @RequestMapping("/formpage")
    public void formPage(HttpServletResponse res) throws IOException {
        byte[] html = U.loadResFromClassPath("static/uploadFile.html");
        IO.sendHTML(res, new String(html, "utf-8"));
    }



    @RequestMapping("/parsepaper")
    public void parsePaper(
            HttpServletResponse response,
            @RequestParam("questions")MultipartFile qs,
            @RequestParam("answers") MultipartFile as) throws IOException,
            ParserConfigurationException, SAXException {
        if(qs.isEmpty() || as.isEmpty()){
            IO.sendError(response, "请上传文件，题目解析一个不能少~");
            return;
        }
        byte[] questions = qs.getBytes();
        byte[] answers = as.getBytes();

        Doc docQ = U.parseDoc(questions);
        Doc docA = U.parseDoc(answers);

        if(!validateDoc(docQ) || !validateDoc(docA)) {
            IO.sendError(response, "文件格式解析失败，仅仅支持 .docx 格式的文件.");
        } else {
            List<N> nodesQ = U.parsePNodeByDoc(docQ);
            List<N> nodesA = U.parsePNodeByDoc(docA);

            List<Question> qList = U.parseQuestionByNodes(nodesQ);
            List<Answer> aList = U.parseAnswerByNodes(nodesA);

            Map<String, Object> paper = new HashMap<String, Object>();
            paper.put("Q",qList);
            paper.put("A",aList);
            String key = System.currentTimeMillis() + "";
            CacheManager.putCache(key, paper);
            response.sendRedirect("/wp/api/show/" + key);
        }
    }


    boolean validateDoc(Doc doc) {
        return doc.getWordXML() != null && doc.getWordXML().length > 0;
    }


    @RequestMapping("/export/{key}")
    public void export(@PathVariable("key") String key,HttpServletResponse response) throws IOException {
        if(CacheManager.getCache(key) == null){
            IO.send404(response);
        } else {
            Map<String, Object> paper = CacheManager.getCache(key);
            List<Question> qList = (List<Question>) paper.get("Q");
            List<Answer> aList = (List<Answer>) paper.get("A");
            String s = U.genJson(qList, aList);
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=paper.json");
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            ServletOutputStream out = response.getOutputStream();
            out.write(s.getBytes("utf-8"));
            out.flush();
        }
    }

    @RequestMapping("/show/{key}")
    public void parsePaper(@PathVariable("key") String key,HttpServletResponse response) throws IOException{
        if(CacheManager.getCache(key) == null) {
            IO.send404(response);
        } else {
            Map<String, Object> paper = CacheManager.getCache(key);
            List<Question> qList = (List<Question>) paper.get("Q");
            List<Answer> aList = (List<Answer>) paper.get("A");
            String s = U.genViewHtml(qList, aList, key);
            IO.sendHTML(response, s);
        }
    }

}
