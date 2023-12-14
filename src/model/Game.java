/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author w
 */
public abstract class Game {

    private int gameID;
    
    private final MovementType[][] GAME_BOARD = {
        {MovementType.NONE, MovementType.NONE, MovementType.NONE},
        {MovementType.NONE, MovementType.NONE, MovementType.NONE},
        {MovementType.NONE, MovementType.NONE, MovementType.NONE}};

    private final int winStates[][] = {
        {0, 1, 2},
        {3, 4, 5},
        {6, 7, 8},
        {0, 3, 6},
        {1, 4, 7},
        {2, 5, 8},
        {0, 4, 8},
        {2, 4, 6}};

    private boolean gameOver;
    
    public int getGameID() {
        return gameID;
    }

    public boolean isGameOver() {
        return gameOver;
    }
    
    public MovementType getIndexValue(int index) {
        switch (index) {
            case 0:
                return GAME_BOARD[0][0];
            case 1:
                return GAME_BOARD[0][1];
            case 2:
                return GAME_BOARD[0][2];
            case 3:
                return GAME_BOARD[1][0];
            case 4:
                return GAME_BOARD[1][1];
            case 5:
                return GAME_BOARD[1][2];
            case 6:
                return GAME_BOARD[2][0];
            case 7:
                return GAME_BOARD[2][1];
            default:
                return GAME_BOARD[2][2];
        }
    }

    public void setIndexValue(int index, MovementType move) {
        switch (index) {
            case 0:
                GAME_BOARD[0][0] = move;
                break;
            case 1:
                GAME_BOARD[0][1] = move;
                break;
            case 2:
                GAME_BOARD[0][2] = move;
                break;
            case 3:
                GAME_BOARD[1][0] = move;
                break;
            case 4:
                GAME_BOARD[1][1] = move;
                break;
            case 5:
                GAME_BOARD[1][2] = move;
                break;
            case 6:
                GAME_BOARD[2][0] = move;
                break;
            case 7:
                GAME_BOARD[2][1] = move;
                break;
            default:
                GAME_BOARD[2][2] = move;
                break;
        }
    }

    public Boolean isValidMove(int index) {
        return getIndexValue(index) == MovementType.NONE;
    }
}