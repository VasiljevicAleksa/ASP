/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.game.engine.player;

import chess.game.Alliance;
import chess.game.engine.board.Board;
import chess.game.engine.board.Move;
import chess.game.engine.piece.King;
import chess.game.engine.piece.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Aleksa
 */
public abstract class Player {

       
    
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;
    
    Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves){
        this.board = board;
        this.playerKing = establishKing();
        //this.legalMoves = legalMoves;
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastles(legalMoves, opponentMoves))); //na legalne poteze konkateniramo i moguce rokade
        this.isInCheck = !Player.calculateAttackOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    } 
    //objasnjenje za zadnji parametar konstruktora:
    // funkcija calculateAttackOnTile za poziciju na koju je kralj(prvi parametar), racuna da li neki od protivnickih poteza(drugi parametar)
    // moze da predje na njegovo polje (odnosno da mu da sah). Ako moze, upisuje potez u listu attackMoves
    //sto znaci, ako je ta lista nije prazna, imamo sah
    
    
    protected static Collection<Move> calculateAttackOnTile(int piecePosition, Collection<Move> moves) {
        
        final List<Move> attackMoves = new ArrayList<>();
        for(final Move move : moves) {
            if(piecePosition == move.getDestinationCoordinate()) {
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
    }
   
    
    public King getPlayerKing() {
        return this.playerKing;
    }
    
    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }


    private King establishKing() {
        for (final Piece piece : getActivePieces()) {
            if(piece.getPieceType().isKing()) {
                return (King) piece;  //kastujem figuru da mi vrati kao figuru tipa king
            }
        }
        throw new RuntimeException("Nema kralja! Nije validna tabla!");
    }
    
    
    public boolean isMoveLegal(final Move move) {
        return this.legalMoves.contains(move);
    }
    
    
    public boolean isInCheck() {
        return this.isInCheck;
    }
    
    //TODO IMPLEMENTS THIS METOD DOWN THERE
    public boolean isInCheckMate() {
        return this.isInCheck && !hasEscapeMoves();
    }
    //ova funkcija vadi sve moguce poteze i odiga ih na nekoj imaginarnoj tabli
    //ako odigra vraca true
    //ako ne moze da odigra jer ga ovaj potez stavlja u sah ili nije legalan onda false
    protected boolean hasEscapeMoves() {
        for(final Move move : this.legalMoves){
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }
    
    //slucaj u kome ne mozes da napravis potez koji ce da spreci da bude sah
    public boolean isInStaleMate() {
        return !this.isInCheck && !hasEscapeMoves();
    }
    
    public boolean isCastled() {
        return false;
    }
     

    //ako je potez nelegalan, onda vracamo staru nepromenjenu tablu (ne updateovanu)
    //ako je legalan izvrsava se potez, ALI SE JOS UVEK NE PRAVI NOVA TABELA, SAMO SMO ODRADILI execute(),  pa se onda pita
    //da li nakon ovog poteza postoji sah nad kraljem. Ako postoji, ne vracamo novu tablu
    //tek u krajnjem else slucaju moze da se izvrsi potez i vracamo novu tablu
    public MoveTransition makeMove(final Move move) {
        
        if(!isMoveLegal(move)) {
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        
        final Board transitionBoard = move.execute();
        final Collection<Move> kingAttacks = Player.calculateAttackOnTile(
                transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.currentPlayer().getLegalMoves());
            
        if(!kingAttacks.isEmpty()) {
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        
        return new MoveTransition(this.board, move, MoveStatus.DONE);
    }
    
    
    
    
   
            
            
    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();   
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals);
    
    
           
}
