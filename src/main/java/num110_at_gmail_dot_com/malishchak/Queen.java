package num110_at_gmail_dot_com.malishchak;

/**
 * Basic container class for an instance of a queen on the chess board
 */
public class Queen {

    /**
     * The x-coordinate for the Queen on the board
     */
    private int x;

    /**
     * @return The x-coordinate for the Queen on the board
     */
    public int getX() {
        return x;
    }

    /**
     * @param x New x-coordinate for Queen on the board
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * The y-coordinate for the Queen on the board
     */
    private int y;

    /**
     * @return The y-coordinate for the Queen on the board
     */
    public int getY() {
        return y;
    }

    /**
     * @param y New y-coordinate for Queen on the board
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Finds the angle between this queen and another queen relative to the horizontal
     * @param other The target queen to which an angle is needed.
     * @return Angle from this queen to the other queen relative to the horizontal.
     */
    public double findAngle(Queen other)
    {
        return Math.toDegrees(Math.atan2(y-other.y, x-other.x));
    }

    /**
     * Finds the angle between this queen and a different x,y coordinate relative to the horizontal
     * @param otherX The target x-position to which an angle is needed.
     * @param otherY The target y-position to which an angle is needed.
     * @return Angle from this queen to the target x,y coordinate relative to the horizontal.
     */
    public double findAngle(int otherX, int otherY)
    {
        return Math.toDegrees(Math.atan2(y-otherY, x-otherX));
    }

    /**
     * Creates a queen with coordinates (x,y)
     * @param newX X-coordinate of Queen on a chess board
     * @param newY Y-coordinate of Queen on a chess board
     */
    public Queen (int newX, int newY)
    {
        x = newX;
        y = newY;
    }
}
