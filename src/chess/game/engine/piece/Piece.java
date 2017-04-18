/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.game.engine.piece;

import chess.game.Alliance;
import chess.game.engine.board.Board;
import chess.game.engine.board.Move;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Aleksa
 */
public abstract class Piece {
    
    protected final PieceType pieceType;
    protected final int piecePosition; //kordinate
    protected final Alliance pieceAlliance; //bela ili crna
    protected final boolean isFirstMove; //zbog pijuna za dupli skok
    private final int cachedHashCode;
    
    Piece(final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        //TODO more work here!!
        this.isFirstMove = false;
        this.cachedHashCode = computerHashCode();
    }
    
    
    private int computerHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }
    
    
    //funkcija za poredjenje figura
    @Override
    public boolean equals(final Object other) {
        if(this == other) {
            return true;
        }
        if(!(other instanceof Piece)) {
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() && 
               pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove();
    }
    
    
    /*@Override
    public int hashcode() {
        return this.cachedHashCode;
    }
    */
    
    public int getPiecePosition() {
        return this.piecePosition;
    }
    
    
    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }
    
   
    public boolean isFirstMove() {
        return this.isFirstMove;
    }
    
    
     public PieceType getPieceType() {
        return this.pieceType;
    }
   
    public abstract Collection<Move> calculateLegalMoves(final Board board);
    
    //metoda koja vraca figuru koja se pomerila (vraca figuru i nove kordinate)
    public abstract Piece movePiece(Move move);

   
    
    
    public enum PieceType {
        
        PAWN("P") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }
            
            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }
            
            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }
            
            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }
            
            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }
            
            @Override
            public boolean isRook() {
                return false;
            }
        };
        
        
        private String pieceName;
        
        PieceType(final String pieceName){
            this.pieceName = pieceName;
        }
        
        @Override
        public String toString() {
            return this.pieceName;
        }
        
        public abstract boolean isKing();
        public abstract boolean isRook();
    }
}
