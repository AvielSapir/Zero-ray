import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class EndPanel extends JPanel {

    int END_WIDTH;
    int END_HEIGHT;
    BufferedImage endBg;
    BufferedImage dontDie;
    Sounds sounds = new Sounds();

    public EndPanel(int SCREEN_WIDTH, int SCREEN_HEIGHT) {
        this.END_WIDTH = SCREEN_WIDTH;
        this.END_HEIGHT = SCREEN_HEIGHT;

        this.setLayout(null);
        this.setBounds(0, 0, END_WIDTH, END_HEIGHT);

        loadImage();
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println("a");
        if (endBg != null && dontDie != null) {
            g.drawImage(endBg, 0, 0, END_WIDTH, END_HEIGHT, this);
            g.drawImage(dontDie, END_WIDTH/2 - 700/2, END_HEIGHT/2 - 150/2, 700, 150,this);
        }
    }

    public void play() {
        sounds.gameOverSound.playSound();
        repaint();

        try{
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadImage() {
        try {
            this.endBg = ImageIO.read(new File("img/cuts/endImg.png"));
            this.dontDie = ImageIO.read(new File("img/cuts/gameOver.png"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
