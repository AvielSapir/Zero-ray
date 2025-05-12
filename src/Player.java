import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class Player {

    // const
    private final double MOVE_SPEED = 2;
    private final double ROTATION_SPEED = 0.03;
    private final double FOV = 75 * Math.PI / 180.0;;
    private final double CELL_HEIGHT;
    private final int PLAYER_SIZE = 20;

    // pos
    private double posX = 45;
    private double posY = 45;
    private double viewAngel = -Math.PI/2;

    // gun animation
    private BufferedImage[] gunImages = new BufferedImage[4];
    private boolean startShoot = false;
    private boolean Shooting  = false;
    private long lastFrameT = 0;
    private int animationFrame = 0;



    // game
    private int[][] map;
    private int health = 100;
    private int ammo = 0;
    private boolean alive = true;

    public Player( int[][] map, double cellHeight){
        CELL_HEIGHT = cellHeight;
        this.map = map;
        try {
            gunImages[0] = ImageIO.read(new File("img/player/gun1.png"));
            gunImages[1] = ImageIO.read(new File("img/player/gun2.png"));
            gunImages[2] = ImageIO.read(new File("img/player/gun3.png"));
            gunImages[3] = ImageIO.read(new File("img/player/gun4.png"));
        } catch (IOException e) {
            e.printStackTrace();
            gunImages[0] = null;
            gunImages[1] = null;
            gunImages[2] = null;
            gunImages[3] = null;
        }
    }

    public void movement(Set<Integer> pressedKeys){
        double deltaX = 0;
        double deltaY = 0;
        double rotation = 0;
        // move check
        if (pressedKeys.contains(KeyEvent.VK_W)){
            deltaX += Math.cos(viewAngel) * MOVE_SPEED;
            deltaY -= Math.sin(viewAngel) * MOVE_SPEED;
        }

        if (pressedKeys.contains(KeyEvent.VK_S)){
            deltaX -= Math.cos(viewAngel) * MOVE_SPEED;
            deltaY += Math.sin(viewAngel) * MOVE_SPEED;
        }

        if (pressedKeys.contains(KeyEvent.VK_D)){
            deltaX -= Math.sin(viewAngel) * MOVE_SPEED;
            deltaY -= Math.cos(viewAngel) * MOVE_SPEED;
        }

        if (pressedKeys.contains(KeyEvent.VK_A)){
            deltaX += Math.sin(viewAngel) * MOVE_SPEED;
            deltaY += Math.cos(viewAngel) * MOVE_SPEED;
        }


        // rotation check
        if (pressedKeys.contains(KeyEvent.VK_E)){
            rotation += ROTATION_SPEED;
        }
        if (pressedKeys.contains(KeyEvent.VK_Q)){
            rotation -= ROTATION_SPEED;
        }


        // rotate
        this.viewAngel += rotation;
        // move (collision)
        int gridX = (int)((posX + deltaX) / CELL_HEIGHT);
        int gridY = (int)((posY + deltaY) / CELL_HEIGHT);
        if (gridY >= 0 && gridY < map.length && gridX >= 0 && gridX < map[0].length && map[gridY][gridX] == 0)
        {
            this.posX = posX + deltaX;
            this.posY = posY + deltaY;
        }
        else {
            // System.out.println("collision detected! ");]
        }

        // gun...
        updateGun();


    }

    public void shoot(Set<Integer> pressedKeys){
        if(pressedKeys.contains(KeyEvent.VK_SPACE) && !Shooting){
            Shooting = true;
            startShoot = true;
            animationFrame = 0;
            lastFrameT = System.currentTimeMillis();
        }
        // todo
    }

    private void updateGun(){
        if(Shooting){
            startShoot = false;
            long currentTime = System.currentTimeMillis();

            if(currentTime - lastFrameT > 150){
                animationFrame++;
                lastFrameT = currentTime;

                if(animationFrame >= gunImages.length){
                    animationFrame = 0;
                    Shooting = false;
                }
            }

        }
    }

    public boolean isShooting() {
        return Shooting && startShoot &&animationFrame == 0;
    }

    public double getPosX() {
        return this.posX;
    }

    public double getPosY() {
        return this.posY;
    }


    public double getFOV() {
        return this.FOV;
    }

    public double getViewAngel() {
        return viewAngel;
    }

    public BufferedImage getGunImage() {
        return gunImages[animationFrame];
    }

    public void gotHit() {
        if(this.health <= 0){
            this.alive = false;
            return;
        }
        this.health -= 1;

    }

    public boolean isAlive() {
        return alive;
    }

    public int getHealth() {
        return health;
    }
}
