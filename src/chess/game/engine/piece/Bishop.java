/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.game.engine.piece;

import chess.game.Alliance;
import chess.game.engine.board.Board;
import chess.game.engine.board.BoardUtils;
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
public class Bishop extends Piece{
    
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -7, 7, 9};

    public Bishop(Alliance pieceAlliance, int piecePosition) {
        super(PieceType.BISHOP, piecePosition, pieceAlliance);
    }
    
    
    
    
    //funkcija uzima jedan po jedan vektor i proverava za svaki:
    //da li je validno polje?
    //da li je popunjeno ili prazno?
    //upisuje u listu pomeranje koje je moguce, i prelazi na sledece polje
    //kad zavrsi sa jednim vektorom (neko polje ne bude validno), prelazi na sledeci
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
       
        final List<Move> legalMoves = new ArrayList<>();
        
        
        for(final int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES ) {
            
            int candidateDestinationCoordinate = this.piecePosition;
            
            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                
                    if ((isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) ||
                        (isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset))) {
                        break;
                    }
                    candidateDestinationCoordinate += candidateCoordinateOffset;
                  
                    if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                  
                      final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
            
                        if (!candidateDestinationTile.isTileOccupied()) { //ako polje nije popunjeno pomeri figuru
                            legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                        }
                        else{ //ako je popunjeno, ispituje se da li je neprijateljska figura na tom polju (po boji)
                            final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                            final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    
                            if(this.pieceAlliance != pieceAlliance){ //neprijateljska figura
                                legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                            }
                            break;
                        }
                    }
            }
        }    
        return ImmutableList.copyOf(legalMoves);
        
    }
    
    
    @Override
    public Bishop movePiece(Move move) {
        return new Bishop(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
    
    @Override
    public String toString() {
        return PieceType.BISHOP.toString();
    }
    
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        
        if (BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == 7)) {
            return true;
        }
        else {
            return false;
        }
    }
    
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        
        if (BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 9)) {
            return true;
        }
        else {
            return false;
        }
    }

      
}
