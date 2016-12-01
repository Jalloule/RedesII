package Client;

import java.math.BigInteger;

public class RSA {

    private BigInteger privateKey;
    private BigInteger publicKey;
    private BigInteger N;

    public RSA(BigInteger myPrivateKey, BigInteger myPublicKey, BigInteger myN) {
        this.privateKey = myPrivateKey;
        this.publicKey = myPublicKey;
        this.N = myN;
    }

    //when use RSA to encript or decript with others key, the key you have 
    //will be use to either decript or encript
    public RSA(BigInteger otherN, BigInteger otherPublicKey) {
        this.N = otherN;
        this.privateKey = otherPublicKey;
        this.publicKey = otherPublicKey;
    }

    //receives one byte and returns 2 bytes representing the int already encripted
    public byte[] encriptByte(byte b) {

        byte[] twoBytes;
        int i = Byte.toUnsignedInt(b);
        i = binExp(i, privateKey.intValueExact(), N.intValueExact());
        twoBytes = intToBytes(i, 2);
        return twoBytes;

    }

    //receives a byte pait that representes a encripted int to decript
    //returns that int decripted in one byte
    public byte decriptBytePair(byte b[]) {

        int i = bytesToInt(b);
        i = binExp(i, privateKey.intValueExact(), N.intValueExact());

        byte[] bDecripted = intToBytes(i, 1);

        return bDecripted[0];
    }

    //returns an array with double size with each 2 bytes representing a encripted int
    public byte[] encriptByteArray(byte[] bArray) {

        //return (new BigInteger(bArray)).modPow(privateKey, N).toByteArray(); 
        byte[] bArrayEncripted = new byte[(int) bArray.length * 2];
        int j = 0;
        for (int i = 0; i < bArray.length; i++) {
            bArrayEncripted[j] = encriptByte(bArray[i])[0];
            bArrayEncripted[j + 1] = encriptByte(bArray[i])[1];
            j += 2;

        }

        return bArrayEncripted;
    }

    //gets a double size byte array,  reads in blocls of 2 bytes, each 2 bytes is an encripted int
    //saves the decripted ints in bytes in an array with half size
    public byte[] decriptByteArray(byte[] bArray) {

        byte[] bArrayDecripted = new byte[(int) bArray.length / 2];
        byte[] toSend = new byte[2];
        int j = 0;
        for (int i = 0; i < bArray.length; i += 2) {
            toSend[0] = bArray[i];
            toSend[1] = bArray[i + 1];
            bArrayDecripted[j] = decriptBytePair(toSend);
            j++;
        }

        return bArrayDecripted;
    }

    public int binExp(int b, int e, int n) {
        int resp = b;

        return resp;

    }

    public int binExpModPow(int b, int e, int n) {
        int resp = b;
        BigInteger bresp, bb, ee, nn;

        bb = BigInteger.valueOf(b);
        ee = BigInteger.valueOf(e);
        nn = BigInteger.valueOf(n);

        // perform modPow operation on bi1 using bi2 and exp
        bresp = bb.modPow(ee, nn);
        resp = bresp.intValue();
        return resp;

    }

    //represents an int in how many bytes I want
    public static byte[] intToBytes(int x, int n) {
        byte[] bytes = new byte[n];
        for (int i = 0; i < n; i++, x >>>= 8) {
            bytes[i] = (byte) (x & 0xFF);
        }
        return bytes;
    }

    //goes back from bytes to int
    public static int bytesToInt(byte[] x) {
        int value = 0;
        for (int i = 0; i < x.length; i++) {
            value += ((long) x[i] & 0xffL) << (8 * i);
        }
        return value;
    }

}
