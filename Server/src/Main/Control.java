/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Krypter.Hasher;
import Database.DB_Connect;
import Message.Message;
import java.io.File;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author User
 */
public class Control {

    private ArrayList<ServerReader> ServerReaderList = new ArrayList<>();
    
    private Keychecker kc;
    private BigInteger primezahlen[];
    private DB_Connect dbc;

    public Control() {
        dbc = new DB_Connect("localhost", "snakedhbw", "snake", "jpWWI13");
        /*Message testmsg = new Message("test@test.de", Connection.Krypter.Hasher.ToMD5("12345"), 10, "");
        String hashit = "benutzer:\"" + testmsg.getBenutzer() + "\",passwort:\"" + testmsg.getPasswort() + "\",punkte:\"" + testmsg.getPunkte() + "\"";
        testmsg.setHash(hashit);
        System.out.println("TestMessage erstellt");
        byte[] cryptmsg = AES.encrypt(testmsg.getString().getBytes(), "123456".getBytes());
        System.out.println("TestMessage verschlüsselt");
        //System.out.println(DB_Getter_Operations.isUserValid(dbc, "test@test.de", Connection.Krypter.Hasher.ToMD5("12345")));
        //byte[] b = AES.encrypt("benutzer:\"Jan\",passwort:\"827ccb0eea8a706c4c34a16891f84e7b\",punkte:\"8\",\"8bd77a9987bbc56bf2181928d2db068d\"".getBytes(), "1234567".getBytes());
        //decryptMessage(b, BigInteger.valueOf(1234567));

        //Empfangener String entschlüsseln
        Message msg = decryptMessage(cryptmsg,"123456");
        //Überprüfe auf Unverändert
        if (isUnveraendert(msg)) {
            if (DB_Getter_Operations.isUserValid(dbc, msg.getBenutzer(), msg.getPasswort())) {
                int userid = DB_Getter_Operations.getUserID(dbc, msg.getBenutzer(), msg.getPasswort());
                DB_Setter_Operations.submitHighscore(dbc, userid, msg.getPunkte());
            }
        }*/

    }

    public void starten() {
        try {
            
            ServerSocket sSocket;
            File pz = new File("Primes.rtf");
            sSocket = new ServerSocket(9876);
            do {
                Socket socket = sSocket.accept();

                System.out.println(pz.getAbsolutePath());
                if (pz.exists()) {
                    /*SharedValue decrypt_key = new SharedValue();
                    ServerThreadclass stc = new ServerThreadclass(socket, pz, decrypt_key, 3);
                    KeyAustauschListe.add(stc);*/
                    ServerReader sr = new ServerReader(socket, ServerReaderList);
                    ServerReaderList.add(sr);
                    sr.start();
                } else {
                    System.err.println("Keine Primes vorhanden.. Bitte nachinstallieren!");
                    System.exit(0);
                }
            } while (true);

            //System.out.println("Decrypt:" + key.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*public Message decryptMessage(byte[] input, String key) {
        Message erg;
        //System.out.println(input);
        byte[] entschluesselt = AES.decrypt(input, key.getBytes());
        String  ent = new String(entschluesselt);
        System.out.println(ent);
        //String[] sp = entschluesselt.split("\",");

        //erg = new Message(sp[0].split(":\"")[1], sp[1].split(":\"")[1], Integer.parseInt(sp[2].split(":\"")[1]), sp[3].split("\"")[0]);

        //return erg;
    }*/

    private boolean isUnveraendert(Message msg) {
        boolean erg = false;
        String hashit = "benutzer:\"" + msg.getBenutzer() + "\",passwort:\"" + msg.getPasswort() + "\",punkte:\"" /* + msg.getPunkte()*/ + "\"";
        String check = Hasher.ToMD5(hashit);
        if (check.equals(msg.getHash())) {
            erg = true;
        }

        return erg;
    }

}
