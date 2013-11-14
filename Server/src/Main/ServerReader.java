     /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Database.DB_Connect;
import Database.DB_Getter_Operations;
import Krypter.Hasher;
import Krypter.Krypt;
import Message.HighscoreMessage;
import Message.LoginRequestMessage;
import Message.Message;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.Key;
import java.util.ArrayList;
import javax.crypto.KeyGenerator;

/**
 *
 * @author User
 */
public class ServerReader extends Thread {

    private Socket socket;
    private ArrayList<ServerReader> lsr;
    private DB_Connect dbc;

    public ServerReader(Socket socket, ArrayList<ServerReader> lsr, DB_Connect dbc) {
        this.socket = socket;
        this.lsr = lsr;
        this.dbc = dbc;
    }

    public void run() {
        try {
            System.out.println("Erstelle Key...");
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(128);
            Key aesKey = keygen.generateKey();
            byte[] bytes = aesKey.getEncoded();
            System.out.println("Key erstellt...");

            OutputStream op = socket.getOutputStream();
            OutputStreamWriter keyfos = new OutputStreamWriter(op);
            PrintWriter pw = new PrintWriter(op);

            System.out.println("Sende Key...");
            op.write(bytes);

            op.flush();
            pw.write("\n");

            pw.flush();
            System.out.println("Key gesendet...");
            Krypt crypt = new Krypt(aesKey, "AES");
            BufferedReader read_input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Reader geöffnet...");

            String input = read_input.readLine();
            int leng = Integer.parseInt(input);
            input = "";
            int breaks = 0;
            pw = new PrintWriter(op);
            while (true) {
                input = input + read_input.readLine();
                if (input.length() >= leng - breaks) {

                    input = crypt.decrypt(input);
                    if (input != null) {
                        if (input.startsWith("benutzer:")) {
                            //Message erstellen.
                            System.out.println("Lese Message...");
                            System.out.println(input);
                            HighscoreMessage m = new HighscoreMessage(input);
                            System.out.println("Message: ");
                            System.out.println("Benuter: " + m.getBenutzer());
                            System.out.println("PW: " + m.getPasswort());
                            System.out.println("Punkte:" + m.getPunkte());
                            if (!doEvent(0, m)) {
                                break;
                            }
                        } else {
                            if (input.equals("loginrequest:")) {
                                LoginRequestMessage m = new LoginRequestMessage(input);
                                if (!doEvent(0, m)) {
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    input = input + "\n";
                    breaks++;
                }
            }
            System.out.println("Schreibe fertig...");

            String returnmsg = crypt.encrypt("ok");

            pw.println(returnmsg.length());
            pw.println(returnmsg);
            pw.flush();
            pw.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            pw.flush();
            keyfos.close();
            read_input.close();
            pw.close();
            socket.close();
            System.out.println("Connection geschlossen");
            lsr.remove(this);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

    }

    public boolean doEvent(int type, Message msg) {
        if (isUnveraendert(msg)) {
            switch (type) {
                case 0:
                    System.out.println("Highscore..");
                    if (DB_Getter_Operations.isUserValid(dbc, msg.getBenutzer(), msg.getPasswort())) {
                        break;
                    }
                case 1:
                    System.out.println("Loginrequest");
                    break;
            }
        }
        return false;
    }

    private boolean isUnveraendert(int type, Message msg) {
        boolean erg = false;
        String hashit = "benutzer:\"" + msg.getBenutzer() + "\",passwort:\"" + msg.getPasswort() + "\",punkte:\"" /* + msg.getPunkte()*/ + "\"";
        String check = Hasher.ToMD5(hashit);
        switch (type) {
            case 0:
                System.out.println("Highscore..");
                if (DB_Getter_Operations.isUserValid(dbc, msg.getBenutzer(), msg.getPasswort())) {
                    break;
                }
            case 1:
                System.out.println("Loginrequest");
                break;
        }

        if (check.equals(msg.getHash())) {
            erg = true;
        }

        return erg;
    }

}
