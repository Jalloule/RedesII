package Client;

public class RSA {

    public RSA() {
    }

    public byte encriptByte(byte b, int key) {

        return b;
    }

    public byte decriptByte(byte b, int key) {

        return b;
    }

    public byte[] encriptByteArray(byte[] bArray, int key) {
        byte[] bArrayEncripted = new byte[(int) bArray.length];
        for (int i = 0; i < bArray.length; i++) {
            bArrayEncripted[i] = encriptByte(bArray[i], key);
        }

        return bArrayEncripted;
    }

    public byte[] decriptByteArray(byte[] bArray, int key) {
        byte[] bArrayDecripted = new byte[(int) bArray.length];
        for (int i = 0; i < bArray.length; i++) {
            bArrayDecripted[i] = decriptByte(bArray[i], key);
        }

        return bArrayDecripted;
    }

}
