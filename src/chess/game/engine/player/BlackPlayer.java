/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.game.engine.player;

import chess.game.Alliance;
import chess.game.engine.board.Board;
import chess.game.engine.board.Move;
import chess.game.engine.board.Move.KingSideCastleMove;
import chess.game.engine.board.Move.QueenSideCastleMove;
import chess.game.engine.board.Tile;
import chess.game.engine.piece.Piece;
import chess.game.engine.piece.Rook;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Aleksa
 */
public class BlackPlayer extends Player{
    
    
    public BlackPlayer (Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    //funkcija calculateKingCastles proverava da li je moguce odraditi rokadu, i ako jeste stavlja je u kingCastles
    //sastoji se iz dva dela: ROKADA SA KRALJEVE STRANE I ROKADA SA KRALJICINE STRANE
    //na pocetku se proverava da li kralj nije pomeran pre toga i da li nad njim nije sah
    //ako je ovaj uslov ispunjen, za obe rokade se proverava da li imedju kralja i topa ne postoje figure i da li nakon rokade nisu ugrozene figure
    //ako ne postoje, rokada je moguca i upisuje se u listu
    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals) {
        final List<Move> kingCastles = new ArrayList<>();
        if(this.playerKing.isFirstMove() && !this.isInCheck()) {
            
            if(!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(7);
                //ROKADA NA KRALJEVOJ STRANI
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if(Player.calculateAttackOnTile(5, opponentsLegals).isEmpty() && 
                       Player.calculateAttackOnTile(6, opponentsLegals).isEmpty() && 
                       rookTile.getPiece().getPieceType().isRook()) {
                            
                            kingCastles.add(new KingSideCastleMove(this.board, this.playerKing, 6, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 5));

                    }
                }
            }
            //ROKADA NA KRALJICINOJ STRANI
            if(!this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied() && !this.board.getTile(3).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(0);
                
                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() ) {
                    if(Player.calculateAttackOnTile(2, opponentsLegals).isEmpty() && 
                       Player.calculateAttackOnTile(3, opponentsLegals).isEmpty() && 
                       rookTile.getPiece().getPieceType().isRook()) {
                    
                            kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing, 2, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 3));
                    }        
                }
            }   
        
        }
        return ImmutableList.copyOf(kingCastles);
    }

    
    
}
