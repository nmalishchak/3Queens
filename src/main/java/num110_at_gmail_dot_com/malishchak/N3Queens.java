package num110_at_gmail_dot_com.malishchak;

import num110_at_gmail_dot_com.malishchak.algorithms.AngleCheckScannerFullRestart;
import num110_at_gmail_dot_com.malishchak.algorithms.AngleCheckScannerUndo;
import num110_at_gmail_dot_com.malishchak.algorithms.BaseN3QueensAlgorithm;
import num110_at_gmail_dot_com.malishchak.databases.N3QueensDataContract;
import num110_at_gmail_dot_com.malishchak.databases.N3QueensDatabaseAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 *  Primary program class to manage and run algorithms to solve the N-Queens problem, with the restriction no
 *  three queens can form a line at any angle.
 *  Parses command line arguments, runs configured algorithm, and outputs results
 *
 * @author nmalishchak
 *  Copyright (C) 2021 Nick Malishchak
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

public class N3Queens {


    /**
     * Formatted output string detailing the command line arguments available
     */
    public static final String m_Usage = "Usage: ./N3Queens -n <NumberOfQueens> [-size <board size>]\n" +
                                   "\tGenerates a solution for the N-Queens problem where n\n" +
                                   "\tnumber of queens are placed on a NxN size chess board\n" +
                                   "\tsuch that no two queens can attack one another.\n" +
                                   "\tAdditionally, no three queens may be placed such that\n" +
                                   "\tthey form a straight line at any angle, measured\n" +
                                   "\tthrough the center of their square.\n" +
                                   "\n" +
                                   "\tArguments:\n" +
                                   "\t\t-n <NumberOfQueens>: Specifies the number of queens\n" +
                                   "\t\t\tto attempt to place. Size of the board will be\n" +
                                   "\t\t\t<NumberOfQueens>x<NumberOfQueens> unless\n" +
                                   "\t\t\totherwise specified using the -size switch.\n" +
                                   "\t\t-size <board size>: Sets the size of the board to\n" +
                                   "\t\t\t<board size>x<board size>. Overwritten by BoardX\n" +
                                   "\t\t\tor BoardY switches\n" +
                                   "\t\t-BoardX <board width>: Sets the width of the board to\n" +
                                   "\t\t\t <board width>. Overwrites values from size switch\n" +
                                   "\t\t-BoardY <board height>: Sets the height of the board to\n" +
                                   "\t\t\t <board height>. Overwrites values from size switch\n" +
                                   "\t\t-algorithm <#>: Sets which algorithm to use. Defaults to 1\n" +
                                   "\t\t\t0=AngleCheckScannerFullRestart, 1=AngleCheckScannerUndo\n" +
                                   "\t\t-logging <#>. Sets logging level to 0-3. Defaults to 1.\n" +
                                   "\t\t\t0=NONE, 1=SUMMARY, 2=VERBOSE, 3=DEBUG\n" +
                                   "\t\t-startX <#>. Sets the starting x position for algorithms.\n" +
                                   "\t\t\t Defaults to 0.\n" +
                                   "\n" +
                                   "\tExamples:\n" +
                                   "\t\t./N3Queens -n 12\n" +
                                   "\t\t\tAttempts to place 12 queens on a 12x12 board\n" +
                                   "\t\t./N3Queens -n 12 -size 16\n" +
                                   "\t\t\tAttempts to place 12 queens on a 16x16 board\n";

    /**
     * How to represent a queen when outputting board at end of run.
     */
    public static final char m_OutputQueenCharacter = 'W';

    /**
     * Parsed argument for number of queens that need to be placed by an algorithm
     */
    public static int m_TargetQueens = -1;

    /**
     * If true, unsuccessful algorithms should output results of run that got the closest
     * to placing all the queens. Otherwise, results of the final attempt is outputted
     */
    public static boolean m_KeepBestRun = true;

    /**
     * Parsed argument for height of the board in number of spaces
     */
    public static int m_BoardHeight = -1;

    /**
     * Parsed argument for width of the board in number of spaces
     */
    public static int m_BoardWidth = -1;

