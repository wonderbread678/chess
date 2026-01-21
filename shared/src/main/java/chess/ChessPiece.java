package chess;

import java.util.Collection;
import java.util.List;

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
        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == PieceType.BISHOP){
            ChessGame.TeamColor color = piece.getTeamColor();
            BishopMoveCalculator moves = new BishopMoveCalculator();
            return moves.bishopMoveCalculations(board, color, myPosition);
        }
        else if(piece.getPieceType() == PieceType.ROOK){
            ChessGame.TeamColor color = piece.getTeamColor();
            RookMoveCalculator moves = new RookMoveCalculator();
            return moves.rookMoveCalculator(board, color, myPosition);
        }
        else if(piece.getPieceType() == PieceType.QUEEN){
            ChessGame.TeamColor color = piece.getTeamColor();
            QueenMoveCalculator moves = new QueenMoveCalculator();
            return moves.queenMoveCalculator(board, color, myPosition);
        }
        else if(piece.getPieceType() == PieceType.PAWN){
            ChessGame.TeamColor color = piece.getTeamColor();
            PawnMoveCalculator moves = new PawnMoveCalculator();
            return moves.pawnMoveCalculator(board, color, myPosition, null);
        }
        else if(piece.getPieceType() == PieceType.KNIGHT){
            ChessGame.TeamColor color = piece.getTeamColor();
            KnightPieceCalculator moves = new KnightPieceCalculator();
            return moves.knightMoveCalculator(board, color, myPosition);
        }
        else if(piece.getPieceType() == PieceType.KING){
            ChessGame.TeamColor color = piece.getTeamColor();
            KingMoveCalculator moves = new KingMoveCalculator();
            return moves.kingMoveCalculator(board, color, myPosition);
        }
        return List.of();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pieceColor == null) ? 0 : pieceColor.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChessPiece other = (ChessPiece) obj;
        if (pieceColor != other.pieceColor)
            return false;
        if (type != other.type)
            return false;
        return true;
    }
//    I Just need to push this
}
