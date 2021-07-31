/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wumpusworld;

/**
 *
 * @author Erik
 */
public class Position {

    public int x;

    public int y;

    public double wumpusProb;

    public double pitProb;

    public boolean hasStench;
    public boolean hasBreeze;
    public boolean hasGlitter;


    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.wumpusProb = 0;
        this.pitProb = 0;
    }

    public Position() {
        this.x = 0;
        this.y = 0;
        this.wumpusProb = 0;
        this.pitProb = 0;
    }

    public Position(int x, int y, boolean hasStench, boolean hasBreeze, boolean hasGlitter) {
        this.x = x;
        this.y = y;
        this.hasStench = hasStench;
        this.hasBreeze = hasBreeze;
        this.hasGlitter = hasGlitter;
    }

    public void log() {
        System.out.println("The position " + x + ", " + y + " has stench: " + hasStench + ", has breeze: " + hasBreeze + ", has glitter: " + hasGlitter + ".");
    }
}
