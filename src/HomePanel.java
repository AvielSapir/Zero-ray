import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class HomePanel extends JPanel {

    int HOME_WIDTH;
    int HOME_HEIGHT;

    int startBtWidth = 500;
    int startBtHeight = 200;
    int htpBtWidth = 250;
    int htpBtHeight = 100;
    int backHtpBtWidth = 50;
    int backHtpBtHeight = 50;

    boolean howToPlay = false;

    private JButton startBt;
    private JButton htpBt;
    private JButton backHtpBt;

    private BufferedImage backgroundImg;
    private BufferedImage howToPlayImg;
    private ImageIcon startImg;
    private ImageIcon htpImg;
    private ImageIcon startImg2;
    private ImageIcon htpImg2;
    private ImageIcon backHtpImg;

    private int pressd = 0;

    public HomePanel(int SCREEN_WIDTH, int SCREEN_HEIGHT) {

        this.HOME_HEIGHT = SCREEN_HEIGHT;
        this.HOME_WIDTH = SCREEN_WIDTH;
        this.setLayout(null);
        this.setBounds(0, 0, HOME_WIDTH, HOME_HEIGHT);
        this.setBackground(new Color(39, 39, 39));
        loadImages();

        startBt = new JButton("Start");
        htpBt = new JButton("How to Play");
        backHtpBt = new JButton();

        // startBt
        startBt.setFocusPainted(false);
        startBt.setBorderPainted(false);
        startBt.setIcon(startImg);
        startBt.setBackground(new Color(0, 0, 0, 0));
        int startBtX = HOME_WIDTH/2 - startBtWidth/2;
        int startBtY = HOME_HEIGHT/2 - startBtHeight/2;

        // htpBt
        htpBt.setFocusPainted(false);
        htpBt.setBorderPainted(false);
        htpBt.setIcon(htpImg);
        htpBt.setBackground(new Color(0, 0, 0, 0));
        int htpBtX = HOME_WIDTH/2 - htpBtWidth/2;
        int htpBtY = (HOME_HEIGHT/2 - startBtHeight/2) + startBtHeight;

        // backHtpBt
        backHtpBt.setFocusPainted(false);
        backHtpBt.setBorderPainted(false);
        backHtpBt.setIcon(backHtpImg);
        backHtpBt.setBackground(new Color(0, 0, 0, 0));
        backHtpBt.setVisible(false);
        int backHtpBtX = HOME_WIDTH - 220;
        int backHtpBtY = 100;

        startBt.setBounds(startBtX, startBtY, startBtWidth, startBtHeight);
        htpBt.setBounds(htpBtX, htpBtY, htpBtWidth, htpBtHeight);
        backHtpBt.setBounds(backHtpBtX,backHtpBtY , backHtpBtWidth, backHtpBtHeight);

        this.add(startBt);
        this.add(htpBt);
        this.add(backHtpBt);

        // Listeners
        startBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pressd = 1;
            }
        });
        startBt.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startBt.setIcon(startImg2);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startBt.setIcon(startImg);
            }
        });

        htpBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                howToPlay = true;
            }
        });
        htpBt.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                htpBt.setIcon(htpImg2);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                htpBt.setIcon(htpImg);
            }
        });

        backHtpBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backHtpBt.setVisible(false);
                howToPlay = false;
                startBt.setVisible(true);
                htpBt.setVisible(true);
            }
        });
    }

    public boolean run(){
        while(true){
            repaint();
            if(pressd != 0){
                return true;
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImg != null) {
            g.drawImage(backgroundImg, -50, 0, getWidth()+100, getHeight()-25, this);
        }
        if (howToPlay && howToPlayImg != null) {
            g.drawImage(howToPlayImg, HOME_WIDTH/2 - getWidth()/2, HOME_HEIGHT/2 - getHeight()/2, getWidth(), getHeight(), this);
            startBt.setVisible(false);
            htpBt.setVisible(false);
            backHtpBt.setVisible(true);
        }
    }



    private void loadImages(){
        System.out.println("Loading images...");

        try{
            this.startImg = new ImageIcon("img/home/start.png");
            this.startImg = new ImageIcon(startImg.getImage().getScaledInstance(this.startBtWidth, this.startBtHeight, Image.SCALE_DEFAULT));
            this.startImg2 = new ImageIcon("img/home/start2.png");
            this.startImg2 = new ImageIcon(startImg2.getImage().getScaledInstance(this.startBtWidth+10, this.startBtHeight+10, Image.SCALE_DEFAULT));

            this.htpImg = new ImageIcon("img/home/htp.png");
            this.htpImg = new ImageIcon(htpImg.getImage().getScaledInstance(this.htpBtWidth, this.htpBtHeight, Image.SCALE_DEFAULT));
            this.htpImg2 = new ImageIcon("img/home/htp2.png");
            this.htpImg2 = new ImageIcon(htpImg2.getImage().getScaledInstance(this.htpBtWidth+10, this.htpBtHeight+10, Image.SCALE_DEFAULT));

            this.backHtpImg = new ImageIcon("img/home/backHtp.png");
            this.backHtpImg = new ImageIcon(backHtpImg.getImage().getScaledInstance(this.backHtpBtWidth, this.backHtpBtHeight, Image.SCALE_DEFAULT));



            this.backgroundImg = ImageIO.read(new File("img/home/Background.png"));
            this.howToPlayImg = ImageIO.read(new File("img/home/howToPlay.png"));


        }catch(Exception e){
            e.printStackTrace();
        }

    }
}