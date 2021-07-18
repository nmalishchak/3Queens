package num110_at_gmail_dot_com.malishchak.algorithms;

import num110_at_gmail_dot_com.malishchak.Queen;

import java.util.ArrayList;

public abstract class BaseN3QueensAlgorithm {

    public static int LOGGING_LEVEL_NONE = 0;
    public static int LOGGING_LEVEL_SUMMARY = 1;
    public static int LOGGING_LEVEL_VERBOSE = 2;
    public static int LOGGING_LEVEL_DEBUG = 3;

    protected int m_Version;
    protected String m_AlgorithmName = "BaseN3QueensAlgorithm";
    protected String m_AlgorithmDescription = "Abstract base class for N3Queens Algorithms";
    protected int m_LoggingLevel = LOGGING_LEVEL_SUMMARY;

    protected int m_TargetQueens = -1;
    protected int m_BoardHeight = -1;
    protected int m_BoardWidth = -1;
    protected boolean m_KeepBestRun = true;


    protected int m_BestRemainingQueens = 0;
    protected int m_BestIteration = 0;
    protected ArrayList<Queen> m_BestPlacedQueens = new ArrayList<Queen>();



    /**
     * @return List of placed Queens from best iteration, assuming 0,0 (origin) point is
     * top-left of chess board
     */
    public ArrayList<Queen> getBestPlacedQueens()
    {
        return m_BestPlacedQueens;
    }

    public int getBestRemainingQueens()
    {
        return m_BestRemainingQueens;
    }

    public int getBestIteration()
    {
        return m_BestIteration;
    }

    public String getAlgorithmName()
    {
        return m_AlgorithmName;
    }

    public String getM_AlgorithmDescription()
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

    public void setTargetQueens(int m_TargetQueens)
    {
        this.m_TargetQueens = m_TargetQueens;
    }

    public void setBoardHeight(int m_BoardHeight)
    {
        this.m_BoardHeight = m_BoardHeight;
    }

    public void setBoardWidth(int m_BoardWidth)
    {
        this.m_BoardWidth = m_BoardWidth;
    }

    public void setKeepBestRun(boolean m_KeepBestRun)
    {
        this.m_KeepBestRun = m_KeepBestRun;
    }

    public void setVersion(int m_Version)
    {
        this.m_Version = m_Version;
    }

    public void setLoggingLevel(int m_LoggingLevel)
    {
        this.m_LoggingLevel = m_LoggingLevel;
    }

    public BaseN3QueensAlgorithm(int targetQueens, int boardWidth, int boardHeight)
    {
        this.m_TargetQueens=targetQueens;
        this.m_BoardWidth=boardWidth;
        this.m_BoardHeight=boardHeight;
    }

    public abstract boolean run();
    
    
}
