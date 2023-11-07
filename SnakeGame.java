import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class SnakeGame extends JPanel implements ActionListener {
    private int width = 600;
    private int height = 600;
    private int gridSize = 15;
    private int snakeLength = 5;
    private int[] x = new int[snakeLength];
    private int[] y = new int[snakeLength];
    private int foodX;
    private int foodY;
    private boolean isGameOver = false;
    private boolean isMovingRight = true;
    private boolean isMovingLeft = false;
    private boolean isMovingUp = false;
    private boolean isMovingDown = false;
    private Timer timer;

    public SnakeGame() {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter());
        initializeGame();
    }

    public void initializeGame() {
        // Inicialize a posição inicial da cobra no centro do painel
        for (int i = 0; i < snakeLength; i++) {
            x[i] = width / 2 - i * gridSize;
            y[i] = height / 2;
        }

        // Gere a posição inicial da comida aleatoriamente
        generateFood();

        // Configure o timer para controlar o movimento da cobra
        int delay = 100; // Delay em milissegundos
        timer = new Timer(delay, this);
        timer.start();
    }

    private void generateFood() {
        boolean validFood = false;
        while (!validFood) {
            foodX = (int) (Math.random() * (width / gridSize)) * gridSize;
            foodY = (int) (Math.random() * (height / gridSize)) * gridSize;

            validFood = true;

            for (int i = 0; i < snakeLength; i++) {
                if (foodX == x[i] && foodY == y[i]) {
                    validFood = false;
                    break;
                }
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver) {
            // Atualize o movimento da cobra
            moveSnake();

            // Verifique se a cobra colidiu com a parede ou com ela mesma
            checkCollisions();

            // Verifique se a cobra comeu a comida
            checkFoodCollision();

            // Redesenhe o jogo
            repaint();
        }
    }

    private void moveSnake() {
        // Atualize as coordenadas da cobra para o próximo movimento
        for (int i = snakeLength - 1; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (isMovingRight) {
            x[0] += gridSize;
        } else if (isMovingLeft) {
            x[0] -= gridSize;
        } else if (isMovingUp) {
            y[0] -= gridSize;
        } else if (isMovingDown) {
            y[0] += gridSize;
        }
    }

    private void checkCollisions() {
        // Verifique colisões com as paredes
        if (x[0] >= width || x[0] < 0 || y[0] >= height || y[0] < 0) {
            isGameOver = true;
        }

        // Verifique colisões com o próprio corpo
        for (int i = 1; i < snakeLength; i++) {
            if (x[0] == x[i] && y[0] == y[i]) {
                isGameOver = true;
            }
        }
    }

    private void checkFoodCollision() {
        if (x[0] == foodX && y[0] == foodY) {
            // A cobra comeu a comida, aumente o comprimento da cobra e gere nova comida
            snakeLength++;
            increaseSnakeLength();
            generateFood();
        }
    }

    private void increaseSnakeLength() {
        int[] newX = new int[snakeLength];
        int[] newY = new int[snakeLength];

        for (int i = 0; i < snakeLength - 1; i++) {
            newX[i] = x[i];
            newY[i] = y[i];
        }

        // Adicione a nova parte do corpo da cobra à cabeça da cobra
        newX[snakeLength - 1] = foodX;
        newY[snakeLength - 1] = foodY;

        x = newX; // Atualize a referência para o novo array
        y = newY;
    }



    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!isGameOver) {
            // Desenhe a comida
            g.setColor(Color.RED);
            g.fillRect(foodX, foodY, gridSize, gridSize);

            // Desenhe a cobra
            g.setColor(Color.GREEN);
            for (int i = 0; i < snakeLength; i++) {
                g.fillRect(x[i], y[i], gridSize, gridSize);
            }
        } else {
            // O jogo terminou, exiba uma mensagem de fim de jogo
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Game Over", width / 2 - 70, height / 2);
        }
    }

    private class KeyAdapter extends java.awt.event.KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_UP && !isMovingDown) {
                isMovingUp = true;
                isMovingRight = false;
                isMovingLeft = false;
            } else if (key == KeyEvent.VK_DOWN && !isMovingUp) {
                isMovingDown = true;
                isMovingRight = false;
                isMovingLeft = false;
            } else if (key == KeyEvent.VK_LEFT && !isMovingRight) {
                isMovingLeft = true;
                isMovingUp = false;
                isMovingDown = false;
            } else if (key == KeyEvent.VK_RIGHT && !isMovingLeft) {
                isMovingRight = true;
                isMovingUp = false;
                isMovingDown = false;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            SnakeGame snakeGame = new SnakeGame();
            frame.add(snakeGame);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
