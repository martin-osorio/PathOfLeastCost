package com.example.pathoflowestcost;

import java.util.ArrayList;
import java.util.List;

/**
 * The Pathfinder class is used to find the least costly path in a matrix,
 * based on a specific rule set:
 * A path can only progress directly right,
 * or diagonally to the adjacent above-right and bellow-right cells.
 * The first and last row are considered adjacent (They "wrap")
 * A path must cost less than 50 to be valid
 * <p>
 * Additionally, this class contains other functionality
 * that is either required for the process, or as a tool for the user
 * <p>
 * The input for the pathfinder is a matrix represented by comma-separated columns,
 * and newline separated rows.
 * For example, "1,2,3\n1,2,3" represents:
 * 1 2 3
 * 1 2 3
 */
public abstract class Pathfinder {
    /**
     * Entry point and only public function of the class
     * <p>
     * Finds the cheapest path based on the rule set
     * The algorithm works as follows:
     * Start from the right, and work your way backwards to the left
     * by (sort-of) recursively comparing only two columns.
     * However, the second column is not the cells that would be to the right of the "left" column
     * but instead a representation of the cheapest possible path further right
     * This is achieved by establishing base cases as described bellow
     *
     * @param input
     * @return the cheapest path
     */
    public static Path findPath(String input) {
        //Storage for the matrix
        List<List<Integer>> listOfRows = Pathfinder.getTableFromCommaDelimitedString(input);
        List<List<Cell>> listOfColumns = convertToColumns(listOfRows);

        //The number of columns in the matrix, this is also the amount of steps in the path
        int numColumns = listOfColumns.size();

        //Storage for the cheapest path
        Path cheapest = null;

        //Base case where there is only one column
        //In this case, the cheapest path is the row with the cheapest single value
        if (numColumns == 1) {
            Cell currentMin = null;
            for (Cell cell : listOfColumns.get(0)) {
                if (currentMin == null || cell.getCost() < currentMin.getCost()) {
                    currentMin = cell;
                }
            }
            cheapest = new Path(currentMin);
        } else if (numColumns == 2) {   //Base case where there are exactly two rows
            List<Path> paths = twoRowCase(listOfColumns.get(--numColumns), listOfColumns.get(--numColumns));
            for (Path p : paths) {
                if (cheapest == null || p.getTotalCost() < cheapest.getTotalCost()) {
                    cheapest = p;
                }
            }
        } else if (numColumns == 3) {   //Base case where there are exactly three rows
            List<Path> lastTwoColumnsOnlyPath =
                    twoRowCase(listOfColumns.get(--numColumns), listOfColumns.get(--numColumns));
            List<Path> paths = generalCase(listOfColumns.get(--numColumns), lastTwoColumnsOnlyPath);
            for (Path p : paths) {
                if (cheapest == null || p.getTotalCost() < cheapest.getTotalCost()) {
                    cheapest = p;
                }
            }
        } else {    //General case
            //Effectively performs the first three cases reading the matrix from right to left

            //Path when looking only at the last two columns
            List<Path> lastTwoColumnsOnlyPath =
                    twoRowCase(listOfColumns.get(--numColumns), listOfColumns.get(--numColumns));

            //Path when looking at the last three columns
            List<Path> pathsWithPreviousColumn =
                    generalCase(listOfColumns.get(--numColumns), lastTwoColumnsOnlyPath);

            //While there are rows remaining, keep adding them to the algorithm
            while (numColumns > 0) {
                ArrayList<Path> copyOfPrevious = new ArrayList<>();
                for (Path p : pathsWithPreviousColumn) {
                    copyOfPrevious.add(new Path(p));
                }
                pathsWithPreviousColumn = generalCase(listOfColumns.get(--numColumns), copyOfPrevious);
            }

            //Of all good paths found, find the best
            for (Path p : pathsWithPreviousColumn) {
                if (cheapest == null || p.getTotalCost() < cheapest.getTotalCost()) {
                    cheapest = p;
                }
            }
        }

        if (cheapest != null) {
            cheapest.validatePath();
        }
        return cheapest;
    }

    //Holdover from an earlier implementation
    static List<List<Integer>> getTableFromCommaDelimitedString(String s) {
        List<Integer> row = new ArrayList<>();
        List<List<Integer>> table = new ArrayList<>();

        String[] separatedCells = s.split(",");
        for (String cell : separatedCells) {
            if (cell.contains("\n")) {//If Cell == Newline
                String[] substring = cell.split("\n");
                if (substring.length > 2) {
                    for (String str : substring) {
                        row.add(Integer.parseInt(str));
                        table.add(row);
                        row = new ArrayList<>();
                    }
                } else if (substring.length == 2) {
                    row.add(Integer.parseInt(substring[0]));
                    table.add(row);
                    row = new ArrayList<>();
                    row.add(Integer.parseInt(substring[1]));
                } else if (substring.length == 1) {
                    row.add(Integer.parseInt(substring[0]));
                    table.add(row);
                    row = new ArrayList<>();
                }
            } else {
                row.add(Integer.parseInt(cell));
            }
        }
        if (row.size() > 0) {
            table.add(row);
        }

        return table;
    }

