package num110_at_gmail_dot_com.malishchak;

import num110_at_gmail_dot_com.malishchak.algorithms.AngleCheckScannerFullRestart;
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
                                   "\t\t\t<board size>x<board size>\n" +
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

    public static void parseArguments(String[] args)
    {
        boolean argumentParseError = true;
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
                            m_BoardHeight = Integer.parseInt(args[i + 1]);
                            m_BoardWidth = m_BoardHeight;
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
                default: {
                    argumentParseError = true;
                }
            }
            if(argumentParseError)
            {
                break;
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

        BaseN3QueensAlgorithm algorithm = new AngleCheckScannerFullRestart(m_TargetQueens, m_BoardWidth, m_BoardHeight);
        algorithm.setKeepBestRun(true);
        algorithm.setLoggingLevel(BaseN3QueensAlgorithm.LOGGING_LEVEL_SUMMARY);

        System.out.println("Placing Queens...");
        timerStart = System.currentTimeMillis();
        boolean success = algorithm.run();
        timerDuration = System.currentTimeMillis() - timerStart;
        if(success) {
            System.out.println("\n\nSolution found in " + timerDuration + "ms total!");
        }
        else
        {
            System.out.println("\n\nNo solution found, attempt completed in " + timerDuration + "ms total.");
            System.out.println((m_KeepBestRun ? "Best" : "Final")+" Run (Iteration "+algorithm.getBestIteration()+"): "+algorithm.getBestPlacedQueens().size()+" of "+m_TargetQueens+" placed. (Remaining: "+algorithm.getBestRemainingQueens()+").");
        }

        System.out.println("Queens placed at:");
        for(int l=0; l<algorithm.getBestPlacedQueens().size(); l++)
        {
            Queen q = algorithm.getBestPlacedQueens().get(l);
            System.out.println("\tQueen "+(l+1)+": ("+q.x+","+q.y+")");
        }

        //Output Board

        System.out.println("");
        m_Board = new char[m_BoardHeight * m_BoardWidth];
        for (int i = 0; i < m_Board.length; i++) {
            m_Board[i] = ' ';
        }

        for (int qIndex = 0; qIndex < algorithm.getBestPlacedQueens().size(); qIndex++) {
            Queen q = algorithm.getBestPlacedQueens().get(qIndex);
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

    }
}
