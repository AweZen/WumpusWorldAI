package wumpusworld;

public class MyAgent implements Agent {

    private World w;
    Naive_Bayes nb;

    public MyAgent(World world) {
        w = world;
        nb = new Naive_Bayes(w);
    }

    public void doAction() {
        //Location of the player
        int cX = w.getPlayerX();
        int cY = w.getPlayerY();

        if (w.hasGlitter(cX, cY)) {
            w.doAction(World.A_GRAB);
            return;
        }
        if (w.gameOver()) {
            return;
        }
        Naive_Bayes nb = new Naive_Bayes(w);

        if (cX == nb.goalTile[0] && nb.goalTile[1] == cY) {
            nb.hasGoal = false;
            nb.goalTile[0] = 0;
            nb.goalTile[1] = 0;

        }
        if (!nb.hasGoal) {
            nb.StateProbability();
        }

        if (nb.GetAmountOfVisisted() == 1) {
            playerAction(cX, cY, nb.goalTile[0], nb.goalTile[1], w, 0, 0, w.hasStench(1, 1), true);
        } else {
            if (nb.foundWumpus) {
                int[] playPos = {cX, cY};
                if (nb.isNeighbor(nb.wumpusTile, playPos)) {
                    playerAction(cX, cY, nb.goalTile[0], nb.goalTile[1], w, nb.wumpusTile[0], nb.wumpusTile[1], w.hasStench(1, 1), false);
                }
                playerAction(cX, cY, nb.goalTile[0], nb.goalTile[1], w, 0, 0, w.hasStench(1, 1), false);
            } else {
                playerAction(cX, cY, nb.goalTile[0], nb.goalTile[1], w, 0, 0, w.hasStench(1, 1), false);
            }
        }
    }

