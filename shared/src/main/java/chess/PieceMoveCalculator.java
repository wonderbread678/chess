package chess;
import java.util.Collection;

public class PieceMoveCalculator {

    public boolean ifPiece(ChessPosition position, ChessBoard board){
        if(board.getPiece(position) == null){
            return false;
        }
        else{
            return true;
        }
    }

    public Collection <ChessMove> handlePiece(ChessPosition startPosition, ChessPosition newPosition, ChessPiece.PieceType promotion, Collection <ChessMove> moveCollection, ChessGame.TeamColor color, ChessBoard board){
        if(board.getPiece(newPosition).getTeamColor() != color){
            ChessMove move = new ChessMove(startPosition, newPosition, promotion);
            moveCollection.add(move);
        }
        return moveCollection;
    }

    public Collection <ChessMove> handleMove(ChessPosition startPosition, ChessPosition newPosition, ChessPiece.PieceType promotion, Collection <ChessMove> moveCollection, ChessGame.TeamColor color, ChessBoard board){
        if(!ifPiece(newPosition, board)){
            ChessMove move = new ChessMove(startPosition, newPosition, promotion);
            moveCollection.add(move);
        }
        else{
            handlePiece(startPosition, newPosition, promotion, moveCollection, color, board);
        }
        return moveCollection;
    }

    public Collection <ChessMove> moveDiagonal(ChessPosition startPosition, ChessPiece.PieceType promotion, Collection <ChessMove> moveCollection, ChessGame.TeamColor color, ChessBoard board){
        ChessPosition myPosition = startPosition;
//        Move up and right
        while(myPosition.getRow() + 1 <= 8 && myPosition.getColumn() + 1 <= 8){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            if(!ifPiece(newPosition, board)){
                ChessMove move = new ChessMove(startPosition, newPosition, promotion);
                moveCollection.add(move);
                myPosition = newPosition;
            }
            else{
                handlePiece(startPosition, newPosition, promotion, moveCollection, color, board);
                break;
            }
        }
//        Move up and left
        myPosition = startPosition;
        while(myPosition.getRow() + 1 <= 8 && myPosition.getColumn() - 1 >= 1){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            if(!ifPiece(newPosition, board)){
                ChessMove move = new ChessMove(startPosition, newPosition, promotion);
                moveCollection.add(move);
                myPosition = newPosition;
            }
            else{
                handlePiece(startPosition, newPosition, promotion, moveCollection, color, board);
                break;
            }

        }
//        Move down and right
        myPosition = startPosition;
        while(myPosition.getRow() - 1 >= 1 && myPosition.getColumn() + 1 <= 8){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            if(!ifPiece(newPosition, board)){
                ChessMove move = new ChessMove(startPosition, newPosition, promotion);
                moveCollection.add(move);
                myPosition = newPosition;
            }
            else{
                handlePiece(startPosition, newPosition, promotion, moveCollection, color, board);
                break;
            }
        }
//        Move down and left
        myPosition = startPosition;
        while(myPosition.getRow() - 1 >= 1 && myPosition.getColumn() - 1 >= 1){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            if(!ifPiece(newPosition, board)){
                ChessMove move = new ChessMove(startPosition, newPosition, promotion);
                moveCollection.add(move);
                myPosition = newPosition;
            }
            else{
                handlePiece(startPosition, newPosition, promotion, moveCollection, color, board);
                break;
            }
        }
        return moveCollection;
    }

    public Collection <ChessMove> moveUp(ChessPosition startPosition, ChessPiece.PieceType promotion, Collection <ChessMove> moveCollection, ChessGame.TeamColor color, ChessBoard board){
        ChessPosition myPosition = startPosition;
        while(myPosition.getRow() + 1 <= 8){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            if(!ifPiece(newPosition, board)){
                ChessMove move = new ChessMove(startPosition, newPosition, promotion);
                moveCollection.add(move);
                myPosition = newPosition;
            }
            else{
                handlePiece(startPosition, newPosition, promotion, moveCollection, color, board);
                break;
            }
        }
        return moveCollection;
    }

