package chess;
import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalculator extends PieceMoveCalculator{

    public Collection <ChessMove> rookMoveCalculator(ChessBoard board, ChessGame.TeamColor pieceColor, ChessPosition startPosition){
        Collection <ChessMove> moveCollection = new ArrayList<ChessMove>();
        Collection <ChessMove> upMoves = new ArrayList<ChessMove>();
        Collection <ChessMove> downMoves = new ArrayList<ChessMove>();
        Collection <ChessMove> leftMoves = new ArrayList<ChessMove>();
        Collection <ChessMove> rightMoves = new ArrayList<ChessMove>();

        upMoves = moveUp(startPosition, moveCollection, pieceColor, board);
        downMoves = moveDown(startPosition, moveCollection, pieceColor, board);
        leftMoves = moveLeft(startPosition, moveCollection, pieceColor, board);
        rightMoves = moveRight(startPosition, moveCollection, pieceColor, board);

        return moveCollection;
    }
}
