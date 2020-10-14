package chess.service;

import chess.model.Cell;
import chess.model.pieces.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PieceService {

    public List<Cell> getLegalMove(Piece piece, BoardService boardService) {
        switch (piece.getType()) {
            case PAWN:
                return getLegalMovePawn(piece, boardService);
            case ROOK:
                return getLegalMoveRook(piece, boardService);
            case KNIGHT:
                return getLegalMoveKnight(piece, boardService);
            case BISHOP:
                return getLegalMoveBishop(piece, boardService);
            case QUEEN:
                return getLegalMoveQueen(piece, boardService);
            case KING:
                return getLegalMoveKing(piece, boardService);
            default:
                try {
                    throw new Exception("EmptyCell");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        List<Cell> legalMoves = new ArrayList<>();
        return legalMoves;
    }

    private List<Cell> getLegalMovePawn(Piece pawn, BoardService boardService) {
        if (pawn.getType() != PieceType.PAWN) {
            try {
                throw new Exception("IncorrectPieceType");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<Cell> legalMoves = new ArrayList<>();
        Cell[][] board = boardService.getBoard();
        int x = pawn.getPosition().getX();      //horizontal
        int y = pawn.getPosition().getY();      //vertical
        Color color = pawn.getColor();
        Cell newCell;       //cell to which a move can be made

        int dx, enPassantX, startX;
        if (color == Color.WHITE) {
            dx = -1;
            enPassantX = 3;
            startX = 6;
        }
        else {
            dx = 1;
            enPassantX = 4;
            startX = 1;
        }

        if (x + dx >= 0 && x + dx < 8 && (newCell = board[x + dx][y]) != null && newCell.getPiece() == null) {        //move forward one cell
            legalMoves.add(newCell);
            if (x == startX && (newCell = board[x + dx + dx][y]) != null && newCell.getPiece() == null) {      //move forward two cell during the first move
                legalMoves.add(newCell);
            }
        }
        if (x + dx >= 0 && x + dx < 8 && y < 7 && (newCell = board[x + dx][y + 1]) != null &&
                newCell.getPiece() != null && newCell.getPiece().getColor() != color) {     //right attack
            legalMoves.add(newCell);
        }
        if (x + dx >= 0 && x + dx < 8 && y > 0 && (newCell = board[x + dx][y - 1]) != null &&
                newCell.getPiece() != null && newCell.getPiece().getColor() != color) {     //left attack
            legalMoves.add(newCell);
        }
        if (x == enPassantX) {
            Cell attackedCell;
            if (y < 7 && (newCell = board[x + dx][y + 1]) != null && (attackedCell =  board[x][y + 1]) != null &&
                    attackedCell.getPiece() != null && attackedCell.getPiece().getType() == PieceType.PAWN &&
                    attackedCell.getPiece().getColor() != color &&
                    (boardService.getCurrentTurn() - attackedCell.getPiece().getFirstTurn()) == 1) {     //right en passant
                legalMoves.add(newCell);
            }
            if (y > 0 && (newCell = board[x + dx][y - 1]) != null && (attackedCell =  board[x][y - 1]) != null &&
                    attackedCell.getPiece() != null &&  attackedCell.getPiece().getType() == PieceType.PAWN &&
                    attackedCell.getPiece().getColor() != color &&
                    (boardService.getCurrentTurn() - attackedCell.getPiece().getFirstTurn()) == 1) {     //left en passant
                legalMoves.add(newCell);
            }
        }

        return legalMoves;
    }

    private List<Cell> getLegalMoveRook(Piece rook, BoardService boardService) {
        if (rook.getType() != PieceType.ROOK) {
            try {
                throw new Exception("IncorrectPieceType");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<Cell> legalMoves = new ArrayList<>();
        int x = rook.getPosition().getX();
        int y = rook.getPosition().getY();

        for (int i = 1; x + i < 8; i++) {
            if (addMove(x + i, y, rook, legalMoves, boardService)) {
                break;
            }
        }
        for (int i = 1; x - i >= 0; i++) {
            if (addMove(x - i, y, rook, legalMoves, boardService)) {
                break;
            }
        }
        for (int i = 1; y + i < 8; i++) {
            if (addMove(x, y + i, rook, legalMoves, boardService)) {
                break;
            }
        }
        for (int i = 1; y - i >= 0; i++) {
            if (addMove(x, y - i, rook, legalMoves, boardService)) {
                break;
            }
        }

        return legalMoves;
    }

    private List<Cell> getLegalMoveKnight(Piece knight, BoardService boardService) {
        if (knight.getType() != PieceType.KNIGHT) {
            try {
                throw new Exception("IncorrectPieceType");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<Cell> legalMoves = new ArrayList<>();
        int x = knight.getPosition().getX();
        int y = knight.getPosition().getY();

        addMove(x - 2, y + 1, knight, legalMoves, boardService);
        addMove(x - 1, y + 2, knight, legalMoves, boardService);
        addMove(x + 1, y + 2, knight, legalMoves, boardService);
        addMove(x + 2, y + 1, knight, legalMoves, boardService);
        addMove(x + 2, y - 1, knight, legalMoves, boardService);
        addMove(x + 1, y - 2, knight, legalMoves, boardService);
        addMove(x - 1, y - 2, knight, legalMoves, boardService);
        addMove(x - 2, y - 1, knight, legalMoves, boardService);

        return legalMoves;
    }

    private List<Cell> getLegalMoveBishop(Piece bishop, BoardService boardService) {
        if (bishop.getType() != PieceType.BISHOP) {
            try {
                throw new Exception("IncorrectPieceType");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<Cell> legalMoves = new ArrayList<>();
        int x = bishop.getPosition().getX();
        int y = bishop.getPosition().getY();

        for (int i = 1; x + i < 8 && y + i < 8; i++) {
            if (addMove(x + i, y + i, bishop, legalMoves, boardService)) {
                break;
            }
        }
        for (int i = 1; x + i < 8 && y - i >= 0; i++) {
            if (addMove(x + i, y - i, bishop, legalMoves, boardService)) {
                break;
            }
        }
        for (int i = 1; x - i >= 0 && y + i < 8; i++) {
            if (addMove(x - i, y + i, bishop, legalMoves, boardService)) {
                break;
            }
        }
        for (int i = 1; x - i >= 0 && y - i >= 0; i++) {
            if (addMove(x - i, y - i, bishop, legalMoves, boardService)) {
                break;
            }
        }

        return legalMoves;
    }

    private List<Cell> getLegalMoveQueen(Piece queen, BoardService boardService) {
        if (queen.getType() != PieceType.QUEEN) {
            try {
                throw new Exception("IncorrectPieceType");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<Cell> legalMoves = new ArrayList<>();

        legalMoves.addAll(getLegalMoveRook(new Rook(queen), boardService));

        Bishop bishop = new Bishop(queen);
        List<Cell> lmb = getLegalMoveBishop(bishop, boardService);
        legalMoves.addAll(lmb);

       // legalMoves.addAll(getLegalMoveBishop(new Bishop(queen), boardService));

        return legalMoves;
    }

    private List<Cell> getLegalMoveKing(Piece king, BoardService boardService) {
        if (king.getType() != PieceType.KING) {
            try {
                throw new Exception("IncorrectPieceType");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<Cell> legalMoves = new ArrayList<>();
        Cell[][] board = boardService.getBoard();
        Color color = king.getColor();
        int x = king.getPosition().getX();
        int y = king.getPosition().getY();
        Set<Cell> cellUnderAttack = boardService.getCellUnderAttack(opponentColor(king));
        Cell newCell;

        for (int dx = -1; dx <=1; dx++) {       //common movement into a non-attacked cell
            for (int dy = -1; dy <= 1; dy++) {
                if ((x + dx >=0 && x + dx < 8 && y + dy >=0 && y + dy < 8)
                        && (dx != 0 || dy != 0) && (newCell = board[x + dx][y + dy]) != null &&
                        !cellUnderAttack.contains(newCell) &&
                        (newCell.getPiece() == null || newCell.getPiece().getColor() != color)) {
                    legalMoves.add(newCell);
                }
            }
        }
        if (king.getFirstTurn() == 0) {     //castling
            Cell rookCell = board[x][7];        //right castling
            if (rookCell != null && rookCell.getPiece() != null &&
                    rookCell.getPiece().getType() == PieceType.ROOK && rookCell.getPiece().getFirstTurn() == 0) {
                boolean canCastling = true;
                for (int i = y + 1; i < 7; i++) {
                    Cell currentCell;
                    if ((currentCell = board[x][i]) != null &&
                            (currentCell.getPiece() != null || cellUnderAttack.contains(currentCell))) {
                        canCastling = false;
                        break;
                    }
                }
                if (canCastling) {
                    legalMoves.add(board[x][6]);
                }
            }
            rookCell = board[x][0];        //left castling
            if (rookCell != null && rookCell.getPiece() != null &&
                    rookCell.getPiece().getType() == PieceType.ROOK && rookCell.getPiece().getFirstTurn() == 0) {
                boolean canCastling = true;
                for (int i = y - 1; i > 0; i--) {
                    Cell currentCell;
                    if ((currentCell = board[x][i]) != null &&
                            (currentCell.getPiece() != null || cellUnderAttack.contains(currentCell))) {
                        canCastling = false;
                        break;
                    }
                }
                if (canCastling) {
                    legalMoves.add(board[x][2]);
                }
            }
        }

        return legalMoves;
    }

    private boolean addMove(int x, int y, Piece piece, List<Cell> moves, BoardService boardService) {
        if (!(x >= 0 && x < 8 && y >=0 && y < 8)) {
            return false;
        }
        Cell newCell = boardService.getCell(x, y);
        if (newCell != null) {
            if (newCell.getPiece() == null) {
                moves.add(newCell);
                return false;       //false == has more moves
            } else if (piece.getColor() != newCell.getPiece().getColor()) {
                moves.add(newCell);
            }
        }
        return true;        //true == hasn't more moves
    }

    public Piece changePieceType(Piece piece, PieceType type) {
        switch (type) {
            case KNIGHT:
                piece = new Knight(piece);
                break;
            case ROOK:
                piece = new Rook(piece);
                break;
            case BISHOP:
                piece = new Bishop(piece);
                break;
            case QUEEN:
                piece = new Queen(piece);
                break;
            default:
                piece = null;
                break;
        }
        return piece;
    }

    public Color opponentColor(Piece piece) {
        return piece.getColor() == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    public char getCharValue(Piece piece) {
        if (piece.getColor() == Color.WHITE) {
            switch (piece.getType()) {
                case PAWN: return 'P';
                case KNIGHT: return 'N';
                case ROOK: return 'R';
                case BISHOP: return 'B';
                case QUEEN: return 'Q';
                case KING: return 'K';
            }
        }
        else {
            switch (piece.getType()) {
                case PAWN: return 'p';
                case KNIGHT: return 'n';
                case ROOK: return 'r';
                case BISHOP: return 'b';
                case QUEEN: return 'q';
                case KING: return 'k';
            }
        }
        return ' ';
    }
}
