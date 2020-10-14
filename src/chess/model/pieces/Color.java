package chess.model.pieces;

public enum Color {
    WHITE("White"),
    BLACK("Black");

    private String name;

    Color(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
