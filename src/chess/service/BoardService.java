package chess.service;

import chess.model.Cell;
import chess.model.pieces.*;

import java.util.*;

public class BoardService {

    private Cell[][] board = new Cell[8][8];
    private int currentTurn = 1;
    private Map<Color, Set<Cell>> cellUnderAttack = new HashMap<>();
    private boolean isFinished = false;

    public void initBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Cell(i, j);
            }
        }
    }

    public void fillBoard() {
        for (int i = 0; i < 8; i++) {
            board[1][i].setPiece(new Pawn(Color.BLACK, board[1][i]));
            board[6][i].setPiece(new Pawn(Color.WHITE, board[6][i]));
        }

        board[0][0].setPiece(new Rook(Color.BLACK, board[0][0]));
        board[0][7].setPiece(new Rook(Color.BLACK, board[0][7]));
        board[7][0].setPiece(new Rook(Color.WHITE, board[7][0]));
        board[7][7].setPiece(new Rook(Color.WHITE, board[7][7]));

        board[0][1].setPiece(new Knight(Color.BLACK, board[0][1]));
        board[0][6].setPiece(new Knight(Color.BLACK, board[0][6]));
        board[7][1].setPiece(new Knight(Color.WHITE, board[7][1]));
        board[7][6].setPiece(new Knight(Color.WHITE, board[7][6]));

        board[0][2].setPiece(new Bishop(Color.BLACK, board[0][2]));
        board[0][5].setPiece(new Bishop(Color.BLACK, board[0][5]));
        board[7][2].setPiece(new Bishop(Color.WHITE, board[7][2]));
        board[7][5].setPiece(new Bishop(Color.WHITE, board[7][5]));

        board[0][3].setPiece(new Queen(Color.BLACK, board[0][3]));
        board[7][3].setPiece(new Queen(Color.WHITE, board[7][3]));

        board[0][4].setPiece(new King(Color.BLACK, board[0][4]));
        board[7][4].setPiece(new King(Color.WHITE, board[7][4]));
    }

    public Cell getCell(int x, int y) {
        return board[x][y];
    }

    public Cell[][] getBoard() {
        return board;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void resetCellUnderAttack() {
        cellUnderAttack.put(Color.WHITE, new HashSet<>());
        cellUnderAttack.put(Color.BLACK, new HashSet<>());
    }

    public void calcCellUnderAttack() {
        resetCellUnderAttack();
        PieceService pieceService = new PieceService();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null && board[i][j].getPiece() != null &&
                        board[i][j].getPiece().getColor() == Color.WHITE) {
                    cellUnderAttack.get(Color.WHITE).addAll(pieceService.getLegalMove(board[i][j].getPiece(), this));
                }
                if (board[i][j] != null && board[i][j].getPiece() != null &&
                        board[i][j].getPiece().getColor() == Color.BLACK) {
                    cellUnderAttack.get(Color.BLACK).addAll(pieceService.getLegalMove(board[i][j].getPiece(), this));
                }
            }
        }
    }

    public Set<Cell> getCellUnderAttack(Color color) {
        return cellUnderAttack.get(color);
    }

    public Color getCurrentPlayer() {
        return currentTurn % 2 == 0 ? Color.BLACK : Color.WHITE;
    }

    public boolean isEnPassant(Cell cellFrom, Cell cellTo) {
        if (cellFrom != null && cellTo != null && cellTo.getPiece() == null && cellFrom.getPiece() != null &&
                getAllPieceAttackedCell(cellFrom).contains(cellTo) && cellFrom.getPiece().getType() == PieceType.PAWN &&
                Math.abs(cellFrom.getX() - cellTo.getX()) == 1 && Math.abs(cellFrom.getY() - cellTo.getY()) == 1) {
            return true;
        }
        return false;
    }

    public boolean isCastling(Cell cellFrom, Cell cellTo) {
        if (cellFrom != null && cellTo != null &&
                cellFrom.getPiece() != null && cellFrom.getPiece().getType() == PieceType.KING &&
                getAllPieceAttackedCell(cellFrom).contains(cellTo) &&
                Math.abs(cellFrom.getY() - cellTo.getY()) == 2) {
            return true;
        }
        return  false;
    }

    public boolean isPawnPromotion(Cell cellFrom, Cell cellTo) {
        if (cellFrom != null && cellTo != null && getAllPieceAttackedCell(cellFrom).contains(cellTo) &&
                cellFrom.getPiece() != null &&cellFrom.getPiece().getType() == PieceType.PAWN &&
                cellTo.getX() == 0 || cellTo.getX() == 7) {
            return true;
        }
        return false;
    }

    private void pawnPromotion(Cell cell, PieceType type) {
        if (cell != null && cell.getPiece() != null && cell.getPiece().getType() == PieceType.PAWN) {
            PieceService pieceService = new PieceService();
            cell.setPiece(pieceService.changePieceType(cell.getPiece(), type));
        }
    }

    public boolean tryMove(Cell cellFrom, Cell cellTo) {
        return tryMove(cellFrom, cellTo, PieceType.QUEEN);
    }

    public boolean tryMove(Cell cellFrom, Cell cellTo, PieceType type) {
        if (isLegalMove(cellFrom, cellTo)) {
            if (isEnPassant(cellFrom, cellTo)){
                board[cellFrom.getX()][cellTo.getY()].setPiece(null);
            }
            if (isCastling(cellFrom, cellTo)) {
                int y, dy;
                if (cellFrom.getY() - cellTo.getY() < 0) {      //right castling
                    y = 7;
                    dy = 1;
                } else {        //left castling
                    y = 0;
                    dy = -1;
                }
                Cell from = board[cellFrom.getX()][y];
                Cell to = board[cellFrom.getX()][cellFrom.getY() + dy];
                from.getPiece().setPosition(to);
                to.setPiece(from.getPiece());
                from.setPiece(null);
            }
            if (isPawnPromotion(cellFrom, cellTo)) {
                pawnPromotion(cellFrom, type);
            }
            Piece piece = cellFrom.getPiece();
            cellTo.setPiece(piece);
            piece.setPosition(cellTo);
            cellFrom.setPiece(null);
            piece.setFirstTurn(currentTurn);
            currentTurn++;
            calcCellUnderAttack();
            isCheckmate(getCurrentPlayer());

            return true;
        }
        return false;
    }

    public boolean isLegalMove(Cell cellFrom, Cell cellTo) {
        PieceService pieceService = new PieceService();
        Piece pieceFrom = cellFrom.getPiece();
        Piece pieceTo = cellTo.getPiece();

        if (cellFrom == null || pieceFrom == null || pieceFrom.getColor() != getCurrentPlayer() ||
                (pieceTo != null && pieceTo.getColor() == getCurrentPlayer())) {
            return false;
        }
        else if (pieceService.getLegalMove(cellFrom.getPiece(), this).size() > 0 &&
                pieceService.getLegalMove(cellFrom.getPiece(), this).contains(cellTo)) {
            cellTo.setPiece(pieceFrom);
            cellFrom.setPiece(null);
            calcCellUnderAttack();
            if (isCheck(getCurrentPlayer())) {
                if (isCheckmate(getCurrentPlayer())) {
                    isFinished = true;
                }
                cellFrom.setPiece(pieceFrom);
                cellTo.setPiece(pieceTo);
                return false;
            }
            cellFrom.setPiece(pieceFrom);
            cellTo.setPiece(pieceTo);
            calcCellUnderAttack();
            return true;
        }
        return false;
    }

    public Cell getKingPosition(Color color) {
        Cell position = new Cell(-1, -1);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j].getPiece();
                if (board[i][j] != null && piece != null && piece.getType() == PieceType.KING &&
                        piece.getColor() == color) {
                    position = board[i][j];
                }
            }
        }
        return position;
    }

    public boolean isCheck(Color color) {       //check for color king
        ColorService colorService = new ColorService();
        return getCellUnderAttack(colorService.reversColor(color)).contains(getKingPosition(color));
    }

    public boolean isCheckmate(Color color) {       //checkmate for color king
        if (!isCheck(color)) return false;

        List<Cell> pieces = getAllPieceCell(color);
        PieceService pieceService = new PieceService();
        for (Cell cellFrom : pieces) {
            Piece pieceFrom = cellFrom.getPiece();
            List<Cell> moves = pieceService.getLegalMove(pieceFrom, this);
            for (Cell cellTo : moves) {
                Piece pieceTo = cellTo.getPiece();
                cellTo.setPiece(pieceFrom);
                cellFrom.setPiece(null);
                calcCellUnderAttack();
                if (!isCheck(color)) {
                    cellFrom.setPiece(pieceFrom);
                    cellTo.setPiece(pieceTo);
                    calcCellUnderAttack();
                    return false;
                }
                cellFrom.setPiece(pieceFrom);
                cellTo.setPiece(pieceTo);
                calcCellUnderAttack();
            }
        }
        isFinished = true;
        return true;
    }

    public List<Cell> getAllPieceCell(Color color) {
        List<Cell> pieces = new ArrayList<>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board[x][y] != null && board[x][y].getPiece() != null && board[x][y].getPiece().getColor() == color) {
                    pieces.add(board[x][y]);
                }
            }
        }
        return pieces;
    }

    public List<Cell> getAllPieceAttackedCell(Cell cell) {
        PieceService pieceService = new PieceService();
        return pieceService.getLegalMove(cell.getPiece(), this);
    }

    public boolean isFinished() {
        return isFinished;
    }
}
