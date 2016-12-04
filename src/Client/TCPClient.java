package Client;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Scanner;

public class TCPClient {

//Public and Prvate keys and Algorithms classes
//------------------------------------------------------------------------------
    private static int myPrivateKey = 29;
    private static int myPublicKey = 1625;
    private static int myN = 2881;

    private static int serverPublicKey = 1625;
    private static int serverN = 2881;

    private static RSA myRSA;
    
    //private static RSA otherRSA = new RSA(serverPublicKey, serverN);

//------------------------------------------------------------------------------
    public static void run() throws Exception {
        myRSA = new RSA(myPrivateKey, myPublicKey, myN);
        myRSA.setOtherRSA(serverPublicKey,serverN);
//Sets and Prints Connection Info
//------------------------------------------------------------------------------
        //Port and adress to connect to the server
        int port = 6789;
        String address;
        address = askHostAddress();

        //control messages
        printControlMessages(port,address);
//------------------------------------------------------------------------------

//Sets file to send Info
//------------------------------------------------------------------------------
        String fileToSend = askFileName();
//------------------------------------------------------------------------------

        try {//tries to connect to server
//Creation of the socket
//------------------------------------------------------------------------------
            Socket clientSocket = new Socket(address, port);
//------------------------------------------------------------------------------

//Initial message interchange
//------------------------------------------------------------------------------            
            DataOutputStream dataOutToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader dataInFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            dataOutToServer.writeBytes("HiFromClient" + "\n");
            System.out.println("Sent " + "HiFromClient\n");
            String serverResponse = dataInFromServer.readLine();
            System.out.println("Received" + " '" + serverResponse + "' " + "from Server\n");
//------------------------------------------------------------------------------   

//Calls the send to file function
//------------------------------------------------------------------------------
            sendFile(fileToSend, clientSocket);
//------------------------------------------------------------------------------

        } catch (IOException ex) {
            System.out.println("Could not connect to server");
        }

    }
    
       private static String askHostAddress() {
        String address;

        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter the host address: ");
        //gets the host address to connect
        address = reader.nextLine();

        return address;
    }

    private static void printControlMessages(int port,String address) {
        System.out.println("Client is On");
        System.out.println("Will sent to Port:" + port + " on: " + address);

    }

       private static String askFileName() {
        String fileName;//name of the outFile

        //Reader to read the user input
        System.out.println("\nWrite the name of the file to send: ");

        // Reading from users keyboars input
        Scanner reader = new Scanner(System.in);
        fileName = reader.nextLine();
        System.out.println("");

        return fileName;
    }
    
    public static void sendFile(String fileToSend, Socket clientSocket) {

        try {
            //creates the buffered output stream to server
            //if output stream to server is created with no error, send the file
            BufferedOutputStream outToServer = new BufferedOutputStream(clientSocket.getOutputStream());

            //Creates the file to send
            File myFile = new File(fileToSend);
            //byteArray to read the file to
            byte[] originalFile = new byte[(int) myFile.length()];

            FileInputStream fis = null;

            try {
                fis = new FileInputStream(myFile);
            } catch (FileNotFoundException ex) {
                System.out.println("Fail to create file input stream, Check the file name");
            }
            if (fis != null) {
                //Stream that will get the array to send to the server
                BufferedInputStream bis = new BufferedInputStream(fis);

                try {

                    bis.read(originalFile, 0, originalFile.length);

//part to add info on the array                
//RSA Encription
//------------------------------------------------------------------------------             
                    //the original file
                    System.out.println("originalFile before encription:");
                    System.out.println(bytesToString(originalFile));
                    //the originall file encripted/signed by the sender
                    //yte[] originalFileEncripted = myRSA.encriptByteArray(originalFile);
                    byte[] originalFileEncripted = myRSA.encriptByteArrayWithPrivate(originalFile);  
                    System.out.println("originalFile encripted with Clients Private Key:");
                    System.out.println(bytesToString(originalFileEncripted));
                    
                    //message thta consists in the file to send 
                    //together with the signed file to send (to later verify integrit and auth)
                    byte[]messageToSend = concatByteArrays(originalFile,originalFileEncripted);
                    System.out.println("(originalFile+originalFileEncripted) before encription:");
                    System.out.println(bytesToString(messageToSend));
                    
                    byte[]messageToSendEncripted = myRSA.encriptByteArrayWithOther(messageToSend);
                    System.out.println("(originalFile+originalFileEncripted) encripted with Servers Public Key:");
                    System.out.println(bytesToString(messageToSendEncripted ));
                    
                    //outToServer.write(mybytearray, 0, mybytearray.length);
                    outToServer.write(messageToSendEncripted, 0, messageToSendEncripted.length);
//RSA Encription
//------------------------------------------------------------------------------                 
                    //sends to server
                    outToServer.flush();
                    outToServer.close();
                    //clientSocket.close();

                    // File sent, exit the main method
                    System.out.println("\nFile " + fileToSend + " was sent successfully");
                    return;
                } catch (IOException ex) {
                    System.out.println("Could not send the file");
                }
            }

        } catch (IOException ex) {
            System.out.println("Could not create file output stream");
        }

    }
    
    //puts together 2 byte arrays
    private static byte[] concatByteArrays(byte[]a, byte[]b){
        byte[] c = new byte[a.length+b.length];
        for (int i = 0; i < a.length; i++) {
            c[i] = a[i];            
        }
        int j = 0;
        for (int i = a.length; i < c.length; i++) {
            c[i] = b[j]; 
            j++;
        }        
        
        return c;
    }

    private static String bytesToString(byte[] e) {
        String test = "";
        for (byte b : e) {
            test += " " + Byte.toString(b);
        }
        return test;
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
