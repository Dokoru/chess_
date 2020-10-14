package chess.view;

import chess.model.Cell;
import chess.service.BoardService;
import chess.service.PieceService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleView {

    public void printBoard(BoardService boardService) {
        Cell[][] board = boardService.getBoard();

        System.out.println();
        System.out.println("     [A][B][C][D][E][F][G][H]");
        System.out.println();
        for(int i = 0; i < 8; i++) {
            System.out.print("[" + (8 - i) + "]  ");
            for (int j = 0; j < 8; j++){
                System.out.print(getStringCellValue(board[i][j]));
            }
            System.out.println();
        }
        System.out.println();
    }

    public String getStringCellValue(Cell cell){
        PieceService pieceService = new PieceService();
        if (cell != null && cell.getPiece() != null){
            return "[" + pieceService.getCharValue(cell.getPiece()) + "]";
        } else {
            return "[ ]";
        }
    }

    private final Pattern correctMove = Pattern.compile("([a-hA-H][1-8])([-])([a-hA-H][1-8])", Pattern.CASE_INSENSITIVE);

    public Cell splitIntoCoordinates(String val, BoardService boardService) {
        int x = coordinate(Integer.parseInt(String.valueOf(val.charAt(1))));
        int y = coordinate(val.charAt(0));

        return boardService.getCell(x, y);
    }

    public Cell getCellFrom(String val, BoardService boardService) {
        Matcher matcher = correctMove.matcher(val);
        matcher.matches();
        String coordinates = matcher.group(1);

        return splitIntoCoordinates(coordinates, boardService);
    }

    public Cell getCellTo(String val, BoardService boardService){
        Matcher matcher = correctMove.matcher(val);
        matcher.matches();
        String coordinates =  matcher.group(3);

        return splitIntoCoordinates(coordinates, boardService);
    }

    public boolean isCorrect(String val){
        return correctMove.matcher(val).matches();
    }

    public int coordinate(int val){
        switch(val){
            case 1: return 7;
            case 2: return 6;
            case 3: return 5;
            case 4: return 4;
            case 5: return 3;
            case 6: return 2;
            case 7: return 1;
            case 8: return 0;
        }
        return -1;
    }

    public int coordinate(char val){
        switch(Character.toLowerCase(val)){
            case 'a': return 0;
            case 'b': return 1;
            case 'c': return 2;
            case 'd': return 3;
            case 'e': return 4;
            case 'f': return 5;
            case 'g': return 6;
            case 'h': return 7;
        }
        return -1;
    }
}
