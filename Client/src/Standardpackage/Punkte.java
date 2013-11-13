package Standardpackage;

import Connection.Connect;
import Connection.common.Message.HighscoreMessage;
import Connection.common.Message.Message;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jan Koschke
 */
public class Punkte {

    private int punktezaehler = 0;
    private int zielZaehler = 0;
    private Timer tmr;
    private int timecounter = 0;

    public void ZielGefressen() {
        TimerStop();
        zielZaehler++;
        punktezaehler += PunkteBerechnen();
        timecounter = 0;
        tmr = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                zaehleZeit();
            }
        });
        TimerStart();
    }

    public void TimerStart() {
        if (tmr != null) {
            tmr.start();
        }
    }

    public void TimerStop() {
        if (tmr != null) {
            tmr.stop();
        }
    }

    private void zaehleZeit() {
        timecounter++;
    }

    private int PunkteBerechnen() {
        int erg;
        int multiplikator = Integer.parseInt("" + (timecounter / 1000)) - 7;
        if (multiplikator >= 0) {
            multiplikator = 1;
        } else {
            multiplikator = multiplikator * -1;
        }
        erg = 1 + (zielZaehler * multiplikator);
        return erg;
    }

    public int getPunktezaehler() {
        return punktezaehler;
    }

    public int getZielZaehler() {
        return zielZaehler;
    }

    public void gameOver() {
        if (JOptionPane.showConfirmDialog(null, "Willst du deine Punkte an die Highscore übermitteln?", "Highscore", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == 0) {
            sendPunkte();
        } else {
            System.out.println(new HighscoreMessage("Jan", "12345",false, punktezaehler).makeMessage());
        }

    }

    private void sendPunkte() {

        Connect c = new Connect("127.0.0.1", 9876, new HighscoreMessage("jankoschkegooglemail.com", "ichmachemal ein ganz langes passwort damit der string auch schön lange wird",false, punktezaehler));
        c.start();
    }
}
