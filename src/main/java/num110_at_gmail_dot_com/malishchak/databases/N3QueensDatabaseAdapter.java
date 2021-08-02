package num110_at_gmail_dot_com.malishchak.databases;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import num110_at_gmail_dot_com.malishchak.Results;
import org.sqlite.SQLiteDataSource;

/**
 * Database Adapter for a simple SQLite Database in which to store and retrieve the Results from algorithm runs.
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
public class N3QueensDatabaseAdapter {

    private static SQLiteDataSource m_DataSource;

    private static final String CREATE_TABLE_RESULTS =
            "CREATE TABLE IF NOT EXISTS " + N3QueensDataContract.ResultsTable.TABLE_NAME
                    + " ( " + N3QueensDataContract.ResultsTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY,"
                    + N3QueensDataContract.ResultsTable.COLUMN_NAME_DATE + " INTEGER NOT NULL,"
                    + N3QueensDataContract.ResultsTable.COLUMN_NAME_ALGORITHM_NAME + " TEXT NOT NULL,"
                    + N3QueensDataContract.ResultsTable.COLUMN_NAME_ALGORITHM_VERSION + " INTEGER NOT NULL,"
                    + N3QueensDataContract.ResultsTable.COLUMN_NAME_TARGET_QUEENS + " INTEGER NOT NULL,"
                    + N3QueensDataContract.ResultsTable.COLUMN_NAME_BOARD_WIDTH + " INTEGER NOT NULL,"
                    + N3QueensDataContract.ResultsTable.COLUMN_NAME_BOARD_HEIGHT + " INTEGER NOT NULL,"
                    + N3QueensDataContract.ResultsTable.COLUMN_NAME_SUCCESS + " INTEGER NOT NULL,"
                    + N3QueensDataContract.ResultsTable.COLUMN_NAME_VERIFIED+ " INTEGER NOT NULL,"
                    + N3QueensDataContract.ResultsTable.COLUMN_NAME_WAS_BEST_RUN+ " INTEGER NOT NULL,"
                    + N3QueensDataContract.ResultsTable.COLUMN_NAME_REMAINING_QUEENS + " INTEGER NOT NULL,"
                    + N3QueensDataContract.ResultsTable.COLUMN_NAME_FIRST_QUEEN_X + " INTEGER NOT NULL,"
                    + N3QueensDataContract.ResultsTable.COLUMN_NAME_BEST_ITERATION + " INTEGER NOT NULL,"
                    + N3QueensDataContract.ResultsTable.COLUMN_NAME_TOTAL_ITERATIONS + " INTEGER NOT NULL,"
                    + N3QueensDataContract.ResultsTable.COLUMN_NAME_SOLUTION_TIME_IN_MS  + " INTEGER NOT NULL);";

    //Initialize Database and DataSource
    static {
            m_DataSource = new SQLiteDataSource();
            m_DataSource.setUrl("jdbc:sqlite:"+N3QueensDataContract.DATABASE_NAME);
            Connection connection = getConnection();
            if(connection!=null)
            {
                try
                {
                    //Initialize Database
                    Statement statement = connection.createStatement();
                    statement.setQueryTimeout(30);  // set timeout to 30 sec.
                    statement.executeUpdate(CREATE_TABLE_RESULTS);
                }
                catch(SQLException e)
                {
                    System.err.println(e.getMessage());
                }
                finally
                {
                    try
                    {
                        if(connection != null)
                            connection.close();
                    }
                    catch(SQLException e)
                    {
                        System.err.println(e.getMessage());
                    }
                }
            }
    }

    /**
     * @return A connection for accessing the N3Queens Database directly. Classes should use N3QueensDatabaseAdapter
     * helper functions where possible.
     */
    private static Connection getConnection() {
        try
        {
            return m_DataSource.getConnection();
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * Adds a result set to the database
     * @param result Set of complete results obtained from an algorithm, and finished with data from the main program
     * @return True if the result set was added successfully
     */
    public static boolean addResults(Results result)
    {
        Connection connection = getConnection();

        if(connection == null)
        {
            System.err.println("Error opening connection to database.");
            return false;
        }

        boolean operationComplete = false;
        try
        {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            statement.executeUpdate("insert into "+N3QueensDataContract.ResultsTable.TABLE_NAME+" values(null, " +
                    result.getDate() + ", " +
                    "'" + result.getAlgorithm() + "', " +
                    result.getAlgorithm_version() + ", " +
                    result.getTarget_queens() + ", " +
                    result.getBoard_width() + ", " +
                    result.getBoard_height() + ", " +
                    result.getSuccess() + ", " +
                    result.getVerified() + ", " +
                    result.getWas_best_run() + ", " +
                    result.getRemaining_queens() + ", " +
                    result.getFirst_queen_x() + ", " +
                    result.getBest_iteration() + ", " +
                    result.getTotal_iterations() + ", " +
                    result.getSolution_time_in_ms() + ")");
            operationComplete = true;
        }
        catch(SQLException e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                System.err.println(e.getMessage());
            }
        }
        return operationComplete;
    }

    /**
     * Returns a list of recent results
     * @param last The number of recent results to return, starting with the newest inserted in the database
     * @return ArrayList of recent results, or null if an error occurs.
     */
    public static ArrayList<Results> queryRecentResults(int last)
    {
        return getResults("order by id desc limit "+last);
    }


    /**
     * Queries the Results Table using the given where clause (to include follow on SQL modifiers) and returns a list
     * of Results matching the given where clause.
     * @param whereSQLString SQL string to append to "select * from results"
     * @return ArrayList of Results matching the whereSQLString, or null if an error occurs
     */
    public static ArrayList<Results> getResults(String whereSQLString)
    {
        Connection connection = getConnection();

        if(connection == null)
        {
            System.err.println("Error opening connection to database.");
            return null;
        }

        ArrayList<Results> returnList = new ArrayList<Results>();
        boolean operationComplete = false;
        try
        {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = statement.executeQuery("select * from results "+whereSQLString);

            while(rs.next())
            {
                Results result = new Results();
                result.setDate(rs.getLong(N3QueensDataContract.ResultsTable.COLUMN_NAME_DATE));
                result.setAlgorithm(rs.getString(N3QueensDataContract.ResultsTable.COLUMN_NAME_ALGORITHM_NAME));
                result.setAlgorithm_version(rs.getInt(N3QueensDataContract.ResultsTable.COLUMN_NAME_ALGORITHM_VERSION));
                result.setTarget_queens(rs.getInt(N3QueensDataContract.ResultsTable.COLUMN_NAME_TARGET_QUEENS));
                result.setBoard_width(rs.getInt(N3QueensDataContract.ResultsTable.COLUMN_NAME_BOARD_WIDTH));
                result.setBoard_height(rs.getInt(N3QueensDataContract.ResultsTable.COLUMN_NAME_BOARD_HEIGHT));
                result.setSuccess(rs.getInt(N3QueensDataContract.ResultsTable.COLUMN_NAME_SUCCESS));
                result.setVerified(rs.getInt(N3QueensDataContract.ResultsTable.COLUMN_NAME_VERIFIED));
                result.setWas_best_run(rs.getInt(N3QueensDataContract.ResultsTable.COLUMN_NAME_WAS_BEST_RUN));
                result.setRemaining_queens(rs.getInt(N3QueensDataContract.ResultsTable.COLUMN_NAME_REMAINING_QUEENS));
                result.setFirst_queen_x(rs.getInt(N3QueensDataContract.ResultsTable.COLUMN_NAME_FIRST_QUEEN_X));
                result.setBest_iteration(rs.getInt(N3QueensDataContract.ResultsTable.COLUMN_NAME_BEST_ITERATION));
                result.setTotal_iterations(rs.getInt(N3QueensDataContract.ResultsTable.COLUMN_NAME_TOTAL_ITERATIONS));
                result.setSolution_time_in_ms(rs.getLong(N3QueensDataContract.ResultsTable.COLUMN_NAME_SOLUTION_TIME_IN_MS));
                returnList.add(result);
            }
            operationComplete = true;
        }
        catch(SQLException e)
        {
            System.err.println("Error: " + e.getMessage());
        }
        finally
        {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                System.err.println("Error: " + e.getMessage());
            }
        }
        if(operationComplete) {
            return returnList;
        }

        return null;
    }
}
