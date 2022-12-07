package GameBase.Chess;

import GameBase.Base.Coordinate;

import java.util.Arrays;

public class ChessGame {
    protected final ChessBoard board;
    public static ChessGame instance;

    private ChessGame() {
        board = ChessBoard.getInstance();
    }

    public static ChessGame getInstance() {
        if (instance == null) instance = new ChessGame();
        return instance;
    }

    public void newGame() {
        board.newField();
        this.showBoard();
    }

    public void makeStep(Coordinate[] coor) {
        if (board.canMove(coor[0], coor[1]) && !board.willCheck(coor[0], coor[1]))
            board.move(coor[0], coor[1]);
        this.showBoard();
    }

    public void showBoard() {
        System.out.println(board);
    }

    public boolean getTurn() {
        return board.getTurn();
    }
}