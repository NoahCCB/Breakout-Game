import java.awt.*;

public class Brick {
    //Brick properties
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean isVisible;

    public Brick(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isVisible = true;
    }

    //Draw method
    public void draw(Graphics g) {
        if (isVisible) {
            g.setColor(Color.green);
            g.fillRect(x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    //Visibility depends on wether it was hit or not
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isVisible() {
        return isVisible;
    }
}
