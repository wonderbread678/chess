package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveCalculator extends PieceMoveCalculator{

    public Collection<ChessMove> knightMoveCalculations(ChessPosition startPosition,
                                                        ChessPiece.PieceType promotion,
                                                        ChessGame.TeamColor color,
                                                        ChessBoard board){
        Collection <ChessMove> moveCollection = new ArrayList<ChessMove>();

        moveCollection = moveKnight(startPosition, promotion, moveCollection, color, board);

        return moveCollection;
    }
}
