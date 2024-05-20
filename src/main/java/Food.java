import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class Food {

    private Point position;
    private Random random;

    private ImageIcon appleImage;

    public Food() {
        random = new Random();
        relocate();
        try {
              appleImage = new ImageIcon("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        if (appleImage != null) {
            g.drawImage(appleImage.getImage(), position.x * 10, position.y * 10, 20, 20, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(position.x * 10, position.y * 10, 10, 10);
        }

    }

    public void relocate() {
        position = new Point(random.nextInt(60), random.nextInt(60));
    }

    public void move() {
        int newX = position.x + random.nextInt(3) - 2;
        int newY = position.y + random.nextInt(3) - 2;
        newX = Math.max(0, Math.min(59, newX));
        newY = Math.max(0, Math.min(59, newY));
        position = new Point(newX, newY);
    }

    public Point getPosition() {
        return position;
    }




}