    public void makeMove(int moveDifferenceX, int moveDifferenceY, World w, boolean startLocation, boolean stenchLocation) {
        if (moveDifferenceX >= 1) {
            if (w.getDirection() == World.DIR_RIGHT) {
                if (startLocation && stenchLocation) {
                    w.doAction(World.A_SHOOT);
                }

                w.doAction(World.A_MOVE);
            } else if (w.getDirection() == World.DIR_LEFT) {
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_TURN_RIGHT);

                if (startLocation && stenchLocation) {
                    w.doAction(World.A_SHOOT);
                }

                w.doAction(World.A_MOVE);
            } else if (w.getDirection() == World.DIR_UP) {
                w.doAction(World.A_TURN_RIGHT);

                if (startLocation && stenchLocation) {
                    w.doAction(World.A_SHOOT);
                }

                w.doAction(World.A_MOVE);
            } else if (w.getDirection() == World.DIR_DOWN) {
                w.doAction(World.A_TURN_LEFT);

                if (startLocation && stenchLocation) {
                    w.doAction(World.A_SHOOT);
                }

                w.doAction(World.A_MOVE);
            }
        } else if (moveDifferenceX <= -1) {
            if (w.getDirection() == World.DIR_RIGHT) {
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_TURN_RIGHT);

                if (startLocation && stenchLocation) {
                    w.doAction(World.A_SHOOT);
                }

                w.doAction(World.A_MOVE);
            } else if (w.getDirection() == World.DIR_LEFT) {
                if (startLocation && stenchLocation) {
                    w.doAction(World.A_SHOOT);
                }

                w.doAction(World.A_MOVE);
            } else if (w.getDirection() == World.DIR_UP) {
                w.doAction(World.A_TURN_LEFT);

                if (startLocation && stenchLocation) {
                    w.doAction(World.A_SHOOT);
                }

                w.doAction(World.A_MOVE);
            } else if (w.getDirection() == World.DIR_DOWN) {
                w.doAction(World.A_TURN_RIGHT);

                if (startLocation && stenchLocation) {
                    w.doAction(World.A_SHOOT);
                }

                w.doAction(World.A_MOVE);
            }
        } else if (moveDifferenceY <= -1) {
            if (w.getDirection() == World.DIR_RIGHT) {
                w.doAction(World.A_TURN_RIGHT);

                if (startLocation && stenchLocation) {
                    w.doAction(World.A_SHOOT);
                }

                w.doAction(World.A_MOVE);
            } else if (w.getDirection() == World.DIR_LEFT) {
                w.doAction(World.A_TURN_LEFT);

                if (startLocation && stenchLocation) {
                    w.doAction(World.A_SHOOT);
                }

                w.doAction(World.A_MOVE);
            } else if (w.getDirection() == World.DIR_UP) {
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_TURN_RIGHT);

                if (startLocation && stenchLocation) {
                    w.doAction(World.A_SHOOT);
                }

                w.doAction(World.A_MOVE);
            } else if (w.getDirection() == World.DIR_DOWN) {
                if (startLocation && stenchLocation) {
                    w.doAction(World.A_SHOOT);
                }

                w.doAction(World.A_MOVE);
            }
        } else if (moveDifferenceY >= 1) {
            if (w.getDirection() == World.DIR_RIGHT) {
                w.doAction(World.A_TURN_LEFT);

                if (startLocation && stenchLocation) {
                    w.doAction(World.A_SHOOT);
                }

                w.doAction(World.A_MOVE);
            } else if (w.getDirection() == World.DIR_LEFT) {
                w.doAction(World.A_TURN_RIGHT);

                if (startLocation && stenchLocation) {
                    w.doAction(World.A_SHOOT);
                }

                w.doAction(World.A_MOVE);
            } else if (w.getDirection() == World.DIR_UP) {
                if (startLocation && stenchLocation) {
                    w.doAction(World.A_SHOOT);
                }

                w.doAction(World.A_MOVE);
            } else if (w.getDirection() == World.DIR_DOWN) {
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_TURN_LEFT);

                if (startLocation && stenchLocation) {
                    w.doAction(World.A_SHOOT);
                }

                w.doAction(World.A_MOVE);
            }
        }
    }

    public Position[] removeElement(Position[] originalArray, int index) {
        Position newArray[] = new Position[originalArray.length];

        for (int i = 0, j = 0; i < originalArray.length; i++) {
            if (i == index) {
                continue;
            }

            newArray[j++] = originalArray[i];
        }

        return newArray;
    }

    public void playerAction(int playerX, int playerY, int destinationX, int destinationY, World w, int xWumpus, int yWumpus, boolean stenchLocation, boolean startLocation) {
        int wumpusDifferenceX = xWumpus - playerX;
        int wumpusDifferenceY = yWumpus - playerY;

        boolean hasShotWumpus = false;

        if (w.hasGlitter(playerX, playerY)) {
            w.doAction(World.A_GRAB);
        }
        if (w.isInPit()) {
            w.doAction(World.A_CLIMB);
        }

        if (xWumpus > 0) // Wumpus is found since coordinates are between 1 and 4.
        {
            System.out.println("Shoots the wumpus!");
            hasShotWumpus = true;
            if (wumpusDifferenceX == 1 && wumpusDifferenceY == 0) {
                // Wumpus to the right
                if (w.getDirection() == World.DIR_RIGHT) {
                    w.doAction(World.A_SHOOT);
                } else if (w.getDirection() == World.DIR_LEFT) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_SHOOT);
                } else if (w.getDirection() == World.DIR_UP) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_SHOOT);
                } else if (w.getDirection() == World.DIR_DOWN) {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_SHOOT);
                }
            }
            if (wumpusDifferenceX == -1 && wumpusDifferenceY == 0) {
                // Wumpus to the left
                if (w.getDirection() == World.DIR_RIGHT) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_SHOOT);
                } else if (w.getDirection() == World.DIR_LEFT) {
                    w.doAction(World.A_SHOOT);
                } else if (w.getDirection() == World.DIR_UP) {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_SHOOT);
                } else if (w.getDirection() == World.DIR_DOWN) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_SHOOT);
                }
            }
            if (wumpusDifferenceX == 0 && wumpusDifferenceY == 1) {
                // Wumpus up
                if (w.getDirection() == World.DIR_RIGHT) {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_SHOOT);
                } else if (w.getDirection() == World.DIR_LEFT) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_SHOOT);
                } else if (w.getDirection() == World.DIR_UP) {
                    w.doAction(World.A_SHOOT);
                } else if (w.getDirection() == World.DIR_DOWN) {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_SHOOT);
                }
            }
            if (wumpusDifferenceX == 0 && wumpusDifferenceY == -1) {
                // Wumpus down
                if (w.getDirection() == World.DIR_RIGHT) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_SHOOT);
                } else if (w.getDirection() == World.DIR_LEFT) {
                    w.doAction(World.A_TURN_LEFT);
                    w.doAction(World.A_SHOOT);
                } else if (w.getDirection() == World.DIR_UP) {
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_TURN_RIGHT);
                    w.doAction(World.A_SHOOT);
                } else if (w.getDirection() == World.DIR_DOWN) {
                    w.doAction(World.A_SHOOT);
                }
            }
        }

        if (!hasShotWumpus) // if we didn't shoot, we can move
        {
            // 1: collect neighbors
            Position neighbours[] = new Position[4];
            int neighbourCounter = 0;
            boolean goalReached = false;
            int goalIndex = 0;

            if (playerX - 1 > 0) {
                neighbours[neighbourCounter] = new Position(playerX - 1, playerY, w.hasStench(playerX - 1, playerY), w.hasBreeze(playerX - 1, playerY), w.hasGlitter(playerX - 1, playerY));

                // 2: if goal ahead go there
                if (destinationX == playerX - 1 && destinationY == playerY) {
                    goalReached = true;
                    goalIndex = neighbourCounter;
                }

                neighbourCounter++;
            }
            if (playerX + 1 < 5) {
                neighbours[neighbourCounter] = new Position(playerX + 1, playerY, w.hasStench(playerX + 1, playerY), w.hasBreeze(playerX + 1, playerY), w.hasGlitter(playerX + 1, playerY));

                // 2: if goal ahead go there
                if (destinationX == playerX + 1 && destinationY == playerY) {
                    goalReached = true;
                    goalIndex = neighbourCounter;
                }

                neighbourCounter++;
            }
            if (playerY - 1 > 0) {
                neighbours[neighbourCounter] = new Position(playerX, playerY - 1, w.hasStench(playerX, playerY - 1), w.hasBreeze(playerX, playerY - 1), w.hasGlitter(playerX, playerY - 1));

                // 2: if goal ahead go there
                if (destinationX == playerX && destinationY == playerY - 1) {
                    goalReached = true;
                    goalIndex = neighbourCounter;
                }

                neighbourCounter++;
            }
            if (playerY + 1 < 5) {
                neighbours[neighbourCounter] = new Position(playerX, playerY + 1, w.hasStench(playerX, playerY + 1), w.hasBreeze(playerX, playerY + 1), w.hasGlitter(playerX, playerY + 1));

                // 2: if goal ahead go there.
                if (destinationX == playerX && destinationY == playerY + 1) {
                    goalReached = true;
                    goalIndex = neighbourCounter;
                }

                neighbourCounter++;
            }
            // 3: if only one neighbor, go to it
            if (goalReached) {
                int neighbourMoveDifferenceX = neighbours[goalIndex].x - playerX;
                int neighbourMoveDifferenceY = neighbours[goalIndex].y - playerY;

                makeMove(neighbourMoveDifferenceX, neighbourMoveDifferenceY, w, startLocation, stenchLocation);
            } else {
                //if we have as many pits as neighbors we have to go though the pit
                int nrOfPitsInNeighbours = 0;
                int nrOfPitFreeNeighbours = 0;
                int safeNeighbours[] = new int[4];
                int pitIndex = 0;

                for (int i = 0; i < neighbourCounter; i++) {
                    if (w.hasPit(neighbours[i].x, neighbours[i].y)) {
                        nrOfPitsInNeighbours++;
                        pitIndex = i;
                    }
                    if (!w.hasPit(neighbours[i].x, neighbours[i].y) && w.isVisited(neighbours[i].x, neighbours[i].y)) {
                        safeNeighbours[nrOfPitFreeNeighbours] = i;
                        nrOfPitFreeNeighbours++;
                    }
                }

                if (nrOfPitFreeNeighbours > 0 && safeNeighbours.length > 0) {
                    // 4: if we can go to a neighbor that doesnt have pit, do it
                    int closestIndexToGoal = safeNeighbours[0];

                    if (nrOfPitFreeNeighbours > 1) {
                        for (int i = 0; i < nrOfPitFreeNeighbours; i++) {
                            if (Math.sqrt(Math.pow(neighbours[safeNeighbours[i]].x - destinationX, 2.0) + Math.pow(neighbours[safeNeighbours[i]].y - destinationY, 2.0))
                                    < Math.sqrt(Math.pow(neighbours[closestIndexToGoal].x - destinationX, 2.0) + Math.pow(neighbours[closestIndexToGoal].y - destinationY, 2.0))) // Distansekvation i 2D
                            {
                                closestIndexToGoal = safeNeighbours[i];
                            }
                        }
                    }

                    int safeMoveDifferenceX = neighbours[closestIndexToGoal].x - playerX;
                    int safeMoveDifferenceY = neighbours[closestIndexToGoal].y - playerY;

                    makeMove(safeMoveDifferenceX, safeMoveDifferenceY, w, startLocation, stenchLocation);
                } else {
                    // 5: else we go though pit                   
                    int pitMoveDifferenceX = neighbours[pitIndex].x - playerX;
                    int pitMoveDifferenceY = neighbours[pitIndex].y - playerY;

                    makeMove(pitMoveDifferenceX, pitMoveDifferenceY, w, startLocation, stenchLocation);
                }
            }
        }
    }
}
