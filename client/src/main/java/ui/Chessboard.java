package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class Chessboard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
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

        drawChessBoard(out);

        out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);

    }

    private static void drawHeadersHorizontal(PrintStream out) {
        setLightGrey(out);
        String[] headers = { "a", "b", "c", "d", "e", "f", "g", "h"};
        out.print("   ");
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol){
            printHeaderText(out, headers[boardCol]);

            if(boardCol != 1 && boardCol != 5){
                out.print(" ");
          }
        }
        System.out.println();

    }

    private static void printHeaderText(PrintStream out, String headerText){
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_YELLOW);

        out.print(" ");
        out.print(headerText);
        out.print(" ");

        setLightGrey(out);
    }

    private static void drawHeadersVertical(PrintStream out, int vert_header){
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_YELLOW);
        out.printf(" %d ", vert_header);

        setLightGrey(out);
    }

    private static void drawChessBoard(PrintStream out) {
        drawHeadersHorizontal(out);
        int i = 8;
        for (int row = 0; row < 8; ++row){
            drawHeadersVertical(out, i);
            drawRowsOfSquares(out, row % 2 != 0);
            drawHeadersVertical(out, i);
            out.println();
            --i;
        }
        drawHeadersHorizontal(out);
    }

    private static void drawRowsOfSquares(PrintStream out, boolean isOdd){
        for (int boardCol = 0; boardCol < 8; ++boardCol){
            if(boardCol % 2 == 0){
                if(!isOdd){
                    setWhite(out);
                }
                else{
                    setDarkGreen(out);
                }

            }
            else{
                if(!isOdd){
                    setDarkGreen(out);
                }
                else{
                    setWhite(out);
                }
            }
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
            setLightGrey(out);
        }

    }

    private static String getPieceIcon(ChessPiece piece){

    }

    private static void drawPiece(PrintStream out, int row, int column, ChessPiece piece){

    }

    private static void parseChessBoard(PrintStream out, ChessBoard board){
        for(int row = 1; row <= 8; ++row){
            for(int col = 1; col <= 8; ++col){
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if(piece != null){
                    drawPiece(out, row, col, piece);
                }
            }
        }
    }


    private static void setLightGrey(PrintStream out){
        out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREY);
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
