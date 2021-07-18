package num110_at_gmail_dot_com.malishchak;

public class Queen {
    public int x;
    public int y;

    public double findAngle(Queen other)
    {
        return Math.toDegrees(Math.atan2(y-other.y, x-other.x));
    }
    public double findAngle(int otherX, int otherY)
    {
        return Math.toDegrees(Math.atan2(y-otherY, x-otherX));
    }

    public Queen (int newX, int newY)
    {
        x = newX;
        y = newY;
    }
}
