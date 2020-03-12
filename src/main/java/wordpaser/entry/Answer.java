package wordpaser.entry;

import java.io.Serializable;

public class Answer implements Serializable {


    private int index;
    private String answer;
    private String content;

    // 仅仅包含答案
    private String pureAnswer;

    public String getPureAnswer() {
        return pureAnswer;
    }

    public void setPureAnswer(String pureAnswer) {
        this.pureAnswer = pureAnswer;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
