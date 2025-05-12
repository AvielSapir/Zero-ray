import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class BarPanel extends JPanel {

    int BAR_WIDTH;
    int BAR_HEIGHT = 200;
    private BufferedImage backImg;


    public BarPanel(int SCREEN_WIDTH, int SCREEN_HEIGHT){
        this.BAR_WIDTH = SCREEN_WIDTH;
        this.setBounds(0, SCREEN_HEIGHT - BAR_HEIGHT, BAR_WIDTH, BAR_HEIGHT);
        this.setBackground(new Color(39, 39, 39));
        GamePanel.kills = 0;
        try {
            backImg = ImageIO.read(new File("img/bar/barImg.png"));
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void barLoop() {
        new Thread() {
            public void run() {
                while (true) {
                    repaint();
                    try {
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backImg, 0, 0, BAR_WIDTH-10, BAR_HEIGHT-30,this);
        g.setColor(Color.WHITE);
        Font font = new Font("Serif", Font.BOLD, 80);
        g.setFont(font);
        g.drawString(getTheKills(), BAR_WIDTH-320, BAR_HEIGHT/2+10);
        g.drawString(getTheHealth(), 200, BAR_HEIGHT/2+10);
    }


    public String getTheKills(){
        return String.valueOf(GamePanel.kills);
    }

    public String getTheHealth(){
        return String.valueOf(GamePanel.health);
    }

}
