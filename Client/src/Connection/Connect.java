/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import Krypter.Hasher;
import Krypter.Krypt;
import MessagePackage.Enums.MessageType;
import MessagePackage.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Jan Koschke
 */
/**
 *
 * @author Jan Koschke
 */
public class Connect extends Thread {

    private Message msg;
    private int port;
    private String ip;
    private boolean waitforrequest = false;
    private Socket socket = null;
    Krypter.Krypt crypt;

    public Connect(String ip, int port, Message msg) {
        //neueVerbindung("localhost", 9876);
        this.port = port;
        this.ip = ip;
        this.msg = msg;
        this.waitforrequest = true;
    }

    public Connect(String ip, int port, Message msg, Connect co) {
        this.port = port;
        this.ip = ip;
        this.msg = msg;
        this.waitforrequest = false;
    }

    public void run() {

        if (msg.getMessageType() != MessageType.AUTHREQUEST && msg.getString(Message.T_AUTHKEY).equals("")) {
            System.out.println(msg.getMessage());
            Connect c = new Connect(ip, port, new Message(MessageType.AUTHREQUEST, msg.getString(Message.T_BENUTZER), msg.getString(Message.T_HASHEDPW), false));
            c.start();
        } else {
            waitforrequest = false;
        }

        while (waitforrequest) {

        }

        try {

            socket = new Socket(ip, port);
        } catch (IOException ex) {

        }
        if (socket != null) {

            try {
                InputStream fis = socket.getInputStream();
                BufferedReader read_input;

                byte[] encodedKey = new byte[16];

                fis.read(encodedKey);
                System.out.println("Key gelesen...");

                // generiere Key
                SecretKey aesKey = new SecretKeySpec(encodedKey, "AES");

                System.out.println("Key erstellt...");
                crypt = new Krypt(aesKey, "AES");

                PrintWriter write_output = new PrintWriter(socket.getOutputStream());
                System.out.println("Ausgabe geöffnet...");
                read_input = new BufferedReader(new InputStreamReader(fis));
                System.out.println("Eingabe geöffnet...");
                System.out.println(msg.getMessageNHash());
                String vermsg = crypt.encrypt(msg.getMessageNHash());

                write_output.println(vermsg.length());
                write_output.println(vermsg);
                write_output.flush();
                System.out.println("Message geschickt...");
                read_input.readLine();
                System.out.println("Input lesen...");

                String input;
                boolean exit = false;

                while (!exit) {
                    int breaks = 0;

                    input = read_input.readLine();
                    int leng = Integer.parseInt(input);
                    input = "";

                    while (true) {
                        input = input + read_input.readLine();
                        if (input.length() >= leng - breaks) {

                            input = crypt.decrypt(input);

                            Message m = new Message(input);
                            if (m.getMessageType().equals(MessageType.CLOSESESSION)) {
                                break;
                            } else {
                                if (!doEvent(m)) {
                                    exit = true;
                                    break;
                                } else {
                                    break;
                                }
                            }

                        } else {
                            input = input + "\n";
                            breaks++;
                        }
                    }
                }
                write_output.close();
                read_input.close();
                socket.close();
                System.out.println("Connection closed");
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }

        }
    }

    public void setAuthKeyFromRequest(String authkey) {
        msg.setString(Message.T_AUTHKEY, authkey);
        Controls.Control.setAuthKey(authkey);
        waitforrequest = false;
    }

    public boolean doEvent(Message msg) {
        if (isUnveraendert(msg)) {
            switch (msg.getMessageType()) {
                case NOAUTH:
                    System.out.println("NoAuth..");
                    try {
                        PrintWriter pw = new PrintWriter(socket.getOutputStream());
                        Message me = new Message(MessageType.AUTHREQUEST, Controls.Control.getAnmeldedaten()[0], Controls.Control.getAnmeldedaten()[1], false);
                        pw.println(crypt.encrypt(me.getMessageNHash()));
                        pw.flush();
                        return true;
                    } catch (Exception ex) {

                    }
                    break;
                case AUTHRESPONSE:
                    System.out.println("AuthResponse: " + msg.getString(Message.T_AUTHKEY) +" ...");
                    Controls.Control.setAuthKey(msg.getString(Message.T_AUTHKEY));
                    break;
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

}
