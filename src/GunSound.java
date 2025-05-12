import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


public class GunSound {
    public void playSound() {
        new Thread(() -> {
            try {
                File audioFile = new File("sounds/shotSound.wav");
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
                System.err.println("gun sound problem!");
            }
        }).start();
    }
}
