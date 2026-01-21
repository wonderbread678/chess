package chess;
import java.util.ArrayList;
import java.util.Collection;

public class KingMoveCalculator extends PieceMoveCalculator{

    public Collection <ChessMove> kingMoveCalculator(ChessBoard board, ChessGame.TeamColor pieceColor, ChessPosition startPosition){
        Collection <ChessMove> moveCollection = new ArrayList<ChessMove>();
        moveCollection = moveKing(startPosition, moveCollection, pieceColor, board);
        return moveCollection;
    }
}
