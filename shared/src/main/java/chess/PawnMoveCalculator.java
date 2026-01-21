package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveCalculator extends PieceMoveCalculator {

    public Collection <ChessMove> pawnMoveCalculator(ChessBoard board, ChessGame.TeamColor pieceColor, ChessPosition startPosition, ChessPiece.PieceType promotion){
        Collection <ChessMove> moveCollection = new ArrayList<ChessMove>();
        if(pieceColor == ChessGame.TeamColor.WHITE){
            if(startPosition.getRow() == 7){
                moveCollection = moveWhitePawn(startPosition, ChessPiece.PieceType.KNIGHT, moveCollection, pieceColor, board);
                moveCollection = moveWhitePawn(startPosition, ChessPiece.PieceType.BISHOP, moveCollection, pieceColor, board);
                moveCollection = moveWhitePawn(startPosition, ChessPiece.PieceType.QUEEN, moveCollection, pieceColor, board);
                moveCollection = moveWhitePawn(startPosition, ChessPiece.PieceType.ROOK, moveCollection, pieceColor, board);
            }
            else{
                moveCollection = moveWhitePawn(startPosition, promotion, moveCollection, pieceColor, board);
            }
        }
        else{
            if(startPosition.getRow() == 2){
                moveCollection = moveBlackPawn(startPosition, ChessPiece.PieceType.KNIGHT, moveCollection, pieceColor, board);
                moveCollection = moveBlackPawn(startPosition, ChessPiece.PieceType.BISHOP, moveCollection, pieceColor, board);
                moveCollection = moveBlackPawn(startPosition, ChessPiece.PieceType.QUEEN, moveCollection, pieceColor, board);
                moveCollection = moveBlackPawn(startPosition, ChessPiece.PieceType.ROOK, moveCollection, pieceColor, board);
            }
            else{
                moveCollection = moveBlackPawn(startPosition, promotion, moveCollection, pieceColor, board);
            }
        }
        return moveCollection;
    }
}
