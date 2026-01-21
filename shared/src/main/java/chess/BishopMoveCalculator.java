package chess;
import java.util.ArrayList;
import java.util.Collection;

public class BishopMoveCalculator extends PieceMoveCalculator {

    public Collection <ChessMove> bishopMoveCalculations(ChessBoard board, ChessGame.TeamColor pieceColor, ChessPosition startPosition){
        Collection <ChessMove> moveCollection = new ArrayList<ChessMove>();
        moveCollection = moveDiagonal(startPosition, moveCollection, pieceColor, board);
        return moveCollection;
    }
}
