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
        currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition currentPosition = move.getStartPosition();
        if(gameBoard.getPiece(currentPosition) == null){
            throw new InvalidMoveException("No piece");
        }
        else if(gameBoard.getPiece(currentPosition) != null){
            Collection <ChessMove> moves = validMoves(currentPosition);
            ChessBoard defaultBoard = gameBoard.clone();
            ChessGame.TeamColor color = gameBoard.getPiece(currentPosition).getTeamColor();
            if(getTeamTurn() == color){
                if(moves.contains(move)) {
                    ChessPosition newPosition = move.getEndPosition();
                    ChessPiece piece = gameBoard.getPiece(currentPosition);
                    //            Making the move
                    gameBoard.addPiece(newPosition, piece);
                    //            Removing the old position
                    gameBoard.squares[currentPosition.getRow() - 1][currentPosition.getColumn() - 1] = null;
                    if(isInCheck(currentTurn)){
                        gameBoard = defaultBoard;
                        throw new InvalidMoveException("King is in check");
                    }
                    if(currentTurn == TeamColor.WHITE){
                        setTeamTurn(TeamColor.BLACK);
                    }
                    else{
                        setTeamTurn(TeamColor.WHITE);
                    }
                }
                else{
                    throw new InvalidMoveException("Invalid move");
                }
            }
            else{
                throw new InvalidMoveException("Invalid move");
            }
        }
        else{
            throw new InvalidMoveException("Invalid move");
        }
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
        Collection <ChessMove> unfilteredMoves = currentPiece.pieceMoves(gameBoard, startPosition);
        ChessPiece.PieceType forDebugging = currentPiece.getPieceType();
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
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = improvedKingFinder(teamColor);
        boolean testCheck = false;
        for(int i = 1; i <= 8; ++i){
            for(int j = 1; j <= 8; ++j){
                ChessPosition currentPosition = new ChessPosition(j, i);
                if(gameBoard.getPiece(currentPosition) != null && gameBoard.getPiece(currentPosition).getTeamColor() != teamColor){
                    ChessPiece.PieceType forDebugging = gameBoard.getPiece(currentPosition).getPieceType();
                    Collection <ChessMove> pieceMoves = gameBoard.getPiece(currentPosition).pieceMoves(gameBoard, currentPosition);
                    for(ChessMove move : pieceMoves){
                        ChessPosition possibility = move.getEndPosition();
                        if(possibility.equals(kingPosition)){
                            testCheck = true;
                            return testCheck;
                        }
                    }
                }
            }
        }
        return false;
    }

    public ChessPosition improvedKingFinder(ChessGame.TeamColor color){
        ChessPosition kingPosition = new ChessPosition(1, 1);
        for(int i = 1; i <= 8; ++i){
            for(int j = 1; j <= 8; ++j){
                ChessPosition space = new ChessPosition(i, j);
                if(gameBoard.getPiece(space) != null){
                    ChessPiece piece = gameBoard.getPiece(space);
                    ChessPiece.PieceType type = piece.getPieceType();
                    ChessGame.TeamColor pieceColor = piece.getTeamColor();
                    if(type == ChessPiece.PieceType.KING && pieceColor == color){
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
        Collection <ChessMove> moves;
        ChessPosition kingPosition = improvedKingFinder(teamColor);
        moves = validMoves(kingPosition);
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

    public Collection <ChessMove> getEveryMove(ChessBoard board){
        for(int )
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
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
