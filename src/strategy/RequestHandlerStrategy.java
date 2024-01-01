/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strategy;

import server.ServerHandler;

/**
 *
 * @author w
 */
@FunctionalInterface
public interface RequestHandlerStrategy {
    void handleRequest(ServerHandler serverHandler);
}