package GameBase.Chess;

import GameBase.Base.Board;
import GameBase.Base.Coordinate;
import GameBase.Base.Movable;
import GameBase.Chess.Figures.*;

import java.util.Arrays;

public class ChessBoard extends Board {
    private static final int chessFieldSize;

    static {
        chessFieldSize = 8;
    }

    private static ChessBoard instance;


    private ChessBoard() {
        this.turn = true;
        this.field = new ChessFigure[chessFieldSize][chessFieldSize];
        for (Movable[] cf : field) Arrays.fill(cf, null);

    }

    public static ChessBoard getInstance() {
        if (instance == null) instance = new ChessBoard();
        return instance;
    }

    //public boolean getTurn() {
    //    return turn;
    //}

    private void resetField() {
        for (Movable[] cf : field) Arrays.fill(cf, null);
    }

    public void newField() {
        this.resetField();
        // white
        field[0][0] = new Rook(true, new Coordinate(0, 0));
        field[0][1] = new Knight(true, new Coordinate(1, 0));
        field[0][2] = new Bishop(true, new Coordinate(2, 0));
        field[0][3] = new Queen(true, new Coordinate(3, 0));
        field[0][4] = new King(true, new Coordinate(4, 0));
        field[0][5] = new Bishop(true, new Coordinate(5, 0));
        field[0][6] = new Knight(true, new Coordinate(6, 0));
        field[0][7] = new Rook(true, new Coordinate(7, 0));
        for (int i = 0; i < chessFieldSize; i++)
            field[1][i] = new Pawn(true, new Coordinate(i, 1));
        //black
        field[7][0] = new Rook(false, new Coordinate(0, 7));
        field[7][1] = new Knight(false, new Coordinate(1, 7));
        field[7][2] = new Bishop(false, new Coordinate(2, 7));
        field[7][3] = new Queen(false, new Coordinate(3, 7));
        field[7][4] = new King(false, new Coordinate(4, 7));
        field[7][5] = new Bishop(false, new Coordinate(5, 7));
        field[7][6] = new Knight(false, new Coordinate(6, 7));
        field[7][7] = new Rook(false, new Coordinate(7, 7));
        for (int i = 0; i < chessFieldSize; i++)
            field[6][i] = new Pawn(false, new Coordinate(i, 6));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(" | A | B | C | D | E | F | G | H \n");//.append("-----------------\n");
        for (int i = 0; i < chessFieldSize; i++) {
            sb.append(8 - i);
            for (int j = 0; j < chessFieldSize; j++)
                sb.append("|").append(
                        field[chessFieldSize - 1 - i][j] != null ? field[chessFieldSize - 1 - i][j] : "   "//(char)8195
                );
            sb.append("\n"); //-----------------
        }
        sb.append(" | A | B | C | D | E | F | G | H \n").append("-----------------\n");
        return sb.toString();
    }

    public boolean isSelfCheck() {
        King king = null;
        for (Movable[] y : field) {
            for (ChessFigure piece : (ChessFigure[]) y) {
                if (piece==null) continue;
                if (piece.getClass().getSimpleName() == "King" && piece.isColorIsWhite() == turn) {
                    king = (King) piece;
                }
            }
        }
        if (king==null)
            return false;
        for (Movable[] y : field) {
            for (ChessFigure piece : (ChessFigure[]) y) {
                if (piece==null) continue;
                if (canMove(piece.getPos(), king.getPos()))
                    return true;
            }
        }
        return false;
    }

    public boolean willCheck(Coordinate from, Coordinate to) {
        //Movable[][] fieldTemp = Arrays.copyOf(field, field.length);
        Movable[][] fieldTemp = new Movable[field.length][];
        for (int y = 0; y < field.length; y++) {
            fieldTemp[y] = new ChessFigure[field[y].length];
            System.arraycopy(field[y], 0, fieldTemp[y], 0, field[y].length);
        }

        ((ChessFigure) field[from.getY()][from.getX()]).moveTo(to);
        field[to.getY()][to.getX()] = field[from.getY()][from.getX()];
        field[from.getY()][from.getX()] = null;
        boolean willCheck = isSelfCheck();
        field = fieldTemp;
        return willCheck;
    }

