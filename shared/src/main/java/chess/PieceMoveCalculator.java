package chess;

import java.util.Collection;

public class PieceMoveCalculator {

    public boolean checkPiece(ChessPosition position, ChessBoard board){
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

    public Collection <ChessMove> handlePiece(ChessPosition start_position, ChessPosition new_position, ChessPiece.PieceType promotion, Collection <ChessMove> moveCollection, ChessGame.TeamColor pieceColor, ChessBoard board){
        if(board.getPiece(new_position).getTeamColor() != pieceColor){
            ChessMove new_move = new ChessMove(start_position, new_position, promotion);
            moveCollection.add(new_move);
        }
        return moveCollection;
    }

    public Collection <ChessMove> moveDiagonal(ChessPosition startPosition, Collection <ChessMove> moveCollection, ChessGame.TeamColor pieceColor, ChessBoard board){
        // Pseudo-code:
        // CM = true;
        // DIAGONAL UP AND TO THE RIGHT
        // while(CM == true){
            // Increment myPosition <1, 1> if it won't go past the edge of the board
            // if it will go past the edge of the board:
                // return CM = false
            // Otherwise, check if there is a piece at that position
                // if there is a piece, check the piece color
                    // If its an opposing piece:
                        // add the position to the moveCollection
                        // return CM = false 
                    // else
                        // return CM = false
            // If there is not a piece, and it is within the board space:
                // add the new position to moveCollection
                // return CM = true
                // CM = true;
        // DIAGONAL UP AND TO THE LEFT
        // while(CM == true){
            // Repeat, but increment myPosition <-1, 1> if it won't go past the edge of the board
        // }
        // CM = true;
        // DIAGONAL DOWN AND TO THE LEFT
        // while(CM == true){
            // Repeat, but decrement myPosition <-1, -1> if it won't go past the edge of the board
        // }
        // CM = true;
        // DIAGONAL DOWN AND TO THE RIGHT
        // while(CM == true){
            // Repeat, but decrement myPosition <1, -1> if it won't go past the edge of the board
        // }
        // return moveCollection
        // UP AND TO THE RIGHT (+1, +1)
        ChessPosition loopReset = startPosition;
        ChessPosition myPosition = loopReset;
        while(myPosition.getRow() + 1 <= 8 && myPosition.getColumn() + 1 <= 8){
            ChessPosition new_position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() +1);
            if(checkPiece(new_position, board) == false){
                ChessMove move = new ChessMove(startPosition, new_position, null);
                moveCollection.add(move);
                myPosition = new_position;
            }
            else{
               handlePiece(startPosition, new_position, null, moveCollection, pieceColor, board);
               break;
            }
        }
        myPosition = loopReset;
        // UP AND TO THE LEFT (+1, -1)
        while(myPosition.getRow() + 1 <= 8 && myPosition.getColumn() - 1 >= 1){
                ChessPosition new_position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() -1);
                if(checkPiece(new_position, board) == false){
                    ChessMove move = new ChessMove(startPosition, new_position, null);
                    moveCollection.add(move);
                    myPosition = new_position;
                }
                else {
                    handlePiece(startPosition, new_position, null, moveCollection, pieceColor, board);
                    break;
                }
        }
        myPosition = loopReset;
        // DOWN AND TO THE RIGHT (-1, +1)
        while(myPosition.getRow() - 1 >= 1 && myPosition.getColumn() + 1 <= 8){
                ChessPosition new_position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() +1);
                if(checkPiece(new_position, board) == false){
                    ChessMove move = new ChessMove(startPosition, new_position, null);
                    moveCollection.add(move);
                    myPosition = new_position;
                }
                else {
                    handlePiece(startPosition, new_position, null, moveCollection, pieceColor, board);
                    break;
                }
        }
        myPosition = loopReset;
        // DOWN AND TO THE LEFT (-1, -1)
        while(myPosition.getRow() - 1 >= 1 && myPosition.getColumn() - 1 >= 1){
                ChessPosition new_position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() -1);
                if(checkPiece(new_position, board) == false){
                    ChessMove move = new ChessMove(startPosition, new_position, null);
                    moveCollection.add(move);
                    myPosition = new_position;
                }
                else{
                   handlePiece(startPosition, new_position, null, moveCollection, pieceColor, board);
                   break;
                }
        }
        return moveCollection;
    }

    public Collection <ChessMove> moveUp(ChessPosition startposition, Collection <ChessMove> moveCollection, ChessGame.TeamColor pieceColor, ChessBoard board){
        // Pseudo-code:
        // CM = true;
        // while(CM == true){
        // increment myPosition Y-value by 1 if it won't go past the edge of the board
        // if it will go past the edge of the board:
        // return CM = false
        // Otherwise, check if there is a piece at that position
        // if there is a piece, check the piece color
        // If its an opposing piece:
        // add the position to the moveCollection
        // return CM = false
        // else
        // return CM = false
        // If there is not a piece, and it is within the board space:
        // add the new position to moveCollection
        // return CM = true
        // return moveCollection
        // }
        ChessPosition loopReset = startposition;
        ChessPosition myPosition = loopReset;
        while(myPosition.getRow() + 1 <= 8){
            ChessPosition new_position = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            if(checkPiece(new_position, board) == false){
                ChessMove move = new ChessMove(startposition, new_position, null);
                moveCollection.add(move);
                myPosition = new_position;
            }
            else {
                handlePiece(startposition, new_position, null, moveCollection, pieceColor, board);
                break;
            }
        }
        return moveCollection;
    }

    public Collection <ChessMove> moveDown(ChessPosition startposition, Collection <ChessMove> moveCollection, ChessGame.TeamColor pieceColor, ChessBoard board){
        // Pseudo-code:
        // CM = true;
        // while(CM == true){
        // Decrement myPosition Y-value by 1 if it won't go past the edge of the board
        // if it will go past the edge of the board:
        // return CM = false
        // Otherwise, check if there is a piece at that position
        // if there is a piece, check the piece color
        // If its an opposing piece:
        // add the position to the moveCollection
        // return CM = false
        // else
        // return CM = false
        // If there is not a piece, and it is within the board space:
        // add the new position to moveCollection
        // return CM = true
        // return moveCollection
        // }
        ChessPosition loopReset = startposition;
        ChessPosition myPosition = loopReset;
        while(myPosition.getRow() - 1 >= 1){
            ChessPosition new_position = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            if(checkPiece(new_position, board) == false){
                ChessMove move = new ChessMove(startposition, new_position, null);
                moveCollection.add(move);
                myPosition = new_position;
            }
            else{
                handlePiece(startposition, new_position, null, moveCollection, pieceColor, board);
                break;
            }
        }
        return moveCollection;
    }


    public Collection <ChessMove> moveLeft(ChessPosition startposition, Collection <ChessMove> moveCollection, ChessGame.TeamColor pieceColor, ChessBoard board){
        // Pseudo-code:
        // CM = true;
        // while(CM == true){
            // Decrement myPosition X-value by 1 if it won't go past the edge of the board
            // if it will go past the edge of the board:
                // return CM = false
            // Otherwise, check if there is a piece at that position
                // if there is a piece, check the piece color
                    // If its an opposing piece:
                        // add the position to the moveCollection
                        // return CM = false 
                    // else
                        // return CM = false
            // If there is not a piece, and it is within the board space:
                // add the new position to moveCollection
                // return CM = true
        // return moveCollection
        // }
        ChessPosition loopReset = startposition;
        ChessPosition myPosition = loopReset;
        while(myPosition.getColumn() - 1 >= 1){
                ChessPosition new_position = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
                if(checkPiece(new_position, board) == false){
                    ChessMove move = new ChessMove(startposition, new_position, null);
                    moveCollection.add(move);
                    myPosition = new_position;
                }
                else{
                    handlePiece(startposition, new_position, null, moveCollection, pieceColor, board);
                    break;
                }
        }
        return moveCollection;
        
    }
    public Collection <ChessMove> moveRight(ChessPosition startposition, Collection <ChessMove> moveCollection, ChessGame.TeamColor pieceColor, ChessBoard board){
        // Pseudo-code:
        // CM = true;
        // while(CM == true){
            // Increment myPosition X-value by 1 if it won't go past the edge of the board
            // if it will go past the edge of the board:
                // return CM = false
            // Otherwise, check if there is a piece at that position
                // if there is a piece, check the piece color
                    // If its an opposing piece:
                        // add the position to the moveCollection
                        // return CM = false 
                    // else
                        // return CM = false
            // If there is not a piece, and it is within the board space:
                // add the new position to moveCollection
                // return CM = true
        // return moveCollection
        // }
        ChessPosition loopReset = startposition;
        ChessPosition myPosition = loopReset;
        while(myPosition.getColumn() + 1 <= 8){
                ChessPosition new_position = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
                if(checkPiece(new_position, board) == false){
                    ChessMove move = new ChessMove(startposition, new_position, null);
                    moveCollection.add(move);
                    myPosition = new_position;
                }
                else{
                    handlePiece(startposition, new_position, null, moveCollection, pieceColor, board);
                    break;
                }
        }
        return moveCollection;
    }

    public Collection <ChessMove> moveWhitePawn(ChessPosition myPosition, ChessPiece.PieceType promotion, Collection <ChessMove> moveCollection, ChessGame.TeamColor pieceColor, ChessBoard board){
        // Pseudo-code:
        // Check if the pawn can move 1 space or two
            // Based on that, increment start-position y-value by 1 or 2
            // check if there is a piece blocking the pawn and what color that piece is
            // Otherwise, add the new position to moveCollection
        // return moveCollection
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if(row == 2){
            ChessPosition specialMove = new ChessPosition(row + 2, col);
            ChessPosition normalMove = new ChessPosition(row + 1, col);
            if(!checkPiece(specialMove, board) && !checkPiece(normalMove, board)){
                ChessMove move2 = new ChessMove(myPosition, specialMove, promotion);
                ChessMove move1 = new ChessMove(myPosition, normalMove, promotion);
                moveCollection.add(move1);
                moveCollection.add(move2);
            }
            else if(checkPiece(specialMove, board) && !checkPiece(normalMove, board)){
                ChessMove move = new ChessMove(myPosition, normalMove, promotion);
                moveCollection.add(move);
            }
        }
        else{
            if(row + 1 <= 8) {
                ChessPosition normalMove = new ChessPosition(row + 1, col);
                if (!checkPiece(normalMove, board)) {
                    ChessMove move = new ChessMove(myPosition, normalMove, promotion);
                    moveCollection.add(move);
                }
            }
        }
        if(col - 1 >= 1){
            ChessPosition diagonalLeft = new ChessPosition(row + 1, col - 1);
            if(checkPiece(diagonalLeft, board)){
                handlePiece(myPosition, diagonalLeft, promotion, moveCollection, pieceColor, board);
            }
        }
        if (col + 1 <= 8){
            ChessPosition diagonalRight = new ChessPosition(row + 1, col + 1);
            if(checkPiece(diagonalRight, board)){
                handlePiece(myPosition, diagonalRight, promotion, moveCollection, pieceColor, board);
            }
        }
        return moveCollection;
    }

    public Collection <ChessMove> moveBlackPawn(ChessPosition myPosition, ChessPiece.PieceType promotion, Collection <ChessMove> moveCollection, ChessGame.TeamColor pieceColor, ChessBoard board){
        // Pseudo-code:
        // Check if the pawn can move 1 space or two
            // Based on that, decrement start-position y-value by 1 or 2
            // check if there is a piece blocking the pawn and what color that piece is
            // Otherwise, add the new position to moveCollection
        // return moveCollection
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if(row == 7){
            ChessPosition specialMove = new ChessPosition(row - 2, col);
            ChessPosition normalMove = new ChessPosition(row - 1, col);
            if(!checkPiece(specialMove, board) && !checkPiece(normalMove, board)){
                ChessMove move2 = new ChessMove(myPosition, specialMove, promotion);
                ChessMove move1 = new ChessMove(myPosition, normalMove, promotion);
                moveCollection.add(move1);
                moveCollection.add(move2);
            }
        }
        else{
            if(row - 1 >= 1) {
                ChessPosition normalMove = new ChessPosition(row - 1, col);
                if (!checkPiece(normalMove, board)) {
                    ChessMove move = new ChessMove(myPosition, normalMove, promotion);
                    moveCollection.add(move);
                }
            }
        }
        if(col - 1 >= 1){
            ChessPosition diagonalLeft = new ChessPosition(row - 1, col - 1);
            if(checkPiece(diagonalLeft, board)){
                handlePiece(myPosition, diagonalLeft, promotion, moveCollection, pieceColor, board);
            }
        }
        if (col + 1 <= 8){
            ChessPosition diagonalRight = new ChessPosition(row - 1, col + 1);
            if(checkPiece(diagonalRight, board)){
                handlePiece(myPosition, diagonalRight, promotion, moveCollection, pieceColor, board);
            }
        }
        return moveCollection;
    }

    public Collection <ChessMove> moveKnight(ChessPosition myPosition, Collection <ChessMove> moveCollection, ChessGame.TeamColor pieceColor, ChessBoard board){
        // Pseudo-code:
        // Have two Collections: one that contains all of the possible moves a knight can move, and one that is empty that will hold possibleMoves
        // Define all of the possibilites that a knight is able to move
        // Based on the start position, check each one and see if it would go outside of the board. If it is a possiblity, add it to the possibleMoves Collection.
        // Then check if there is a piece at the possible positions and what color they are, if applicable
        // If there isn't a same-color piece, add the new position to moveCollection
        // return moveCollection
        // (+2, -1) up and to the left
        // (+2, 1) up and to the right
        // (-2, -1) down and to the left
        // (-2, 1) down and to the left
        // (1, -2) left and up one
        // (1, +2) right and up one
        // (-1, -2) left and down one
        // (-1, +2) right and down one
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if(row + 2 <= 8){
            if(col - 1 >= 1){
                ChessPosition new_move = new ChessPosition(row + 2, col -1);
                if(!checkPiece(new_move, board)){
                    ChessMove move = new ChessMove(myPosition, new_move, null);
                    moveCollection.add(move);
                }
                else{
                    handlePiece(myPosition, new_move, null, moveCollection, pieceColor, board);
                }
            }
            if(col + 1 <= 8){
                ChessPosition new_move = new ChessPosition(row + 2, col + 1);
                if(!checkPiece(new_move, board)){
                    ChessMove move = new ChessMove(myPosition, new_move, null);
                    moveCollection.add(move);
                }
                else{
                    handlePiece(myPosition, new_move, null, moveCollection, pieceColor, board);
                }
            }
        }
        if(row - 2 >= 1){
            if(col - 1 >= 1){
                ChessPosition new_move = new ChessPosition(row - 2, col - 1);
                if(!checkPiece(new_move, board)){
                    ChessMove move = new ChessMove(myPosition, new_move, null);
                    moveCollection.add(move);
                }
                else{
                    handlePiece(myPosition, new_move, null, moveCollection, pieceColor, board);
                }
            }
            if(col + 1 <= 8){
                ChessPosition new_move = new ChessPosition(row - 2, col + 1);
                if(!checkPiece(new_move, board)){
                    ChessMove move = new ChessMove(myPosition, new_move, null);
                    moveCollection.add(move);
                }
                else{
                    handlePiece(myPosition, new_move, null, moveCollection, pieceColor, board);
                }
            }
        }
        if(col + 2 <= 8){
            if(row + 1 <= 8){
                ChessPosition new_move = new ChessPosition(row + 1, col + 2);
                if(!checkPiece(new_move, board)){
                    ChessMove move = new ChessMove(myPosition, new_move, null);
                    moveCollection.add(move);
                }
                else{
                    handlePiece(myPosition, new_move, null, moveCollection, pieceColor, board);
                }

            }
            if(row - 1 >= 1){
                ChessPosition new_move = new ChessPosition(row - 1, col + 2);
                if(!checkPiece(new_move, board)){
                    ChessMove move = new ChessMove(myPosition, new_move, null);
                    moveCollection.add(move);
                }
                else{
                    handlePiece(myPosition, new_move, null, moveCollection, pieceColor, board);
                }

            }
        }
        if(col - 2 >= 1){
            if(row + 1 <= 8){
                ChessPosition new_move = new ChessPosition(row + 1, col - 2);
                if(!checkPiece(new_move, board)){
                    ChessMove move = new ChessMove(myPosition, new_move, null);
                    moveCollection.add(move);
                }
                else{
                    handlePiece(myPosition, new_move, null, moveCollection, pieceColor, board);
                }

            }
            if(row - 1 >= 1){
                ChessPosition new_move = new ChessPosition(row - 1, col - 2);
                if(!checkPiece(new_move, board)){
                    ChessMove move = new ChessMove(myPosition, new_move, null);
                    moveCollection.add(move);
                }
                else{
                    handlePiece(myPosition, new_move, null, moveCollection, pieceColor, board);
                }
                
            }
        }

        return moveCollection;
    }

    public Collection <ChessMove> moveKing(ChessPosition startposition, Collection <ChessMove> moveCollection, ChessGame.TeamColor pieceColor, ChessBoard board){
        if(startposition.getRow() + 1 <= 8){
            ChessPosition new_position = new ChessPosition(startposition.getRow() + 1, startposition.getColumn());
            if(checkPiece(new_position, board) == false){
                ChessMove move = new ChessMove(startposition, new_position, null);
                moveCollection.add(move);
            }
            else{
                handlePiece(startposition, new_position, null, moveCollection, pieceColor, board);
            }
        }
        if(startposition.getRow() - 1 >= 1){
            ChessPosition new_position = new ChessPosition(startposition.getRow() - 1, startposition.getColumn());
            if(checkPiece(new_position, board) == false){
                ChessMove move = new ChessMove(startposition, new_position, null);
                moveCollection.add(move);
            }
            else{
                handlePiece(startposition, new_position, null, moveCollection, pieceColor, board);
            }

        }
        if(startposition.getColumn() + 1 <= 8){
            ChessPosition new_position = new ChessPosition(startposition.getRow(), startposition.getColumn() + 1);
            if(checkPiece(new_position, board) == false){
                ChessMove move = new ChessMove(startposition, new_position, null);
                moveCollection.add(move);
            }
            else{
                handlePiece(startposition, new_position, null, moveCollection, pieceColor, board);
            }
        }
        if(startposition.getColumn() - 1 >= 1){
            ChessPosition new_position = new ChessPosition(startposition.getRow(), startposition.getColumn() - 1);
            if(checkPiece(new_position, board) == false){
                ChessMove move = new ChessMove(startposition, new_position, null);
                moveCollection.add(move);
            }
            else{
                handlePiece(startposition, new_position, null, moveCollection, pieceColor, board);
            }
        }
        if(startposition.getRow() + 1 <= 8 && startposition.getColumn() + 1 <= 8){
            ChessPosition new_position = new ChessPosition(startposition.getRow() + 1, startposition.getColumn() + 1);
            if(checkPiece(new_position, board) == false){
                ChessMove move = new ChessMove(startposition, new_position, null);
                moveCollection.add(move);
            }
            else{
                handlePiece(startposition, new_position, null, moveCollection, pieceColor, board);
            }
        }
        if(startposition.getRow() + 1 <= 8 && startposition.getColumn() - 1 >= 1){
            ChessPosition new_position = new ChessPosition(startposition.getRow() + 1, startposition.getColumn() - 1);
            if(checkPiece(new_position, board) == false){
                ChessMove move = new ChessMove(startposition, new_position, null);
                moveCollection.add(move);
            }
            else{
                handlePiece(startposition, new_position, null, moveCollection, pieceColor, board);
            }
        }
        if(startposition.getRow() - 1 >= 1 && startposition.getColumn() + 1 <= 8){
            ChessPosition new_position = new ChessPosition(startposition.getRow() - 1, startposition.getColumn() + 1);
            if(checkPiece(new_position, board) == false){
                ChessMove move = new ChessMove(startposition, new_position, null);
                moveCollection.add(move);
            }
            else{
                handlePiece(startposition, new_position, null, moveCollection, pieceColor, board);
            }

        }
        if(startposition.getRow() - 1 >= 1 && startposition.getColumn() - 1 >= 1){
            ChessPosition new_position = new ChessPosition(startposition.getRow() - 1, startposition.getColumn() - 1);
            if(checkPiece(new_position, board) == false){
                ChessMove move = new ChessMove(startposition, new_position, null);
                moveCollection.add(move);
            }
            else{
                handlePiece(startposition, new_position, null, moveCollection, pieceColor, board);
            }
        }
        return moveCollection;
    }
}
