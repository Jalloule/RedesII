package Client;

import java.math.BigInteger;

public class RSAold {

    private BigInteger privateKey;
    private BigInteger publicKey;
    private BigInteger N;

    public RSAold(BigInteger myPrivateKey, BigInteger myPublicKey, BigInteger myN) {
        this.privateKey = myPrivateKey;
        this.publicKey = myPublicKey;
        this.N = myN;
    }

    //when use RSA to encript or decript with others key, the key you have 
    //will be use to either decript or encript
    public RSAold(BigInteger otherN, BigInteger otherPublicKey) {
        this.N = otherN;
        this.privateKey = otherPublicKey;
        this.publicKey = otherPublicKey;
    }

    public byte encriptByte(byte b) {

        return binExp(b, privateKey.intValueExact(), N.intValueExact());

    }

    public byte decriptByte(byte b) {

        //return (new BigInteger(ba)).modPow(publicKey, N).byteValue();
        return binExp(b, publicKey.intValueExact(), N.intValueExact());

    }

    //Deve encriptar em blocos de 1
    public byte[] encriptByteArray(byte[] bArray) {

        //return (new BigInteger(bArray)).modPow(privateKey, N).toByteArray(); 
        byte[] bArrayEncripted = new byte[(int) bArray.length];
        for (int i = 0; i < bArray.length; i++) {
            bArrayEncripted[i] = encriptByte(bArray[i]);
        }

        return bArrayEncripted;
    }

    //deve decriptar em blocos de 2 bytes, pois quando foi encriptado ele dobrou
    public byte[] decriptByteArray(byte[] bArray) {

        //return (new BigInteger(bArray)).modPow(publicKey, N).toByteArray(); 
        byte[] bArrayDecripted = new byte[(int) bArray.length];
        for (int i = 0; i < bArray.length; i++) {
            bArrayDecripted[i] = decriptByte(bArray[i]);
        }

        return bArrayDecripted;
    }

    public byte binExp(byte b, int e, int n) {
        byte resp = b;
        return resp;

    }

}
