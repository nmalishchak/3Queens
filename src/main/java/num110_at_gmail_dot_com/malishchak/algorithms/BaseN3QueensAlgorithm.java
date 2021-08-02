package num110_at_gmail_dot_com.malishchak.algorithms;

import num110_at_gmail_dot_com.malishchak.Queen;
import num110_at_gmail_dot_com.malishchak.Results;

import java.util.ArrayList;

/**
 * Abstract base class for any algorithm to be run by the N3Queens utility.
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
public abstract class BaseN3QueensAlgorithm {

    public final static int LOGGING_LEVEL_NONE = 0;
    public final static int LOGGING_LEVEL_SUMMARY = 1;
    public final static int LOGGING_LEVEL_VERBOSE = 2;
    public final static int LOGGING_LEVEL_DEBUG = 3;

    protected int m_Version;
    protected String m_AlgorithmName = "BaseN3QueensAlgorithm";
    protected String m_AlgorithmDescription = "Abstract base class for N3Queens Algorithms";
    protected int m_LoggingLevel = LOGGING_LEVEL_SUMMARY;

    protected int m_TargetQueens = -1;
    protected int m_BoardHeight = -1;
    protected int m_BoardWidth = -1;
    protected int m_StartXOffset = 0;
    protected boolean m_KeepBestRun = true;


    protected int m_BestRemainingQueens = 0;
    protected int m_BestIteration = 0;
    protected ArrayList<Queen> m_BestPlacedQueens = new ArrayList<Queen>();


    /**
     * @return The name of the N3QueensAlgorithm
     */
    public String getAlgorithmName()
    {
        return m_AlgorithmName;
    }

    /**
     * @return A plain-text description of how the algorithm works
     */
    public String getAlgorithmDescription()
    {
        return m_AlgorithmDescription;
    }

    /**
     * @return Version of N3Queen Algorithm. Higher int values are assumed to be newer versions
     */
    public int getVersion()
    {
        return m_Version;
    }

    /**
     * @param m_TargetQueens The number of queens the algorithm should attempt to place
     */
    public void setTargetQueens(int m_TargetQueens)
    {
        this.m_TargetQueens = m_TargetQueens;
    }

    /**
     * @param m_BoardHeight The height of the board on which queens should be placed
     */
    public void setBoardHeight(int m_BoardHeight)
    {
        this.m_BoardHeight = m_BoardHeight;
    }

    /**
     * @param m_BoardWidth The width of the board on which queens should be placed
     */
    public void setBoardWidth(int m_BoardWidth)
    {
        this.m_BoardWidth = m_BoardWidth;
    }

    /**
     * @param m_KeepBestRun True if the algorithm should report the best overall iteration if it fails
     *                      to find a solution, false if only the last run should be reported
     */
    public void setKeepBestRun(boolean m_KeepBestRun)
    {
        this.m_KeepBestRun = m_KeepBestRun;
    }

    /**
     * @param m_LoggingLevel Set the logging level for any log statements set inside the algorithm.
     *                       Higher logging levels are useful for debugging but may reduce overall
     *                       performance
     */
    public void setLoggingLevel(int m_LoggingLevel)
    {
        this.m_LoggingLevel = m_LoggingLevel;
    }

    /**
     * Instatiate an instance of a BaseN3QueensAlgorithm
     * @param targetQueens Number of queens the algorithm should attempt to place on the board
     * @param boardWidth Width of the chess board
     * @param boardHeight Height of the chess board
     * @param startXoffset X Position for first chess piece.
     */
    public BaseN3QueensAlgorithm(int targetQueens, int boardWidth, int boardHeight, int startXoffset)
    {
        this.m_TargetQueens=targetQueens;
        this.m_BoardWidth=boardWidth;
        this.m_BoardHeight=boardHeight;
        this.m_StartXOffset=startXoffset;
    }

    /**
     * Executes the algorithm to find a solution to the N3Queens problem
     * @return The results from the run. Overall time is tracked by the main program
     */
    public abstract Results run();
    
    
}
