package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import server.ResponseException;

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

        ChessBoard board = new ChessBoard();
        board.resetBoard();

        ChessGame.TeamColor playerColor = ChessGame.TeamColor.WHITE;

        out.print(EscapeSequences.ERASE_SCREEN);

        drawChessBoard(out, board, playerColor);

        out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);

    }

    private static void drawHeadersHorizontal(PrintStream out, ChessGame.TeamColor playerColor) {
        setDarkGrey(out);
        String[] whiteHeaders = { "a", "b", "c", "d", "e", "f", "g", "h"};
        String[] blackHeaders = { "h", "g", "f", "e", "d", "c", "b", "a"};
        out.print("   ");
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol){
            if(playerColor == ChessGame.TeamColor.WHITE){
                printHeaderText(out, whiteHeaders[boardCol]);
            }
            else{
                printHeaderText(out, blackHeaders[boardCol]);
            }
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

        setDarkGrey(out);
    }

    private static void drawHeadersVertical(PrintStream out, int vert_header){
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_YELLOW);
        out.printf(" %d ", vert_header);

        setDarkGrey(out);
    }

    private static void drawChessBoard(PrintStream out, ChessBoard board,ChessGame.TeamColor playerColor) {
        drawHeadersHorizontal(out, playerColor);
        int i = 8;
        if (playerColor == ChessGame.TeamColor.BLACK){
            i = 1;
        }
        for (int row = 0; row < 8; ++row){
            drawHeadersVertical(out, i);
            drawRowsOfSquares(out, row % 2 != 0, i, board);
            drawHeadersVertical(out, i);
            out.println();
            if (playerColor == ChessGame.TeamColor.BLACK){
                ++i;
            }
            else{
                --i;
            }
        }
        drawHeadersHorizontal(out, playerColor);
    }

    private static void drawRowsOfSquares(PrintStream out, boolean isOdd, int row, ChessBoard board){
        for (int boardCol = 0; boardCol < 8; ++boardCol){
            drawPiece(out, row, boardCol, board, isOdd);
        }

    }

    private static String getPieceIcon(ChessPiece piece){
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            return switch(piece.getPieceType()){
                case KING -> WHITE_KING;
                case QUEEN -> WHITE_QUEEN;
                case ROOK -> WHITE_ROOK;
                case BISHOP -> WHITE_BISHOP;
                case KNIGHT -> WHITE_KNIGHT;
                case PAWN -> WHITE_PAWN;
            };
        }
        else{
            return switch(piece.getPieceType()){
                case KING -> BLACK_KING;
                case QUEEN -> BLACK_QUEEN;
                case ROOK -> BLACK_ROOK;
                case BISHOP -> BLACK_BISHOP;
                case KNIGHT -> BLACK_KNIGHT;
                case PAWN -> BLACK_PAWN;
            };
        }
    }

    private static void drawPiece(PrintStream out, int row, int column, ChessBoard board, boolean isOdd){
        ChessPiece piece = board.getPiece(new ChessPosition(row, column + 1));
        if(!isOdd){
            if(column % 2 == 0){
                out.print(SET_BG_COLOR_WHITE);
            }
            else{
                out.print(SET_BG_COLOR_DARK_GREEN);
            }
        }
        else{
            if(column % 2 == 0){

                out.print(SET_BG_COLOR_DARK_GREEN);
            }
            else{
                out.print(SET_BG_COLOR_WHITE);
            }
        }
        if(piece != null){
            String icon = getPieceIcon(piece);
            out.print(icon);
        }
        else{
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
        }

    }

//    private static void parseChessBoardRow(PrintStream out, ChessBoard board, int row, int boardCol){
//        for(int col = 1; col <= 8; ++col){
//            ChessPiece piece = board.getPiece(new ChessPosition(row, col));
//            if(piece != null){
//                drawPiece(out, row, col, piece);
//            }
//        }
//    }


    private static void setDarkGrey(PrintStream out){
        out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREY);
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
