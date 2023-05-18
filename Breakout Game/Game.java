import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Game extends JPanel implements KeyListener {
    //Frame dimensions and various game elements
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;
    private static final int PADDLE_HEIGHT = 20;
    private static final int BALL_DIAMETER = 20;
    private static final int BRICK_WIDTH = 80;
    private static final int BRICK_HEIGHT = 30;
    private static final int BRICK_GAP = 5;
    private static final int BRICK_ROWS = 5;
    private static final int BRICK_COLUMNS = 10;
    //Variables for game settings (change with level)
    private static int PADDLE_WIDTH = 120;
    private static double PADDLE_SPEED = 4.0;
    private static double BALL_SPEED = 2.0;
    private Color background;
    //Variables for game state
    private int score;
    private int level;
    //Game objects
    private Ball ball;
    private Paddle paddle;
    private List<Brick> bricks;

    public Game() {
        score = 0;
        level = 1;

        background = Color.black;

        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        setFocusable(true);
        addKeyListener(this);

        //Initialize game objects
        ball = new Ball(FRAME_WIDTH / 2 - BALL_DIAMETER / 2, FRAME_HEIGHT / 2 - BALL_DIAMETER / 2, BALL_DIAMETER, BALL_SPEED, BALL_SPEED);
        paddle = new Paddle(FRAME_WIDTH / 2 - PADDLE_WIDTH / 2, FRAME_HEIGHT - PADDLE_HEIGHT - 10, PADDLE_WIDTH, PADDLE_HEIGHT, 0);
        bricks = createBricks();

        //Start game timer (effectively starts game)
        Timer timer = new Timer(5, e -> {
            update();
            repaint();
        });
        timer.start();
    }
    //Method for initializing grid of bricks
    private List<Brick> createBricks() {
        List<Brick> bricks = new ArrayList<>();
        int brickX = BRICK_GAP;
        int brickY = BRICK_GAP + 50;

        for (int row = 0; row < BRICK_ROWS; row++) {
            for (int col = 0; col < BRICK_COLUMNS; col++) {
                Brick brick = new Brick(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT);
                bricks.add(brick);
                brickX += BRICK_WIDTH + BRICK_GAP;
            }
            brickX = BRICK_GAP;
            brickY += BRICK_HEIGHT + BRICK_GAP;
        }

        return bricks;
    }

    //Updates game logic
    private void update() {
        ball.move();
        handleBallCollision();
        paddle.move();
        handlePaddleCollision();
        handleBrickCollision();
        checkGameOver();
    }

    //Methods for handling different collisions
    private void handleBallCollision() {
        if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.reverseYSpeed();
        }

        if (ball.getX() <= 0 || ball.getX() >= FRAME_WIDTH - BALL_DIAMETER) {
            ball.reverseXSpeed();
        }

        if (ball.getY() <= 0) {
            ball.reverseYSpeed();
        }

        if (ball.getY() >= FRAME_HEIGHT - BALL_DIAMETER) {
            //Game over - must reset settings for level 1
            score = 0;
            level = 1;
            background = Color.black;
            BALL_SPEED = 2.0;
            PADDLE_SPEED = 4.0;
            resetGame();
        }
    }
    //Limit paddle movement within the game frame
    private void handlePaddleCollision() {
        if (paddle.getX() <= 0 && paddle.getXSpeed() < 0) {
            paddle.setXSpeed(0);
        } else if (paddle.getX() >= FRAME_WIDTH - PADDLE_WIDTH && paddle.getXSpeed() > 0) {
            paddle.setXSpeed(0);
        }
    }

    private void handleBrickCollision() {
        for (Brick brick : bricks) {
            if (brick.isVisible() && ball.getBounds().intersects(brick.getBounds())) {
                brick.setVisible(false);
                ball.reverseYSpeed();
                score += 100; //Increment score by 100
            }
        }
    }
    //Start a new level and change appropriate settings
    private void newLevel(){
        level++;
        //Random dark color each level after 1
        int randR = (int)(Math.random() * 60);
        int randG = (int)(Math.random() * 60);
        int randB = (int)(Math.random() * 60);
        background = new Color(randR, randG, randB);
        //Increase game difficulty at high levels
        BALL_SPEED += 0.5; //Increase ball speed
        PADDLE_SPEED += 0.5; //Increase paddle speed

        if (level % 5 == 0 && PADDLE_WIDTH > 60){ //Maintain a minimum paddle width of 60
            PADDLE_WIDTH -= 10; //Every 10 levels, paddle gets smaller
        }
    }

    //Check to see if all bricks cleared
    private void checkGameOver() {
        boolean gameOver = true;
        for (Brick brick : bricks) {
            if (brick.isVisible()) {
                gameOver = false;
                break;
            }
        }
        if (gameOver) {
            //All bricks cleared - level up
            newLevel();
            resetGame();
        }
    }

    //Resets game to initial state (beginning of level)
    private void resetGame() {
        ball = new Ball(FRAME_WIDTH / 2 - BALL_DIAMETER / 2, FRAME_HEIGHT / 2 - BALL_DIAMETER / 2, BALL_DIAMETER, BALL_SPEED, BALL_SPEED);
        paddle = new Paddle(FRAME_WIDTH / 2 - PADDLE_WIDTH / 2, FRAME_HEIGHT - PADDLE_HEIGHT - 10, PADDLE_WIDTH, PADDLE_HEIGHT, 0);
        bricks = createBricks();
    }

    //Render graphics
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //Set background color according to level
        g2d.setColor(background);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        //Draw game objects
        ball.draw(g);
        paddle.draw(g);
        for (Brick brick : bricks) {
            brick.draw(g);
        }
        //Draw score and level information
        g2d.setColor(Color.white);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Score: " + score, 10, 30);
        g2d.drawString("" + level, 770, 30);
    }

    //Handle Key Events for paddle movement
    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) {
            paddle.setXSpeed(-PADDLE_SPEED);
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            paddle.setXSpeed(PADDLE_SPEED);
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
            paddle.setXSpeed(0);
        }
    }

    //Main entry point for game
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Breakout");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(new Game());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
