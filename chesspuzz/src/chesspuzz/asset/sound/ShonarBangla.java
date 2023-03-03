package chesspuzz.asset.sound;

import database.helper.Console;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

import static javax.sound.sampled.Clip.LOOP_CONTINUOUSLY;

public class ShonarBangla {

    private Thread thread;
    private Runnable runnable;
    private MediaPlayer mediaPlayer;
    private Media media;

    public ShonarBangla(int loop) {
        String filePath = "src/app/asset/sound/amar_shonar_bangla.wav";

        try {
            // create AudioInputStream object
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

            // create clip reference
            Clip clip = AudioSystem.getClip();

            // open audioInputStream to the clip
            clip.open(audioInputStream);

            loop = loop == 0 ? LOOP_CONTINUOUSLY : loop;
            clip.loop(loop);
            clip.start();
        } catch (Exception e) {
            Console.log(ShonarBangla.class, e.getMessage());
        }
    }
}
