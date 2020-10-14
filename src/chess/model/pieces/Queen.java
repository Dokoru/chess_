package chess.model.pieces;

import chess.model.Cell;

public class Queen extends Piece{

    public Queen(Color color) {
        super(PieceType.QUEEN, color);
    }

    public Queen(Color color, Cell position) {
        super(PieceType.QUEEN, color, position);
    }

    public Queen(Piece piece) {
        super(piece);
        this.setType(PieceType.QUEEN);
    }
}
