package wordpaser.entry;


public class TableNode extends N{

    private int[] widths;

    private PNode[][] cc;

    public int[] getWidths() {
        return widths;
    }

    public void setWidths(int[] widths) {
        this.widths = widths;
    }


    public PNode[][] getCc() {
        return cc;
    }

    public void setCc(PNode[][] cc) {
        this.cc = cc;
    }
}
