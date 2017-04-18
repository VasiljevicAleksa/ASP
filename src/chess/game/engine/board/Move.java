/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.game.engine.board;

import chess.game.engine.board.Board.Builder;
import chess.game.engine.piece.Pawn;
import chess.game.engine.piece.Piece;
import chess.game.engine.piece.Rook;

/**
 *
 * @author Aleksa
 */
public abstract class Move {
    
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;
    
    public static final Move NULL_MOVE = new NullMove();
    
    private Move (final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }
    
    
    //objasnjenje funkcije execute():
    //ako je npr na potezu Player1, sve njegove figure se postavljaju na novu tablu (jer se ne vrsi update table, vec pravi novu nakon svakog poteza)
    //SEM ONE KOJA JE POMERENA! Kod Player2 sve figure se postavljaju na novu tablu (logicno, jer se nista ne menja).
    //ispod toga postavljamo figuru koja je pomerena, postavljamo Player2 da bude na potezu i vracamo novu tablu
    public Board execute() {
           
            final Builder builder = new Builder();
            
            for(final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if(!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        return result;
    }
    
    @Override
    public boolean equals(final Object other) {
         if(this == other) {
            return true;
        }
        if(!(other instanceof Move)) {
            return false;
        }
        final Move otherMove = (Move) other;
        return getDestinationCoordinate() == otherMove.getDestinationCoordinate() && getMovedPiece().equals(otherMove.getMovedPiece());
    }
    
    
    public int getCurrentCoordinate() {
        return this.getMovedPiece().getPiecePosition();
    }
    
    
    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }
    
    
    
    public Piece getMovedPiece() {
        return this.movedPiece;
    }
    
    public boolean isAttack() {
        return false;
    }
    
    public boolean isCastlingMove() {
        return false;
    }
    
    public Piece getAttackedPiece() {
        return null;
    }
    
    
    
    
    public static final class MajorMove extends Move {
        
        public MajorMove (final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate); 
        }
       
    }
    
      
    
    
    public static class AttackMove extends Move {
        
        final Piece attackedPiece;
        
        public AttackMove (final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate); 
            this.attackedPiece = attackedPiece;
        }

        
        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }
        
        @Override
        public boolean isAttack() {
            return true;
        }
        
        @Override
        public Board execute() {
            return null;
        }
        
        @Override
        public boolean equals(final Object other) {
            if(this == other) {
            return true;
        }
        if(!(other instanceof AttackMove)) {
            return false;
        }
        final AttackMove otherAttackMove = (AttackMove) other;
        return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
    }
        
        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }
    }
    
    
    
    public static final class PawnMove extends Move {

        public PawnMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }        
    }
    
    
    public static class PawnAttackMove extends AttackMove {

        public PawnAttackMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }  
    
    
    public static final class PawnEnPassantAttackMove extends PawnAttackMove {

        public PawnEnPassantAttackMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    } 

    
    public static final class PawnJump extends Move {

        public PawnJump(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        } 
        
        @Override
        public Board execute() {
            final Builder builder = new Builder();
            
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if(!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn)this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
            
        }
    }
    
    //rokada
    static abstract class CastleMove extends Move {
        
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;
        
        public CastleMove (final Board board, final Piece movedPiece, final int destinationCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }
        
        public Rook getCastleRook() {
            return this.castleRook;
        }
        
        @Override
        public boolean isCastlingMove() {
            return true;
        }
        
        
        //razlika izmedju osnovne funkcije je u tome sto kod ove pored kralja ne diramo ni topa (linija 244)
        // posle pomeramo i kralja i novokreiranog topa
        @Override
        public Board execute() {
        final Builder builder = new Builder();
            
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) {
                if(!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            //TODO: pogledati prvi potez za figure(fali parametar dole)
            builder.setPiece(new Rook(this.castleRook.getPieceAlliance(), this.castleRookDestination));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
            
        }
    
    }
    
   //rokada sa kraljeve strane 
    public static final class KingSideCastleMove extends CastleMove {

        public KingSideCastleMove(Board board, Piece movedPiece, int destinationCoordinate,final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        } 
        
        @Override
        public String toString() {
            return "0-0";
        }
    }
    
    //rokada sa kraljicine strane
    public static final class QueenSideCastleMove extends CastleMove {

        public QueenSideCastleMove(Board board, Piece movedPiece, int destinationCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        } 
        
        @Override
        public String toString() {
            return "0-0-0";
        }
    }
    
    
    public static final class NullMove extends Move {

        public NullMove () {
            super(null, null, -1);
        }      
        
        @Override
        public Board execute(){
            throw new RuntimeException("Ne moze da izvrsi null move");
        }
    }
    
    
    
    
    public static class MoveFactory {
        
        private MoveFactory() {
            throw new RuntimeException("Not instantiable!");
        }
        
        public static Move createMove(Board board, final int currentCoordinate, final int destinationCoordinate) {
            for (final Move move : board.getAllLegalMoves()) {
                if(move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }

}