    public Collection <ChessMove> moveDown(ChessPosition startPosition, ChessPiece.PieceType promotion, Collection <ChessMove> moveCollection, ChessGame.TeamColor color, ChessBoard board){
        ChessPosition myPosition = startPosition;
        while(myPosition.getRow() - 1 >= 1){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            if(!ifPiece(newPosition, board)){
                ChessMove move = new ChessMove(startPosition, newPosition, promotion);
                moveCollection.add(move);
                myPosition = newPosition;
            }
            else{
                handlePiece(startPosition, newPosition, promotion, moveCollection, color, board);
                break;
            }
        }
        return moveCollection;
    }

    public Collection <ChessMove> moveLeft(ChessPosition startPosition, ChessPiece.PieceType promotion, Collection <ChessMove> moveCollection, ChessGame.TeamColor color, ChessBoard board){
        ChessPosition myPosition = startPosition;
        while(myPosition.getColumn() - 1 >= 1){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
            if(!ifPiece(newPosition, board)){
                ChessMove move = new ChessMove(startPosition, newPosition, promotion);
                moveCollection.add(move);
                myPosition = newPosition;
            }
            else{
                handlePiece(startPosition, newPosition, promotion, moveCollection, color, board);
                break;
            }
        }
        return moveCollection;
    }

    public Collection <ChessMove> moveRight(ChessPosition startPosition, ChessPiece.PieceType promotion, Collection <ChessMove> moveCollection, ChessGame.TeamColor color, ChessBoard board){
        ChessPosition myPosition = startPosition;
        while(myPosition.getColumn() + 1 <= 8){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
            if(!ifPiece(newPosition, board)){
                ChessMove move = new ChessMove(startPosition, newPosition, promotion);
                moveCollection.add(move);
                myPosition = newPosition;
            }
            else{
                handlePiece(startPosition, newPosition, promotion, moveCollection, color, board);
                break;
            }
        }
        return moveCollection;
    }

