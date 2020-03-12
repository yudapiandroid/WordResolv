package wordpaser.entry.math;


// 根号
public class M_Rad extends Block {
    boolean haveDeg = false; // 是否包含子数

    public boolean isHaveDeg() {
        return haveDeg;
    }

    public void setHaveDeg(boolean haveDeg) {
        this.haveDeg = haveDeg;
    }
}
