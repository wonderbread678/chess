package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Chessboard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 8;

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

    }
    private static void makeHeaders(PrintStream out){

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
