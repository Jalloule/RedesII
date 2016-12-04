package Client;

public class test {

    public static void main(String[] args) {
        int myPrivateKey = 29;

        int myPublicKey = 1625;
        
        int clientPublicKey = 1625;

        int myN = 2881;

        RSA myRSA = new RSA(myPrivateKey, myPublicKey, myN);
        myRSA.setOtherRSA(clientPublicKey, myN);

        int base = 25;
        System.out.println("base " + 25);
        int encript = myRSA.binExp(base, myPrivateKey, myN);
        System.out.println(encript);

        int decript = myRSA.binExp(encript, myPublicKey, myN);
        System.out.println(decript);

        byte[] baseByte = new byte[1];
        baseByte[0] = 25;

        System.out.println("TEST");
        
        System.out.println(bytesToString(baseByte));
       //byte[] encrip = myRSA.encriptByteArrayWithPrivate(baseByte);
       byte[] encrip = myRSA.encriptByteArrayWithPrivate(baseByte);
        bytesToStringBlock2(encrip);
        System.out.println("");
        
        //byte[] decrip = myRSA.decriptByteArrayWithPublic(encrip);
        byte[] decrip = myRSA.decriptByteArrayWithPublic(encrip);
        System.out.println(bytesToString(decrip));
        
        
        System.out.println("TEST");
        
        

        byte[] encripted = myRSA.encriptByteArrayWithPrivate(baseByte);
        System.out.println(bytesToString(encripted));
        
        byte[] concatMessage = new byte[baseByte.length+encripted.length];
        for (int i = 0; i < baseByte.length; i++) {
            concatMessage[i] = baseByte[i];            
        }
        int j = 0;
        for (int i = baseByte.length; i < concatMessage.length; i++) {
            concatMessage[i] = encripted[j]; 
            j++;
        }
        
        System.out.println("concat Message"+bytesToString(concatMessage));
        byte[] concatEncripted = myRSA.encriptByteArrayWithOther(concatMessage);
        System.out.println("concat Message encripted"+bytesToString(concatEncripted));
        
        
        //--------------------------------------------

        byte[] concatDecripted = myRSA.decriptByteArray(concatEncripted);
        int size = concatDecripted.length/3;        
        System.out.println("\n"+bytesToString(concatDecripted ));
        
        byte[] originalMessage = new byte[size];
        byte[] encriptedMessage = new byte[size*2];
        
        for (int i = 0; i < size; i++) {
            originalMessage[i] =  concatDecripted[i];            
        }
        j = 0;
        for (int i = size; i < size*3; i++) {
            encriptedMessage[j] =  concatDecripted[i];
            j++;
        }
        
        System.out.println("\n"+bytesToString(originalMessage));
        System.out.println("\n"+bytesToString(encriptedMessage));
        
        byte[] decriptedMessage = myRSA.decriptByteArrayWithPublic(encriptedMessage);
        
        System.out.println("\n"+bytesToString(originalMessage));
        System.out.println("\n"+bytesToString(decriptedMessage));
        
        System.out.println(isByteArrayEqual(originalMessage,decriptedMessage));
        
               
        

    }
    private static boolean isByteArrayEqual(byte[]original, byte[]decripted){
        if(original.length!=decripted.length) return false;
        for (int i = 0; i < decripted.length; i++) {
            if(original[i]!=decripted[i])return false;            
        }
        
        return true;
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
            System.out.print(" "+bytesToInt(toShow));

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
