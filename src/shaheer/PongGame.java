package shaheer;

import gamewindow.MainMenuBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PongGame extends JPanel implements ActionListener, KeyListener {

    private int ballX = 390, ballY = 290;
    private int ballXDir = 4; // Faster ball speed
    private int ballYDir = 4; // Faster ball speed
    private int paddle1Y = 250, paddle2Y = 250;
    private int paddleSpeed = 8; // Faster paddle speed

    private int score1 = 0, score2 = 0;
    private int misses1 = 0, misses2 = 0;
    private int timeElapsed = 0;

    private boolean gameOver = false;

    private Timer timer;

    public PongGame() {
        setFocusable(true);
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        addKeyListener(this);

        timer = new Timer(5, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Serif", Font.BOLD, 72));
            g.drawString("Game Over", 200, 300);
            g.setFont(new Font("Serif", Font.BOLD, 36));
            g.drawString("Player 1: " + score1 + "  Player 2: " + score2, 200, 400);
            return;
        }

        g.setColor(Color.WHITE);

        // Draw paddles
        g.setColor(Color.GREEN); // Left paddle color
        g.fillRect(50, paddle1Y, 10, 100);

        g.setColor(Color.RED); // Right paddle color
        g.fillRect(740, paddle2Y, 10, 100);

        // Draw ball
        g.setColor(Color.WHITE);
        g.fillOval(ballX, ballY, 20, 20);

        // Draw scores
        g.setColor(Color.GREEN);
        g.setFont(new Font("Serif", Font.BOLD, 36));
        g.drawString("Player 1: " + score1, 50, 50);
        g.drawString("Player 2: " + score2, 550, 50);

        // Draw misses
        g.setColor(Color.ORANGE);
        g.drawString("Misses: " + misses1, 50, 100);
        g.drawString("Misses: " + misses2, 550, 100);

        // Draw timer
        g.setColor(Color.YELLOW);
        g.drawString("Time: " + (timeElapsed / 1000) + "s", 350, 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            moveBall();
            checkCollisions();
            repaint();
            timeElapsed += 5;
        }
    }

    private void moveBall() {
        ballX += ballXDir;
        ballY += ballYDir;
    }

    private void checkCollisions() {
        // Ball hits top or bottom
        if (ballY <= 0 || ballY >= getHeight() - 20) {
            ballYDir *= -1;
        }

        // Ball hits left paddle
        if (ballX <= 60 && ballY >= paddle1Y && ballY <= paddle1Y + 100) {
            ballXDir *= -1;
        }

        // Ball hits right paddle
        if (ballX >= 730 && ballY >= paddle2Y && ballY <= paddle2Y + 100) {
            ballXDir *= -1;
        }

        // Ball goes out of bounds
        if (ballX <= 0) {
            score2++;
            misses1++;
            checkGameOver();
            resetBall();
        }

        if (ballX >= getWidth() - 20) {
            score1++;
            misses2++;
            checkGameOver();
            resetBall();
        }
    }

    private void checkGameOver() {
        if (misses1 >= 4 || misses2 >= 4) {
            gameOver = true;
            timer.stop();
        }
    }

    private void resetBall() {
        ballX = 390;
        ballY = 290;
        ballXDir *= -1;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_W && paddle1Y > 0) {
                paddle1Y -= paddleSpeed;
            }
            if (e.getKeyCode() == KeyEvent.VK_S && paddle1Y < getHeight() - 100) {
                paddle1Y += paddleSpeed;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP && paddle2Y > 0) {
                paddle2Y -= paddleSpeed;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN && paddle2Y < getHeight() - 100) {
                paddle2Y += paddleSpeed;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    public void main() {
        JFrame frame = new JFrame("Pong Game");
        frame.setJMenuBar(new MainMenuBar(frame));
        PongGame pongGame = new PongGame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(pongGame);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}