package num110_at_gmail_dot_com.malishchak.algorithms;

import num110_at_gmail_dot_com.malishchak.Queen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class AngleCheckScannerUndo extends BaseN3QueensAlgorithm
{
    private List<Queen> m_PlacedQueens = new ArrayList<Queen>();
    private int m_RemainingQueens = 0;
    private int m_LastRootQueenRemovedIndex = -1;
    private boolean m_StartPositionsExhausted = false;
    private long m_IterationTimerStart = 0;
    private long m_IteratonTimerDuration = 0;
    private int m_XStartPosition = 0;
    private int m_YStartPosition = 0;
    private int m_IterationCount = 0;

    private HashMap<Integer, Integer> m_BlockedXPositions = new HashMap<Integer, Integer>();

    public AngleCheckScannerUndo(int targetQueens, int boardWidth, int boardHeight, int startXOffset)
    {
        super(targetQueens, boardWidth, boardHeight, startXOffset);
        m_Version = 3;
        m_AlgorithmName = "AngleCheckScannerUndo";
        m_AlgorithmDescription = "Iterates row-by-row through the chess board placing queens.\n" +
                "First queen is placed at (0,0), each following potential queen checks the angle\n" +
                "to each other queen on the chess board relative to horizontal. If a queen is\n" +
                "found at an angle that is a multiple of 45 degrees, the position is discarded\n" +
                "as the potential queen is threatened by an existing queen. If not threatened,\n" +
                "the algorithm then checks to see if two existing queens are either at the same\n" +
                "angle to the potential new queen (new queen forms the tail of a line with other\n" +
                "two queens), or if two other queens are at precisely opposite angles of the new\n" +
                "queen (new queen is the middle point of a line between two queens). When a new\n" +
                "queen is placed, the algorithm automatically moves to the next row on the board.\n" +
                "If a solution is not found, the algorithm discards last-placed queens until one\n" +
                "is reached where a solution can still be found (queens remaining <= rows\n" +
                "remaining), and then resumes processing from one position to the right of that\n" +
                "queen. This repeats each time a solution is not found. If the algorithm returns\n" +
                "to the first queen, it will move the first queen one position to the right and\n" +
                "start a new iteration. When all start positions for the first Queen on the first\n" +
                "row are exhausted, the algorithm will give up.";
    }

    @Override
    public boolean run() {
        m_RemainingQueens = m_TargetQueens;
        m_PlacedQueens.clear();
        m_IterationCount = 1;
        m_XStartPosition = m_StartXOffset;
        while(m_RemainingQueens!=0 &&!m_StartPositionsExhausted) {
            m_IterationTimerStart = System.currentTimeMillis();

            for (int y = m_YStartPosition; y < m_BoardHeight; y++)
            {
                //Check if completes
                if (m_RemainingQueens == 0 || (m_PlacedQueens.size()>0 && !isSolutionPossibleIfQueenLast(m_PlacedQueens.size()-1))) {
                    if(m_RemainingQueens != 0)
                    {
                        if(m_LoggingLevel>=LOGGING_LEVEL_VERBOSE) System.out.println("X Solution not possible with "+m_RemainingQueens+" queens and "+(m_BoardHeight-y)+" rows remaining.");
                    }
                    checkBestRun();
                    break;
                }
                for (int x = (y==m_YStartPosition ? m_XStartPosition : 0); x < m_BoardWidth; x++)
                {
                    if(m_BlockedXPositions.containsKey(x))
                    {
                        continue;
                    }
                    if (m_PlacedQueens.isEmpty()) {
                        m_PlacedQueens.add(new Queen(x, y));
                        if(m_LoggingLevel>=LOGGING_LEVEL_VERBOSE) System.out.println("(SUCCESS) First Queen " + m_PlacedQueens.size() + " placed at (" + x + "," + y + ").");
                        m_BlockedXPositions.put(x,x);
                        m_RemainingQueens--;
                        break;
                    } else {
                        List<Double> angles = new ArrayList<Double>();

                        boolean successfulPlace = true;
                        for (int j = 0; j < m_PlacedQueens.size(); j++) {
                            Queen existingQueen = m_PlacedQueens.get(j);
                            double angle = existingQueen.findAngle(x, y);
                            if (m_LoggingLevel>=LOGGING_LEVEL_DEBUG)
                                System.out.println("New Queen "+(m_PlacedQueens.size()+1)+" at (" + x + "," + y + ") has angle " + angle + " degrees to queen " + (j + 1) + " at (" + existingQueen.x + "," + existingQueen.y + ").");
                            if (Math.round(angle) % 45 == 0) {
                                //Threatened
                                if (m_LoggingLevel>=LOGGING_LEVEL_VERBOSE)
                                    System.out.println("(FAILURE) New Queen "+(m_PlacedQueens.size()+1)+" at (" + x + "," + y + ") threatened by queen " + (j + 1) + " at (" + existingQueen.x + "," + existingQueen.y + ").");
                                successfulPlace = false;
                                break;
                            } else {
                                //Search for queens found on same line
                                double oppositeAngle = (angle >= 0d ? angle - 180d : angle + 180d);
                                if (oppositeAngle < 0d) {
                                    oppositeAngle += 360d;
                                }
                                if (angle < 0d) {
                                    angle += 360d;
                                }
                                if (m_LoggingLevel>=LOGGING_LEVEL_DEBUG)
                                    System.out.println("Checking angle " + angle + " and opposite " + oppositeAngle + ".");
                                if (angles.contains(angle) || angles.contains(oppositeAngle)) {
                                    //3 Queens in one line
                                    if (m_LoggingLevel>=LOGGING_LEVEL_VERBOSE) {
                                        Queen conflict = null;
                                        double conflictAngle;
                                        int conflictIndex;
                                        if (angles.contains(angle)) {
                                            if (m_LoggingLevel>=LOGGING_LEVEL_DEBUG) System.out.println("Conflict at angle " + angle + ".");
                                            conflictIndex = angles.indexOf(angle);
                                            conflict = m_PlacedQueens.get(conflictIndex);
                                            conflictAngle = angle;
                                        } else {
                                            if (m_LoggingLevel>=LOGGING_LEVEL_DEBUG) System.out.println("Conflict at angle " + oppositeAngle + ".");
                                            conflictIndex = angles.indexOf(oppositeAngle);
                                            conflict = m_PlacedQueens.get(conflictIndex);
                                            conflictAngle = oppositeAngle;
                                        }
                                        if(m_LoggingLevel>=LOGGING_LEVEL_VERBOSE) System.out.println("(FAILURE) New Queen "+(m_PlacedQueens.size()+1)+" at (" + x + "," + y + ") on line with queen " + (j + 1) + " (" + existingQueen.x + "," + existingQueen.y + ") at angle " + angle + " degrees and queen " + (conflictIndex + 1) + " (" + conflict.x + "," + conflict.y + ") at angle " + conflictAngle + " degrees.");
                                    }
                                    successfulPlace = false;
                                    break;
                                } else {
                                    angles.add(angle);
                                }
                            }
                        }
                        if (successfulPlace) {
                            //No Conflicts!
                            m_PlacedQueens.add(new Queen(x, y));
                            if(m_LoggingLevel>=LOGGING_LEVEL_VERBOSE) System.out.println("(SUCCESS) New Queen " + m_PlacedQueens.size() + " placed at (" + x + "," + y + ").");
                            m_BlockedXPositions.put(x,x);
                            m_RemainingQueens--;
                            break;
                        }
                    }
                }
            }

            if (m_RemainingQueens!=0)
            {
                //Revert to last queen. If all variations since last root queen failed, revert root queen up one node
                if(m_LoggingLevel>=LOGGING_LEVEL_DEBUG) System.out.println("Run failed! "+m_PlacedQueens.size()+" of "+m_TargetQueens+" placed.");
                int nextRootIndex = m_PlacedQueens.size()-1;
                while(!isSolutionPossibleIfQueenLast(nextRootIndex) && nextRootIndex>=0)
                {
                    if(m_LoggingLevel>=LOGGING_LEVEL_DEBUG)
                    {
                        Queen q = m_PlacedQueens.get(nextRootIndex);
                        System.out.println("Solution not possible from Queen "+(nextRootIndex+1)+" ("+q.x+","+q.y+").");
                    }
                    nextRootIndex--;
                }
                if(m_LoggingLevel>=LOGGING_LEVEL_DEBUG) System.out.println("NextRootIndex = "+(nextRootIndex+1)+".");
                if(nextRootIndex<0)
                {
                    //No solution possible
                    break;
                }
                else if(nextRootIndex==0)
                {
                    //Need to move first queen, new run
                    resetFirstQueen();

                }
                else {
                    checkBestRun();
                    //Move back to next possible index
                    m_LastRootQueenRemovedIndex = nextRootIndex;
                    Queen removed = m_PlacedQueens.get(m_LastRootQueenRemovedIndex);

                    //Remove all placed queens from the next possible index onwards
                    if (m_LoggingLevel >= LOGGING_LEVEL_DEBUG)
                        System.out.println("Removing " + (m_PlacedQueens.size() - m_LastRootQueenRemovedIndex) + " queens for revert from list of " + m_PlacedQueens.size() + " queens.");
                    List<Queen> deletedQueens = m_PlacedQueens.subList(m_LastRootQueenRemovedIndex, m_PlacedQueens.size());
                    for(int dq = 0; dq<deletedQueens.size(); dq++)
                    {
                        m_BlockedXPositions.remove(deletedQueens.get(dq).x);
                    }
                    deletedQueens.clear();
                    if (m_LoggingLevel >= LOGGING_LEVEL_DEBUG)
                        System.out.println("Post revert list of placed queens has " + m_PlacedQueens.size() + " queens.");

                    int oldRemaining = m_RemainingQueens;
                    m_RemainingQueens = (m_TargetQueens - m_PlacedQueens.size());
                    m_XStartPosition = removed.x + 1; //for loop will auto-roll to next row if end of row
                    m_YStartPosition = removed.y;

                    if (m_LoggingLevel >= LOGGING_LEVEL_VERBOSE)
                    {
                        System.out.println("X No solution found. Reverted back to placing Queen " + (m_LastRootQueenRemovedIndex + 1) + ", previously at (" + removed.x + "," + removed.y + "). Resuming at (" + m_XStartPosition + "," + m_YStartPosition + "), " + m_RemainingQueens + " remain.");
                    }
                }
            }
            else
            {
                if(m_LoggingLevel>=LOGGING_LEVEL_SUMMARY) System.out.println("!!! Success! Iteration "+(m_IterationCount)+" completed in "+m_IteratonTimerDuration+"ms.");
            }
        }


        if(m_RemainingQueens==0)
        {
            if(m_LoggingLevel>=LOGGING_LEVEL_SUMMARY) System.out.println("!!! Solution found after "+m_IterationCount+" iterations.");
            //Solution Found, this is the new best run
            m_BestPlacedQueens = new ArrayList<Queen>(m_PlacedQueens);
            m_BestRemainingQueens = m_RemainingQueens;
            m_BestIteration = m_IterationCount;

            return true;
        }
        else
        {
            //No Solution found
            if(m_LoggingLevel>=LOGGING_LEVEL_SUMMARY) System.out.println("XXX Algorithm failed after "+m_IterationCount+" iterations.");
            if(!m_KeepBestRun)
            {
                //We weren't told to track best run, so report final run
                m_BestPlacedQueens = new ArrayList<Queen>(m_PlacedQueens);
                m_BestRemainingQueens = m_RemainingQueens;
                m_BestIteration = m_IterationCount;
            }
            return false;
        }
    }

    private boolean isSolutionPossibleIfQueenLast(Queen last)
    {
        return (m_BoardHeight-last.y>=m_RemainingQueens && last.x < (m_BoardWidth-1));
    }

    private boolean isSolutionPossibleIfQueenLast(int index)
    {
        if(index < 0 || index > m_PlacedQueens.size())
        {
            return false;
        }
        Queen last = m_PlacedQueens.get(index);
        return (m_BoardHeight-last.y>m_RemainingQueens);// && last.x < (m_BoardWidth-1));
    }

    private void resetFirstQueen()
    {
        m_YStartPosition = 0;
        Queen q = m_PlacedQueens.get(0);
        m_XStartPosition = q.x+1;
        m_IteratonTimerDuration = System.currentTimeMillis() - m_IterationTimerStart;
        if(m_LoggingLevel>=LOGGING_LEVEL_SUMMARY) System.out.println("XXX Iteration "+(m_IterationCount)+" completed in "+m_IteratonTimerDuration+"ms. No solution found at Queen 1 start position ("+q.x+","+q.y+"). XXX");
        checkBestRun();

        if(m_XStartPosition>=m_BoardWidth)
        {
            m_StartPositionsExhausted = true;
        }
        else
        {
            m_PlacedQueens.clear();
            m_RemainingQueens = m_TargetQueens;
            m_IterationCount++;
            m_BlockedXPositions.clear();
            if(m_LoggingLevel>=LOGGING_LEVEL_VERBOSE) System.out.println("XXX Retrying with Queen 1 X start position "+m_XStartPosition+". XXX\n\n");
        }
    }

    private void checkBestRun()
    {
        if(m_KeepBestRun)
        {
            if(m_PlacedQueens.size()>m_BestPlacedQueens.size())
            {
                //Remember new Best
                m_BestPlacedQueens = new ArrayList<Queen>(m_PlacedQueens);
                m_BestRemainingQueens = m_RemainingQueens;
                m_BestIteration = (m_XStartPosition+1);
            }
        }
    }
}
