package com.example.pathoflowestcost;

/**
 * A single value in the matrix
 */
class Cell {
    private int YPos;
    private int XPos;
    private int cost;

    Cell(int XPos, int YPos, int cost) {
        this.YPos = YPos;
        this.XPos = XPos;
        this.cost = cost;
    }

    int getYPos() {
        return YPos;
    }

    int getXPos() {
        return XPos;
    }

    int getCost() {
        return cost;
    }
}
