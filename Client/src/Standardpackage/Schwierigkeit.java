package Standardpackage;

import Enums.EnumSchwierigkeit;
import javax.swing.Timer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jan Koschke
 */
public class Schwierigkeit {
    
    private Punkte pkt; private Timer tmr; private Enums.EnumSchwierigkeit diff;

    public Schwierigkeit(Punkte pkt, Timer tmr, EnumSchwierigkeit diff) {
        this.pkt = pkt;
        this.tmr = tmr;
        this.diff = diff;
    }
    
    public void zielGefressen(){
        tmr.setDelay(tmr.getInitialDelay()-pkt.getZielZaehler());
    }
    
}