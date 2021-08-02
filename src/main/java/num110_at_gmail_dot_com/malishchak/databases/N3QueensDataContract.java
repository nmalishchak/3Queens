package num110_at_gmail_dot_com.malishchak.databases;

/**
 * N3QueensDataContract is a class of static constants used to define the structure
 * of the N3Queens database. Contained are subclasses for each table containing the table names,
 * and column names within the table, as well as other constants used by the database.
 *
 * @author nmalishchak
 */
public final class N3QueensDataContract {

    /**
     * Name of the entire database
     */
    public static final String DATABASE_NAME = "n3queens.db";

    public N3QueensDataContract()
    {

    }

    public static final class ResultsTable {
        public static final String TABLE_NAME = "results";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_ALGORITHM_NAME = "algorithm";
        public static final String COLUMN_NAME_ALGORITHM_VERSION = "algorithm_version";
        public static final String COLUMN_NAME_TARGET_QUEENS = "target_queens";
        public static final String COLUMN_NAME_BOARD_WIDTH = "board_width";
        public static final String COLUMN_NAME_BOARD_HEIGHT = "board_height";
        public static final String COLUMN_NAME_SUCCESS = "success";
        public static final String COLUMN_NAME_VERIFIED = "verified";
        public static final String COLUMN_NAME_WAS_BEST_RUN = "was_best_run";
        public static final String COLUMN_NAME_REMAINING_QUEENS = "remaining_queens";
        public static final String COLUMN_NAME_FIRST_QUEEN_X = "first_queen_x";
        public static final String COLUMN_NAME_BEST_ITERATION = "best_iteration";
        public static final String COLUMN_NAME_TOTAL_ITERATIONS = "total_iteration";
        public static final String COLUMN_NAME_SOLUTION_TIME_IN_MS = "solution_time_in_ms";
    }
}
