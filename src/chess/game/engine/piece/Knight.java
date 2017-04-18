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
import chess.game.engine.board.Move.AttackMove;
import chess.game.engine.board.Move.MajorMove;
import chess.game.engine.board.Tile;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Aleksa
 */
public class Knight extends Piece {
    
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17}; //u odnosu na trenutnu poziciju
                                                                                               //ovo je offset na koje moze da skoci
    public Knight(final Alliance pieceAlliance, int piecePosition) {
        super(PieceType.KNIGHT, piecePosition, pieceAlliance);
    }
    
    
    
    @Override
    //funkcija koja racuna legalne poteze
    public Collection<Move> calculateLegalMoves(final Board board) {
        
        int candidateDestinationCoordinate;
        final Collection<Move> legalMoves = new ArrayList<>();
        
        for(final int currentCandidateOffset: CANDIDATE_MOVE_COORDINATES){
        
            candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            
            if(isValidTileCoordinate(candidateDestinationCoordinate)) {
                if((isFirstColumnExclusion(this.piecePosition, currentCandidateOffset)) ||
                   (isSecondColumnExclusion(this.piecePosition, currentCandidateOffset)) ||
                   (isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset)) ||
                   (isEighthColumnExclusion(this.piecePosition, currentCandidateOffset))){
                    continue;
                }
                
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
            
                if (!candidateDestinationTile.isTileOccupied()) { //ako polje nije popunjeno pomeri figuru
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }
                else{ //ako je popunjeno, ispituje se da li je neprijateljska figura na tom polju (po boji)
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    
                    if(this.pieceAlliance != pieceAlliance){ //neprijateljska figura
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
       return ImmutableList.copyOf(legalMoves);
    }
   
    
    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
    
    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        
        if (BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == -10
                                                         || candidateOffset == 6 || candidateOffset == 15)) {
            return true;
        }
        else {
            return false;
        }
    }
    
    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
        
        if (BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6)) {
            return true;
        }
        else {
            return false;
        }
    }
    
    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
        
        if (BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset == 10)) {
            return true;
        }
        else {
            return false;
        }
    }
    
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        
        if (BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -15 || candidateOffset == -6
                                                       || candidateOffset == 10 || candidateOffset == 17)) {
            return true;
        }
        else {
            return false;
        }
    }


    
    
    
}
