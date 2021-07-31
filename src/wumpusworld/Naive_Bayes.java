/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wumpusworld;

import static java.lang.Math.pow;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Erik Eliasson
 */
public class Naive_Bayes {

    World w;
    World wCopy;
    ArrayList<Position> tiles = new ArrayList<Position>();

    double PIT_PROBABILITY = 3. / 15.;
    double WUMPUS_PROBABILITY = 1. / 15.;

    boolean hasGoal = false;
    int[] goalTile = new int[2];
    boolean foundWumpus = false;
    int[] wumpusTile = new int[2];

    public Naive_Bayes(World input) {
        w = input;
        wCopy = w.cloneWorld();
        goalTile[0] = 0;
        goalTile[1] = 0;
    }

    //Calculates the amount of tiles that we have visited
    public int GetAmountOfVisisted() {
        int visitedCount = 0;
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {
                if (wCopy.isVisited(i, j)) {
                    visitedCount++;
                }
            }
        }
        return visitedCount;
    }

    //Checking if this tile is a unknown tile that we can move to.
    public boolean isMoveable(int x, int y) {
        if (wCopy.isVisited(x, y)) //if this node already is visisted we dont need to check probability, just move along.
        {
            return false;
        }
        boolean Moveable = false;

        if (wCopy.isVisited(x - 1, y) && !wCopy.hasWumpus(x - 1, y) && (!(wCopy.hasPit(x - 1, y)) || (wCopy.getPlayerX() == x - 1 && wCopy.getPlayerY() == y))) {
            Moveable = true;
        } else if (wCopy.isVisited(x, y - 1) && !wCopy.hasWumpus(x, y - 1) && (!(wCopy.hasPit(x, y - 1)) || (wCopy.getPlayerX() == x && wCopy.getPlayerY() == y - 1))) {
            Moveable = true;
        } else if (wCopy.isVisited(x + 1, y) && !wCopy.hasWumpus(x + 1, y) && (!(wCopy.hasPit(x + 1, y)) || (wCopy.getPlayerX() == x + 1 && wCopy.getPlayerY() == y))) {
            Moveable = true;
        } else if (wCopy.isVisited(x, y + 1) && !wCopy.hasWumpus(x, y - 1) && (!(wCopy.hasPit(x, y + 1)) || (wCopy.getPlayerX() == x && wCopy.getPlayerY() == y + 1))) {
            Moveable = true;
        }
        return Moveable;
    }

    //Checking if this tile only has stench tiles as neighbors that are visited
    public boolean checkIfStench(int x, int y) {
        boolean onlyStench = true;
        
        if (!wCopy.hasStench(x - 1, y) && wCopy.isVisited(x - 1, y)) {
            onlyStench = false;
        } else if (!wCopy.hasStench(x, y - 1) && wCopy.isVisited(x, y - 1)) {
            onlyStench = false;
        } else if (!wCopy.hasStench(x + 1, y) && wCopy.isVisited(x + 1, y)) {
            onlyStench = false;
        } else if (!wCopy.hasStench(x, y + 1) && wCopy.isVisited(x, y + 1)) {
            onlyStench = false;
        }
        return onlyStench;
    }
    
    //Checking if this tile only has breeze tiles as neighbors that are visited
    public boolean checkIfBreeze(int x, int y) {
        boolean onlyBreeze = true;
        if (!wCopy.hasBreeze(x - 1, y) && wCopy.isVisited(x - 1, y)) {
            onlyBreeze = false;
        } else if (!wCopy.hasBreeze(x, y - 1) && wCopy.isVisited(x, y - 1)) {
            onlyBreeze = false;
        } else if (!wCopy.hasBreeze(x + 1, y) && wCopy.isVisited(x + 1, y)) {
            onlyBreeze = false;
        } else if (!wCopy.hasBreeze(x, y + 1) && wCopy.isVisited(x, y + 1)) {
            onlyBreeze = false;
        }
        return onlyBreeze;

    }

    //Check the amount of stench neighbors visible
    public int checkAmountOfStench(int x, int y) {
        int onlyStench = 0;
        //If any of theses if-statements are true, there is a neighbor to this tile we have visisted therefore we can
        //move here, we need to check proability.
        if (wCopy.hasStench(x - 1, y) && wCopy.isVisited(x - 1, y)) {
            onlyStench++;
        } else if (wCopy.hasStench(x, y - 1) && wCopy.isVisited(x, y - 1)) {
            onlyStench++;
        } else if (wCopy.hasStench(x + 1, y) && wCopy.isVisited(x + 1, y)) {
            onlyStench++;
        } else if (wCopy.hasStench(x, y + 1) && wCopy.isVisited(x, y + 1)) {
            onlyStench++;
        }
        return onlyStench;
    }
    
    //Check the amount of breeze neighbors visible
    public int checkAmountOfBreeze(int x, int y) {
        int onlyBreeze = 0;
        //If any of theses if-statements are true, there is a neighbor to this tile we have visisted therefore we can
        //move here, we need to check proability.
        if (wCopy.hasBreeze(x - 1, y) && wCopy.isVisited(x - 1, y)) {
            onlyBreeze++;
        } else if (wCopy.hasBreeze(x, y - 1) && wCopy.isVisited(x, y - 1)) {
            onlyBreeze++;
        } else if (wCopy.hasBreeze(x + 1, y) && wCopy.isVisited(x + 1, y)) {
            onlyBreeze++;
        } else if (wCopy.hasBreeze(x, y + 1) && wCopy.isVisited(x, y + 1)) {
            onlyBreeze++;
        }
        return onlyBreeze;
    }

    //the total amount of neighbors (unknown and known)
    public int getAmountOfNeighbors(int x, int y) {
        int temp = 4;
        if (x == 1 || x == 4) {
            temp--;
        }
        if (y == 1 || y == 4) {
            temp--;
        }
        return temp;
    }

    //compare two positions and see if they are neighbors
    public boolean isNeighbor(int[] pos, int[] pos2) {
        boolean temp = false;
        if (pos[0] - 1 == pos2[0] && pos[1] == pos2[1]) {
            temp = true;
        }
        if (pos[0] + 1 == pos2[0] && pos[1] == pos2[1]) {
            temp = true;
        }
        if (pos[0] == pos2[0] && pos[1] - 1 == pos2[1]) {
            temp = true;
        }
        if (pos[0] == pos2[0] && pos[1] + 1 == pos2[1]) {
            temp = true;
        }
        return temp;
    }

    //check how many unknown common neighbors this tiles neighboring stenches has.
    public int stenchCommonNeighbors(int x, int y) {
        int commonNeighbors = 0;
        //If any of theses if-statements are true, there is a neighbor to this tile we have visisted therefore we can
        //move here, we need to check proability.
        ArrayList<int[]> stenches = new ArrayList<int[]>();
        if (wCopy.hasStench(x - 1, y) && wCopy.isVisited(x - 1, y)) {
            int[] temp = {x - 1, y};
            stenches.add(temp);
        }
        if (wCopy.hasStench(x, y - 1) && wCopy.isVisited(x, y - 1)) {
            int[] temp = {x, y - 1};
            stenches.add(temp);
        }
        if (wCopy.hasStench(x + 1, y) && wCopy.isVisited(x + 1, y)) {
            int[] temp = {x + 1, y};
            stenches.add(temp);
        }
        if (wCopy.hasStench(x, y + 1) && wCopy.isVisited(x, y + 1)) {
            int[] temp = {x, y + 1};
            stenches.add(temp);
        }

        if (stenches.size() >= 3) {
            commonNeighbors = 1;
        } else if (stenches.size() == 2) {
            int[] pos = {stenches.get(0)[0] - 1, stenches.get(0)[1]};
            if (checkIfStench(pos[0], pos[1])) {
                if (w.isUnknown(pos[0], pos[1])) {
                    if (isNeighbor(pos, stenches.get(1))) {
                        commonNeighbors++;
                    }
                }
            }
            pos[0] = stenches.get(0)[0];
            pos[1] = stenches.get(0)[1] - 1;
            if (checkIfStench(pos[0], pos[1])) {
                if (w.isUnknown(pos[0], pos[1])) {
                    if (isNeighbor(pos, stenches.get(1))) {
                        commonNeighbors++;
                    }
                }
            }
            pos[0] = stenches.get(0)[0] + 1;
            pos[1] = stenches.get(0)[1];
            if (checkIfStench(pos[0], pos[1])) {

                if (w.isUnknown(pos[0], pos[1])) {
                    if (isNeighbor(pos, stenches.get(1))) {
                        commonNeighbors++;
                    }
                }
            }
            pos[0] = stenches.get(0)[0];
            pos[1] = stenches.get(0)[1] + 1;
            if (checkIfStench(pos[0], pos[1])) {

                if (w.isUnknown(pos[0], pos[1])) {
                    if (isNeighbor(pos, stenches.get(1))) {
                        commonNeighbors++;
                    }
                }
            }
        } else if (stenches.size() == 1) {
            int[] pos = {stenches.get(0)[0] - 1, stenches.get(0)[1]};
            if (w.isUnknown(pos[0], pos[1]) && checkIfStench(pos[0], pos[1])) {
                commonNeighbors++;
            }
            pos[0] = stenches.get(0)[0];
            pos[1] = stenches.get(0)[1] - 1;
            if (w.isUnknown(pos[0], pos[1]) && checkIfStench(pos[0], pos[1])) {
                commonNeighbors++;
            }
            pos[0] = stenches.get(0)[0] + 1;
            pos[1] = stenches.get(0)[1];
            if (w.isUnknown(pos[0], pos[1]) && checkIfStench(pos[0], pos[1])) {
                commonNeighbors++;
            }
            pos[0] = stenches.get(0)[0];
            pos[1] = stenches.get(0)[1] + 1;
            if (w.isUnknown(pos[0], pos[1]) && checkIfStench(pos[0], pos[1])) {
                commonNeighbors++;
            }
        }
        return commonNeighbors;
    }

    //Check the odds for this tile being a  wumpus
    public double checkWumpusOdds(int x, int y) {
        double ba = 0; //Odds that this tile is a wumpus;
        double a = WUMPUS_PROBABILITY; // odds that any tile is a wumpus
        double b = 1 - a; // odds that any tile isnt a wumpus
        double numerator = 0.;
        double denominator = 0.;
        if (checkIfStench(x, y)) { //Check if neighbors only has stench
            int amountOfStench = checkAmountOfStench(x, y);

            ba = (double) amountOfStench - 1 / (double) getAmountOfNeighbors(x, y);
            numerator = a * ba;
            int neighbors = stenchCommonNeighbors(x, y);
            b = b * ((double) amountOfStench - 1 / (double) neighbors);
            denominator = numerator + b;

            return numerator / denominator;
        }
        return 0.;
    }

    //Check how many of this tiles neighbors that are known && has breezes how many diferenet unknown neighbors they have
    public int UnknownNeighbors(int x, int y) {
        ArrayList<int[]> breezes = new ArrayList<int[]>();
        if (wCopy.hasBreeze(x - 1, y) && wCopy.isVisited(x - 1, y)) {
            int[] temp = {x - 1, y};
            breezes.add(temp);
        }
        if (wCopy.hasBreeze(x, y - 1) && wCopy.isVisited(x, y - 1)) {
            int[] temp = {x, y - 1};
            breezes.add(temp);
        }
        if (wCopy.hasBreeze(x + 1, y) && wCopy.isVisited(x + 1, y)) {
            int[] temp = {x + 1, y};
            breezes.add(temp);
        }
        if (wCopy.hasBreeze(x, y + 1) && wCopy.isVisited(x, y + 1)) {
            int[] temp = {x, y + 1};
            breezes.add(temp);
        }
        ArrayList<int[]> unknowns = new ArrayList<int[]>();
        for (int i = 0; i < breezes.size(); i++) {
            int cX = breezes.get(i)[0];
            int cY = breezes.get(i)[1];

            if (wCopy.isUnknown(cX - 1, cY)) {
                int[] temp = {cX - 1, cY};
                if (unknowns.size() > 0) {
                    boolean exist = false;
                    for (int j = 0; j < unknowns.size(); j++) {
                        if (unknowns.get(j)[0] == temp[0] && unknowns.get(j)[1] == temp[1]) {
                            exist = true;

                        }
                    }
                    if (!exist) {
                        unknowns.add(temp);
                    }
                } else {
                    unknowns.add(temp);

                }
            }
            if (wCopy.isUnknown(cX, cY - 1)) {
                int[] temp = {cX, cY - 1};
                if (unknowns.size() > 0) {
                    boolean exist = false;
                    for (int j = 0; j < unknowns.size(); j++) {
                        if (unknowns.get(j)[0] == temp[0] && unknowns.get(j)[1] == temp[1]) {
                            exist = true;

                        }
                    }
                    if (!exist) {
                        unknowns.add(temp);
                    }
                } else {
                    unknowns.add(temp);

                }
            }
            if (wCopy.isUnknown(cX, cY + 1)) {
                int[] temp = {cX, cY + 1};
                if (unknowns.size() > 0) {
                    boolean exist = false;
                    for (int j = 0; j < unknowns.size(); j++) {
                        if (unknowns.get(j)[0] == temp[0] && unknowns.get(j)[1] == temp[1]) {
                            exist = true;

                        }
                    }
                    if (!exist) {
                        unknowns.add(temp);
                    }
                } else {
                    unknowns.add(temp);

                }
            }
            if (wCopy.isUnknown(cX + 1, cY)) {
                int[] temp = {cX + 1, cY};
                if (unknowns.size() > 0) {
                    boolean exist = false;
                    for (int j = 0; j < unknowns.size(); j++) {
                        if (unknowns.get(j)[0] == temp[0] && unknowns.get(j)[1] == temp[1]) {
                            exist = true;

                        }
                    }
                    if (!exist) {
                        unknowns.add(temp);
                    }
                } else {
                    unknowns.add(temp);

                }
            }

        }
        return unknowns.size();
    }

    //checking how many unknown neighbors this tiles neighbor have that has breeze
    public int LowestUnknownNeighbors(int x, int y) {
        ArrayList<int[]> breezes = new ArrayList<int[]>();
        if (wCopy.hasBreeze(x - 1, y) && wCopy.isVisited(x - 1, y)) {
            int[] temp = {x - 1, y};
            breezes.add(temp);
        }
        if (wCopy.hasBreeze(x, y - 1) && wCopy.isVisited(x, y - 1)) {
            int[] temp = {x, y - 1};
            breezes.add(temp);
        }
        if (wCopy.hasBreeze(x + 1, y) && wCopy.isVisited(x + 1, y)) {
            int[] temp = {x + 1, y};
            breezes.add(temp);
        }
        if (wCopy.hasBreeze(x, y + 1) && wCopy.isVisited(x, y + 1)) {
            int[] temp = {x, y + 1};
            breezes.add(temp);
        }
        int lowestUnknowns = Integer.MAX_VALUE;
        ArrayList<int[]> unknowns = new ArrayList<int[]>();
        for (int i = 0; i < breezes.size(); i++) {
            int cX = breezes.get(i)[0];
            int cY = breezes.get(i)[1];

            if (wCopy.isUnknown(cX - 1, cY) || wCopy.hasPit(cX - 1, cY)) {
                int[] temp = {cX - 1, cY};
                if (unknowns.size() > 0) {
                    boolean exist = false;
                    for (int j = 0; j < unknowns.size(); j++) {
                        if (unknowns.get(j)[0] == temp[0] && unknowns.get(j)[1] == temp[1]) {
                            exist = true;

                        }
                    }
                    if (!exist) {
                        unknowns.add(temp);
                    }
                } else {
                    unknowns.add(temp);

                }
            }
            if (wCopy.isUnknown(cX, cY - 1) || wCopy.hasPit(cX, cY - 1)) {
                int[] temp = {cX, cY - 1};
                if (unknowns.size() > 0) {
                    boolean exist = false;
                    for (int j = 0; j < unknowns.size(); j++) {
                        if (unknowns.get(j)[0] == temp[0] && unknowns.get(j)[1] == temp[1]) {
                            exist = true;

                        }
                    }
                    if (!exist) {
                        unknowns.add(temp);
                    }
                } else {
                    unknowns.add(temp);

                }
            }
            if (wCopy.isUnknown(cX, cY + 1) || wCopy.hasPit(cX, cY + 1)) {
                int[] temp = {cX, cY + 1};
                if (unknowns.size() > 0) {
                    boolean exist = false;
                    for (int j = 0; j < unknowns.size(); j++) {
                        if (unknowns.get(j)[0] == temp[0] && unknowns.get(j)[1] == temp[1]) {
                            exist = true;

                        }
                    }
                    if (!exist) {
                        unknowns.add(temp);
                    }
                } else {
                    unknowns.add(temp);

                }
            }
            if (wCopy.isUnknown(cX + 1, cY) || wCopy.hasPit(cX + 1, cY)) {
                int[] temp = {cX + 1, cY};
                if (unknowns.size() > 0) {
                    boolean exist = false;
                    for (int j = 0; j < unknowns.size(); j++) {
                        if (unknowns.get(j)[0] == temp[0] && unknowns.get(j)[1] == temp[1]) {
                            exist = true;

                        }
                    }
                    if (!exist) {
                        unknowns.add(temp);
                    }
                } else {
                    unknowns.add(temp);

                }
            }
            if (unknowns.size() < lowestUnknowns) {
                lowestUnknowns = unknowns.size();
            }
            unknowns.clear();
        }
        return lowestUnknowns;
    }

    //Check odds for this tile being a pit
    public double checkPitOdds(int x, int y) {
        double ba = 0;
        double a = PIT_PROBABILITY;
        double b = 1 - a;
        double numerator = 0.;
        double denominator = 0.;
        if (checkIfBreeze(x, y)) {
            int amountOfBreeze = checkAmountOfBreeze(x, y);
            if (amountOfBreeze > 0) {
                ba = (double) amountOfBreeze - 1 / (double) getAmountOfNeighbors(x, y);
                numerator = a * ba;

                int neighborsNum = LowestUnknownNeighbors(x, y);
                int neightborsDenom = UnknownNeighbors(x, y);

                b = b * ((double) neighborsNum - 1 / (double) neightborsDenom);
                denominator = numerator + b;
            }
            return numerator / denominator;
        }
        return 0.f;
    }

    //do pit calculations and get all possible moves and assign probabilitys
    public void StateProbability() {
        PIT_PROBABILITY = (3.) / (16. - GetAmountOfVisisted());
        WUMPUS_PROBABILITY = 1. / (16. - GetAmountOfVisisted());
        ArrayList<Position> moves = new ArrayList<Position>();
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 5; j++) {

                if (isMoveable(i, j)) { //if this tile is moveable we need probability of that tile.
                    Position temp = new Position(i, j);
                    double wumpusOdds = 0.;

                    if (!foundWumpus) {
                        //If our wumpus isnt found, calculate probability that wumpus tile = this tile
                        wumpusOdds = checkWumpusOdds(i, j);
                    }
                    if (wumpusOdds >= 1) {
                        //Wumpus is on this tile, save this tile as wumpus tile.
                        foundWumpus = true;
                        wumpusTile[0] = i;
                        wumpusTile[1] = j;
                    }
                    double pitOdds = checkPitOdds(i, j); //Probability that this tile has a pit.
                    temp.wumpusProb = wumpusOdds;
                    temp.pitProb = pitOdds;
                    moves.add(temp);

                }
            }
        }
        createGoal(moves);
    }

    //Choose a goal of all possbile moves 
    public void createGoal(ArrayList<Position> moves) {
        double wumpusModifier = 50; //Rather go to pit than wumpus. give wumpus probability move value.
        double bestProb = Double.MAX_VALUE; //best probability, (Lower is better)
        int bestMove = -1;
        if (moves.size() == 1) {
            hasGoal = true;
            goalTile[0] = moves.get(0).x;
            goalTile[1] = moves.get(0).y;
            return;
        }
        if (!this.foundWumpus) {
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i).pitProb + (moves.get(i).wumpusProb * wumpusModifier) < bestProb) {
                    bestProb = moves.get(i).pitProb + (moves.get(i).wumpusProb * wumpusModifier);
                    bestMove = i;
                } else if (moves.get(i).pitProb + (moves.get(i).wumpusProb * wumpusModifier) == bestProb) {
                    if (Math.sqrt(Math.pow(w.getPlayerX() - moves.get(bestMove).x, 2.0) + Math.pow(w.getPlayerY() - moves.get(bestMove).y, 2.0)) > Math.sqrt(Math.pow(w.getPlayerX() - moves.get(i).x, 2.0) + Math.pow(w.getPlayerY() - moves.get(i).y, 2.0))) {
                        //Distance calculation, choose lowest distance. 
                        bestMove = i;
                    }
                }
            }
        } else {
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i).pitProb < bestProb) {
                    bestProb = moves.get(i).pitProb;
                    bestMove = i;
                } else if (moves.get(i).pitProb == bestProb) {
                    if (Math.sqrt(Math.pow(w.getPlayerX() - moves.get(bestMove).x, 2.0) + Math.pow(w.getPlayerY() - moves.get(bestMove).y, 2.0)) > Math.sqrt(Math.pow(w.getPlayerX() - moves.get(i).x, 2.0) + Math.pow(w.getPlayerY() - moves.get(i).y, 2.0))) {
                        bestMove = i;
                    }
                }
            }
        }

        hasGoal = true;
        goalTile[0] = moves.get(bestMove).x;
        goalTile[1] = moves.get(bestMove).y;

    }
    
    public void updateWorld(World input){
        w = input;
        wCopy = input.cloneWorld();    
    }
}
