package Connection;

import MessagePackage.Message;
import java.math.BigInteger;
import java.net.*;
import java.security.SecureRandom;
import java.io.*;

public class ClientThread extends Thread {

    Socket connection;
    BufferedReader read_input;
    PrintWriter write_output;
    SecureRandom random;
    SharedValue sharedValue;
    private BigInteger key = null;
    private Message msg;

    public ClientThread(Socket con, SharedValue sharedValue, Message msg) {
        try {
            this.connection = con;
            System.out.println("Verbunden");
            this.read_input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            this.write_output = new PrintWriter(connection.getOutputStream(), true);
            this.random = new SecureRandom();
            this.sharedValue = sharedValue;
            this.msg = msg;
        } catch (Exception e) {

        }
    }

    public void run() {
        try {
            write_output.println("newKey");
            write_output.flush();
            BigInteger base = new BigInteger(read_input.readLine());
            BigInteger prime = new BigInteger(read_input.readLine());
            BigInteger result_server = new BigInteger(read_input.readLine());

            int exponent = getExponent(prime);
            write_output.println(calculateResulut(base, prime, exponent));
            write_output.flush();

            //System.out.println("Primzahl:" + prime.toString() + "\nBasis:" + base.toString() + "\nExponent:" + exponent + "\nResultServer:" + result_server);
            BigInteger encryptKey = calculateResulut(result_server, prime, exponent);
            System.out.println("Encryptkey:" + encryptKey.toString());
            sharedValue.setEncryptionKey(encryptKey);

        } catch (Exception e) {

        }

        
    }

    public Socket getSocket() {
        return connection;
    }

    public int getExponent(BigInteger prime) {
        int result = 10000;
        prime = prime.subtract(new BigInteger("-2"));
        while (result > 255
                && prime.compareTo(new BigInteger(String.valueOf(result))) == 1) {
            result = random.nextInt(255);

        }

        return result;
    }

    public BigInteger calculateResulut(BigInteger base, BigInteger prime, int exponent) {
        //System.out.println("Start Calculating");
        BigInteger result = base.pow(exponent);
        result = result.mod(prime);
        //System.out.println("finished");
        return result;

    }

    
}
