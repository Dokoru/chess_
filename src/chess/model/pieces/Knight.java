package chess.model.pieces;

import chess.model.Cell;

public class Knight extends Piece{

    public Knight(Color color) {
        super(PieceType.KNIGHT, color);
    }

    public Knight(Color color, Cell position) {
        super(PieceType.KNIGHT, color, position);
    }

    public Knight(Piece piece) {
        super(piece);
        this.setType(PieceType.KNIGHT);
    }
}