    /**
     * Stores a representation of the final board for outputting. Single dimension for iteration speed.
     */
    public static char[] m_Board;

    /**
     * Abstract class containing the specific algorithm that will be run to provide a solution
     */
    public static BaseN3QueensAlgorithm m_CurrentAlgorithm = null;

    /**
     * The index of the algorithm to be run
     */
    public static int m_TargetAlgorithmIndex = 1;

    /**
     * Defines the level of logs that should be output to console. Higher log levels may
     * produce slower results.
     */
    public static int m_TargetLoggingLevel = BaseN3QueensAlgorithm.LOGGING_LEVEL_SUMMARY;

    /**
     * Sets the X position on the first row where the first queen should be placed. Algorithms
     * will then continue to the right until end of the board is reached. Default is the first
     * square in the row, however, some algorithms may find faster solutions with different
     * start positions.
     */
    public static int m_StartXOffset = 0;

    /**
     * Parses the arguments provided on the command line into their respective variables.
     * If there is an error parsing the arguments, the m_Usage string will be output and the
     * program will exit.
     * @param args String array of command line arguments, split using the space character as
     *             a delimeter.
     */
    public static void parseArguments(String[] args)
    {
        //Did an error occur while parsing an argument
        boolean argumentParseError = false;

        //Did the user specify a specific size for the board? Overrides height/width from -n
        boolean boardSizeSpecified = false;

        //Did the user specify a specific width for the board? Overrides width from -size and -n
        boolean boardXSpecified = false;

        //Did the user specify a specific height for the board? Overrides height from -size and -n
        boolean boardYSpecified = false;

        //Print Usage if no arguments provided
        if(args.length==0)
        {
            argumentParseError = true;
        }
        else {
            //Parse arguments
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-n": { //Number of queens and size of board equal to provided argument. "-n 10"
                        if ((i + 1) < args.length) {
                            try {
                                m_TargetQueens = Integer.parseInt(args[i + 1]);
                                if (!boardYSpecified && !boardSizeSpecified) m_BoardHeight = m_TargetQueens;
                                if (!boardXSpecified && !boardSizeSpecified) m_BoardWidth = m_TargetQueens;
                                i++;
                            } catch (NumberFormatException e) {
                                System.err.println("Error parsing number of queens: " + e);
                                argumentParseError = true;
                            }
                        } else {
                            argumentParseError = true;
                        }
                        break;
                    }
                    case "-size": { //Size of board equal to provided argument. "-size 10"
                        if ((i + 1) < args.length) {
                            try {
                                int value = Integer.parseInt(args[i + 1]);
                                if (!boardYSpecified) m_BoardHeight = value;
                                if (!boardXSpecified) m_BoardWidth = value;
                                boardSizeSpecified = true;
                                i++;
                            } catch (NumberFormatException e) {
                                System.err.println("Error parsing size of board:" + e);
                                argumentParseError = true;
                            }
                        } else {
                            argumentParseError = true;
                        }
                        break;
                    }
                    case "-BoardX": { //Width of board equal to provided argument. "-BoardX 10"
                        if ((i + 1) < args.length) {
                            boardXSpecified = true;
                            try {
                                m_BoardWidth = Integer.parseInt(args[i + 1]);
                                i++;
                            } catch (NumberFormatException e) {
                                System.err.println("Error parsing BoardX:" + e);
                                argumentParseError = true;
                            }
                        } else {
                            argumentParseError = true;
                        }
                        break;
                    }
                    case "-BoardY": { //Height of board equal to provided argument
                        boardYSpecified = true;
                        if ((i + 1) < args.length) {
                            try {
                                m_BoardHeight = Integer.parseInt(args[i + 1]);
                                i++;
                            } catch (NumberFormatException e) {
                                System.err.println("Error parsing BoardY:" + e);
                                argumentParseError = true;
                            }
                        } else {
                            argumentParseError = true;
                        }
                        break;
                    }
                    case "-algorithm": { //Index of algorithm to run equal to provided argument. "-algorithm 1"
                        if ((i + 1) < args.length) {
                            try {
                                m_TargetAlgorithmIndex = Integer.parseInt(args[i + 1]);
                                i++;
                            } catch (NumberFormatException e) {
                                System.err.println("Error parsing algorithm number:" + e);
                                argumentParseError = true;
                            }
                        } else {
                            argumentParseError = true;
                        }
                        break;
                    }
                    case "-logging": { // Max level of logging based on provided int argument. "-logging 1."
                        if ((i + 1) < args.length) {
                            try {
                                m_TargetLoggingLevel = Integer.parseInt(args[i + 1]);
                                if (m_TargetLoggingLevel < 0 || m_TargetLoggingLevel > 3) {
                                    argumentParseError = true;
                                }
                                i++;
                            } catch (NumberFormatException e) {
                                System.err.println("Error parsing logging level:" + e);
                                argumentParseError = true;
                            }
                        } else {
                            argumentParseError = true;
                        }
                        break;
                    }
                    case "-startX": { //First zero-indexed X position for an algorithm equal to provided argument. "-startX 1"
                        if ((i + 1) < args.length) {
                            try {
                                m_StartXOffset = Integer.parseInt(args[i + 1]);
                                i++;
                            } catch (NumberFormatException e) {
                                System.err.println("Error parsing logging level:" + e);
                                argumentParseError = true;
                            }
                        } else {
                            argumentParseError = true;
                        }
                        break;
                    }
                    default: {
                        //Unrecognized command line argument provided
                        argumentParseError = true;
                    }
                }

