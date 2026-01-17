package chess;

import java.util.List;

public class PieceMoveCalculator {

    private final ChessPosition [] moveList;
    private final ChessPosition startPosition;
    private final ChessPosition previousMove;

    public PieceMoveCalculator (ChessPosition [] moveList, ChessPosition startPosition, ChessPosition previousMove){
        this.moveList = moveList;
        this.startPosition = startPosition;
        this.previousMove = previousMove;
    }

    public ChessPosition moveUpAndDown(ChessPosition startposition){
        // Pseudo-code:

        throw new RuntimeException("Not implemented");
    }

    public ChessPosition moveDiagonal(ChessPosition startposition){
        // Pseudo-code:
        
        throw new RuntimeException("Not implemented");
    }

    public ChessPosition moveLeftAndRight(ChessPosition startPosition){
        // Pseudo-code:

        throw new RuntimeException("Not implemented");
    }

    public ChessPosition movePawn(ChessPosition startPosition, ChessPosition previousMove){
        // Pseudo-code:

        throw new RuntimeException("Not implemented");
    }

    public ChessPosition moveKnight(ChessPosition startPosition){
        // Pseudo-code:

        throw new RuntimeException("Not implemented");
    }
}
