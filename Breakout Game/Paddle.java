import java.awt.*;

public class Paddle {
    //Paddle properties
    private int x;
    private int y;
    private int width;
    private int height;
    private double xSpeed;

    public Paddle(int x, int y, int width, int height, double xSpeed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xSpeed = xSpeed;
    }
    
    //Only moves in x direction
    public void move() {
        x += xSpeed;
    }

    //Draw method
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);
    }

    public void setXSpeed(double speed) {
        xSpeed = speed;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public double getXSpeed() {
        return xSpeed;
    }
}
