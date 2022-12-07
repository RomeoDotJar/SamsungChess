package GameBase.Chess;

import GameBase.Base.Coordinate;
import GameBase.Base.Figure;
import GameBase.Base.Movable;

public abstract class ChessFigure extends Figure implements Movable {
    protected boolean colorIsWhite; // true = white, false = black

    public ChessFigure(boolean colorIsWhite, char symbol, Coordinate coordinateFrom) {
        super(symbol, coordinateFrom);
        this.colorIsWhite = colorIsWhite;
    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }

    public boolean isColorIsWhite() {
        return colorIsWhite;
    }
}
