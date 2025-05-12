import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.File;
import java.io.IOException;

public class GameOverSound {
    public void playSound() {
        new Thread(() -> {
            try {
                File audioFile = new File("sounds/gameOverSound.wav");
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
                clip.start();

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("game over sound problem!");
            }
        }).start();
    }
}
