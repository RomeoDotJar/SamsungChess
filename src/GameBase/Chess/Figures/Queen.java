package GameBase.Chess.Figures;

import GameBase.Chess.ChessFigure;
import GameBase.Base.Coordinate;

public class Queen extends ChessFigure {
    public Queen(boolean colorIsWhite, Coordinate coordinateFrom) {
        super(colorIsWhite, colorIsWhite ? '\u2655' : '\u265b', coordinateFrom);
    }

    @Override
    public boolean canMove(Coordinate coordinateTo) {
        return from.getX() == coordinateTo.getX() || from.getY() == coordinateTo.getY() || Math.abs(coordinateTo.getX()-from.getX())==Math.abs(coordinateTo.getY()-from.getY());
    }
}
