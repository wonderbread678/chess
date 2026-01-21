package chess;
import java.util.ArrayList;
import java.util.Collection;

public class KnightPieceCalculator extends PieceMoveCalculator{

    public Collection <ChessMove> knightMoveCalculator(ChessBoard board, ChessGame.TeamColor pieceColor, ChessPosition startPosition){
        Collection <ChessMove> moveCollection = new ArrayList<ChessMove>();
        moveCollection = moveKnight(startPosition, moveCollection, pieceColor, board);
        return moveCollection;
    }
}
