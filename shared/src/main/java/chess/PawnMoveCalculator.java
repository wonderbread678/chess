package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveCalculator extends PieceMoveCalculator{

    public Collection<ChessMove> pawnMoveCalculations(ChessPosition startPosition, ChessPiece.PieceType promotion, ChessGame.TeamColor color, ChessBoard board){
        Collection <ChessMove> moveCollection = new ArrayList<ChessMove>();

        if(color == ChessGame.TeamColor.WHITE){
            if(startPosition.getRow() == 7){
                moveCollection = moveWhitePawn(startPosition, ChessPiece.PieceType.ROOK, moveCollection, color, board);
                moveCollection = moveWhitePawn(startPosition, ChessPiece.PieceType.BISHOP, moveCollection, color, board);
                moveCollection = moveWhitePawn(startPosition, ChessPiece.PieceType.QUEEN, moveCollection, color, board);
                moveCollection = moveWhitePawn(startPosition, ChessPiece.PieceType.KNIGHT, moveCollection, color, board);
            }
            else{
                moveCollection = moveWhitePawn(startPosition, null, moveCollection, color, board);
            }
        }
        else{
            if(startPosition.getRow() == 2){
                moveCollection = moveBlackPawn(startPosition, ChessPiece.PieceType.ROOK, moveCollection, color, board);
                moveCollection = moveBlackPawn(startPosition, ChessPiece.PieceType.BISHOP, moveCollection, color, board);
                moveCollection = moveBlackPawn(startPosition, ChessPiece.PieceType.QUEEN, moveCollection, color, board);
                moveCollection = moveBlackPawn(startPosition, ChessPiece.PieceType.KNIGHT, moveCollection, color, board);
            }
            else{
                moveCollection = moveBlackPawn(startPosition, null, moveCollection, color, board);
            }
        }

        return moveCollection;
    }
}
