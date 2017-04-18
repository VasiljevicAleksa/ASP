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
import chess.game.engine.board.Move.MajorMove;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Aleksa
 */
public class Pawn extends Piece {
    
    private final static int[] CANDIDATE_MOVE_COORDINATE = {8, 16};

    public Pawn(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.PAWN, piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        
        int candidateDestinationCoordinate;
        final List<Move> legalMoves = new ArrayList<>();
        
        for(final int currentCandidateOffset: CANDIDATE_MOVE_COORDINATE ) {
            
            //ovo nam treba jer za bele pijune kretanje napred je ofset +8 (zato sto idu na dole), dok za crne je ofset -8 (idu na gore)
            candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidateOffset);
            
             if(!isValidTileCoordinate(candidateDestinationCoordinate)) {
                 continue;
             }
             //ako se odemo jedno polje napred, i to polje nije zauzeto dodaj potez listi poteza
             if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                
                //TODO more work to here(deal with promotions)!!! 
                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
             }
             //ovo sad je za dupli skok pijuna na pocetku
             else if(candidateDestinationCoordinate == 16 && this.isFirstMove() && 
                     (BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()) || 
                     (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isWhite())) {
                 
                 //za dupli skok proveravamo polje na koje treba da skoci. A sta je sa prethodnim? Njega ne moze da preskoci ako je neka figura na tom polju
                 //zato kreiramo ovu promenljivu da pamti sta se nalazi u tom polju
                 final int behindCandidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * 8);
                 
                 //ako su oba prazna, moze da skoci
                 if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() && !board.getTile(candidateDestinationCoordinate).isTileOccupied())  {
                     
                     legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                 }
                 //ovo je za pijuna koji ide na gore u zadnjoj koloni, i pijuna koji ide na dole u prvoj koloni
                 //ne mogu da jedu figure sa svoje desne strane
                 //ispitujemo da li NIJE taj slucaj, ako nije, moze da se izvrsi potez
                } else if (currentCandidateOffset == 7 &&
                         !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                           (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()) ) ) {
                 
                    if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                        final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                        
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                            //TODO more here!!!
                            legalMoves.add(new MajorMove (board, this, candidateDestinationCoordinate));
                        }
                    }
                 }
                 //ovo je za pijuna koji ide na gore u prvoj koloni, i pijuna koji ide na dole u zadnjoj koloni
                 //ne mogu da jedu figure sa svoje leve strane
                 //takodje ispitujemo da li NIJE taj slucaj, ako nije, moze da se izvrsi potez
                 else if (currentCandidateOffset ==9 &&
                         !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                           (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()) )) {
                     
                    if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                        final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                        
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                            //TODO more here!!!
                            legalMoves.add(new MajorMove (board, this, candidateDestinationCoordinate));
                        }
                    }
                 }
            }
        return ImmutableList.copyOf(legalMoves);
        }
    

    
    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }
    
}

    

