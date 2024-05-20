import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GamePanel extends JPanel implements KeyListener {

    private Snake snake;
    private Food food;
    private boolean isGameOver = false;
    private Semaphore mutex = new Semaphore(1);
    private int score = 0;
    private int timeLeft = 30;

    public GamePanel() {
        snake = new Snake();
        food = new Food();
        addKeyListener(this);
        setFocusable(true);
        setPreferredSize(new Dimension(600, 600));

        new Thread(new TimerThread()).start();
        new Thread(new FoodMoveThread()).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over", 180, 300);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Score: " + score, 250, 350);
        } else {
            try {
                mutex.acquire();
                snake.draw(g);
                food.draw(g);

                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Score: " + score, 10, 20);
                g.drawString("Time Left: " + timeLeft + "s", 10, 40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mutex.release();
            }
        }
    }

    public void updateGame() {
        try {
            mutex.acquire();
            if (!isGameOver) {
                if (snake.isEating(food)) {
                    snake.grow();
                    food.relocate();
                    score++; // Incrementa a pontuação
                    timeLeft = 30;
                    SoundPlayer.playSound("");
                } else {
                    snake.move();
                }
                if (snake.checkCollision() && snake.isEating(food)|| snake.isOutOfBounds(60, 60)) {
                    isGameOver = true;
                    SoundPlayer.playSound("");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mutex.release();
        }
        repaint();
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            mutex.acquire();
            snake.changeDirection(e.getKeyCode());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            mutex.release();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    private class TimerThread implements Runnable{
        @Override
        public void run() {
            while (!isGameOver) {
                try {
                    Thread.sleep(2000);
                    mutex.acquire();
                    if (!isGameOver) {
                        timeLeft--;
                        if (timeLeft <= 0) {
                            isGameOver = true;
                            SoundPlayer.playSound("");
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.release();
                }
                repaint();
            }
        }
    }

    private class FoodMoveThread implements Runnable {
        @Override
        public void run() {
            while (!isGameOver) {
                try {
                    Thread.sleep(1000);
                    mutex.acquire();
                    if (!isGameOver) {
                        food.move();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.release();
                }
                repaint();
            }
        }
    }

}


