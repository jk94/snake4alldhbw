package Main;

import java.io.*;
import java.net.*;
import java.security.SecureRandom;
import java.math.*;

public class ServerThread extends Thread {

    Socket connection;
    BufferedReader read_input;
    PrintWriter write_output;
    SecureRandom random;
    BufferedReader primeReader;
    String[] primZahlen;
    SharedValue decryptK;
    int baseSize;

    public ServerThread(Socket server_s, File primes, SharedValue decryptK, int baseSize) {
        try {
            this.baseSize = baseSize;

            this.connection = server_s;
            System.out.println("Client Connected");
            this.read_input = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            this.write_output = new PrintWriter(connection.getOutputStream(), true);
            this.random = new SecureRandom();
            this.primeReader = new BufferedReader(new FileReader(primes));

            this.decryptK = decryptK;
            int length = 0;

            while (this.primeReader.readLine() != null) {
                length++;
            }
            this.primeReader.close();

            this.primeReader = new BufferedReader(new FileReader(primes));
            primZahlen = new String[length];

            for (int i = 0; i < primZahlen.length; i++) {
                primZahlen[i] = primeReader.readLine();
            }
            this.primeReader.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            String content = read_input.readLine();
            System.out.println(content);
            if (content.equals("newKey")) {

                BigInteger prime = getPrime();
                BigInteger basis = getBase(prime);

                int exponent = getExponent(prime);
                System.out.println("Primzahl:" + prime.toString() + "\nBase:" + basis.toString() + "\nExponent:" + exponent);

                write_output.println(basis.toString());

                write_output.println(prime.toString());

                write_output.println(calculateResulut(basis, prime,
                        exponent).toString());

                write_output.flush();

                BigInteger resultClient = new BigInteger(
                        read_input.readLine());

                System.out.println("Result:Client" + resultClient.toString());
                BigInteger decryptKey = calculateResulut(resultClient,
                        prime, exponent);

                System.out.println("DecryptionKey:" + decryptKey.toString());
                decryptK.setDecryptionKey(decryptKey);
                //write_output.close();
                //read_input.close();

            }

        } catch (Exception e) {
            System.out.println(e);
        }
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

    public BigInteger calculateResulut(BigInteger base, BigInteger prime,
            int exponent) {
        System.out.println("Start Calculating");
        BigInteger result = base.pow(exponent);
        result = result.mod(prime);
        System.out.println("finished");
        return result;

    }

    public BigInteger getBase(BigInteger prime) {
        prime.subtract(new BigInteger("-2"));
        BigInteger base_bInt;
        while (true) {
            byte[] base = new byte[baseSize];

            for (int i = 0; i < base.length; i++) {
                base[i] = (byte) random.nextInt(255);
            }
            base_bInt = new BigInteger(base);
            base_bInt = base_bInt.negate();

            if (prime.compareTo(base_bInt) == 1 && base_bInt.compareTo(new BigInteger("0")) == 1) {
                break;
            }
        }

        return base_bInt;

    }

    public BigInteger getPrime() {
        int number = random.nextInt(primZahlen.length);

        return new BigInteger("" + primZahlen[number]);
        //return new BigInteger("15485863");
    }
}
