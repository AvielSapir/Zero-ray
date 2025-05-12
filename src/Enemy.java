import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Enemy extends Sprite{

    // animation
    private BufferedImage[] enemyImages = new BufferedImage[6];
    private int walkAnimFrame = 0;
    private long walkLastFrameT = 0;

    private BufferedImage[] deadImages = new BufferedImage[7];
    private int deadAnimFrame = 0;
    private long deadLastFrameT = 0;

    private boolean onTarget = false;
    private boolean goingToDie = false;
    private boolean dead = false;
    private boolean close = false;

    private final double MOVE_SPEED = 0.015;


    public Enemy(double x, double y) {
        super(x, y);
        try {
            enemyImages[0] = ImageIO.read(new File("img/enemy/enemy1.png"));
            enemyImages[1] = ImageIO.read(new File("img/enemy/enemy2.png"));
            enemyImages[2] = ImageIO.read(new File("img/enemy/enemy3.png"));
            enemyImages[3] = ImageIO.read(new File("img/enemy/enemy4.png"));
            enemyImages[4] = ImageIO.read(new File("img/enemy/enemy5.png"));
            enemyImages[5] = ImageIO.read(new File("img/enemy/enemy6.png"));

            deadImages[0] = ImageIO.read(new File("img/enemy/dead1.png"));
            deadImages[1] = ImageIO.read(new File("img/enemy/dead2.png"));
            deadImages[2] = ImageIO.read(new File("img/enemy/dead3.png"));
            deadImages[3] = ImageIO.read(new File("img/enemy/dead4.png"));
            deadImages[4] = ImageIO.read(new File("img/enemy/dead5.png"));
            deadImages[5] = ImageIO.read(new File("img/enemy/dead6.png"));
            deadImages[6] = ImageIO.read(new File("img/enemy/dead7.png"));
        } catch (IOException e) {
            e.printStackTrace();
            enemyImages[0] = null;
            enemyImages[1] = null;
            enemyImages[2] = null;

            deadImages[0] = null;
            deadImages[1] = null;
            deadImages[2] = null;
            deadImages[3] = null;
            deadImages[4] = null;
            deadImages[5] = null;
            deadImages[6] = null;

        }
    }


    public boolean moveTowards(double playerX, double playerY, int[][] map) {
        double dirX = playerX - this.getX();
        double dirY = playerY - this.getY();

        double distance = Math.sqrt(dirX * dirX + dirY * dirY);

        if (distance > 0.05) {
            close = false;
            dirX /= distance;
            dirY /= distance;
            int rand = new Random().nextInt(6);
            double nextX = this.getX() + dirX * (this.MOVE_SPEED + rand*0.001);
            double nextY = this.getY() + dirY * (this.MOVE_SPEED);

            int mapCellX2 = (int)(nextX);
            int mapCellY1 = (int)(this.getY());

            int mapCellX1 = (int)(this.getX());
            int mapCellY2 = (int)(nextY);

            if (mapCellY2 >= 0 && mapCellY2 < map.length &&
                    mapCellX2 >= 0  && mapCellX2 < map[0].length && map[mapCellY2][mapCellX2] == 0)
            {
                this.setX(nextX);
                this.setY(nextY);
                return false;
            }
            if (mapCellY1 >= 0 && mapCellY1 < map.length &&
                    mapCellX2 >= 0 && mapCellX2 < map[0].length &&
                    map[mapCellY1][mapCellX2] == 0)
            {
                this.setX(nextX);
                return false;
            }
            if (mapCellY2 >= 0 && mapCellY2 < map.length &&
                    mapCellX1 >= 0 && mapCellX1 < map[0].length &&
                    map[mapCellY2][mapCellX1] == 0)
            {
                this.setY(nextY);
                return false;
            }

//            if (Math.sqrt(Math.pow(this.x - playerX, 2) + Math.pow(this.y - playerY, 2)) <= 0.05) {
            //todo
//            }
            return false;

        }
        else {
            this.kill();
            close = true;
            return true;
        }
    }


    public BufferedImage getSprite() {
        if(dead) {
            return deadImages[deadImages.length - 1];
        }
        if(goingToDie) {
            return goingToDieAnim();
        }
        return walkAnim();
    }

    private BufferedImage walkAnim() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - walkLastFrameT > 100) {
            walkAnimFrame++;
            if (walkAnimFrame >= enemyImages.length) {
                walkAnimFrame = 0;
            }
            walkLastFrameT = currentTime;
        }
        return enemyImages[walkAnimFrame];
    }

    private BufferedImage goingToDieAnim() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - deadLastFrameT > 100) {
            deadAnimFrame++;
            this.setHeight(this.getHeight() - 0.07);
            if (deadAnimFrame >= deadImages.length) {
                dead = true;
                deadAnimFrame = 6;
            }
            deadLastFrameT = currentTime;
        }
        if (deadAnimFrame >= 0 && deadAnimFrame < deadImages.length && deadImages[deadAnimFrame] != null) {
            return deadImages[deadAnimFrame];
        } else {
            return null;
        }
    }

    public boolean isDead() {
        return dead;
    }

    public void setOnTarget(boolean onTarget) {
        this.onTarget = onTarget;
    }

    public boolean isOnTarget() {
        return onTarget;
    }

    public void kill() {
        goingToDie = true;
    }

    public boolean isClose() {
        return close;
    }

}

