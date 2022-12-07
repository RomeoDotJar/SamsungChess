package GameBase.Base;

public abstract class Figure {

    protected boolean isOnBoard;  // true = onBoard
    protected char symbol;
    protected Coordinate from;


    public Figure(char symbol, Coordinate from) {
        this.isOnBoard = true;
        this.symbol = symbol;
        this.from = from;
    }

    public void moveTo(Coordinate to) {
        this.from = to;
    }
    public Coordinate getPos() {
        return from;
    }

}
