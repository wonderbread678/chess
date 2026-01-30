package chess;

import java.util.Collection;
import java.util.ArrayList;

public class BishopMoveCalculator extends PieceMoveCalculator{

    public Collection <ChessMove> bishopMoveCalculations(ChessPosition startPosition, ChessPiece.PieceType promotion, ChessGame.TeamColor color, ChessBoard board){
        Collection <ChessMove> moveCollection = new ArrayList<ChessMove>();
        moveCollection = moveDiagonal(startPosition, promotion, moveCollection, color, board);
        return moveCollection;
    }
}
