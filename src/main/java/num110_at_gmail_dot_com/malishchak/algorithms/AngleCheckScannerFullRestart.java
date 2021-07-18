package num110_at_gmail_dot_com.malishchak.algorithms;

import num110_at_gmail_dot_com.malishchak.Queen;

import java.util.ArrayList;
import java.util.List;

public class AngleCheckScannerFullRestart extends BaseN3QueensAlgorithm
{
    public List<Queen> m_PlacedQueens = new ArrayList<Queen>();
    public int m_RemainingQueens = 0;

    public AngleCheckScannerFullRestart(int targetQueens, int boardWidth, int boardHeight)
    {
        super(targetQueens, boardWidth, boardHeight);
        m_Version = 1;
        m_AlgorithmName = "AngleCheckScannerFullRestart";
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
                "If a solution is not found, the algorithm discards the run and starts anew with\n" +
                "the first queen being placed one position to the right of the previous iteration.\n" +
                "When the entire first row is exhausted without a solution, the algorithm gives up.";
    }

    @Override
    public boolean run() {
        long secondTimerStart = 0;
        long secondTimerDuration = 0;
        boolean firsttest = false;
        boolean startPositionsExhausted = false;
        int xStartPosition = 0;
        int yStartPosition = 0;
        m_RemainingQueens = m_TargetQueens;
        while(m_RemainingQueens!=0 &&!startPositionsExhausted) {
            secondTimerStart = System.currentTimeMillis();
            m_RemainingQueens = m_TargetQueens;
            m_PlacedQueens.clear();
            for (int y = yStartPosition; y < m_BoardHeight; y++) {
                if (m_RemainingQueens == 0) {
                    break;
                }
                for (int x = (y==0 ? xStartPosition : 0); x < m_BoardWidth; x++) {


                    if (m_PlacedQueens.isEmpty() && !firsttest) {
                        m_PlacedQueens.add(new Queen(x, y));
                        if(m_LoggingLevel>=LOGGING_LEVEL_VERBOSE) System.out.println("(SUCCESS) First Queen " + m_PlacedQueens.size() + " placed at (" + x + "," + y + ").");
                        m_RemainingQueens--;
                        break;
                    } else {
                        List<Double> angles = new ArrayList<Double>();
                        if (firsttest) {
                            //TODO: Delete this before final submission
                            x = 2;
                            firsttest = false;
                            m_PlacedQueens.add(new Queen(0, 0));
                            //angles.add(m_PlacedQueens.get(0).findAngle(x,y));
                            m_PlacedQueens.add(new Queen(5, 0));
                            //angles.add(m_PlacedQueens.get(1).findAngle(x,y));
                        }
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
                            m_RemainingQueens--;
                            break;
                        }
                    }
                }
            }
            secondTimerDuration = System.currentTimeMillis() - secondTimerStart;
            if(m_RemainingQueens!=0)
            {
                if(m_LoggingLevel>=LOGGING_LEVEL_SUMMARY) System.out.println("XXX Iteration "+(xStartPosition+1)+" completed in "+secondTimerDuration+"ms. No solution found at X start position "+xStartPosition+". XXX");
                if(m_KeepBestRun)
                {
                    if(m_PlacedQueens.size()>m_BestPlacedQueens.size())
                    {
                        //Remember new Best
                        m_BestPlacedQueens = new ArrayList<Queen>(m_PlacedQueens);
                        m_BestRemainingQueens = m_RemainingQueens;
                        m_BestIteration = (xStartPosition+1);
                    }
                }
                xStartPosition++;
                if(xStartPosition>=m_BoardWidth)
                {
                    startPositionsExhausted = true;
                }
                else
                {
                    if(m_LoggingLevel>=LOGGING_LEVEL_VERBOSE) System.out.println("XXX Retrying with X start position "+xStartPosition+". XXX\n\n");
                }
            }
            else
            {
                if(m_LoggingLevel>=LOGGING_LEVEL_SUMMARY) System.out.println("!!! Success! Iteration "+(xStartPosition+1)+" completed in "+secondTimerDuration+"ms.");
            }
        }


        if(m_RemainingQueens==0)
        {
            //Solution Found, this is the new best run
            m_BestPlacedQueens = new ArrayList<Queen>(m_PlacedQueens);
            m_BestRemainingQueens = m_RemainingQueens;
            m_BestIteration = (xStartPosition+1);

            return true;
        }
        else
        {
            if(!m_KeepBestRun)
            {
                //We weren't told to track best run, so report final run
                m_BestPlacedQueens = new ArrayList<Queen>(m_PlacedQueens);
                m_BestRemainingQueens = m_RemainingQueens;
                m_BestIteration = (xStartPosition+1);
            }
            return false;
        }
    }
}
