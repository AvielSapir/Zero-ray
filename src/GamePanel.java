import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//+
import java.awt.image.BufferedImage;
import java.util.concurrent.CopyOnWriteArrayList;
//+

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private final int[][] map = {
            {1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1},
            {1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 1},
            {1, 0, 1, 1, 0, 2, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1},
            {1, 0, 0, 1, 0, 2, 2, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 1, 1, 1, 0, 0, 2, 0, 0, 0, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 0, 0, 1, 0, 2, 2, 2, 2, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 2, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 2, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };


    public static int kills = 0;
    public static int health = 0;

    private Player player;
    private boolean playerGotHit = false;

    private final int GPANEL_WIDTH;
    private final int GPANEL_HEIGHT;
    private final int GRID_SIZE = 16;
    private final double CELL_HEIGHT;

    private final int FPS = 60;
    private long SPAWN_TIME = 15_000_000_000L;
    private long LAST_SPAWN_TIME = 0;

    private final Set<Integer> pressedKeys = new HashSet<>();
    private CopyOnWriteArrayList<Enemy> enemies = new CopyOnWriteArrayList<>();
    private double[] zDepth;
    private EnemySpawner enemySpawner;
    private Sounds sounds = new Sounds();
    private BufferedImage bloodImg;


    public GamePanel(int SCREEN_WIDTH, int SCREEN_HEIGHT) {
        // panel settings
        this.GPANEL_WIDTH = SCREEN_WIDTH;
        this.GPANEL_HEIGHT = SCREEN_HEIGHT - 200;
        this.CELL_HEIGHT = (double) this.GPANEL_HEIGHT / this.GRID_SIZE;
        this.setBounds(0, 0, this.GPANEL_WIDTH, this.GPANEL_HEIGHT);
        this.setBackground(new Color(34, 34, 34));
        this.setFocusable(true);
        this.addKeyListener(this);

        // create objects
        player = new Player(map, CELL_HEIGHT);
        zDepth = new double[GPANEL_WIDTH];
        enemySpawner = new EnemySpawner(map, (int) (player.getPosX() / CELL_HEIGHT), (int) (player.getPosY() / CELL_HEIGHT), enemies);
        loadImages();

        // start on create
        this.sounds.backGroundMusic.playSound();
        enemySpawner.spawnEnemies(20);
    }

    public void gameLoop() {
        while (true) {
            long startFrame = System.nanoTime();
            // player
            player.movement(pressedKeys);
            player.shoot(pressedKeys);
            updateHealth(player);
            // enemy
            this.ShootOnEnemy();
            if (startFrame - LAST_SPAWN_TIME >= SPAWN_TIME) {
                enemySpawner.spawnEnemies(5);
                LAST_SPAWN_TIME = System.nanoTime();
                if (SPAWN_TIME > 5_000_000_000L) {
                    SPAWN_TIME -= 1_000_000_000L;
                }
            }
            if (!player.isAlive()) {
                sounds.backGroundMusic.stopSound();
                return;
            }
            this.updateEnemies();
            repaint();
            //fps
            long endFrame = System.nanoTime();
            long frameTime = endFrame - startFrame;
            long sleepNeed = (1000000000 / FPS - frameTime) / 1000000;
            handleFPS(sleepNeed);
        }
    }

    // prints
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paint3D(g);

        enemies.sort((enemy1, enemy2) -> {
            double playerMapX = player.getPosX() / CELL_HEIGHT;
            double playerMapY = player.getPosY() / CELL_HEIGHT;
            double dist1Sq = Math.pow(enemy1.getX() - playerMapX, 2) + Math.pow(enemy1.getY() - playerMapY, 2);
            double dist2Sq = Math.pow(enemy2.getX() - playerMapX, 2) + Math.pow(enemy2.getY() - playerMapY, 2);
            if (dist1Sq < dist2Sq)
                return 1;
            if (dist1Sq > dist2Sq)
                return -1;
            return 0;
        });
        Enemy onTop = null;
        for (Enemy enemy : enemies) {
            enemy.setOnTarget(false);
            if (paintSprites(g, enemy) && !enemy.isDead())
                onTop = enemy;
        }
        if (onTop != null) {
            onTop.setOnTarget(true);
        }

        if (playerGotHit && bloodImg != null) {
            g.drawImage(bloodImg, -50, -50, this);
            sounds.zombieSound.playSound();
            this.playerGotHit = false;
        }

        BufferedImage gunImage = player.getGunImage();
        if (gunImage != null) {
            int x = (GPANEL_WIDTH - 200) / 2 + 100;
            int y = GPANEL_HEIGHT - 300;
            g.drawImage(gunImage, x, y, 300, 300, this);
            g.setColor(Color.WHITE);
            g.fillOval(GPANEL_WIDTH / 2 - 4, GPANEL_HEIGHT / 2 - 4, 8, 8);
        }
    }

    // {walls}
    public void paint3D(Graphics g) {
        g.setColor(new Color(78, 101, 118, 255));
        g.fillRect(0, GPANEL_HEIGHT / 2, GPANEL_WIDTH, GPANEL_HEIGHT);

        g.setColor(new Color(41, 55, 70));
        g.fillRect(0, 0, GPANEL_WIDTH, GPANEL_HEIGHT / 2);

        for (int i = 0; i < this.GPANEL_WIDTH; i++) {
            double cameraX = 2 * i / (double) GPANEL_WIDTH - 1;
            double rayDirX = Math.cos(player.getViewAngel() + cameraX * player.getFOV() / 2);
            double rayDirY = -Math.sin(player.getViewAngel() + cameraX * player.getFOV() / 2);
            int mapX = (int) player.getPosX() / (int) CELL_HEIGHT;
            int mapY = (int) player.getPosY() / (int) CELL_HEIGHT;

            double deltaDistX = Math.abs(1 / (rayDirX + 0.00001));
            double deltaDistY = Math.abs(1 / (rayDirY + 0.00001));

            int stepX;
            int stepY;
            double sideDistX;
            double sideDistY;

            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (player.getPosX() / CELL_HEIGHT - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1 - player.getPosX() / CELL_HEIGHT) * deltaDistX;
            }

            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (player.getPosY() / CELL_HEIGHT - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1 - player.getPosY() / CELL_HEIGHT) * deltaDistY;
            }

            boolean hit = false;
            int side = 0;
            while (!hit) {
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
                if (mapY >= 0 && mapY < map.length && mapX >= 0 && mapX < map[0].length && map[mapY][mapX] > 0) {
                    hit = true;
                }
            }

            double wallDist;
            if (side == 0) {
                wallDist = sideDistX - deltaDistX;
            } else {
                wallDist = sideDistY - deltaDistY;
            }

            // fish eye
            double angleFromCenter = cameraX * player.getFOV() / 2;
            double correctedWallDist = wallDist / Math.cos(angleFromCenter);

            zDepth[i] = correctedWallDist;

            int lineHeight = (int) (GPANEL_HEIGHT / (correctedWallDist + 0.00001));
            int drawStart = -lineHeight / 2 + GPANEL_HEIGHT / 2;
            if (drawStart < 0)
                drawStart = 0;

            int drawEnd = lineHeight / 2 + GPANEL_HEIGHT / 2;
            if (drawEnd >= GPANEL_HEIGHT)
                drawEnd = GPANEL_HEIGHT - 1;

            Color wallColor;
            int red, green, blue, addToColor;
            addToColor = (int) (side * 20 - wallDist * 10);

            // todo add colors and typs of walls
            if (map[mapY][mapX] == 1) {
                red = Math.max(0, Math.min(255, 60 + addToColor));
                green = Math.max(0, Math.min(255, 60 + addToColor));
                blue = Math.max(0, Math.min(255, 70 + addToColor));
            } else if (map[mapY][mapX] == 2) {
                red = Math.max(0, Math.min(255, 70 + addToColor));
                green = Math.max(0, Math.min(255, 60 + addToColor));
                blue = Math.max(0, Math.min(255, 60 + addToColor));
            } else {
                red = 0;
                green = 0;
                blue = 0;
            }
            wallColor = new Color(red, green, blue);
            g.setColor(wallColor);
            g.drawLine(i, drawStart, i, drawEnd);
        }

    }

    // {enemy print}
    public boolean paintSprites(Graphics g, Enemy sprite) {
        final double PLAYER_HEIGHT = 0.5;
        double spriteBase = 0.0;
        double spriteX = sprite.getX();
        double spriteY = sprite.getY();
        double spriteWidth = sprite.getWidth();
        double spriteHeight = sprite.getHeight();

        BufferedImage currentSpriteImage = sprite.getSprite();
        if (currentSpriteImage == null) {
            return false;
        }


        double playerMapX = player.getPosX() / CELL_HEIGHT;
        double playerMapY = player.getPosY() / CELL_HEIGHT;
        double relX = spriteX - playerMapX;
        double relY = spriteY - playerMapY;

        double playerDirX = Math.cos(player.getViewAngel());
        double playerDirY = -Math.sin(player.getViewAngel());

        double planeLength = Math.tan(player.getFOV() / 2);
        double playerPlaneX = -Math.sin(player.getViewAngel()) * planeLength;
        double playerPlaneY = -Math.cos(player.getViewAngel()) * planeLength;

        double invDet = 1.0 / ((playerPlaneX * playerDirY - playerDirX * playerPlaneY) + 0.000001);

        double transformX = invDet * (playerDirY * relX - playerDirX * relY);
        double transformY = invDet * (-playerPlaneY * relX + playerPlaneX * relY);


        if (transformY > 0.0001) {

            int newSpriteWidth = Math.abs((int) (spriteWidth * GPANEL_WIDTH / transformY));
            int newSpriteHeight = Math.abs((int) (spriteHeight * GPANEL_HEIGHT / transformY));

            int spriteScreenCenterX = (int) ((GPANEL_WIDTH / 2) * (1 + transformX / transformY));
            // Reminder to myself - the Y axis is reversed so - minus the player's height!!!
            int spriteScreenStartY = (int) (GPANEL_HEIGHT / 2 - ((spriteBase - PLAYER_HEIGHT) * GPANEL_HEIGHT) / transformY);

            int drawStartX = spriteScreenCenterX - newSpriteWidth / 2;
            int drawEndX = spriteScreenCenterX + newSpriteWidth / 2;

            // for the shooting...
            boolean hit = false;

            if (!sprite.isClose()) {
                for (int i = Math.max(0, drawStartX); i < Math.min(GPANEL_WIDTH, drawEndX); i++) {
                    if (zDepth != null && i >= 0 && i < zDepth.length && transformY < zDepth[i]) {
                        hit = true;
                        int drawFromY = (int) Math.max(0, spriteScreenStartY - newSpriteHeight);
                        int drawToY = Math.min(GPANEL_HEIGHT - 1, spriteScreenStartY);
                        int drawnHeight = drawToY - drawFromY;


                        if (drawnHeight > 0 && newSpriteWidth > 0) {
                            for (int y = drawFromY; y < drawToY; y++) {

                                int texY = (int) ((y - drawFromY) * (double) currentSpriteImage.getHeight() / drawnHeight);
                                if (texY < 0)
                                    texY = 0;
                                if (texY >= currentSpriteImage.getHeight()) {
                                    texY = currentSpriteImage.getHeight() - 1;
                                }

                                int texX = (int) ((i - drawStartX) * (double) currentSpriteImage.getWidth() / newSpriteWidth);
                                if (texX < 0)
                                    texX = 0;
                                if (texX >= currentSpriteImage.getWidth()) {
                                    texX = currentSpriteImage.getWidth() - 1;
                                }

                                int colorRGB = currentSpriteImage.getRGB(texX, texY);
                                Color color = new Color(colorRGB, true);
                                if (color.getAlpha() > 0) {
                                    g.setColor(color);
                                    g.drawLine(i, y, i, y);
                                }
                            }
                        }
                    }
                }
            }
            if (hit && GPANEL_WIDTH / 2 >= spriteScreenCenterX - newSpriteWidth / 2 && GPANEL_WIDTH / 2 <= spriteScreenCenterX + newSpriteWidth / 2) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void ShootOnEnemy() {
        if (!player.isShooting())
            return;
        for (Enemy enemy : enemies) {
            if (enemy.isOnTarget()) {
                this.sounds.zombieSound.playSound();
                enemy.kill();
                addKill();
            }
        }
        this.sounds.gunSound.playSound();
    }

    public void updateEnemies() {
        double playerMapX = player.getPosX() / CELL_HEIGHT;
        double playerMapY = player.getPosY() / CELL_HEIGHT;
        for (Enemy enemy : enemies) {
            if (!enemy.isDead()) {
                if (enemy.moveTowards(playerMapX, playerMapY, map)) {
                    player.gotHit();
                    this.playerGotHit = true;

                }
            }
        }
    }

    public static void addKill() {
        kills++;
    }

    public static void updateHealth(Player player) {
        health = player.getHealth();
    }

    private void loadImages(){
        try {
            this.bloodImg = ImageIO.read(new File("img/player/blood.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleFPS(long sleepNeed){
        if (sleepNeed > 0) {
            try {
                Thread.sleep(sleepNeed);
                //System.out.println("sleep: " + sleepNeed);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            System.out.println("drop!");
        }
    }

    // keys inputs
    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());

    }
    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }
    @Override
    public void actionPerformed(ActionEvent e) {


    }

}
