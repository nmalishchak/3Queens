# 3Queens
Proof of Concept tool to find solutions to the N-Queens problem with the additional constraint that no 3 queens can form a straight line at any angle, measured through the center of the squares.

## Overview
Generates a solution for the N-Queens problem where N-number of queens are placed on a NxN size chess board such that no two queens can attack one another. Additionally, no three queens may be placed such that they form a straight line at any angle, measured through the center of their square.

## Arguments
-n <NumberOfQueens>: Specifies the number of queens to attempt to place. Size of the board will be <NumberOfQueens>x<NumberOfQueens> unless otherwise specified using the -size or                      -BoardX/-BoardY switches.

-size <board size>: Sets the size of the board to <board size>x<board size>. Overwritten by BoardX or BoardY switches

-BoardX <board width>: Sets the width of the board to <board width>. Overwrites values from size switch

-BoardY <board height>: Sets the height of the board to <board height>. Overwrites values from size switch

-algorithm <#>: Sets which algorithm to use. Defaults to 1. 0=AngleCheckScannerFullRestart, 1=AngleCheckScannerUndo

-logging <#>. Sets logging level to 0-3. Defaults to 1. 0=NONE, 1=SUMMARY, 2=VERBOSE, 3=DEBUG

## Benchmarks

Single-Threaded Benchmarks on Windows 10 w/ i7-4890HQ @ 2.80 Ghz:  
  
===RUN SUMMARY===
TARGET: 18 Queens. BOARD: 18x18.
ALGORITHM: "AngleCheckScannerUndo." VERSION: 2.
RESULT: Success.
RUNTIME: 4544ms.

===RUN SUMMARY===
TARGET: 19 Queens. BOARD: 19x19.
ALGORITHM: "AngleCheckScannerUndo." VERSION: 2.
RESULT: Success.
RUNTIME: 13407ms.

===RUN SUMMARY===
TARGET: 20 Queens. BOARD: 20x20.
ALGORITHM: "AngleCheckScannerUndo." VERSION: 2.
RESULT: Success.
RUNTIME: 71182ms.
