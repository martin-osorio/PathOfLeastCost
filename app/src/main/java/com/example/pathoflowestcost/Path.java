package com.example.pathoflowestcost;

import java.util.ArrayList;
import java.util.List;

/**
 * A path of costs
 *
 * Does not enforce the rule set, nor have a maximum length,
 * nor knows when it is completed
 * as it is agnostic to the matrix it belongs to.
 *
 * User is expected to call validatePath() when finished,
 * however, trying to output the path using the toString() will validate.
 */
public class Path {
    private boolean success;    //True if totalCost is <= 50
    private int totalCost;      //The sum of all costs in the path
    private List<Cell> pathReversed;    //The sequence of cells in the path
    private boolean badPath = false;    //Used when a path's next step would be over 50 total

    /**
     * Constructor when the first cell is known
     *
     * @param firstCell
     */
    Path(Cell firstCell) {
        this.success = false;
        this.totalCost = 0;
        this.pathReversed = new ArrayList<>();
        this.pathReversed.add(firstCell);
    }

    /**
     * Copy constructor
     *
     * @param path
     */
    Path(Path path) {
        this.success = path.success;
        this.totalCost = path.totalCost;
        this.pathReversed = new ArrayList<>();
        this.pathReversed.addAll(path.pathReversed);
    }

    /**
     * Empty constructor
     */
    Path(){
        this.success = false;
        this.totalCost = 0;
        this.pathReversed = new ArrayList<>();
    }

    /**
     * Adds a cell to the path
     *
     * @param cell
     */
    void add(Cell cell) {
        pathReversed.add(cell);
    }

    /**
     * Used when navigation through the matrix is complete
     *
     * Since the Path is agnostic to the matrix,
     * this is used to declare that the path is complete
     */
    void validatePath() {
        validateCost();
        validateSuccess();
    }

    /**
     * Recalculates the sum total of all cell's costs
     */
    private void validateCost() {
        totalCost = 0;

        for (Cell cell : pathReversed) {
            totalCost = totalCost + cell.getCost();
        }
    }

    /**
     * Determines if the path adheres to the rule
     * where the total cost must not be greater than 50
     */
    private void validateSuccess() {
        success = !badPath && totalCost <= 50;
    }

    /**
     * Creates a string representation of the path
     *
     * Additionally, to avoid NPE and bad data, re-validates the path
     *
     * @return a string representation of the path
     */
    public String toString() {
        validatePath();

        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append(success ? "Yes\n" : "No\n").append(totalCost).append("\n[");

        List<Cell> reverse = reverse();
        for(int i = 0; i < reverse.size()-1; i++){
            if (reverse.get(i) != null) {
                stringBuilder.append(reverse.get(i).getXPos()+1).append(" ");
            }
        }
        stringBuilder.append(reverse.get(reverse.size()-1).getXPos()+1);

        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    /**
     * Getter for totalCost
     *
     * @return the total cost of the path
     */
    int getTotalCost() {
        return totalCost;
    }

    /**
     * Getter for the success of the path based on the rule set
     *
     * @return whether the path was successful
     */
    boolean isSuccess() {
        return success;
    }

    /**
     * Used to mark when a path's next step would cause it to go over 50
     */
    void setBadPath(){
        badPath = true;
    }

    /**
     * Since the logic above works from right to left,
     * use this to reverse it before printing
     *
     * @return The path in left to right order
     */
    private List<Cell> reverse(){
        List<Cell> path = new ArrayList<>();
        for(int i = pathReversed.size()-1; i >= 0; i--){
            path.add(pathReversed.get(i));
        }
        return path;
    }
}
