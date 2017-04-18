/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.game.engine.piece;

import chess.game.Alliance;
import chess.game.engine.board.Board;
import chess.game.engine.board.BoardUtils;
import static chess.game.engine.board.BoardUtils.isValidTileCoordinate;
import chess.game.engine.board.Move;
import chess.game.engine.board.Tile;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Aleksa
 */
public class King extends Piece {
    
        private final static int[] CANDIDATE_MOVE_COORDINATE = {-9, -8, -7, -1, 1, 7, 8, 9};


    public King(Alliance pieceAlliance, int piecePosition) {
        super(PieceType.KING, piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
      
        int candidateDestinationCoordinate;
        final List<Move> legalMoves = new ArrayList<>();
        
        for(final int currentCandidateOffset: CANDIDATE_MOVE_COORDINATE ) {
               
            candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            
            if(isValidTileCoordinate(candidateDestinationCoordinate)) {
            
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                
                if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) || isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                    continue;
                }
            
                if (!candidateDestinationTile.isTileOccupied()) { //ako polje nije popunjeno pomeri figuru
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                }
                else{ //ako je popunjeno, ispituje se da li je neprijateljska figura na tom polju (po boji)
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    
                    if(this.pieceAlliance != pieceAlliance){ //neprijateljska figura
                        legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
       return ImmutableList.copyOf(legalMoves);
    }
    
    
    @Override
    public King movePiece(Move move) {
        return new King(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
    
    @Override
    public String toString() {
        return PieceType.KING.toString();
    }
    
    
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        
        if (BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1 || candidateOffset == -9 || candidateOffset == 7)) {
            return true;
        }
        else {
            return false;
        }
    }
    
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        
        if (BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 9 || candidateOffset == 1)) {
            return true;
        }
        else {
            return false;
        }
    }
    
}
