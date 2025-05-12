import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class BackGroundMusic {
    private Clip backgroundClip;
    private AudioInputStream audioStream;


    public void playSound() {
        new Thread(() -> {
            try {
                File audioFile = new File("sounds/music1.wav");
                this.audioStream = AudioSystem.getAudioInputStream(audioFile);
                this.backgroundClip = AudioSystem.getClip();
                this.backgroundClip.open(this.audioStream);

                if (backgroundClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gainControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(-5);
                } else {
                    System.err.println("Master Gain control is not supported for this audio format/clip.");
                }
                this.backgroundClip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {

                    } else if (event.getType() == LineEvent.Type.START) {

                    }
                });

                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("background music sound problem!");
            }
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
        }).start();
    }


    public void stopSound() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
        }
        if (audioStream != null) {
            try {
                audioStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
