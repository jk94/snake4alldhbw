/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Connection.Krypter.AES;
import Connection.Krypter.Krypt;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Jan Koschke
 */
public class ServerThreadReader extends Thread {

    Socket s;
    BigInteger decKey;

    public ServerThreadReader(Socket socket, BigInteger decryptionKey) {
        this.s = socket;
        this.decKey = decryptionKey;
    }

    @Override
    public void run() {
        try {
            String thekey = decKey.toString();
            while(thekey.getBytes().length<32){
                thekey = thekey + thekey;
            }
            
            Key k = new SecretKeySpec(thekey.getBytes(), "AES");
            Krypt crypt = new Krypt(k, "AES");
            
            BufferedReader bfr = new BufferedReader(new InputStreamReader(crypt.decryptInputStream(s.getInputStream())));
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            String input = "";
            while (true) {

                input = bfr.readLine();
                if (input != null) {
                    System.out.println(input);
                    byte[] entschluesselt = AES.decrypt(input.getBytes(), decKey.toByteArray());
                    String ent = new String(entschluesselt);
                    System.out.println(ent);
                    // TODO BEFEHL EINLESEN!!
                    break;
                }
            }
            pw.println(ByteArrayToString(AES.encrypt("ok".getBytes(), decKey.toByteArray())));
            pw.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String ByteArrayToString(byte[] bt) {
        return new String(bt);
    }

}
