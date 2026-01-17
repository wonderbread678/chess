package chess;

import java.util.List;

public class PieceMoveCalculator {

    private final ChessPosition [] moveList;
    private final ChessPosition startPosition;
    private final ChessPosition previousMove;

    public PieceMoveCalculator (ChessPosition [] moveList, ChessPosition startPosition, ChessPosition previousMove){
        this.moveList = moveList;
        this.startPosition = startPosition;
        this.previousMove = previousMove;
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
                // CM = true;
        // DIAGONAL UP AND TO THE LEFT
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
        // }
        // CM = true;
        // DIAGONAL DOWN AND TO THE LEFT
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
        // }
        // CM = true;
        // DIAGONAL DOWN AND TO THE RIGHT
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
        // }
        // return moveList
        
        
        throw new RuntimeException("Not implemented");
    }


    public ChessPosition moveLeft(ChessPosition startPosition){
        // Pseudo-code:

        throw new RuntimeException("Not implemented");
    }
    public ChessPosition moveRight(ChessPosition startPosition){
        // Pseudo-code:

        throw new RuntimeException("Not implemented");
    }

    public ChessPosition moveWhitePawn(ChessPosition startPosition, ChessPosition previousMove){
        // Pseudo-code:

        throw new RuntimeException("Not implemented");
    }

    public ChessPosition moveBlackPawn(ChessPosition startPosition, ChessPosition previousMove){
        // Pseudo-code:

        throw new RuntimeException("Not implemented");
    }

    public ChessPosition moveKnight(ChessPosition startPosition){
        // Pseudo-code:

        throw new RuntimeException("Not implemented");
    }
}
