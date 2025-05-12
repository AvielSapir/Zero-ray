import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class ZombieSound {

    private String path;

    ZombieSound() {
        this.path = "sounds/zombieDead1.wav";
    }

    public void playSound() {
        new Thread(() -> {
            try {
                Thread.sleep(120);
                File audioFile = new File(this.path);
                AudioInputStream sound = AudioSystem.getAudioInputStream(audioFile);
                Clip clip = AudioSystem.getClip();
                clip.open(sound);

                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                        try {
                            sound.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(-3);
                clip.start();

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("zombie sound problem!");
            }
        }).start();
    }
}
