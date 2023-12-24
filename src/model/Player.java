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
public class Player {
    private int id;
    private String name;
    private String email;//Maybe Validation On Pass
    private String password;// Maybe Validation On Pass
    private int score;
    private boolean isOnline;
    private boolean isPlaying;

    {
        isPlaying= isOnline= false;
        score= 0;
    }

    public Player(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public Player(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Player(int id, String name, String email, String password, int score, boolean isOnline) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.score = score;
        this.isOnline = isOnline;
    }

    public Player(int id, String name, int score) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Player(String email, String name, int score) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public boolean isIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public boolean isIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    @Override
    public String toString() {
        return "Player{" + "id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", score=" + score + ", isOnline=" + isOnline + ", isPlaying=" + isPlaying + '}';
    }
}
