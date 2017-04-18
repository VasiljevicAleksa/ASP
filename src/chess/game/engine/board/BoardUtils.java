/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.game.engine.board;

/**
 *
 * @author Aleksa
 */
public class BoardUtils {
    
    public static final boolean[] FIRST_COLUMN = initColumn(0); //zbog exceptiona za konja
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);
    
    public static final boolean[] FIRST_ROW = initRow(0);      //ova dva (SECOND I SEVENTH) nam trebaju da bi odredili pocetnu poziciju pijuna zbog duplog skoka
    public static final boolean[] SECOND_ROW = initRow(8);    //na oba nismo kao parametar definisali koji je red, vec ID pocetka tog reda
    public static final boolean[] THIRD_ROW = initRow(16);      
    public static final boolean[] FOURTH_ROW = initRow(24);
    public static final boolean[] FIFTH_ROW = initRow(32);      
    public static final boolean[] SIXTH_ROW = initRow(40);
    public static final boolean[] SEVENTH_ROW = initRow(48);      
    public static final boolean[] EIGHTH_ROW = initRow(56);
    
    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;
    
    
    
    //postavlja true vrednosti na nekoj koloni koja je data kao parametar funkcije
    private static boolean[] initColumn (int columnNumber) {
        final boolean[] column = new boolean[64];
        
        do {
            column[columnNumber] = true;
            columnNumber += 8;
        }   
        while (columnNumber < 64);
        
        return column;
    }
    
    
    private BoardUtils(){
        throw new RuntimeException("You cannot instantiate me!");
    }
    
    
    private static boolean[] initRow (int rowNumber) {
        final boolean[] row = new boolean[64];
        
        do {
            row[rowNumber] = true;
            rowNumber ++;
        }   
        while (rowNumber % 8 !=0);
        
        return row;
    }
    
    
    public static boolean isValidTileCoordinate(final int coordinate) {
        if(coordinate >=0 && coordinate < 64){
            return true;
        }
        else {
        return false;
        }
    }
}