    /**
     * Convert the above result into the format used by the new implementation
     * Simply reverses the way the data is being stored
     * Does not affect the data
     *
     * @param table a list of rows
     * @return a list of columns
     */
    private static List<List<Cell>> convertToColumns(List<List<Integer>> table) {
        List<Cell> column = new ArrayList<>();
        List<List<Cell>> listOfColumns = new ArrayList<>();

        int numColumns = table.get(0).size();

        //for each column
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < table.size(); j++) {
                column.add(new Cell(j, i, table.get(j).get(i)));
            }
            listOfColumns.add(column);
            column = new ArrayList<>();
        }

        return listOfColumns;
    }

    /**
     * Since the matrix "wraps", the next value might be on the other end
     * Use this to avoid checking this regularly
     *
     * @param currentY
     * @param numRows
     * @return
     */
    static int getNextYAbove(int currentY, int numRows) {
        int nextY = currentY + 1;
        if (nextY >= numRows) {
            return 0;
        } else {
            return nextY;
        }
    }


    /**
     * Since the matrix "wraps", the next value might be on the other end
     * Use this to avoid checking this regularly
     *
     * @param currentY
     * @param numRows
     * @return
     */
    static int getNextYBelow(int currentY, int numRows) {
        int nextY = currentY - 1;
        if (nextY < 0) {
            return numRows - 1;
        } else {
            return nextY;
        }
    }

    /**
     * Implementation of the case where there are two rows
     *
     * @param right right column
     * @param left  left column
     * @return a set of good paths based on each rows initial cell
     */
    private static List<Path> twoRowCase(List<Cell> right, List<Cell> left) {
        ArrayList<Path> paths = new ArrayList<>();

        //For each row in the left column
        //Determine which of its three options would produce the cheapest path
        //Add this path to a set of possible good paths
        for (int i = 0; i < left.size(); i++) {
            int indexA = getNextYAbove(i, left.size()); //Index of the cell above-right
            int indexC = getNextYBelow(i, left.size()); //Index of the cell bellow-right
            //Note that i is the index of the cell to the right

            //Determine what the combined cost would be with all three options
            int sumWithA = right.get(indexA).getCost() + left.get(i).getCost();
            int sumWithB = right.get(i).getCost() + left.get(i).getCost();
            int sumWithC = right.get(indexC).getCost() + left.get(i).getCost();

            //Compare which option is cheapest
            //Then create a path with those two cells
            if (sumWithA <= sumWithB) {
                if (sumWithA <= sumWithC) {
                    Path p = new Path(right.get(indexA));
                    p.add(left.get(i));
                    paths.add(p);
                } else {
                    Path p = new Path(right.get(indexC));
                    p.add(left.get(i));
                    paths.add(p);
                }
            } else {
                if (sumWithB <= sumWithC) {
                    Path p = new Path(right.get(i));
                    p.add(left.get(i));
                    paths.add(p);
                } else {
                    Path p = new Path(right.get(indexC));
                    p.add(left.get(i));
                    paths.add(p);
                }
            }
        }

        return paths;
    }

    /**
     * Implementation of the case where there are more than two rows
     *
     * @param right a parallel set of paths to the left column,
     *              which contain what the optimal steps are further to thr right
     * @param left  the left column
     * @return a set of good paths based on each rows initial cell
     */
    private static List<Path> generalCase(List<Cell> left, List<Path> right) {
        ArrayList<Path> paths = new ArrayList<>();

        //For each row in the left column
        //Determine which of its three options would produce the cheapest path
        //This time, do so by adding it
        // to the value that represents the cheapest possible value
        // for all columns further right
        //Add this path to a set of possible good paths
        for (int i = 0; i < left.size(); i++) {
            int indexA = getNextYAbove(i, left.size()); //Index of the cell above-right
            int indexC = getNextYBelow(i, left.size()); //Index of the cell bellow-right
            //Note that i is the index of the cell to the right

            //Determine what the combined cost would be with all three options
            //In this case, the right column represents not a single column,
            // but the cheapest path achievable to the right of the "left" column
            int sumWithA = right.get(indexA).getTotalCost() + left.get(i).getCost();
            int sumWithB = right.get(i).getTotalCost() + left.get(i).getCost();
            int sumWithC = right.get(indexC).getTotalCost() + left.get(i).getCost();

            //Compare which option is cheapest
            //Then create a path with those two cells
            if (sumWithA <= sumWithB) {
                if (sumWithA <= sumWithC) {
                    Path p = new Path(right.get(indexA));
                    p.add(left.get(i));
                    paths.add(p);
                } else {
                    Path p = new Path(right.get(indexC));
                    p.add(left.get(i));
                    paths.add(p);
                }
            } else {
                if (sumWithB <= sumWithC) {
                    Path p = new Path(right.get(i));
                    p.add(left.get(i));
                    paths.add(p);
                } else {
                    Path p = new Path(right.get(indexC));
                    p.add(left.get(i));
                    paths.add(p);
                }
            }
        }

        return paths;
    }
}