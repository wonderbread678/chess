package chess;
import java.util.ArrayList;
import java.util.List;

public class BishopMoveCalculator extends PieceMoveCalculator {

    public List <ChessPosition> bishopMoveCalculations(ChessBoard board, ChessGame.TeamColor pieceColor, ChessPosition startPosition){
        List <ChessPosition> moveList = new ArrayList<ChessPosition>();
        moveList = moveDiagonal(startPosition, moveList, pieceColor, board);
        return moveList;
    }
}
