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
public class Message {
    private final String message;
    private final int sourceId;
    private final int destinationId;
    
    public Message(String message, int sourceId, int destinationId) {
        this.message = message;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
    }

    public String getMessage() {
        return message;
    }

    public int getSourceId() {
        return sourceId;
    }

    public int getDestinationId() {
        return destinationId;
    }
    
}
