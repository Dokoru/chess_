package chess.service;

import chess.model.pieces.Color;

public class ColorService {
    public Color reversColor(Color color) {
        return color == Color.WHITE ? Color.BLACK : Color.WHITE;
    }
}
