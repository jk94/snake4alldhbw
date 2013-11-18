package Standardpackage;

import Connection.Connect;
import MessagePackage.Enums.MessageType;
import MessagePackage.Message;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public void gameOver(String authkey) {
        //if (JOptionPane.showConfirmDialog(null, "Willst du deine Punkte an die Highscore übermitteln?", "Highscore", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == 0) {
        sendPunkte(authkey);

    }

    private void sendPunkte(String authkey) {
        Message m = new Message(MessageType.HIGHSCORE, authkey, false);
        m.addInt(Message.T_POINTS, getPunktezaehler());
        Connect c = new Connect("85.214.255.178", 9876, m);
        c.start();
    }
}