    public Collection <ChessMove> moveKing(ChessPosition startPosition, ChessPiece.PieceType promotion, Collection <ChessMove> moveCollection, ChessGame.TeamColor color, ChessBoard board){
        if(startPosition.getRow() + 1 <= 8){
            ChessPosition newPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
            handleMove(startPosition, newPosition, promotion, moveCollection, color, board);
        }
        if(startPosition.getRow() - 1 >= 1){
            ChessPosition newPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn());
            handleMove(startPosition, newPosition, promotion, moveCollection, color, board);
        }
        if(startPosition.getColumn() + 1 <= 8){
            ChessPosition newPosition = new ChessPosition(startPosition.getRow(), startPosition.getColumn() + 1);
            handleMove(startPosition, newPosition, promotion, moveCollection, color, board);
        }
        if(startPosition.getColumn() - 1 >= 1){
            ChessPosition newPosition = new ChessPosition(startPosition.getRow(), startPosition.getColumn() - 1);
            handleMove(startPosition, newPosition, promotion, moveCollection, color, board);
        }
        if(startPosition.getRow() + 1 <= 8 && startPosition.getColumn() + 1 <= 8){
            ChessPosition newPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
            handleMove(startPosition, newPosition, promotion, moveCollection, color, board);
        }
        if(startPosition.getRow() + 1 <= 8 && startPosition.getColumn() - 1 >= 1){
            ChessPosition newPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 1);
            handleMove(startPosition, newPosition, promotion, moveCollection, color, board);
        }
        if(startPosition.getRow() - 1 >= 1 && startPosition.getColumn() + 1 <= 8){
            ChessPosition newPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 1);
            handleMove(startPosition, newPosition, promotion, moveCollection, color, board);
        }
        if(startPosition.getRow() - 1 >= 1 && startPosition.getColumn() - 1 >= 1){
            ChessPosition newPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 1);
            handleMove(startPosition, newPosition, promotion, moveCollection, color, board);
        }
        return moveCollection;
    }

    public Collection <ChessMove> moveKnight(ChessPosition startPosition, ChessPiece.PieceType promotion, Collection <ChessMove> moveCollection, ChessGame.TeamColor color, ChessBoard board){
        if(startPosition.getRow() + 2 <= 8 && startPosition.getColumn() + 1 <= 8){
            ChessPosition newPosition = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn() + 1);
            handleMove(startPosition, newPosition, promotion, moveCollection, color, board);
        }
        if(startPosition.getRow() + 2 <= 8 && startPosition.getColumn() - 1 >= 1){
            ChessPosition newPosition = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn() - 1);
            handleMove(startPosition, newPosition, promotion, moveCollection, color, board);
        }
        if(startPosition.getRow() - 2 >= 1 && startPosition.getColumn() + 1 <= 8){
            ChessPosition newPosition = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() + 1);
            handleMove(startPosition, newPosition, promotion, moveCollection, color, board);
        }
        if(startPosition.getRow() - 2 >= 1 && startPosition.getColumn() - 1 >= 1){
            ChessPosition newPosition = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn() - 1);
            handleMove(startPosition, newPosition, promotion, moveCollection, color, board);
        }
        if(startPosition.getRow() + 1 <= 8 && startPosition.getColumn() + 2 <= 8){
            ChessPosition newPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 2);
            handleMove(startPosition, newPosition, promotion, moveCollection, color, board);
        }
        if(startPosition.getRow() - 1 >= 1 && startPosition.getColumn() + 2 <= 8){
            ChessPosition newPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 2);
            handleMove(startPosition, newPosition, promotion, moveCollection, color, board);
        }
        if(startPosition.getRow() + 1 <= 8 && startPosition.getColumn() - 2 >= 1){
            ChessPosition newPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 2);
            handleMove(startPosition, newPosition, promotion, moveCollection, color, board);
        }
        if(startPosition.getRow() - 1 >= 1 && startPosition.getColumn() - 2 >= 1){
            ChessPosition newPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 2);
            handleMove(startPosition, newPosition, promotion, moveCollection, color, board);
        }
        return moveCollection;
    }

    public Collection <ChessMove> moveWhitePawn(ChessPosition startPosition, ChessPiece.PieceType promotion, Collection <ChessMove> moveCollection, ChessGame.TeamColor color, ChessBoard board){
        if(startPosition.getRow() + 1 <= 8){
            ChessPosition normalPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
            if(!ifPiece(normalPosition, board)){
                ChessMove normalMove = new ChessMove(startPosition, normalPosition, promotion);
                moveCollection.add(normalMove);
                if(startPosition.getRow() == 2){
                    ChessPosition specialPosition = new ChessPosition(startPosition.getRow() + 2, startPosition.getColumn());
                    if(!ifPiece(specialPosition, board) && !ifPiece(normalPosition, board)){
                        ChessMove specialMove = new ChessMove(startPosition, specialPosition, promotion);
                        moveCollection.add(specialMove);
                    }
                }
            }
        }
        if(startPosition.getRow() + 1 <= 8 && startPosition.getColumn() + 1 <= 8){
            ChessPosition d1 = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
            if(ifPiece(d1, board)){
                handlePiece(startPosition, d1, promotion, moveCollection, color, board);
            }
        }
        if(startPosition.getRow() + 1 <= 8 && startPosition.getColumn() - 1 >= 1){
            ChessPosition d2 = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 1);
            if(ifPiece(d2, board)){
                handlePiece(startPosition, d2, promotion, moveCollection, color, board);
            }
        }

        return moveCollection;
    }

    public Collection <ChessMove> moveBlackPawn(ChessPosition startPosition, ChessPiece.PieceType promotion, Collection <ChessMove> moveCollection, ChessGame.TeamColor color, ChessBoard board){
        if(startPosition.getRow() - 1 >= 1){
            ChessPosition normalPosition = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn());
            if(!ifPiece(normalPosition, board)){
                ChessMove normalMove = new ChessMove(startPosition, normalPosition, promotion);
                moveCollection.add(normalMove);
                if(startPosition.getRow() == 7){
                    ChessPosition specialPosition = new ChessPosition(startPosition.getRow() - 2, startPosition.getColumn());
                    if(!ifPiece(specialPosition, board) && !ifPiece(normalPosition, board)){
                        ChessMove specialMove = new ChessMove(startPosition, specialPosition, promotion);
                        moveCollection.add(specialMove);
                    }
                }
            }
        }
        if(startPosition.getRow() - 1 >= 1 && startPosition.getColumn() + 1 <= 8){
            ChessPosition d1 = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 1);
            if(ifPiece(d1, board)){
                handlePiece(startPosition, d1, promotion, moveCollection, color, board);
            }
        }
        if(startPosition.getRow() - 1 >= 1 && startPosition.getColumn() - 1 >= 1){
            ChessPosition d2 = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 1);
            if(ifPiece(d2, board)){
                handlePiece(startPosition, d2, promotion, moveCollection, color, board);
            }
        }
        return moveCollection;
    }
}
