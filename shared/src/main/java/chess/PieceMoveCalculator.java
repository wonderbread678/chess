package chess;

import java.util.List;

public class PieceMoveCalculator {

    private final ChessPosition [] moveList;
    private final ChessPosition startPosition;
    private final ChessPosition previousMove;
    private final ChessBoard board;

    public PieceMoveCalculator (ChessPosition [] moveList, ChessPosition startPosition, ChessPosition previousMove, ChessBoard board){
        this.moveList = moveList;
        this.startPosition = startPosition;
        this.previousMove = previousMove;
        this.board = board;
    }

    public boolean checkPiece(ChessPosition position){
        // Psuedo-code
        // Based on the given position, check if there is a piece there
        // If it is empty, return false
        // If there is a piece, return true
        if(board.getPiece(position) != null){
            return true;
        }
        else{
            return false;
        }
    }

    public ChessPosition moveUp(ChessPosition startposition, ChessPosition [] moveList){
        // Pseudo-code:
        // CM = true;
        // while(CM == true){
            // increment startPosition Y-value by 1 if it won't go past the edge of the board
            // if it will go past the edge of the board:
                // return CM = false
            // Otherwise, check if there is a piece at that position
                // if there is a piece, check the piece color
                    // If its an opposing piece:
                        // add the position to the moveList
                        // return CM = false
                    // else
                        // return CM = false
            // If there is not a piece, and it is within the board space:
                // add the new position to moveList
                // return CM = true
        // return moveList
        // }
        

        throw new RuntimeException("Not implemented");
    }

    public ChessPosition moveDown(ChessPosition startposition){
        // Pseudo-code:
        // CM = true;
        // while(CM == true){
            // Decrement startPosition Y-value by 1 if it won't go past the edge of the board
            // if it will go past the edge of the board:
                // return CM = false
            // Otherwise, check if there is a piece at that position
                // if there is a piece, check the piece color
                    // If its an opposing piece:
                        // add the position to the moveList
                        // return CM = false 
                    // else
                        // return CM = false
            // If there is not a piece, and it is within the board space:
                // add the new position to moveList
                // return CM = true
        // return moveList
        // }

        throw new RuntimeException("Not implemented");
    }

    public ChessPosition moveDiagonal(ChessPosition startposition){
        // Pseudo-code:
        // CM = true;
        // DIAGONAL UP AND TO THE RIGHT
        // while(CM == true){
            // Increment startPosition <1, 1> if it won't go past the edge of the board
            // if it will go past the edge of the board:
                // return CM = false
            // Otherwise, check if there is a piece at that position
                // if there is a piece, check the piece color
                    // If its an opposing piece:
                        // add the position to the moveList
                        // return CM = false 
                    // else
                        // return CM = false
            // If there is not a piece, and it is within the board space:
                // add the new position to moveList
                // return CM = true
                // CM = true;
        // DIAGONAL UP AND TO THE LEFT
        // while(CM == true){
            // Repeat, but increment startPosition <-1, 1> if it won't go past the edge of the board
        // }
        // CM = true;
        // DIAGONAL DOWN AND TO THE LEFT
        // while(CM == true){
            // Repeat, but decrement startPosition <-1, -1> if it won't go past the edge of the board
        // }
        // CM = true;
        // DIAGONAL DOWN AND TO THE RIGHT
        // while(CM == true){
            // Repeat, but decrement startPosition <1, -1> if it won't go past the edge of the board
        // }
        // return moveList
        
        throw new RuntimeException("Not implemented");
    }


    public ChessPosition moveLeft(ChessPosition startPosition){
        // Pseudo-code:
        // CM = true;
        // while(CM == true){
            // Decrement startPosition X-value by 1 if it won't go past the edge of the board
            // if it will go past the edge of the board:
                // return CM = false
            // Otherwise, check if there is a piece at that position
                // if there is a piece, check the piece color
                    // If its an opposing piece:
                        // add the position to the moveList
                        // return CM = false 
                    // else
                        // return CM = false
            // If there is not a piece, and it is within the board space:
                // add the new position to moveList
                // return CM = true
        // return moveList
        // }

        throw new RuntimeException("Not implemented");
    }
    public ChessPosition moveRight(ChessPosition startPosition){
        // Pseudo-code:
        // CM = true;
        // while(CM == true){
            // Increment startPosition X-value by 1 if it won't go past the edge of the board
            // if it will go past the edge of the board:
                // return CM = false
            // Otherwise, check if there is a piece at that position
                // if there is a piece, check the piece color
                    // If its an opposing piece:
                        // add the position to the moveList
                        // return CM = false 
                    // else
                        // return CM = false
            // If there is not a piece, and it is within the board space:
                // add the new position to moveList
                // return CM = true
        // return moveList
        // }

        throw new RuntimeException("Not implemented");
    }

    public ChessPosition moveWhitePawn(ChessPosition startPosition, ChessPosition previousMove){
        // Pseudo-code:
        // Check if the pawn can move 1 space or two
            // Based on that, increment start-position y-value by 1 or 2
            // check if there is a piece blocking the pawn and what color that piece is
            // Otherwise, add the new position to moveList
        // return moveList

        throw new RuntimeException("Not implemented");
    }

    public ChessPosition moveBlackPawn(ChessPosition startPosition, ChessPosition previousMove){
        // Pseudo-code:
        // Check if the pawn can move 1 space or two
            // Based on that, decrement start-position y-value by 1 or 2
            // check if there is a piece blocking the pawn and what color that piece is
            // Otherwise, add the new position to moveList
        // return moveList

        throw new RuntimeException("Not implemented");
    }

    public ChessPosition moveKnight(ChessPosition startPosition){
        // Pseudo-code:
        // Have two lists: one that contains all of the possible moves a knight can move, and one that is empty that will hold possibleMoves
        // Define all of the possibilites that a knight is able to move
        // Based on the start position, check each one and see if it would go outside of the board. If it is a possiblity, add it to the possibleMoves list.
        // Then check if there is a piece at the possible positions and what color they are, if applicable
        // If there isn't a same-color piece, add the new position to moveList
        // return moveList

        throw new RuntimeException("Not implemented");
    }
}
