package wordpaser.entry;


import java.util.Iterator;
import java.util.Map;

// docx  文件内容实体
public class Doc {

    private byte[] wordXML;
    private byte[] resXML;

    private Map<String, byte[]> files;

    public byte[] getWordXML() {
        return wordXML;
    }

    public void setWordXML(byte[] wordXML) {
        this.wordXML = wordXML;
    }

    public byte[] getResXML() {
        return resXML;
    }

    public void setResXML(byte[] resXML) {
        this.resXML = resXML;
    }

    public Map<String, byte[]> getFiles() {
        return files;
    }

    public void setFiles(Map<String, byte[]> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        System.out.println("[xml] " + new String(wordXML));
        System.out.println("[res] " + new String(resXML));
        System.out.println("[files]");
        if(files != null){
            Iterator<Map.Entry<String, byte[]>> it = files.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, byte[]> next = it.next();
                System.out.println("  [file] " + next.getKey() + "  size: "+ next.getValue().length);
            }
        }
        return "";
    }
}