                //If error in parsing, stop processing arguments
                if (argumentParseError) {
                    break;
                }
            }

            switch (m_TargetAlgorithmIndex) {
                case 0: {
                    m_CurrentAlgorithm = new AngleCheckScannerFullRestart(m_TargetQueens, m_BoardWidth, m_BoardHeight, m_StartXOffset);
                    break;
                }
                case 1: {
                    m_CurrentAlgorithm = new AngleCheckScannerUndo(m_TargetQueens, m_BoardWidth, m_BoardHeight, m_StartXOffset);
                    break;
                }
                default: {
                    argumentParseError = true;
                }
            }
        }

        //If there was an error print usage and exit
        if(argumentParseError)
        {
            System.out.println(m_Usage);
            System.exit(1);
        }


    }

    /**
     * Confirms that the found solution is correct
     * @param queens List of queens places by the solution
     * @return True if the solution is verified as correct, false otherwise.
     */
    public static boolean verifyResults(ArrayList<Queen> queens)
    {
        System.out.println("Verifying results...");

        //Check all queens placed
        if(queens.size() != m_TargetQueens)
        {
            System.out.println("SOLUTION REJECTED. Target was to place "+m_TargetQueens+" queens, solution placed "+queens.size()+".");
            return false;
        }

        //Check to ensure no queens threaten another, and no three queens form a straight line at any angle
        HashMap<Double, Integer> angles = new HashMap<Double, Integer>();
        for(int i = 0; i < queens.size(); i++)
        {
            angles.clear();
            Queen current = queens.get(i);
            for (int j = 0; j < queens.size(); j++)
            {
                //Skip current queen
                if(j==i)
                {
                    continue;
                }

                Double angle = current.findAngle(queens.get(j));
                if(angle % 45d == 0d)
                {
                    System.out.println("SOLUTION REJECTED. Queen "+(i+1)+" threatened by Queen "+(j+1)+".");
                    return false;
                }
                Double oppositeAngle = (angle > 0d ? angle-180d : angle +180d);
                if(angles.containsKey(angle) || angles.containsKey(oppositeAngle))
                {
                    int secondConflict = (angles.containsKey(angle) ? angles.get(angle) : angles.get(oppositeAngle));
                    System.out.println("SOLUTION REJECTED. Queen "+(i+1)+" on a line with Queens "+(secondConflict+1)+" and "+(j+1)+".");
                    return false;
                }
                else
                {
                    angles.put(angle, j);
                }
            }
        }
        System.out.println("Solution verified!");
        return true;
    }

    public static void main(String[] args) {
        parseArguments(args);
        System.out.println("Argument Parsing complete. Queens: "+m_TargetQueens+", Board: "+m_BoardHeight+"x"+m_BoardWidth+", StartX: "+m_StartXOffset+".");

        //Validate parsed arguments. Format was correct, but bad values may have been used
        if(m_StartXOffset < 0 || m_StartXOffset >= m_BoardWidth)
        {
            //StartX has to be at least the first position and at most the last position on the board
            System.out.println("Please choose a StartX between 0 and "+(m_BoardWidth-1)+".");
            System.exit(1);
        }

        //Track solution times
        long timerStart = 0;
        long timerDuration = 0;

        //Instantiate algorithm to be run
        if(m_CurrentAlgorithm == null)
        {
            m_CurrentAlgorithm = new AngleCheckScannerUndo(m_TargetQueens, m_BoardWidth, m_BoardHeight, m_StartXOffset);
        }
        m_CurrentAlgorithm.setKeepBestRun(m_KeepBestRun);
        m_CurrentAlgorithm.setLoggingLevel(m_TargetLoggingLevel);

        //Output algorithm to be run
        System.out.println("\nRunning algorithm \""+m_CurrentAlgorithm.getAlgorithmName()+".\" version "+m_CurrentAlgorithm.getVersion()+".");
        System.out.println("\""+m_CurrentAlgorithm.getAlgorithmDescription()+"\"\n");

        //Execute algorithm, keeping track of total time to run
        System.out.println("Placing Queens...");
        timerStart = System.currentTimeMillis();
        Results results = m_CurrentAlgorithm.run();
        timerDuration = System.currentTimeMillis() - timerStart;
        boolean success = results.getSuccess() > 0;

        //Update results
        results.setSolution_time_in_ms(timerDuration);

        //If algorithm reported solution was not found, assume algorithm was correct
        boolean verified = (success ? verifyResults(results.getPlaced_queens()) : true);
        results.setVerified((verified ? 1 : 0 ));


        //Process and output results
        if(success) {
            System.out.println("\n\nSolution found in " + timerDuration + "ms total!");
        }
        else
        {
            System.out.println("\n\nNo solution found, attempt completed in " + timerDuration + "ms total.");
            System.out.println((m_KeepBestRun ? "Best" : "Final")+" Run (Iteration "+results.getBest_iteration()+"): "+results.getPlaced_queens().size()+" of "+m_TargetQueens+" placed. (Remaining: "+results.getRemaining_queens()+").");
        }

        results.printPlacedQueens();
        results.printBoard();
        System.out.println("\n");
        results.printResultsSummary();
        System.out.println("\n");

        if(N3QueensDatabaseAdapter.addResults(results))
        {
            System.out.println("Results saved to database.");
        }
        else
        {
            System.err.println("Results failed to save to database.");
        }

        //Print best result thus far for this problem set
        System.out.println("\n");
        System.out.println("Current Best Successful Solution For Problem Set: ");
        ArrayList<Results> bestResults = N3QueensDatabaseAdapter.getResults("where "
                + N3QueensDataContract.ResultsTable.COLUMN_NAME_TARGET_QUEENS + " = " + m_TargetQueens
                + " AND " + N3QueensDataContract.ResultsTable.COLUMN_NAME_BOARD_WIDTH + " = " + m_BoardWidth
                + " AND " + N3QueensDataContract.ResultsTable.COLUMN_NAME_BOARD_HEIGHT + " = " + m_BoardHeight
                + " AND " + N3QueensDataContract.ResultsTable.COLUMN_NAME_SUCCESS + " = 1"
                + " AND " + N3QueensDataContract.ResultsTable.COLUMN_NAME_VERIFIED + " = 1 "
                + " ORDER BY " + N3QueensDataContract.ResultsTable.COLUMN_NAME_SOLUTION_TIME_IN_MS + " ASC");

        if(bestResults==null)
        {
            System.err.println("Error finding best results.");
        }
        else if (bestResults.size() == 0)
        {
            System.err.println("No verified solutions yet found for this problem set.");
        }
        else
        {
            Results best = bestResults.get(0);
            best.printResultsSummary();
            Date date = new Date(best.getDate());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format.setTimeZone(TimeZone.getDefault());
            String formatted = format.format(date);
            System.out.println("DATE: "+ formatted +". STARTX: "+best.getFirst_queen_x());
        }
    }

}
