/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Message;

import Krypter.Hasher;

/**
 *
 * @author Jan Koschke
 */
public abstract class Message {

    private String benutzer, passwort;
    private boolean rec = false;
    private String hash;

    
    public Message(String benutzer, String passwort, boolean recieved) {
        this.benutzer = benutzer;
        this.passwort = Hasher.ToMD5(passwort);
        this.rec = recieved;
    }
    public Message(String input){
        
    }

    public abstract String makeMessage();

    public abstract void initFromString(String message);

    public boolean isRecieved() {
        return rec;
    }

    public void setRecieved(boolean rec) {
        this.rec = rec;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
    
    public String getBenutzer() {
        return benutzer;
    }

    public void setBenutzer(String benutzer) {
        this.benutzer = benutzer;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

}
