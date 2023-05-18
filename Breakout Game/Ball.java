import java.awt.*;

public class Ball {
    //Ball properties
    private int x;
    private int y;
    private int diameter;
    private double xSpeed;
    private double ySpeed;

    public Ball(int x, int y, int diameter, double xSpeed, double ySpeed) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    //Moves in both directions
    public void move() {
        x += xSpeed;
        y += ySpeed;
    }

    //Reverse speeds on collision
    public void reverseXSpeed() {
        xSpeed = -xSpeed;
    }

    public void reverseYSpeed() {
        ySpeed = -ySpeed;
    }

    //Draw method
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x, y, diameter, diameter);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}