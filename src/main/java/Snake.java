import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Snake {

    private ArrayList<Point> body;
    private int direction; // 0 = Up, 1 = Right, 2 = Down, 3 = Left
    private boolean directionChanged;
    private Semaphore mutex = new Semaphore(1);

    public Snake() {
        body = new ArrayList<>();
        body.add(new Point(10, 10));
        direction = 1;
        directionChanged = false;
    }

    public void draw(Graphics g) {
        try {
            mutex.acquire();
            g.setColor(Color.GREEN);
            for (Point point : body) {
                g.fillRect(point.x * 10, point.y * 10, 10, 10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mutex.release();
        }
    }



    public void move() {
        try {
            mutex.acquire();
            Point head = body.get(0);
            Point newHead = new Point(head);

            switch (direction) {
                case 0: newHead.y--; break;
                case 1: newHead.x++; break;
                case 2: newHead.y++; break;
                case 3: newHead.x--; break;
            }

            body.add(0, newHead);
            body.remove(body.size() - 1);
            directionChanged = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mutex.release();
        }
    }

    public void grow() {
        try {
            mutex.acquire();
            body.add(0, new Point(body.get(0)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mutex.release();
        }
    }

    public void changeDirection(int keyCode) {
        try {
            mutex.acquire();
            if (directionChanged) {
                return;
            }

            switch (keyCode) {
                case KeyEvent.VK_UP:
                    if (direction != 2) {
                        direction = 0;
                        directionChanged = true;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 3) {
                        direction = 1;
                        directionChanged = true;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 0) {
                        direction = 2;
                        directionChanged = true;
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != 1) {
                        direction = 3;
                        directionChanged = true;
                    }
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mutex.release();
        }
    }

    public boolean isEating(Food food) {
        try {
            mutex.acquire();
            return body.get(0).equals(food.getPosition());
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } finally {
            mutex.release();
        }
    }

    public boolean checkCollision() {
        try {
            mutex.acquire();
            Point head = body.get(0);
            for (int i = 1; i < body.size(); i++) {
                if (body.get(i).equals(head)) {
                    return true;
                }
            }
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return true;
        } finally {
            mutex.release();
        }
    }
    public boolean isOutOfBounds(int width, int height) {
        try {
            mutex.acquire();
            Point head = body.get(0);
            if (head.x < 0 || head.x >= width || head.y < 0 || head.y >= height) {
                return true;
            }
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return true;
        } finally {
            mutex.release();
        }
    }

}
