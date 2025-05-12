public class test {
    public static void main(String[] args) throws InterruptedException {
        BackGroundMusic bg = new BackGroundMusic();
        bg.playSound();
        for (int i = 0; i < 100; i++) {
//            GunSound gunSound = new GunSound();
//            gunSound.playSound();
            Sounds sounds = new Sounds();
            sounds.gunSound.playSound();
            Thread.sleep(1000);
            Thread.sleep(1000);
        }






        Thread.sleep(1000);
    }
}
