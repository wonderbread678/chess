package chess;

import java.util.List;

public abstract class PieceMoveCalculator {
    private List<ChessPosition[]> moves;
    private ChessPiece piece;

    public ChessMove moves(ChessPiece piece, List<ChessPosition[]> moves){
        this.piece = piece;
        this.moves = moves;
    }

    public
}
