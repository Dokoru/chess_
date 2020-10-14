package chess.model.pieces;

import chess.model.Cell;

public class Pawn extends Piece{

    public Pawn(Color color) {
        super(PieceType.PAWN, color);
    }

    public Pawn(Color color, Cell position) {
        super(PieceType.PAWN, color, position);
    }

    public Pawn(Piece piece) {
        super(piece);
    }
}
