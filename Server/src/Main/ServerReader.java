     /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Database.DB_Connect;
import Database.DB_Getter_Operations;
import Database.DB_Setter_Operations;
import Krypter.Hasher;
import Krypter.Krypt;
import MessagePackage.Enums.MessageType;
import MessagePackage.Message;
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
    private Krypt crypt;

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
            crypt = new Krypt(aesKey, "AES");
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
                    if (input.startsWith(Message.T_TYPE + ":")) {
                        //Message erstellen.
                        System.out.println("Lese Message...");
                        System.out.println(input);
                        Message m = new Message(input);
                        System.out.println("Message: ");
                        System.out.println("Benuter: " + m.getString(Message.T_BENUTZER));
                        System.out.println("PW: " + m.getString(Message.T_HASHEDPW));
                        System.out.println("Punkte:" + m.getString(Message.T_POINTS));
                        if (!doEvent(m)) {
                            input = "";
                            break;
                        }
                    } else {

                    }
                } else {
                    input = input + "\n";
                    breaks++;
                }
            }
            System.out.println("Schreibe fertig...");
            Message cs = new Message(MessageType.CLOSESESSION, "", false);
            String returnmsg = crypt.encrypt(cs.getMessageNHash());

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

    public boolean doEvent(Message msg) {
        if (isUnveraendert(msg)) {
            switch (msg.getMessageType()) {
                case HIGHSCORE:
                    System.out.println("Highscore..");
                    if (isUserValid(msg)) {
                        if (msg.getString(Message.T_AUTHKEY).equals("") == false) {
                            DB_Setter_Operations.submitHighscore(dbc, DB_Getter_Operations.getUserID(dbc, msg.getString(Message.T_AUTHKEY)), msg.getInt(Message.T_POINTS));
                        } else {
                            //Authkey schicken/Anforderung eines Loginrequests
                            System.out.println("Kein Authkey vorhanden...");
                        }
                    }
                case AUTHREQUEST:
                    System.out.println("Authrequest");
                    try {
                        PrintWriter pw = new PrintWriter(socket.getOutputStream());
                        Message me = new Message(MessageType.AUTHRESPONSE, "", "", false);
                        pw.println(crypt.encrypt(me.getMessageNHash()));
                        pw.flush();
                        return true;
                    } catch (Exception ex) {

                    }
                    return true;
            }
        }
        return false;
    }

    private boolean isUnveraendert(Message msg) {
        boolean erg = false;
        String check = Hasher.ToMD5(msg.getMessage());
        if (check.equals(msg.getString(Message.T_HASH))) {
            erg = true;
        }
        return erg;
    }

    private boolean isUserValid(Message msg) {
        return DB_Getter_Operations.isUserValid(dbc, msg.getString(Message.T_BENUTZER), msg.getString(Message.T_HASH));
    }

}
