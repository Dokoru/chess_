package chess;

import chess.service.BoardService;
import chess.service.ColorService;
import chess.view.ConsoleView;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        BoardService boardService = new BoardService();
        ConsoleView consoleView = new ConsoleView();
        ColorService colorService = new ColorService();

        Scanner scanner = new Scanner(System.in);
        boardService.initBoard();
        boardService.fillBoard();
        boardService.calcCellUnderAttack();
        consoleView.printBoard(boardService);
        while (!boardService.isFinished()) {
            System.out.println("Enter move (a2-a4): ");
            String string = scanner.nextLine();
            if (!consoleView.isCorrect(string)) {
                System.out.println("Incorrect value. Enter move (a2-a4): ");
            } else {
                if (!boardService.tryMove(consoleView.getCellFrom(string, boardService),
                        consoleView.getCellTo(string, boardService))) {
                    System.out.println("Illegal move. Enter move (a2-a4): ");
                }
                consoleView.printBoard(boardService);
            }
        }
        System.out.println(String.format("Game end. %s win.",
                colorService.reversColor(boardService.getCurrentPlayer()).getName()));
    }
}
