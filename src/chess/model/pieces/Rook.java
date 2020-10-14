package chess.model.pieces;

import chess.model.Cell;

public class Rook extends Piece{

    public Rook(Color color) {
        super(PieceType.ROOK, color);
    }

    public Rook(Color color, Cell position) {
        super(PieceType.ROOK, color, position);
    }

    public Rook(Piece piece) {
        super(piece);
        this.setType(PieceType.ROOK);
    }
}
