/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.game;

import chess.game.engine.board.Board;
import chess.game.gui.Table;

/**
 *
 * @author Aleksa
 */
public class ChessGame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Board board = Board.createStandardBoard();
        
        System.out.println(board);
        
        Table table = new Table();
    }
    
}
