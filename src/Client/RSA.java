package Client;

import static Client.test.bytesToInt;


public class RSA {
    
    private int realPrivateKey;
    private int realPublicKey;
    private int realN;
    
    private int privateKey;
    private int publicKey;
    private int N;
    
    private int otherPublicKey;
    private int otherN;

    public RSA(int myPrivateKey, int myPublicKey, int myN) {
        this.privateKey = this.realPrivateKey = myPrivateKey;
        this.publicKey  = this.realPublicKey = myPublicKey;
        this.N = this.realN= myN;
    }

    //when use RSA to encript or decript with others key, the key you have 
    //will be use to either decript or encript
    public void setOtherRSA(int otherPublicKey, int otherN) {
      this.otherN = otherN;
      this.otherPublicKey = otherPublicKey;
    }

    //receives one byte and returns 2 bytes representing the int already encripted
    public byte[] encriptByte(byte b) {
        //
        byte[] oneByte = new byte[1];
        oneByte[0] = b;
        //
        byte[] twoBytes;
        //int i = Byte.toUnsignedInt(b);
        int i = bytesToInt(oneByte);
        i = binExp(i, publicKey, N);
        twoBytes = intToBytes(i, 2);
        return twoBytes;

    }
    


    //receives a byte pait that representes a encripted int to decript
    //returns that int decripted in one byte
    public byte decriptBytePair(byte b[]) {

        int i = bytesToInt(b);
       

        i = binExp(i, privateKey, N);

        byte[] bDecripted = intToBytes(i, 1);
        

        return bDecripted[0];

    }

    //returns an array with double size with each 2 bytes representing a encripted int
    public byte[] encriptByteArray(byte[] bArray) {

        byte[] bArrayEncripted = new byte[(int) bArray.length * 2];
        int j = 0;
        for (int i = 0; i < bArray.length; i++) {
            bArrayEncripted[j] = encriptByte(bArray[i])[0];
            bArrayEncripted[j + 1] = encriptByte(bArray[i])[1];
            j += 2;

        }

        return bArrayEncripted;
    }
    
    public byte[] encriptByteArrayWithOther(byte[] bArray) {
        byte[]resp;
        useOtherKeys();
        resp = encriptByteArray(bArray);
        defaultKeys();
        return resp;   
        
       
    }
    
     public byte[] encriptByteArrayWithPrivate(byte[] bArray) {
        byte[]resp;
        invertKeys();
        resp = encriptByteArray(bArray);
        defaultKeys();
        return resp;   
        
       
    }
     
    public byte[] decriptByteArrayWithPublic(byte[] bArray) {
        byte[]resp;
        invertKeys();
        resp = decriptByteArray(bArray);
        defaultKeys();
        return resp;   
        
       
    } 
     
     public byte[] decriptByteArrayWithOther(byte[] bArray) {
        byte[]resp;
        useOtherKeys();
        resp = decriptByteArray(bArray);
        defaultKeys();
        return resp;   
        
       
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
        int res = b;
        int y = 1;

        if (e == 0) {
            return 1;
        }

        while (e > 1) {
            if ((e % 2) != 0) {
                //expoente  impar
                y = (y * res) % n;
                e = e - 1;
            }

            res = (res * res) % n;
            e = e / 2;

        }
        return ((res * y) % n);

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
    
    public void invertKeys(){
        int aux = privateKey;
        privateKey = publicKey;
        publicKey = aux;
        
    }
    
    public void useOtherKeys(){
        
        privateKey = otherPublicKey;
        publicKey = otherPublicKey;
        N = otherN;
        
    }
    public void defaultKeys(){
        privateKey = realPrivateKey;
        publicKey = realPublicKey;
        N = realN;
        
    }

     
 
    

}
