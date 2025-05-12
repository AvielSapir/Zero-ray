import javax.swing.*;
import java.awt.*;

public class  Main {

    public static void main(String[] args) {
        final int SCREEN_WIDTH = 1080;
        final int SCREEN_HEIGHT = 720;
        Image iconImage = null;
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        window.setResizable(false);
        window.setLayout(null);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.setTitle("Zero Ray");
        try {
            iconImage = new ImageIcon("img/home/zeroRay.png").getImage();
        } catch (Exception e) {
            System.err.println("icon canot found" + e.getMessage());
        }
        if (iconImage != null) {
            window.setIconImage(iconImage);
        }else {
            System.out.println("icon canot found");
        }

        while (true){
            HomePanel homePanel = new HomePanel(SCREEN_WIDTH, SCREEN_HEIGHT);
            window.add(homePanel);
            homePanel.grabFocus();
            if (homePanel.run()){
                window.remove(homePanel);
                startGame(window, SCREEN_WIDTH, SCREEN_HEIGHT);
            }
        }
    }

    public static void startGame(Window window, int SCREEN_WIDTH, int SCREEN_HEIGHT){
        StartPanel startPanel = new StartPanel(SCREEN_WIDTH, SCREEN_HEIGHT);
        BarPanel barPanel = new BarPanel(SCREEN_WIDTH, SCREEN_HEIGHT);


        // 1.
        window.add(startPanel);
        startPanel.play();
        window.remove(startPanel);

        //2.
        GamePanel gamePanel = new GamePanel(SCREEN_WIDTH, SCREEN_HEIGHT);
        EndPanel endPanel = new EndPanel(SCREEN_WIDTH, SCREEN_HEIGHT);
        window.add(gamePanel);
        window.add(barPanel);

        gamePanel.grabFocus();
        barPanel.barLoop();
        gamePanel.gameLoop();

        window.remove(barPanel);
        window.remove(gamePanel);

        //3.
        window.add(endPanel);
        endPanel.play();
        window.remove(endPanel);
    }

}