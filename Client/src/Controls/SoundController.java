/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controls;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

/**
 *
 * @author Jan Koschke
 */
public class SoundController {

    private Control cnt;
    private AudioInputStream food, gameover, move1, move2;
    private boolean wechsledich = true;
    private int z = 0;

    public SoundController(Control cnt) {
        this.cnt = cnt;
    }

    private AudioInputStream loadmove1() {
        AudioInputStream erg = null;
        try {
            erg = AudioSystem.getAudioInputStream(new File("resources//sound//move1.wav"));
        } catch (Exception ex) {

        }
        return erg;
    }

    private AudioInputStream loadmove2() {
        AudioInputStream erg = null;
        try {
            erg = AudioSystem.getAudioInputStream(new File("resources//sound//move2.wav"));
        } catch (Exception ex) {

        }
        return erg;
    }

    private AudioInputStream loadfood() {
        AudioInputStream erg = null;
        try {
            erg = AudioSystem.getAudioInputStream(new File("resources//sound//food.wav"));
        } catch (Exception ex) {

        }
        return erg;
    }

    private AudioInputStream loadgameover() {
        AudioInputStream erg = null;
        try {
            erg = AudioSystem.getAudioInputStream(new File("resources//sound//gameover.wav"));
        } catch (Exception ex) {

        }
        return erg;
    }

    public void move() {
        if (z >= 3) {
            playMove();
            z = 0;
        }
        z++;
    }

    private void playMove() {
        if (wechsledich) {
            playSound(loadmove1());
        } else {
            playSound(loadmove2());
        }
        wechsledich = !wechsledich;
    }

    public void playFood() {
        playSound(loadfood());
    }

    public void playGameOver() {
        playSound(loadgameover());
    }

    private void playSound(AudioInputStream ais) {
        if (cnt.getTon()) {
            try {

                AudioFormat af = ais.getFormat();
                int size = (int) (af.getFrameSize() * ais.getFrameLength());
                byte[] audio = new byte[size];
                DataLine.Info info = new DataLine.Info(Clip.class, af, size);
                ais.read(audio, 0, size);

                // for(int i=0; i < 32; i++) {
                Clip clip = (Clip) AudioSystem.getLine(info);
                clip.open(af, audio, 0, size);
                clip.start();

                // }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
