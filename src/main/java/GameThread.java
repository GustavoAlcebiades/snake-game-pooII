public class GameThread implements Runnable{

    private GamePanel gamePanel;

    public GameThread(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        while (true) {
            gamePanel.updateGame();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
