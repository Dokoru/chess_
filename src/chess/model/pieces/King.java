package chess.model.pieces;

import chess.model.Cell;

public class King extends Piece{

    public King(Color color) {
        super(PieceType.KING, color);
    }

    public King(Color color, Cell position) {
        super(PieceType.KING, color, position);
    }
}
