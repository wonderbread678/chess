package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class Chessboard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 2;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    private static final String EMPTY = EscapeSequences.EMPTY;
    private static final String WHITE_KING = EscapeSequences.WHITE_KING;
    private static final String WHITE_QUEEN = EscapeSequences.WHITE_QUEEN;
    private static final String WHITE_ROOK = EscapeSequences.WHITE_ROOK;
    private static final String WHITE_BISHOP = EscapeSequences.WHITE_BISHOP;
    private static final String WHITE_KNIGHT = EscapeSequences.WHITE_KNIGHT;
    private static final String WHITE_PAWN = EscapeSequences.WHITE_PAWN;

    private static final String BLACK_KING = EscapeSequences.BLACK_KING;
    private static final String BLACK_QUEEN = EscapeSequences.BLACK_QUEEN;
    private static final String BLACK_ROOK = EscapeSequences.BLACK_ROOK;
    private static final String BLACK_BISHOP = EscapeSequences.BLACK_BISHOP;
    private static final String BLACK_KNIGHT = EscapeSequences.BLACK_KNIGHT;
    private static final String BLACK_PAWN = EscapeSequences.BLACK_PAWN;

    public static void main(String[] args){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(EscapeSequences.ERASE_SCREEN);

        drawHeadersHorizontal(out);

        drawChessBoard(out);

        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);

    }

    private static void drawHeadersHorizontal(PrintStream out) {
        setLightGrey(out);
        String[] headers = { "a", "b", "c", "d", "e", "f", "g", "h"};
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol){
            drawHeader(out, headers[boardCol]);

            if(boardCol < BOARD_SIZE_IN_SQUARES - 1){
                out.print(EscapeSequences.EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
            }
        }
        System.out.println();

    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = 1;

        out.print("  ");
        printHeaderText(out, headerText);
        out.print(" ");
    }

    private static void printHeaderText(PrintStream out, String headerText){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_DARK_GREY);

        out.print(headerText);

        setLightGrey(out);
    }

    private static void drawChessBoard(PrintStream out) {
        for (int row = 0; row < 8; ++row){
            if(row % 2 == 0){
                drawRowsOfSquares1(out);
                out.println();
                drawRowsOfSquares1(out);
                out.println();
            }
            else{
                drawRowsOfSquares2(out);
                out.println();
                drawRowsOfSquares2(out);
                out.println();
            }
        }
    }

    private static void drawRowsOfSquares1(PrintStream out){
        for (int boardCol = 0; boardCol < 8; ++boardCol){
            if(boardCol % 2 == 0){
                setWhite(out);
            }
            else{
                setDarkGreen(out);
            }
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
            setLightGrey(out);
        }
    }

    private static void drawRowsOfSquares2(PrintStream out){
        for (int boardCol = 0; boardCol < 8; ++boardCol){
            if(boardCol % 2 == 0){
                setDarkGreen(out);
            }
            else{
                setWhite(out);
            }
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
            setLightGrey(out);
        }
    }


    private static void setLightGrey(PrintStream out){
        out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void setDarkGreen(PrintStream out){
        out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
    }

    private static void setYellow(PrintStream out){
        out.print(EscapeSequences.SET_BG_COLOR_YELLOW);
        out.print(EscapeSequences.SET_TEXT_COLOR_YELLOW);
    }

    private static void setWhite(PrintStream out){
        out.print(EscapeSequences.SET_BG_COLOR_WHITE);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private static void setBlack(PrintStream out){
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
    }
}
