package Client;

public class test {

    public static void main(String[] args) {
        int myPrivateKey = 29;

        int myPublicKey = 1625;

        int myN = 2881;

        RSA myRSA = new RSA(myPrivateKey, myPublicKey, myN);

        int base = 25;
        System.out.println("base " + 25);
        int encript = myRSA.binExp(base, myPublicKey, myN);
        System.out.println(encript);

        int decript = myRSA.binExp(encript, myPrivateKey, myN);
        System.out.println(decript);

        byte[] baseByte = new byte[4];
        baseByte[0] = 25;
        baseByte[1] = 25;
        baseByte[2] = 25;
        baseByte[3] = 25;
        System.out.println(bytesToString(baseByte));

        byte[] encripted = myRSA.encriptByteArray(baseByte);
        bytesToStringBlock2(encripted);

        byte[] decripted = myRSA.decriptByteArray(encripted);
        System.out.println(bytesToString(decripted));

    }

    private static String bytesToString(byte[] e) {
        String test = "";
        for (byte b : e) {
            test += " " + Byte.toString(b);
        }
        return test;
    }

    private static void bytesToStringBlock2(byte[] e) {

        for (int i = 0; i < e.length; i += 2) {
            byte toShow[] = new byte[2];
            toShow[0] = e[i];
            toShow[1] = e[i + 1];
            System.out.println(bytesToInt(toShow));

        }

    }

    public static int bytesToInt(byte[] x) {
        int value = 0;
        for (int i = 0; i < x.length; i++) {
            value += ((long) x[i] & 0xffL) << (8 * i);
        }
        return value;
    }

}
