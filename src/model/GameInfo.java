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
    private final int prevSrcScore;
    private final int prevDestScore;

    public GameInfo(String srcPlayerName, String destPlayerName, int srcPlayerId, int destPlayerId, int srcPlayerScore, int destPlayerScore, int prevSrcScore, int prevDestScore) {
        this.srcPlayerName = srcPlayerName;
        this.destPlayerName = destPlayerName;
        this.srcPlayerId = srcPlayerId;
        this.destPlayerId = destPlayerId;
        this.srcPlayerScore = srcPlayerScore;
        this.destPlayerScore = destPlayerScore;
        this.prevSrcScore = prevSrcScore;
        this.prevDestScore = prevDestScore;
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

    public int getPrevSrcScore() {
        return prevSrcScore;
    }

    public int getPrevDestScore() {
        return prevDestScore;
    }

}
