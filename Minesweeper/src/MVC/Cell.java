package MVC;

public class Cell {

    int column;
    int row;
    String state;
    boolean mined;
    int counter;

    public Cell(int row, int column) {
        this.column = column;
        this.row = row;
        this.state = "closed";
        this.mined = false;
        this.counter = 0;
    }

    public void open() {
        if (this.state != "flagged")
            this.state = "opened";
    }

    public void flagged() {
        if (this.state == "flagged")
            this.state = "closed";
        else
            if (this.state == "closed")
                this.state = "flagged";
    }



}
