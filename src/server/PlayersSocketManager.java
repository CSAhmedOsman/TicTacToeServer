/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.Vector;

/**
 *
 * @author w
 */
public class PlayersSocketManager {
    private static final Vector<ServerHandler> PLAYERS_SOCKET = new Vector<>();

    private PlayersSocketManager() {
    }

    public static Vector<ServerHandler> getPlayersSocket() {
        return PLAYERS_SOCKET;
    }
}