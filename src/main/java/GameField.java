import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private static final int SIZE = 320;
    private static final int DOT_SIZE = 16;
    private static final int ALL_DOTS = 400;
    private static final int GAME_OVER_X = 125;
    private static final int GAME_OVER_Y = SIZE / 2;

    private Image snake;
    private Image mouse;
    private int mouseX;
    private int mouseY;
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private int dots;
    private Timer timer;
    private boolean inGame = true;

    private enum Direction { UP, DOWN, LEFT, RIGHT }

    private Direction currentDirection = Direction.RIGHT;
    private Random random = new Random();

    public GameField() {
        setBackground(Color.white);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    public void initGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i * DOT_SIZE;
            y[i] = 48;
        }
        timer = new Timer(250, this);
        timer.start();
        createApple();
    }

    public void createApple() {
        mouseX = random.nextInt(20) * DOT_SIZE;
        mouseY = random.nextInt(20) * DOT_SIZE;
    }

    public void loadImages() {
        ImageIcon iia = new ImageIcon(getClass().getResource("/mouse.png"));
        mouse = iia.getImage();
        ImageIcon iib = new ImageIcon(getClass().getResource("/snake.png"));
        snake = iib.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            g.drawImage(mouse, mouseX, mouseY, this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(snake, x[i], y[i], this);
            }
        } else {
            String str = "Game Over";
            g.setColor(Color.BLACK);
            g.drawString(str, GAME_OVER_X, GAME_OVER_Y);
        }
    }

    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (currentDirection == Direction.LEFT) {
            x[0] -= DOT_SIZE;
        }
        if (currentDirection == Direction.RIGHT) {
            x[0] += DOT_SIZE;
        }
        if (currentDirection == Direction.UP) {
            y[0] -= DOT_SIZE;
        }
        if (currentDirection == Direction.DOWN) {
            y[0] += DOT_SIZE;
        }
    }

    public void checkApple() {
        if (x[0] == mouseX && y[0] == mouseY) {
            dots++;
            createApple();
        }
    }

    public void checkCollisions() {
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
        if (x[0] > SIZE || x[0] < 0 || y[0] > SIZE || y[0] < 0) {
            inGame = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollisions();
            move();
        }
        repaint();
    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && currentDirection != Direction.RIGHT) {
                currentDirection = Direction.LEFT;
            }
            if (key == KeyEvent.VK_RIGHT && currentDirection != Direction.LEFT) {
                currentDirection = Direction.RIGHT;
            }
            if (key == KeyEvent.VK_UP && currentDirection != Direction.DOWN) {
                currentDirection = Direction.UP;
            }
            if (key == KeyEvent.VK_DOWN && currentDirection != Direction.UP) {
                currentDirection = Direction.DOWN;
            }
        }
    }
}
