package chess;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if(board.getPiece(myPosition).getPieceType() == PieceType.BISHOP){
            ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
            BishopMoveCalculator moves = new BishopMoveCalculator();
            return moves.bishopMoveCalculations(myPosition, null, color, board);
        }
        else if(board.getPiece(myPosition).getPieceType() == PieceType.ROOK){
            ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
            RookMoveCalculator moves = new RookMoveCalculator();
            return moves.rookMoveCalculations(myPosition, null, color, board);
        }
        else if(board.getPiece(myPosition).getPieceType() == PieceType.QUEEN){
            ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
            QueenMoveCalculator moves = new QueenMoveCalculator();
            return moves.queenMoveCalculations(myPosition, null, color, board);
        }
        else if(board.getPiece(myPosition).getPieceType() == PieceType.KING){
            ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
            KingMoveCalculator moves = new KingMoveCalculator();
            return moves.kingMoveCalculations(board, color, myPosition,null);
        }
        else if(board.getPiece(myPosition).getPieceType() == PieceType.KNIGHT){
            ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
            KnightMoveCalculator moves = new KnightMoveCalculator();
            return moves.knightMoveCalculations(myPosition, null, color, board);
        }
        else if(board.getPiece(myPosition).getPieceType() == PieceType.PAWN){
            ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
            PawnMoveCalculator moves = new PawnMoveCalculator();
            return moves.pawnMoveCalculations(myPosition, null, color, board);
        }

        return List.of();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessPiece that)) {
            return false;
        }
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
