import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class StartPanel extends JPanel {

    int START_WIDTH;
    int START_HEIGHT;
    BufferedImage startBg;
    BufferedImage dontDie;
    Sounds sounds = new Sounds();

    public StartPanel(int SCREEN_WIDTH, int SCREEN_HEIGHT) {
        this.START_WIDTH = SCREEN_WIDTH;
        this.START_HEIGHT = SCREEN_HEIGHT;

        this.setLayout(null);
        this.setBounds(0, 0, START_WIDTH, START_HEIGHT);

        loadImage();
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("a");
        if (startBg != null && dontDie != null) {
            g.drawImage(startBg, 0, 0, START_WIDTH, START_HEIGHT, this);
            g.drawImage(dontDie, START_WIDTH/2 - 700/2, START_HEIGHT/2 - 150/2, 700, 150,this);
            System.out.println("print");
        }
    }

    public void play() {
        sounds.startSound.playSound();
        repaint();

        try{
            Thread.sleep(5000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void loadImage() {
        try {
            this.startBg = ImageIO.read(new File("img/cuts/endImg.png"));
            this.dontDie = ImageIO.read(new File("img/cuts/dontDie.png"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
