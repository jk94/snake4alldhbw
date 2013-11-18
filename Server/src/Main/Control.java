/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Database.DB_Connect;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author User
 */
public class Control {

    private ArrayList<ServerReader> ServerReaderList = new ArrayList<>();
    private final DB_Connect dbc;

    public Control() {
        dbc = new DB_Connect("localhost", "snakedhbw", "snakeserver", "jpWWI13A");
    }

    public void starten() {
        try {

            ServerSocket sSocket;
            File pz = new File("Primes.rtf");
            sSocket = new ServerSocket(9876);
            do {
                Socket socket = sSocket.accept();
                //if (pz.exists()) {
                ServerReader sr = new ServerReader(socket, ServerReaderList, dbc);
                ServerReaderList.add(sr);
                sr.start();
                //} else {
                //  System.err.println("Keine Primes vorhanden.. Bitte nachinstallieren!");
                //System.exit(0);
                //}
            } while (true);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
