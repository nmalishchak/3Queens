package num110_at_gmail_dot_com.malishchak.algorithms;

import num110_at_gmail_dot_com.malishchak.Queen;
import num110_at_gmail_dot_com.malishchak.Results;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic algorithm that tries to place queens on the board abiding by the N3Queens conditions. Uses each square on the
 * first row beginning with the startXOffset and makes a single attempt to find a solution. If no solution found, it
 * discards the entire run and restarts at the next square on the first row.
 *
 * @author nmalishchak
 * Copyright (C) 2021  Nick Malishchak
 *
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
public class AngleCheckScannerFullRestart extends BaseN3QueensAlgorithm
{
    private List<Queen> m_PlacedQueens = new ArrayList<Queen>();
    private int m_RemainingQueens = 0;

    public AngleCheckScannerFullRestart(int targetQueens, int boardWidth, int boardHeight, int startXOffset)
    {
        super(targetQueens, boardWidth, boardHeight, startXOffset);
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
    public Results run() {
        long secondTimerStart = 0;
        long secondTimerDuration = 0;
        boolean startPositionsExhausted = false;
        int xStartPosition = m_StartXOffset;
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


                    if (m_PlacedQueens.isEmpty()) {
                        m_PlacedQueens.add(new Queen(x, y));
                        if(m_LoggingLevel>=LOGGING_LEVEL_VERBOSE) System.out.println("(SUCCESS) First Queen " + m_PlacedQueens.size() + " placed at (" + x + "," + y + ").");
                        m_RemainingQueens--;
                        break;
                    } else {
                        List<Double> angles = new ArrayList<Double>();

                        boolean successfulPlace = true;
                        for (int j = 0; j < m_PlacedQueens.size(); j++) {
                            Queen existingQueen = m_PlacedQueens.get(j);
                            double angle = existingQueen.findAngle(x, y);
                            if (m_LoggingLevel>=LOGGING_LEVEL_DEBUG)
                                System.out.println("New Queen "+(m_PlacedQueens.size()+1)+" at (" + x + "," + y + ") has angle " + angle + " degrees to queen " + (j + 1) + " at (" + existingQueen.getX() + "," + existingQueen.getY() + ").");
                            if (Math.round(angle) % 45 == 0) {
                                //Threatened
                                if (m_LoggingLevel>=LOGGING_LEVEL_VERBOSE)
                                    System.out.println("(FAILURE) New Queen "+(m_PlacedQueens.size()+1)+" at (" + x + "," + y + ") threatened by queen " + (j + 1) + " at (" + existingQueen.getX() + "," + existingQueen.getY() + ").");
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
                                        if(m_LoggingLevel>=LOGGING_LEVEL_VERBOSE) System.out.println("(FAILURE) New Queen "+(m_PlacedQueens.size()+1)+" at (" + x + "," + y + ") on line with queen " + (j + 1) + " (" + existingQueen.getX() + "," + existingQueen.getY() + ") at angle " + angle + " degrees and queen " + (conflictIndex + 1) + " (" + conflict.getX() + "," + conflict.getY() + ") at angle " + conflictAngle + " degrees.");
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


        Results results = new Results();
        results.setAlgorithm(m_AlgorithmName);
        results.setAlgorithm_version(m_Version);
        results.setTarget_queens(m_TargetQueens);
        results.setBoard_height(m_BoardHeight);
        results.setBoard_width(m_BoardWidth);
        results.setDate(System.currentTimeMillis());

        if(m_RemainingQueens==0)
        {
            //Solution Found, this is the new best run
            results.setPlaced_queens(new ArrayList<Queen>(m_PlacedQueens));
            results.setRemaining_queens(m_RemainingQueens);
            results.setBest_iteration(xStartPosition+1);
            results.setTotal_iterations(xStartPosition+1);
            results.setSuccess(1);
            results.setFirst_queen_x((m_PlacedQueens.size()>0 ? m_PlacedQueens.get(0).getX() : m_StartXOffset ));
            results.setWas_best_run(1);

            return results;
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
            results.setPlaced_queens(new ArrayList<Queen>(m_BestPlacedQueens));
            results.setRemaining_queens(m_BestRemainingQueens);
            results.setBest_iteration(m_BestIteration);
            results.setTotal_iterations(xStartPosition+1);
            results.setSuccess(0);
            results.setFirst_queen_x((m_PlacedQueens.size()>0 ? m_PlacedQueens.get(0).getX() : m_StartXOffset ));
            results.setWas_best_run((m_KeepBestRun ? 1: 0));

            return results;
        }
    }
}
