package wordpaser.entry;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {


    // 判断
    public static final int TYPE_JUM = 1;
    // 单选
    public static final int TYPE_SINGLE = 2;
    // 多选
    public static final int TYE_MUTIL = 3;

    private String title;
    private List<String> choses;

    private int index;
    private int type;


    private Answer answer;

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getChoses() {
        return choses;
    }

    public void setChoses(List<String> choses) {
        this.choses = choses;
    }


    @Override
    public String toString() {
        System.out.println("[title] " + title);
        System.out.println("[chose] ");
        for (String s : choses) {
            System.out.println("    " + s);
        }
        return "";
    }
}
