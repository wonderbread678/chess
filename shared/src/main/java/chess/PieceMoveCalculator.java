package chess;

import java.util.List;

public class PieceMoveCalculator {

    private final List <ChessPosition> moveList;
    private final ChessPosition startPosition;
    private final ChessBoard board;
    private final ChessGame.TeamColor pieceColor;

    public PieceMoveCalculator (List <ChessPosition> moveList, ChessPosition startPosition, ChessPosition previousMove, ChessBoard board, ChessGame.TeamColor pieceColor){
        this.moveList = moveList;
        this.startPosition = startPosition;
        this.board = board;
        this.pieceColor = pieceColor;
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

    public List <ChessPosition> handlePiece(ChessPosition new_position, List <ChessPosition> moveList, ChessGame.TeamColor pieceColor){
        if(board.getPiece(new_position).getTeamColor() != pieceColor){
            moveList.add(new_position);
            return moveList;
        }
        else{
            return moveList;
        }
    }

    public List <ChessPosition> moveUp(ChessPosition startposition, List <ChessPosition> moveList, ChessGame.TeamColor pieceColor, ChessBoard board){
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
        while(true){
            int col = startposition.getColumn();
            int row = startposition.getRow();
            if(row + 1 <= 7){
                ChessPosition new_position = new ChessPosition(row + 1, col);
                if(checkPiece(new_position) == false){
                    moveList.add(new_position);
                }
                else{
                    return handlePiece(new_position, moveList, pieceColor);
                }
            }
            else {
                return moveList;
            }
        }
    }

    public List <ChessPosition> moveDown(ChessPosition startposition, List <ChessPosition> moveList, ChessGame.TeamColor pieceColor, ChessBoard board){
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
        while(true){
            int col = startposition.getColumn();
            int row = startposition.getRow();
            if(row - 1 >= 0){
                ChessPosition new_position = new ChessPosition(row - 1, col);
                if(checkPiece(new_position) == false){
                    moveList.add(new_position);
                }
                else{
                    return handlePiece(new_position, moveList, pieceColor);
                }
            }
            else {
                return moveList;
            }
        }
    }

    public List <ChessPosition> moveDiagonal(ChessPosition startposition, List <ChessPosition> moveList, ChessGame.TeamColor pieceColor, ChessBoard board){
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
        int col = startposition.getColumn();
        int row = startposition.getRow();
        // UP AND TO THE RIGHT (+1, +1)
        while(true){
            if(row + 1 <= 7 && col + 1 <= 7){
                ChessPosition new_position = new ChessPosition(row + 1, col +1);
                if(checkPiece(new_position) == false){
                    moveList.add(new_position);
                }
                else{
                   handlePiece(new_position, moveList, pieceColor);
                   break;
                }
            }
            else {
                break;
            }
        }
        // UP AND TO THE LEFT (+1, -1)
        while(true){
            if(row + 1 <= 7 && col - 1 >= 0){
                ChessPosition new_position = new ChessPosition(row + 1, col -1);
                if(checkPiece(new_position) == false){
                    moveList.add(new_position);
                }
                else{
                   handlePiece(new_position, moveList, pieceColor);
                   break;
                }
            }
            else {
                break;
            }
        }
        // DOWN AND TO THE RIGHT (-1, +1)
        while(true){
            if(row - 1 >= 0 && col + 1 <= 7){
                ChessPosition new_position = new ChessPosition(row - 1, col +1);
                if(checkPiece(new_position) == false){
                    moveList.add(new_position);
                }
                else{
                   handlePiece(new_position, moveList, pieceColor);
                   break;
                }
            }
            else {
                break;
            }
        }
        // DOWN AND TO THE LEFT (-1, -1)
        while(true){
            if(row - 1 >= 0 && col - 1 >= 0){
                ChessPosition new_position = new ChessPosition(row - 1, col -1);
                if(checkPiece(new_position) == false){
                    moveList.add(new_position);
                }
                else{
                   handlePiece(new_position, moveList, pieceColor);
                   break;
                }
            }
            else {
                break;
            }
        }
        return moveList;
    }


    public List <ChessPosition> moveLeft(ChessPosition startposition, List <ChessPosition> moveList, ChessGame.TeamColor pieceColor, ChessBoard board){
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
        while(true){
            int col = startposition.getColumn();
            int row = startposition.getRow();
            if(col - 1 >= 0){
                ChessPosition new_position = new ChessPosition(row, col - 1);
                if(checkPiece(new_position) == false){
                    moveList.add(new_position);
                }
                else{
                    return handlePiece(new_position, moveList, pieceColor);
                }
            }
            else {
                return moveList;
            }
        }
        
    }
    public List <ChessPosition> moveRight(ChessPosition startposition, List <ChessPosition> moveList, ChessGame.TeamColor pieceColor, ChessBoard board){
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
        while(true){
            int col = startposition.getColumn();
            int row = startposition.getRow();
            if(col + 1 <= 7){
                ChessPosition new_position = new ChessPosition(row, col + 1);
                if(checkPiece(new_position) == false){
                    moveList.add(new_position);
                }
                else{
                    return handlePiece(new_position, moveList, pieceColor);
                }
            }
            else {
                return moveList;
            }
        }
    }

    public List <ChessPosition> moveWhitePawn(ChessPosition startposition, List <ChessPosition> moveList, ChessGame.TeamColor pieceColor, ChessBoard board, ChessPosition previousMove){
        // Pseudo-code:
        // Check if the pawn can move 1 space or two
            // Based on that, increment start-position y-value by 1 or 2
            // check if there is a piece blocking the pawn and what color that piece is
            // Otherwise, add the new position to moveList
        // return moveList
        int row = startposition.getRow();
        int col = startposition.getColumn();
        if(row == 1){
            ChessPosition specialMove = new ChessPosition(row + 2, col);
            ChessPosition normalMove = new ChessPosition(row + 1, col);
            if(checkPiece(specialMove) == false && checkPiece(normalMove) == false){
                moveList.add(specialMove);
                moveList.add(normalMove);
            }
        }
        else{
            if(row + 1 <= 7){
                ChessPosition normalMove = new ChessPosition(row + 1, col);
                if(checkPiece(normalMove) == false){
                    moveList.add(normalMove);
                }
                if(col - 1 >= 0){
                    ChessPosition diagonalLeft = new ChessPosition(row + 1, col - 1);
                    if(checkPiece(diagonalLeft) == true){
                        return handlePiece(diagonalLeft, moveList, pieceColor);
                    } 
                }
                if (col + 1 <= 7){
                    ChessPosition diagonalRight = new ChessPosition(row + 1, col + 1); 
                    if(checkPiece(diagonalRight) == true){
                        return handlePiece(diagonalRight, moveList, pieceColor);
                    }
                }
            }
        }
        return moveList;
    }

    public List <ChessPosition> moveBlackPawn(ChessPosition startposition, List <ChessPosition> moveList, ChessGame.TeamColor pieceColor, ChessBoard board){
        // Pseudo-code:
        // Check if the pawn can move 1 space or two
            // Based on that, decrement start-position y-value by 1 or 2
            // check if there is a piece blocking the pawn and what color that piece is
            // Otherwise, add the new position to moveList
        // return moveList
        int row = startposition.getRow();
        int col = startposition.getColumn();
        if(row == 6){
            ChessPosition specialMove = new ChessPosition(row - 2, col);
            ChessPosition normalMove = new ChessPosition(row - 1, col);
            if(checkPiece(specialMove) == false && checkPiece(normalMove) == false){
                moveList.add(specialMove);
                moveList.add(normalMove);
            }
        }
        else{
            if(row - 1 >= 0){
                ChessPosition normalMove = new ChessPosition(row - 1, col);
                if(checkPiece(normalMove) == false){
                    moveList.add(normalMove);
                }
                if(col - 1 >= 0){
                    ChessPosition diagonalLeft = new ChessPosition(row - 1, col - 1);
                    if(checkPiece(diagonalLeft) == true){
                        return handlePiece(diagonalLeft, moveList, pieceColor);
                    } 
                }
                if (col + 1 <= 7){
                    ChessPosition diagonalRight = new ChessPosition(row - 1, col + 1); 
                    if(checkPiece(diagonalRight) == true){
                        return handlePiece(diagonalRight, moveList, pieceColor);
                    }
                }
            }
        }
        return moveList;
    }

    public List <ChessPosition> moveKnight(ChessPosition startposition, List <ChessPosition> moveList, ChessGame.TeamColor pieceColor, ChessBoard board){
        // Pseudo-code:
        // Have two lists: one that contains all of the possible moves a knight can move, and one that is empty that will hold possibleMoves
        // Define all of the possibilites that a knight is able to move
        // Based on the start position, check each one and see if it would go outside of the board. If it is a possiblity, add it to the possibleMoves list.
        // Then check if there is a piece at the possible positions and what color they are, if applicable
        // If there isn't a same-color piece, add the new position to moveList
        // return moveList
        // (+2, -1) up and to the left
        // (+2, 1) up and to the right
        // (-2, -1) down and to the left
        // (-2, 1) down and to the left
        // (1, -2) left and up one
        // (-1, -2) left and down one
        // (1, +2) right and up one
        // (-1, +2) right and down one

        throw new RuntimeException("Not implemented");
    }
}
