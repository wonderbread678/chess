package chess;

import java.util.Collection;
import java.util.ArrayList;

public class RookMoveCalculator extends PieceMoveCalculator{

    public Collection <ChessMove> rookMoveCalculations(ChessPosition startPosition,
                                                       ChessPiece.PieceType promotion,
                                                       ChessGame.TeamColor color,
                                                       ChessBoard board){
        Collection <ChessMove> moveCollection = new ArrayList<ChessMove>();

        moveCollection = moveUp(startPosition, promotion, moveCollection, color, board);
        moveCollection = moveDown(startPosition, promotion, moveCollection, color, board);
        moveCollection = moveLeft(startPosition, promotion, moveCollection, color, board);
        moveCollection = moveRight(startPosition, promotion, moveCollection, color, board);

        return moveCollection;
    }
}
