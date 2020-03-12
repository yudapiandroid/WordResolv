package wordpaser.util;


import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class IO {


    public static void writeToFile(String filePath, byte[] content) throws IOException {
        FileOutputStream out = new FileOutputStream(filePath);
        out.write(content);
        out.flush();
        out.close();
    }

    public static byte[] readFileFromStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024 * 10];
        int length;
        while ((length = in.read(buf)) != -1) {
            out.write(buf, 0, length);
        }
        return out.toByteArray();
    }

    public static byte[] readFile(String path) throws IOException {
        File f = new File(path);
        FileInputStream in = new FileInputStream(f);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024 * 10];
        int length;
        while ((length = in.read(buf)) != -1) {
            out.write(buf, 0, length);
        }
        in.close();
        return out.toByteArray();
    }


    public static byte[] objToByteArr(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream outObj = new ObjectOutputStream(out);
        outObj.writeObject(obj);
        outObj.flush();
        outObj.close();
        return out.toByteArray();
    }


    public static Object byteArrToObj(byte[] arr) throws IOException, ClassNotFoundException {
        ObjectInputStream inObj = new ObjectInputStream(new ByteArrayInputStream(arr));
        return inObj.readObject();
    }



    public static void sendHTML(HttpServletResponse res, String html) throws IOException {
        res.setContentType("text/html");
        res.getOutputStream().write(html.getBytes("utf-8"));
        res.getOutputStream().flush();
    }


    private static final int CACHE_MAX_AGE = 30 * 24 * 60 * 60;

    public static void sendResource(HttpServletResponse res, byte[] content, String type) throws IOException {
        if("css".equals(type)){
            res.setContentType("text/css");
        }
        if("png".equals(type) || "jpg".equals(type) || "jpeg".equals(type) || "gif".equals(type)){
            res.setContentType("image/" + type);
        }
        if("ico".equals(type)){
            res.setContentType("x-icon");
        }
        if("js".equals(type)){
            res.setContentType("application/javascript; charset=utf-8");
        }
        res.setHeader("Cache-Control", "max-age=" + CACHE_MAX_AGE);
        res.getOutputStream().write(content);
        res.getOutputStream().flush();
    }

    public static void sendResource(HttpServletResponse res, InputStream in) throws IOException {
        res.setContentType("text/html");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] temp = new byte[5 * 1024];
        int length = 0;
        while ((length = in.read(temp)) != -1) {
            out.write(temp, 0,length);
        }
        res.getOutputStream().write(out.toByteArray());
        res.getOutputStream().flush();
    }

    public static void send404(HttpServletResponse res) {
        res.setStatus(404);
    }


    public static void sendError(HttpServletResponse res, String msg) throws IOException {
        res.setStatus(500);
        res.getOutputStream().write(msg.getBytes());
    }

}
