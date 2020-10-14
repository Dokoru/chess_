package chess.model.pieces;

import chess.model.Cell;

import java.util.List;

public abstract class Piece {

    private PieceType type;
    private Color color;
    private Cell position;
    private int firstTurn = 0;

    //private List<Cell> legalMove;

    //private char pieceChar;     //to console

    public Piece(PieceType type, Color color) {
        this.type = type;
        this.color = color;
    }

    public Piece(PieceType type, Color color, Cell position) {
        this.type = type;
        this.color = color;
        this.position = position;
    }

    public Piece(Piece piece) {
        this.color = piece.getColor();
        this.position = piece.getPosition();
        this.firstTurn = piece.getFirstTurn();
    }

    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Cell getPosition() {
        return position;
    }

    public void setPosition(Cell position) {
        this.position = position;
    }

    public int getFirstTurn() {
        return firstTurn;
    }

    public void setFirstTurn(int firstTurn) {
        this.firstTurn = firstTurn;
    }
}
