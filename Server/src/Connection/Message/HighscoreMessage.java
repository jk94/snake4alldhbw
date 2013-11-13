/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection.Message;

import Connection.Krypter.Hasher;

/**
 *
 * @author User
 */
public class HighscoreMessage extends Message {

    private int punkte;

    public HighscoreMessage(String benutzer, String passwort, boolean recieved, int punkte) {
        super(benutzer, passwort, recieved);
        this.punkte = punkte;
    }
    
    public HighscoreMessage(String input){
        super(input);
    }

    @Override
    public String makeMessage() {
        String erg = "";
        erg = "benutzer:\"" + getBenutzer() + "\",passwort:\"" + getPasswort() + "\",punkte:\"" + punkte + "\"";
        String erg2 = erg + ",\"" + Hasher.ToMD5(erg) + "\"";
        return erg2;
    }

    public int getPunkte() {
        return punkte;
    }

    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }

    @Override
    public void initFromString(String message) {
        String[] sp = message.split("\",");
        setBenutzer(sp[0].split(":\"")[1]);
        setPasswort(sp[1].split(":\"")[1]);
        setPunkte(Integer.parseInt(sp[2].split(":\"")[1]));
        setRecieved(true);
        setHash(sp[3].split(":\"")[1]);
    }
}
