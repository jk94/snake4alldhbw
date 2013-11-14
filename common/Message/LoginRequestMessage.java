/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Message;

import Krypter.Hasher;

/**
 *
 * @author User
 */
public class LoginRequestMessage extends Message{

    public LoginRequestMessage(String benutzer, String passwort, boolean recieved) {
        super(benutzer, passwort, recieved);
    }

    public LoginRequestMessage(String input){
        super(input);
        initFromString(input);
    }
    @Override
    public String makeMessage() {
        String erg;
        erg = "loginrequest:\"\"," + "benutzer:\"" + getBenutzer() + "\",passwort:\"" + getPasswort() + "\"";
        String erg2 = erg + ",\"" + Hasher.ToMD5(erg) + "\"";
        return erg2;
    }
    @Override
    public void initFromString(String message){
        String[] sp = message.split("\",");
        setBenutzer(sp[1].split(":\"")[1]);
        setPasswort(sp[2].split(":\"")[1]);
        setRecieved(true);
        setHash(sp[3].split(":\"")[1]);
    }
    
}