    public boolean canMove(Coordinate from, Coordinate to) {
        if (from == null || field[from.getY()][from.getX()] == null || ((ChessFigure) field[from.getY()][from.getX()]).isColorIsWhite()!=turn) return false;
        else {
            ChessFigure tempFrom = (ChessFigure) field[from.getY()][from.getX()];
            ChessFigure tempTo = (ChessFigure) field[to.getY()][to.getX()];
            //System.out.println("from = " + from);
            //System.out.println("Ffrom = " + tempFrom);
            switch (tempFrom.getClass().getSimpleName()) {
                case "Bishop":
                    if (tempTo != null && tempFrom.isColorIsWhite() == tempTo.isColorIsWhite())
                        return false;

                    if (tempFrom.canMove(to)) {
                        Coordinate f = new Coordinate(from.getX(), from.getY());
                        Coordinate t = new Coordinate(to.getX(), to.getY());
                        if (f.equals(t)) return false;

                        int maxY = Math.max(to.getY(), from.getY());
                        int minY = Math.min(to.getY(), from.getY());

                        int direction = from.getX()<to.getX() ? 1 : -1;

                        for (int x=from.getX(); direction==1 ? x<to.getX() : x>to.getX(); x+=direction) {
                            for (int y=minY; y<maxY; y++) {
                                if ( field[y][x]!=null &&
                                        x!=to.getX() && x!=from.getX() && y!=to.getY() && y!=from.getY() )
                                    return false;
                            }
                        }
                        return tempTo == null || tempFrom.isColorIsWhite() != tempTo.isColorIsWhite();
                    }
                    return false;
                case "Queen":
                    if (tempFrom.canMove(to)) {
                        Coordinate f = new Coordinate(from.getX(), from.getY());
                        Coordinate t = new Coordinate(to.getX(), to.getY());
                        if (f.equals(t)) return false;

                        int xx = f.getX()-t.getX();
                        if (xx > 0) {
                            for (int i = t.getX()+1; i < f.getX(); i++) {
                                if (field[f.getY()][i] != null) return false;
                            }
                        } else if (xx < 0) {
                            for (int i = t.getX()-1; i > f.getX(); i--) {
                                if (field[f.getY()][i] != null) return false;
                            }
                        } else {
                            int yy = f.getY()-t.getY();

                            if (yy > 0) {
                                for (int i = t.getY()+1; i < f.getY(); i++) {
                                    if (field[i][f.getX()] != null) return false;
                                }
                            } else if (yy < 0) {
                                for (int i = t.getY()-1; i > f.getY(); i--) {
                                    if (field[i][f.getX()] != null) return false;
                                }
                            } else return false;
                        }
                        int maxY = Math.max(to.getY(), from.getY());
                        int minY = Math.min(to.getY(), from.getY());

                        int direction = from.getX()<to.getX() ? 1 : -1;

                        for (int x=from.getX(); direction==1 ? x<to.getX() : x>to.getX(); x+=direction) {
                            for (int y=minY; y<maxY; y++) {
                                if ( field[y][x]!=null &&
                                        x!=to.getX() && x!=from.getX() && y!=to.getY() && y!=from.getY() )
                                    return false;
                            }
                        }
                        return tempTo == null || tempFrom.isColorIsWhite() != tempTo.isColorIsWhite();
                    }
                    else return false;
                case "King":
                    if (tempFrom.canMove(to))
                        return tempTo == null || tempFrom.isColorIsWhite() != tempTo.isColorIsWhite();
                    else
                        return false;
                case "Rook":
                    if (tempTo != null && tempFrom.isColorIsWhite() == tempTo.isColorIsWhite())
                        return false;

                    if (tempFrom.canMove(to)) {
                        Coordinate f = new Coordinate(from.getX(), from.getY());
                        Coordinate t = new Coordinate(to.getX(), to.getY());
                        if (f.equals(t)) return false;

                        int xx = f.getX()-t.getX();
                        if (xx > 0) {
                            for (int i = t.getX()+1; i < f.getX(); i++) {
                                if (field[f.getY()][i] != null) return false;
                            }
                        } else if (xx < 0) {
                            for (int i = t.getX()-1; i > f.getX(); i--) {
                                if (field[f.getY()][i] != null) return false;
                            }
                        } else {
                            int yy = f.getY()-t.getY();

                            if (yy > 0) {
                                for (int i = t.getY()+1; i < f.getY(); i++) {
                                    if (field[i][f.getX()] != null) return false;
                                }
                            } else if (yy < 0) {
                                for (int i = t.getY()-1; i > f.getY(); i--) {
                                    if (field[i][f.getX()] != null) return false;
                                }
                            } else return false;
                        }
                        return true;
                    }
                    else return false;
                case "Knight":
                    if (tempFrom.canMove(to))
                        if (tempTo == null) return true;
                        else return tempFrom.isColorIsWhite() != tempTo.isColorIsWhite();
                    else return false;
                case "Pawn":
                    if (tempFrom.canMove(to)) {
                        if (tempTo == null) return from.getX() == to.getX();
                        else return tempFrom.isColorIsWhite() != tempTo.isColorIsWhite() && from.getX() != to.getX();
                    }
                    else return false;
                default:
                    System.out.println("how the fuck did you even get here");
                    return false;
            }
        }
    }

    public void move(Coordinate from, Coordinate to) {
        ((ChessFigure) field[from.getY()][from.getX()]).moveTo(to);
        field[to.getY()][to.getX()] = field[from.getY()][from.getX()];
        field[from.getY()][from.getX()] = null;
        turn = !turn;
    }

    public boolean getTurn() {
        return turn;
    }

    /*private void changePlayer() {
        turn = !turn;
    }*/
}