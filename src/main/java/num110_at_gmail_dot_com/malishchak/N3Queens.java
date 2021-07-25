package num110_at_gmail_dot_com.malishchak;

import num110_at_gmail_dot_com.malishchak.algorithms.AngleCheckScannerFullRestart;
import num110_at_gmail_dot_com.malishchak.algorithms.AngleCheckScannerUndo;
import num110_at_gmail_dot_com.malishchak.algorithms.BaseN3QueensAlgorithm;

/**
 *  Primary program class to manage and run algorithms to solve the N-Queens problem
 *  Parses command line arguments, runs configured algorithm, and outputs results
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
                                   "\t\t-algorithm <#>: Sets which algorithm to use.\n" +
                                   "\t\t-logging <#>. Sets logging level to 0-3. Defaults to 1.\n" +
                                   "\t\t\t0=NONE, 1=SUMMARY, 2=VERBOSE, 3=DEBUG" +
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

        //Parse arguments
        for(int i =0; i< args.length; i++)
        {
            switch (args[i]) {
                case "-n": { //Number of queens and size of board equal to provided argument. "-n 10"
                    if((i+1)<args.length)
                    {
                        try {
                            m_TargetQueens = Integer.parseInt(args[i + 1]);
                            if(!boardYSpecified&&!boardSizeSpecified) m_BoardHeight = m_TargetQueens;
                            if(!boardXSpecified&&!boardSizeSpecified) m_BoardWidth = m_TargetQueens;
                            i++;
                        }
                        catch (NumberFormatException e)
                        {
                            System.out.println("Error parsing number of queens: "+ e);
                            argumentParseError = true;
                        }
                    }
                    else
                    {
                        argumentParseError = true;
                    }
                    break;
                }
                case "-size": { //Size of board equal to provided argument. "-size 10"
                    if((i+1)<args.length)
                    {
                        try {
                            int value = Integer.parseInt(args[i + 1]);
                            if(!boardYSpecified) m_BoardHeight = value;
                            if(!boardXSpecified) m_BoardWidth = value;
                            boardSizeSpecified = true;
                            i++;
                        }
                        catch (NumberFormatException e)
                        {
                            System.out.println("Error parsing size of board:" + e);
                            argumentParseError = true;
                        }
                    }
                    else
                    {
                        argumentParseError = true;
                    }
                    break;
                }
                case "-BoardX": { //Width of board equal to provided argument. "-BoardX 10"
                    if((i+1)<args.length)
                    {
                        boardXSpecified = true;
                        try {
                            m_BoardWidth = Integer.parseInt(args[i + 1]);
                            i++;
                        }
                        catch (NumberFormatException e)
                        {
                            System.out.println("Error parsing BoardX:" + e);
                            argumentParseError = true;
                        }
                    }
                    else
                    {
                        argumentParseError = true;
                    }
                    break;
                }
                case "-BoardY": { //Height of board equal to provided argument
                    boardYSpecified = true;
                    if((i+1)<args.length)
                    {
                        try
                        {
                            m_BoardHeight = Integer.parseInt(args[i + 1]);
                            i++;
                        }
                        catch (NumberFormatException e)
                        {
                            System.out.println("Error parsing BoardY:" + e);
                            argumentParseError = true;
                        }
                    }
                    else
                    {
                        argumentParseError = true;
                    }
                    break;
                }
                case "-algorithm": { //Index of algorithm to run equal to provided argument. "-algorithm 1"
                    if((i+1)<args.length)
                    {
                        try
                        {
                            m_TargetAlgorithmIndex = Integer.parseInt(args[i + 1]);
                            i++;
                        }
                        catch (NumberFormatException e)
                        {
                            System.out.println("Error parsing algorithm number:" + e);
                            argumentParseError = true;
                        }
                    }
                    else
                    {
                        argumentParseError = true;
                    }
                    break;
                }
                case "-logging": { // Max level of logging based on provided int argument. "-logging 1."
                    if((i+1)<args.length)
                    {
                        try
                        {
                            m_TargetLoggingLevel = Integer.parseInt(args[i + 1]);
                            if(m_TargetLoggingLevel<0 || m_TargetLoggingLevel>3)
                            {
                                argumentParseError = true;
                            }
                            i++;
                        }
                        catch (NumberFormatException e)
                        {
                            System.out.println("Error parsing logging level:" + e);
                            argumentParseError = true;
                        }
                    }
                    else
                    {
                        argumentParseError = true;
                    }
                    break;
                }
                case "-startX": { //First zero-indexed X position for an algorithm equal to provided argument. "-startX 1"
                    if((i+1)<args.length)
                    {
                        try
                        {
                            m_StartXOffset = Integer.parseInt(args[i + 1]);
                            i++;
                        }
                        catch (NumberFormatException e)
                        {
                            System.out.println("Error parsing logging level:" + e);
                            argumentParseError = true;
                        }
                    }
                    else
                    {
                        argumentParseError = true;
                    }
                    break;
                }
                default:
                {
                    //Unrecognized command line argument provided
                    argumentParseError = true;
                }
            }

            //If error in parsing, stop processing arguments
            if(argumentParseError)
            {
                break;
            }
        }

        switch (m_TargetAlgorithmIndex)
        {
            case 0:
            {
                m_CurrentAlgorithm = new AngleCheckScannerFullRestart(m_TargetQueens, m_BoardWidth, m_BoardHeight, m_StartXOffset);
                break;
            }
            case 1:
            {
                m_CurrentAlgorithm = new AngleCheckScannerUndo(m_TargetQueens, m_BoardWidth, m_BoardHeight, m_StartXOffset);
                break;
            }
            default:
            {
                argumentParseError = true;
            }
        }

        //If there was an error print usage and exit
        if(argumentParseError)
        {
            System.out.println(m_Usage);
            System.exit(1);
        }


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
        System.out.println("\""+m_CurrentAlgorithm.getM_AlgorithmDescription()+"\"\n");

        //Execute algorithm, keeping track of total time to run
        System.out.println("Placing Queens...");
        timerStart = System.currentTimeMillis();
        boolean success = m_CurrentAlgorithm.run();
        timerDuration = System.currentTimeMillis() - timerStart;

        //Process and output results
        if(success) {
            System.out.println("\n\nSolution found in " + timerDuration + "ms total!");
        }
        else
        {
            System.out.println("\n\nNo solution found, attempt completed in " + timerDuration + "ms total.");
            System.out.println((m_KeepBestRun ? "Best" : "Final")+" Run (Iteration "+m_CurrentAlgorithm.getBestIteration()+"): "+m_CurrentAlgorithm.getBestPlacedQueens().size()+" of "+m_TargetQueens+" placed. (Remaining: "+m_CurrentAlgorithm.getBestRemainingQueens()+").");
        }

        //Print (x,y) coordinates of all placed queens in returned result
        System.out.println("Queens placed at:");
        for(int l=0; l<m_CurrentAlgorithm.getBestPlacedQueens().size(); l++)
        {
            Queen q = m_CurrentAlgorithm.getBestPlacedQueens().get(l);
            System.out.println("\tQueen "+(l+1)+": ("+q.x+","+q.y+")");
        }

        //Add Queen positions to board for printing
        System.out.println("");
        m_Board = new char[m_BoardHeight * m_BoardWidth];
        for (int i = 0; i < m_Board.length; i++) {
            m_Board[i] = ' ';
        }

        for (int qIndex = 0; qIndex < m_CurrentAlgorithm.getBestPlacedQueens().size(); qIndex++) {
            Queen q = m_CurrentAlgorithm.getBestPlacedQueens().get(qIndex);
            m_Board[q.y * m_BoardWidth + q.x] = m_OutputQueenCharacter;
        }

        //Print final board
        String topBottomBorder = "  +";
        String squareLettering = "   ";
        for (int bIndex = 0; bIndex < m_BoardWidth; bIndex++) {
            topBottomBorder += "-";
            squareLettering += (char)((bIndex%26)+97);
        }
        topBottomBorder+="+";
        System.out.println(squareLettering);
        System.out.println(topBottomBorder);
        for (int yIndex = 0; yIndex < m_BoardHeight; yIndex++) {
            int yValue = yIndex * m_BoardWidth;
            System.out.format("%2d|", (m_BoardHeight-yIndex));
            for (int xIndex = 0; xIndex < m_BoardWidth; xIndex++) {
                System.out.print(m_Board[yValue + xIndex]);
            }
            System.out.print("|"+(m_BoardHeight-yIndex)+"\n");
        }
        System.out.println(topBottomBorder);
        System.out.println(squareLettering);

        System.out.println("\n");

        System.out.println("===RUN SUMMARY===");
        System.out.println("TARGET: "+m_TargetQueens+" Queens. BOARD: "+m_BoardWidth+"x"+m_BoardHeight+".");
        System.out.println("ALGORITHM: \""+m_CurrentAlgorithm.getAlgorithmName()+".\" VERSION: "+m_CurrentAlgorithm.getVersion()+".");
        System.out.println("RESULT: "+(success ? "Success." : "Failure - Best Result was "+(m_TargetQueens - m_CurrentAlgorithm.getBestRemainingQueens())+" of "+m_TargetQueens+" placed."));
        System.out.println("RUNTIME: "+timerDuration+"ms.");
    }
}
