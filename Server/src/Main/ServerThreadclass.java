/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Main;

import java.io.File;
import java.net.Socket;

/**
 *
 * @author User
 */
public class ServerThreadclass {
    
    private ServerThread st=null;
    private Socket s;
    private SharedValue decK;
    public ServerThreadclass(Socket server_s, File primes, SharedValue decryptK, int baseSize){
        s = server_s;
        decK = decryptK;
        st = new ServerThread(server_s, primes, decryptK, baseSize);
        st.start();
    }
    public void interruptThread(){
        st.interrupt();
    }
    public ServerThread getSt() {
        return st;
    }

    public void setSt(ServerThread st) {
        this.st = st;
    }

    public Socket getS() {
        return s;
    }

    public void setS(Socket s) {
        this.s = s;
    }

    public SharedValue getDecK() {
        return decK;
    }

    public void setDecK(SharedValue decK) {
        this.decK = decK;
    }
}
