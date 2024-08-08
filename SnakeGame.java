import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {

    private final int SIZE = 40;
    private final int WIDTH = 1000;
    private final int HEIGHT = 800;
    private final int INIT_LENGTH = 1;
    private final int DELAY = 250;

    private Timer timer;
    private int[] x, y;
    private int length;
    private char direction;
    private boolean gameOver;
    private int appleX, appleY;
    private final Random random = new Random();

    public class SnakeGame extends JPanel implements ActionListener {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(110, 110, 5));
        setFocusable(true);

        x = new int[WIDTH * HEIGHT / (SIZE * SIZE)];
        y = new int[WIDTH * HEIGHT / (SIZE * SIZE)];
        length = INIT_LENGTH;
        direction = 'D'; // Initial direction: down
        gameOver = false;

        spawnApple();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameOver) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT:
                            if (direction != 'R') direction = 'L';
                            break;
                        case KeyEvent.VK_RIGHT:
                            if (direction != 'L') direction = 'R';
                            break;
                        case KeyEvent.VK_UP:
                            if (direction != 'D') direction = 'U';
                            break;
                        case KeyEvent.VK_DOWN:
                            if (direction != 'U') direction = 'D';
                            break;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    restartGame();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
        });

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void spawnApple() {
        appleX = random.nextInt(WIDTH / SIZE) * SIZE;
        appleY = random.nextInt(HEIGHT / SIZE) * SIZE;
    }

    private void restartGame() {
        length = INIT_LENGTH;
        for (int i = 0; i < length; i++) {
            x[i] = 40;
            y[i] = 40;
        }
        direction = 'D';
        spawnApple();
        gameOver = false;
        timer.start();
        repaint();
    }

    private void move() {
        int prevX = x[0];
        int prevY = y[0];
        int prev2X, prev2Y;
        x[0] += (direction == 'R') ? SIZE : (direction == 'L') ? -SIZE : 0;
        y[0] += (direction == 'D') ? SIZE : (direction == 'U') ? -SIZE : 0;

        for (int i = 1; i < length; i++) {
            prev2X = x[i];
            prev2Y = y[i];
            x[i] = prevX;
            y[i] = prevY;
            prevX = prev2X;
            prevY = prev2Y;
        }

        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            gameOver = true;
        }

        for (int i = 1; i < length; i++) {
            if (x[0] == x[i] && y[0] == y[i]) {
                gameOver = true;
            }
        }

        if (x[0] == appleX && y[0] == appleY) {
            length++;
            spawnApple();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.RED);
        g.fillRect(appleX, appleY, SIZE, SIZE);

        g.setColor(Color.GREEN);
        for (int i = 0; i < length; i++) {
            g.fillRect(x[i], y[i], SIZE, SIZE);
        }

        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over! Press Enter to Play Again", WIDTH / 4, HEIGHT / 2);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
