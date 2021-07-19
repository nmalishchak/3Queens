package num110_at_gmail_dot_com.malishchak;

import num110_at_gmail_dot_com.malishchak.algorithms.AngleCheckScannerFullRestart;
import num110_at_gmail_dot_com.malishchak.algorithms.AngleCheckScannerUndo;
import num110_at_gmail_dot_com.malishchak.algorithms.BaseN3QueensAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class N3Queens {

    public static final boolean DEBUG = true;
    public static final boolean DEBUG_ANGLES = false;

    public static String m_Usage = "Usage: ./N3Queens -n <NumberOfQueens> [-size <board size>]\n" +
                                   "\tGenerates a solution for the N-Queens problem where n\n" +
                                   "\tnumber of queens are placed on a NxN size chess board\n" +
                                   "\tsuch that no two queens can attach one another.\n" +
                                   "\tAdditionally, no three queens may be places such that\n" +
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

    public static int m_TargetQueens = -1;

    public static boolean m_KeepBestRun = true;

    public static int m_BoardHeight = -1;
    public static int m_BoardWidth = -1;

    public static char[] m_Board;

    public static BaseN3QueensAlgorithm m_CurrentAlgorithm = null;
    public static int m_TargetAlgorithmIndex = 1;
    public static int m_TargetLoggingLevel = BaseN3QueensAlgorithm.LOGGING_LEVEL_SUMMARY;

    public static void parseArguments(String[] args)
    {
        boolean argumentParseError = true;
        boolean boardXSpecified = false;
        boolean boardYSpecified = false;
        for(int i =0; i< args.length; i++)
        {
            argumentParseError = false;
            switch (args[i]) {
                case "-n": {
                    if((i+1)<args.length)
                    {
                        try {
                            m_TargetQueens = Integer.parseInt(args[i + 1]);
                            m_BoardHeight = m_TargetQueens;
                            m_BoardWidth = m_TargetQueens;
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
                case "-size": {
                    if((i+1)<args.length)
                    {
                        try {
                            int value = Integer.parseInt(args[i + 1]);
                            if(!boardYSpecified) m_BoardHeight = value;
                            if(!boardXSpecified) m_BoardWidth = value;
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
                case "-BoardX": {
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
                case "-BoardY": {
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
                case "-algorithm": {
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
                case "-logging": {
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
                default:
                {
                    argumentParseError = true;
                }
            }
            if(argumentParseError)
            {
                break;
            }
        }

        switch (m_TargetAlgorithmIndex)
        {
            case 0:
            {
                m_CurrentAlgorithm = new AngleCheckScannerFullRestart(m_TargetQueens, m_BoardWidth, m_BoardHeight);
                break;
            }
            case 1:
            {
                m_CurrentAlgorithm = new AngleCheckScannerUndo(m_TargetQueens, m_BoardWidth, m_BoardHeight);
                break;
            }
            default:
            {
                argumentParseError = true;
            }
        }

        if(argumentParseError)
        {
            System.out.println(m_Usage);
            System.exit(1);
        }


    }

    public static void main(String[] args) {
        parseArguments(args);
        if(DEBUG) System.out.println("Parsing complete. Queens: "+m_TargetQueens+", Board: "+m_BoardHeight+"x"+m_BoardWidth+".");

        long timerStart = 0;
        long timerDuration = 0;

        long secondTimerStart = 0;
        long secondTimerDuration = 0;

        if(m_CurrentAlgorithm == null)
        {
            m_CurrentAlgorithm = new AngleCheckScannerUndo(m_TargetQueens, m_BoardWidth, m_BoardHeight);
        }
        m_CurrentAlgorithm.setKeepBestRun(true);
        m_CurrentAlgorithm.setLoggingLevel(m_TargetLoggingLevel);

        System.out.println("\nRunning algorithm \""+m_CurrentAlgorithm.getAlgorithmName()+".\"");
        System.out.println("\""+m_CurrentAlgorithm.getM_AlgorithmDescription()+"\"\n");

        System.out.println("Placing Queens...");
        timerStart = System.currentTimeMillis();
        boolean success = m_CurrentAlgorithm.run();
        timerDuration = System.currentTimeMillis() - timerStart;
        if(success) {
            System.out.println("\n\nSolution found in " + timerDuration + "ms total!");
        }
        else
        {
            System.out.println("\n\nNo solution found, attempt completed in " + timerDuration + "ms total.");
            System.out.println((m_KeepBestRun ? "Best" : "Final")+" Run (Iteration "+m_CurrentAlgorithm.getBestIteration()+"): "+m_CurrentAlgorithm.getBestPlacedQueens().size()+" of "+m_TargetQueens+" placed. (Remaining: "+m_CurrentAlgorithm.getBestRemainingQueens()+").");
        }

        System.out.println("Queens placed at:");
        for(int l=0; l<m_CurrentAlgorithm.getBestPlacedQueens().size(); l++)
        {
            Queen q = m_CurrentAlgorithm.getBestPlacedQueens().get(l);
            System.out.println("\tQueen "+(l+1)+": ("+q.x+","+q.y+")");
        }

        //Output Board

        System.out.println("");
        m_Board = new char[m_BoardHeight * m_BoardWidth];
        for (int i = 0; i < m_Board.length; i++) {
            m_Board[i] = ' ';
        }

        for (int qIndex = 0; qIndex < m_CurrentAlgorithm.getBestPlacedQueens().size(); qIndex++) {
            Queen q = m_CurrentAlgorithm.getBestPlacedQueens().get(qIndex);
            m_Board[q.y * m_BoardWidth + q.x] = 'W';
        }

        String topBottomBorder = "  +";
        String squareLettering = "   ";
        for (int bIndex = 0; bIndex < m_BoardWidth; bIndex++) {
            topBottomBorder += "-";
            squareLettering+=(char)((bIndex%26)+97);
        }
        topBottomBorder+="+";
        System.out.println(squareLettering);
        System.out.println(topBottomBorder);
        for (int yIndex = 0; yIndex < m_BoardHeight; yIndex++) {
            int yValue = yIndex * m_BoardWidth;
            System.out.format("%2d|", (m_BoardHeight-yIndex));
            //System.out.print("|");
            for (int xIndex = 0; xIndex < m_BoardWidth; xIndex++) {
                System.out.print(m_Board[yValue + xIndex]);
            }
            System.out.print("|\n");
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
