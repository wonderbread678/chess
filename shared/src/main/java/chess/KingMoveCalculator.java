package chess;
import java.util.ArrayList;
import java.util.Collection;

public class KingMoveCalculator extends PieceMoveCalculator{

    public Collection <ChessMove> kingMoveCalculations(ChessBoard board,
                                                       ChessGame.TeamColor pieceColor,
                                                       ChessPosition startPosition,
                                                       ChessPiece.PieceType promotion){
        Collection <ChessMove> moveCollection = new ArrayList<ChessMove>();
        moveCollection = moveKing(startPosition, promotion, moveCollection, pieceColor, board);
        return moveCollection;
    }
}
