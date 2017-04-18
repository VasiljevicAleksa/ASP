/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.game.engine.board;

import chess.game.engine.piece.Piece;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Aleksa
 */
public abstract class Tile {
    
    protected final int tileCoordinate;
    
    public static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();
    
    //Umesto da kreiramo prazna polja svaki put kada nam zatrebaju
    //kreiramo jednom svma moguca, i posle svaki put kada nam zatrebaju, mi vadimo iz hesha (factory metodom CreateTile)
    //vadimo kopije zato sto ne zelimo da imaju mogucnost da se promene
    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
         
        for(int i = 0; i < BoardUtils.NUM_TILES; i++){ 
            emptyTileMap.put(i, new EmptyTile(i));
        }
         
         return ImmutableMap.copyOf(emptyTileMap);
    }
    
    //funkcija koja "kreira" (vadi iz kesa prazna polja), u drugom slucaju pravi zauzeto polje
    public static Tile createTile (final int tileCoordinate, final Piece piece) {
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
    }
    
    private Tile (int tileCoordinate){
        this.tileCoordinate = tileCoordinate;
    }
    
    public int getTileCoordinate() {
        return this.tileCoordinate;
    }
    
    public abstract boolean isTileOccupied(); //da li je polje zauzeto
    public abstract Piece getPiece(); //vraca figuru sa polja
    
    
    
    
     public static final class EmptyTile extends Tile {
    
        private EmptyTile(final int coordinate) {
            super(coordinate); //superclass constructor
        }
        
        //string prikaza za prazno polje
        @Override
        public String toString() {
            return "-";
        }
        
        @Override
        public boolean isTileOccupied(){
            return false;
        }
        
        @Override
        public Piece getPiece() {
            return null;
        }
    }
    
    //klasa zauzeto polje
    //za razliku od praznog kao parametar konstruktor sadrzi i figuru koja je na polju
    public static final class OccupiedTile extends Tile {
        
        private final Piece pieceOnTile;
        
        private OccupiedTile(int tileCoordinate, Piece pieceOnTile){
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }
        
        
        //vraca string oznaku za figuru. Veliko slovo ako je crna figura, malo ako je bela
        @Override
        public String toString() {
            return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase(): getPiece().toString();
        }
        
        @Override
        public boolean isTileOccupied() {
            return true;
        }
        
        public Piece getPiece(){
            return this.pieceOnTile;
        }
    
    }
    
}
