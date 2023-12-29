/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author LENOVO
 */
public class GameInfo {
    private final String srcPlayerName;
    private final String destPlayerName;
    private final int srcPlayerId;
    private final int destPlayerId;
    private final int srcPlayerScore;
    private final int destPlayerScore;


    public GameInfo(String srcPlayerName, String destPlayerName, int srcPlayerId, int destPlayerId, int srcPlayerScore, int destPlayerScore) {
        this.srcPlayerName = srcPlayerName;
        this.destPlayerName = destPlayerName;
        this.srcPlayerId = srcPlayerId;
        this.destPlayerId = destPlayerId;
        this.srcPlayerScore = srcPlayerScore;
        this.destPlayerScore = destPlayerScore;
    }

    public String getSrcPlayerName() {
        return srcPlayerName;
    }

    public String getDestPlayerName() {
        return destPlayerName;
    }

    public int getSrcPlayerId() {
        return srcPlayerId;
    }

    public int getDestPlayerId() {
        return destPlayerId;
    }

    public int getSrcPlayerScore() {
        return srcPlayerScore;
    }

    public int getDestPlayerScore() {
        return destPlayerScore;
    }

   
}