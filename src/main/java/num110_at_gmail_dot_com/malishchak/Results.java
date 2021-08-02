package num110_at_gmail_dot_com.malishchak;

import java.util.ArrayList;

/**
 * Container class for storing the results of algorithm runs
 *
 * @author nmalishchak
 * Copyright (C) 2021 Nick Malishchak
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class Results {

    /**
     * How to represent a queen when outputting board at end of run.
     */
    public static final char m_OutputQueenCharacter = 'W';

    /**
     * @return The date on which the run producing these results was completed as milliseconds after the UNIX epoch
     */
    public long getDate() {
        return date;
    }

    /**
     * @param date The date on which the run producing these results was completed as milliseconds after the UNIX epoch
     */
    public void setDate(long date) {
        this.date = date;
    }

    /**
     * @return The name of the algorithm that produced these results
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * @param algorithm The name of the algorithm that produced these results
     */
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * @return The version of the algorithm that produced these results
     */
    public int getAlgorithm_version() {
        return algorithm_version;
    }

    /**
     * @param algorithm_version The version of the algorithm that produced these results
     */
    public void setAlgorithm_version(int algorithm_version) {
        this.algorithm_version = algorithm_version;
    }

    /**
     * @return The target number of queens that needed to be placed on a chess board
     */
    public int getTarget_queens() {
        return target_queens;
    }

    /**
     * @param target_queens The target number of queens that needed to be placed on a chess board
     */
    public void setTarget_queens(int target_queens) {
        this.target_queens = target_queens;
    }

    /**
     * @return The width of the chess board on which the target number of queens needed to be placed
     */
    public int getBoard_width() {
        return board_width;
    }

    /**
     * @param board_width The width of the chessboard on which the target number of queens needed to be placed
     */
    public void setBoard_width(int board_width) {
        this.board_width = board_width;
    }

    /**
     * @return The height of the chessboard on which the target number of queens needed to be placed
     */
    public int getBoard_height() {
        return board_height;
    }

    /**
     * @param board_height The height of the chessboard on which the target number of queens needed to be placed
     */
    public void setBoard_height(int board_height) {
        this.board_height = board_height;
    }

    /**
     * @return True if the algorithm believed it was successful in finding a solution
     */
    public int getSuccess() {
        return success;
    }

    /**
     * @param success True if the algorithm believed it was successful in finding a solution
     */
    public void setSuccess(int success) {
        this.success = success;
    }

    /**
     * @return True if the N3Queens main program successfully verified the success reported by the algorithm
     */
    public int getVerified() {
        return verified;
    }

    /**
     * @param verified True if the N3Queens main program successfully verified the success reported by the algorithm
     */
    public void setVerified(int verified) {
        this.verified = verified;
    }

    /**
     * @return True if the results were reported was from the best iteration of the algorithm.
     */
    public int getWas_best_run() {
        return was_best_run;
    }

    /**
     * @param was_best_run True if the results were reported was from the best iteration of the algorithm.
     */
    public void setWas_best_run(int was_best_run) {
        this.was_best_run = was_best_run;
    }

    /**
     * @return The number of queens the algorithm was able to place on the chess board from the reported results
     */
    public int getRemaining_queens() {
        return remaining_queens;
    }

    /**
     * @param remaining_queens The number of queens the algorithm was able to place on the chess board from the reported results
     */
    public void setRemaining_queens(int remaining_queens) {
        this.remaining_queens = remaining_queens;
    }

    /**
     * @return The x-coordinate of the first queen placed on the chess board
     */
    public int getFirst_queen_x() {
        return first_queen_x;
    }

    /**
     * @param first_queen_x The x-coordinate of the first queen placed on the chess board
     */
    public void setFirst_queen_x(int first_queen_x) {
        this.first_queen_x = first_queen_x;
    }

    /**
     * @return The total calculated time in ms it took the algorithm to report a final result
     */
    public long getSolution_time_in_ms() {
        return solution_time_in_ms;
    }

    /**
     * @param solution_time_in_ms The total calculated time in ms it took the algorithm to report a final result
     */
    public void setSolution_time_in_ms(long solution_time_in_ms) {
        this.solution_time_in_ms = solution_time_in_ms;
    }

    /**
     * @return An ArrayList of all queens placed by the algorithm
     */
    public ArrayList<Queen> getPlaced_queens() {
        return placed_queens;
    }

    /**
     * @param placed_queens An ArrayList of all queens placed by the algorithm
     */
    public void setPlaced_queens(ArrayList<Queen> placed_queens) {
        this.placed_queens = placed_queens;
    }

    /**
     * @return Which iteration produced the provided result. Iteration numbering is determined by the algorithm.
     */
    public int getBest_iteration() {
        return best_iteration;
    }

    /**
     * @param best_iteration Which iteration produced the provided result. Iteration numbering is determined by the algorithm.
     */
    public void setBest_iteration(int best_iteration) {
        this.best_iteration = best_iteration;
    }

    /**
     * @return Total iterations completed by the algorithm. Iteration numbering is determined by the algorithm.
     */
    public int getTotal_iterations() {
        return total_iterations;
    }

    /**
     * @param total_iterations Total iterations completed by the algorithm. Iteration numbering is determined by the algorithm.
     */
    public void setTotal_iterations(int total_iterations) {
        this.total_iterations = total_iterations;
    }


    private long date;
    private String algorithm;
    private int algorithm_version;
    private int target_queens;
    private int board_width;
    private int board_height;
    private int success;
    private int verified;
    private int was_best_run;
    private int remaining_queens;
    private int first_queen_x;
    private long solution_time_in_ms;
    private int best_iteration;
    private int total_iterations;
    private ArrayList<Queen> placed_queens;

    /**
     * @return A single-string representation of the Results, not
     * @param printQueens True if the position of each queen in placed_queens should be included
     */
    public String toString(boolean printQueens)
    {
        String result = "";

        result += "Date: " + date + ", ";
        result += "Algorithm: " + algorithm + ", ";
        result += "AlgVers: " + algorithm_version + ", ";
        result += "TargetQueens: " + target_queens + ", ";
        result += "BoardWidth: " + board_width + ", ";
        result += "BoardHeight: " + board_height + ", ";
        result += "Success: " + success + ", ";
        result += "Verified: " + verified + ", ";
        result += "WasBestRun: " + was_best_run + ", ";
        result += "RemainQueen: " + remaining_queens + ", ";
        result += "FirstQueenX: " + first_queen_x + ", ";
        result += "SolutionTime: " + solution_time_in_ms + "ms, ";
        result += "BestIteration: " + best_iteration + ", ";
        result += "TotalIterations: " + total_iterations + ", ";

        if(printQueens)
        {
            if(placed_queens==null)
            {
                result += "Placed Queens: null";
            }
            else
            {
                result += "Placed Queens: {";
                for(int i = 0; i < placed_queens.size(); i++)
                {
                    result += "("+placed_queens.get(i).getX()+","+placed_queens.get(i).getY()+")";
                    if(i!=placed_queens.size()-1)
                    {
                        result += ", ";
                    }
                }
                result += "}";
            }
        }

        return result;
    }

    /**
     * Print a simple summary of the results
     */
    public void printResultsSummary()
    {
        System.out.println("===RUN SUMMARY===");
        System.out.println("TARGET: "+target_queens+" Queens. BOARD: "+board_width+"x"+board_height+".");
        System.out.println("ALGORITHM: \""+algorithm+".\" VERSION: "+algorithm_version+".");
        if(verified>0)
        {
            System.out.println("RESULT: "+(success >0 ? "Success." : "Failure - Best Result was "+(target_queens - remaining_queens)+" of "+target_queens+" placed."));
        }
        else
        {
            System.out.println("RESULT: Failure - Algorithm reported success but solution failed verification.");
        }
        System.out.println("RUNTIME: "+solution_time_in_ms+"ms.");
    }

    /**
     * Print (x,y) coordinates of all placed queens in returned result
     */
    public void printPlacedQueens()
    {
        System.out.println("Queens placed at:");
        for(int l=0; l< getPlaced_queens().size(); l++)
        {
            Queen q = getPlaced_queens().get(l);
            System.out.println("\tQueen "+(l+1)+": "+getAlgebraicNotation(q)+" ("+q.getX()+","+q.getY()+")");
        }
    }

    public void printBoard()
    {
        //Add Queen positions to board for printing
        System.out.println("");
        char[] board = new char[board_height * board_width];
        for (int i = 0; i < board.length; i++) {
            board[i] = ' ';
        }

        for (int qIndex = 0; qIndex < getPlaced_queens().size(); qIndex++) {
            Queen q = getPlaced_queens().get(qIndex);
            board[q.getY() * board_width + q.getX()] = m_OutputQueenCharacter;
        }

        //Print final board
        String topBottomBorder = "  +";
        String squareLettering = "   ";
        for (int bIndex = 0; bIndex < board_width; bIndex++) {
            topBottomBorder += "-";
            squareLettering += (char)((bIndex%26)+97);
        }
        topBottomBorder+="+";
        System.out.println(squareLettering);
        System.out.println(topBottomBorder);
        for (int yIndex = 0; yIndex < board_height; yIndex++) {
            int yValue = yIndex * board_width;
            System.out.format("%2d|", (board_height-yIndex));
            for (int xIndex = 0; xIndex < board_width; xIndex++) {
                System.out.print(board[yValue + xIndex]);
            }
            System.out.print("|"+(board_height-yIndex)+"\n");
        }
        System.out.println(topBottomBorder);
        System.out.println(squareLettering);
    }

    /**
     * Recursively converts a given integer to an alphabetic base-26 value. For example, "28" = "ac"
     * @param xPosition Integer to convert
     * @return Alphabetic zero-indexed base-26 representation of xPosition
     */
    public String getANLetters(int xPosition)
    {
        if(xPosition<26)
        {
            return ((char)(xPosition+'a')) + "";
        }
        else
        {
            int currentPosition = xPosition % 26;
            return getANLetters((xPosition/26)-1) + ((char)(currentPosition+'a'));
        }
    }

    /**
     * Calculates the board position of a queen by converting its x,y coordinates to Algebraic
     * Notation. For example, the upper left portion of the board (0,0) would equate to "a8"
     * @param queen The Queen for which to find the Algebraic Notation of its position
     * @return The Algebraic Notation location of the given queen
     */
    public String getAlgebraicNotation(Queen queen)
    {
        return getANLetters(queen.getX()) + (board_height-queen.getY());
    }
}
