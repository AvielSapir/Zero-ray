import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class EnemySpawner {

    private int[][] map;
    private int playerX;
    private int playerY;

    private CopyOnWriteArrayList<Enemy> enemies;

   public  EnemySpawner(int[][] map, int playerX, int playerY, CopyOnWriteArrayList<Enemy> enemies) {
        this.map = map;
        this.playerX = playerX;
        this.playerY = playerY;
        this.enemies = enemies;
   }

   public void spawnEnemies(int num) {
       for (int j = 0; j < num; j++) {
           for (int i = 0; i < 100; i++) {
               int x = new Random().nextInt(this.map.length);
               int y = new Random().nextInt(this.map[0].length);
               if (this.map[x][y] == 0){
                   if (Math.abs(x - playerX) >= 5 && Math.abs(y - playerY) >= 5) {
                       this.enemies.add(new Enemy(x, y));
                       break;
                   }
               }
           }
       }

   }


}
