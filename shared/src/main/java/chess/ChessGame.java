package chess;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessGame.TeamColor currentTurn = TeamColor.WHITE;
    ChessBoard gameBoard = new ChessBoard();

    public ChessGame() {
        gameBoard.resetBoard();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessGame chessGame)) {
            return false;
        }
        return currentTurn == chessGame.currentTurn && Objects.equals(gameBoard, chessGame.gameBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentTurn, gameBoard);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        if(currentTurn == TeamColor.WHITE){
            currentTurn = TeamColor.BLACK;
        }
        else{
            currentTurn = TeamColor.BLACK;
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection <ChessMove> moves = new ArrayDeque<>();
        ChessPiece currentPiece = gameBoard.getPiece(startPosition);
        ChessGame.TeamColor colorCheck = currentPiece.getTeamColor();
        if(colorCheck == TeamColor.WHITE){
            colorCheck = TeamColor.BLACK;
        }
        else{
            colorCheck = TeamColor.WHITE;
        }
        Collection <ChessMove> unfilteredMoves = currentPiece.pieceMoves(gameBoard, startPosition);
        for(ChessMove move : unfilteredMoves){
            ChessBoard cloneBoard = getBoard().clone();
            ChessPosition endPosition = move.getEndPosition();
            gameBoard.addPiece(endPosition, currentPiece);
            gameBoard.squares[startPosition.getRow() - 1][startPosition.getColumn() - 1] = null;
            if(!isInCheck(colorCheck)){
                moves.add(move);
            }
            gameBoard = cloneBoard;

        }
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition currentPosition = move.getStartPosition();
        Collection <ChessMove> moves = gameBoard.getPiece(currentPosition).pieceMoves(gameBoard, currentPosition);
        if(gameBoard.getPiece(currentPosition) != null){
            ChessGame.TeamColor color = gameBoard.getPiece(currentPosition).getTeamColor();
            if(getTeamTurn() == color){
                if(moves.contains(move)) {
                    ChessPosition newPosition = move.getEndPosition();
                    ChessPiece piece = gameBoard.getPiece(currentPosition);
                    //            Making the move
                    gameBoard.addPiece(newPosition, piece);
                    //            Removing the old position
                    gameBoard.squares[currentPosition.getRow() - 1][currentPosition.getColumn() - 1] = null;
                    setTeamTurn(currentTurn);
                }
            }
            else{
                throw new InvalidMoveException("Invalid Move");
            }
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = kingPositionFinder(gameBoard, teamColor);
        for(int i = 1; i <= 8; ++i){
            for(int j = 1; j <= 8; ++j){
                ChessPosition currentPosition = new ChessPosition(i, j);
                if(gameBoard.getPiece(currentPosition) != null && gameBoard.getPiece(currentPosition).getTeamColor() != teamColor){
                    Collection <ChessMove> pieceMoves = gameBoard.getPiece(currentPosition).pieceMoves(gameBoard, currentPosition);
                    for(ChessMove move : pieceMoves){
                        if(move.getEndPosition() == kingPosition){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public ChessPosition kingPositionFinder(ChessBoard board, ChessGame.TeamColor color){
        ChessPosition kingPosition = null;
        for(int i = 1; i <= 8; ++i){
            for(int k = 1; k <= 8; ++k){
                ChessPosition space = new ChessPosition(i, k);
                if(board.getPiece(space) != null){
                    ChessPiece piece = board.getPiece(space);
                    if(piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color){
                        kingPosition = space;
                    }
                }
            }
        }
        return kingPosition;
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection <ChessMove> moves = new ArrayDeque<ChessMove>();
        if(isInCheck(teamColor) && moves.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection <ChessMove> moves = new ArrayDeque<ChessMove>();
        if(!isInCheck(teamColor) && moves.isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        for(int i = 1; i < 8; ++i){
            for(int j = 1; j < 8; ++j){
                ChessPosition space = new ChessPosition(i, j);
                if(board.getPiece(space) != null){
                    ChessGame.TeamColor color = board.getPiece(space).getTeamColor();
                    ChessPiece piece = new ChessPiece(color, board.getPiece(space).getPieceType());
                    gameBoard.addPiece(space, piece);
                }
            }
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }
}
