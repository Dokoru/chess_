package chess.model.pieces;

import chess.model.Cell;

public class Bishop extends Piece{

    public Bishop(Color color) {
        super(PieceType.BISHOP, color);
    }

    public Bishop(Color color, Cell position) {
        super(PieceType.BISHOP, color, position);
    }

    public Bishop(Piece piece) {
        super(piece);
        this.setType(PieceType.BISHOP);
    }
}
