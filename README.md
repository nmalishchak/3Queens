# N3Queens
Proof of Concept tool to find solutions to the N-Queens problem with the additional constraint that no 3 queens can form a straight line at any angle, measured through the center of the squares.

## Overview
Generates a solution for the N-Queens problem where N-number of queens are placed on a NxN size chess board such that no two queens can attack one another. Additionally, no three queens may be placed such that they form a straight line at any angle, measured through the center of their square.

Algorithms attempting to solve the issue must implement the BaseN3QueensAlgorithm class and provide a Result object upon completion.

The N3Queens tool will create a local SQLite database to store the results of runs. Upon completion of a run, a list of placed queens, an ASCII chess board, and a run summary will be displayed. Additionally, the best run for the problem attempted will also be displayed. If this is the first attempt for a given number of queens and chess board size, the current run will be displayed if it was successful.

## Building
N3Queens was developed using Gradle, Java version "16" 2021-03-16, with, Java(TM) SE Runtime Environment (build 16+36-2231), and Java HotSpot(TM) 64-Bit Server VM (build 16+36-2231, mixed mode, sharing). To build, execute either ./gradlew clean build (Linux) or ./gradlew.bat clean build from the command line in the N3Queen root folder, and the resulting distributions will be available in N3Queens/build/distributions. N3Queens also supports executing directly via Gradle using the command ./gradlew run --args="<N3Queens Args>." 

## Arguments
-n <NumberOfQueens>: Specifies the number of queens to attempt to place. Size of the board will be <NumberOfQueens>x<NumberOfQueens> unless otherwise specified using the -size or -BoardX/-BoardY switches.

-size <board size>: Sets the size of the board to <board size>x<board size>. Overwritten by BoardX or BoardY switches

-BoardX <board width>: Sets the width of the board to <board width>. Overwrites values from size switch

-BoardY <board height>: Sets the height of the board to <board height>. Overwrites values from size switch

-algorithm <#>: Sets which algorithm to use. Defaults to 1. 0=AngleCheckScannerFullRestart, 1=AngleCheckScannerUndo

-logging <#>. Sets logging level to 0-3. Defaults to 1. 0=NONE, 1=SUMMARY, 2=VERBOSE, 3=DEBUG
 
-startX <#>. Sets the starting x position for algorithms. Defaults to 0.

## Benchmarks

Single-Threaded Benchmarks on Windows 10 w/ i7-4890HQ @ 2.80 Ghz using a startX of 1:  
  
===RUN SUMMARY===
TARGET: 20 Queens. BOARD: 20x20.
ALGORITHM: "AngleCheckScannerUndo." VERSION: 3.
RESULT: Success.
RUNTIME: 700ms.

  ===RUN SUMMARY===
TARGET: 25 Queens. BOARD: 25x25.
ALGORITHM: "AngleCheckScannerUndo." VERSION: 3.
RESULT: Success.
RUNTIME: 19616ms.
  
===RUN SUMMARY===
TARGET: 30 Queens. BOARD: 30x30.
ALGORITHM: "AngleCheckScannerUndo." VERSION: 3.
RESULT: Success.
RUNTIME: 633296ms.


