package Client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPServer {

    //Public and Prvate keys and Algorithms classes
//------------------------------------------------------------------------------
    private int myPrivateKey;
    private int myPublicKey;
    private int myN;

    private int clientPublicKey;
    private int clientN;

    private RSA myRSA;

//------------------------------------------------------------------------------
    public TCPServer() {
        this.myPrivateKey = 19;
        this.myPublicKey = 479;
        this.myN = 781;

    }

    public void run() throws Exception {
        myRSA = new RSA(myPrivateKey, myPublicKey, myN);
        //myRSA.setOtherRSA(clientPublicKey,clientN);

//Opens and Prints Connection Info
//------------------------------------------------------------------------------
        int port = 6789;//Port to open the server      
        //Opens the socket
        ServerSocket welcomeSocket = new ServerSocket(6789);
        //Control messages
        printControlMessages(port);

//------------------------------------------------------------------------------       
//Sets file to save info
//------------------------------------------------------------------------------    
        String fileOutput;//name of the outFile        
        fileOutput = askFileName();
//------------------------------------------------------------------------------   
        //Server loop, will wait for messages
        System.out.println("\nWaiting to receive something from the Client...\n");
        while (true) {
//Creation of the socket
//------------------------------------------------------------------------------
            Socket connectionSocket = welcomeSocket.accept();
//------------------------------------------------------------------------------

//Initial message interchange
//------------------------------------------------------------------------------ 
            publicKeyExchange(connectionSocket);
//------------------------------------------------------------------------------ 

//Calls the send to file function
//------------------------------------------------------------------------------     
            receiveFile(fileOutput, connectionSocket);
//------------------------------------------------------------------------------        
        }
    }

    public void publicKeyExchange(Socket connectionSocket) {
        try {
            BufferedReader dataInFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream dataOutToClient = new DataOutputStream(connectionSocket.getOutputStream());

            String clientMessage = dataInFromClient.readLine();
            System.out.println("Received pair " + "'" + clientMessage + "'" + " from client\n");

            String[] nAndKey = clientMessage.split(" ");
            setClientN(Integer.parseInt(nAndKey[0]));
            setClientPublicKey(Integer.parseInt(nAndKey[1]));
            myRSA.setOtherRSA(clientPublicKey, clientN);

            dataOutToClient.writeBytes(myN + " " + myPublicKey + "\n");
            System.out.println("Sent " + "N:" + myN + " PublicKey:" + myPublicKey + " \n");

        } catch (IOException ex) {
            System.out.println("Could not exchange keys");
        }

    }

    private void printControlMessages(int port) {
        System.out.println("Socked opened");
        System.out.println("Server is On");
        System.out.println("Listening on Port:" + port + "\n");

    }

    private String askFileName() {
        String fileName;//name of the outFile

        //Reader to read the user input
        System.out.println("Write the name of the output file: ");

        // Reading from users keyboars input
        Scanner reader = new Scanner(System.in);
        fileName = reader.nextLine();
        System.out.println("");

        return fileName;
    }

    public void receiveFile(String fileOutput, Socket connectionSocket) {

        try {
            //Input stream to read the file
            InputStream is = connectionSocket.getInputStream();

            //Output stream to write the file
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] aByte = new byte[1];//array of bytes so put the input in
            int bytesRead;

            if (is != null) {

                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                try {
                    fos = new FileOutputStream(fileOutput);
                    bos = new BufferedOutputStream(fos);
                    //reads from the input stream, from the client
                    bytesRead = is.read(aByte, 0, aByte.length);

                    do {
                        //writes  a byte on the out stream to the file
                        baos.write(aByte);
                        //reads a byte form the input
                        bytesRead = is.read(aByte);
                    } while (bytesRead != -1);//will do this until thereis something to read
                    //once it is done
//RSA Decription
//------------------------------------------------------------------------------              
                    //the array received
                    byte[] receivedMessageEncripted = baos.toByteArray();
                    System.out.println("(originalFile+originalFileEncripted) encripted with Servers Public Key:");
                    System.out.println(bytesToString(receivedMessageEncripted));

                    byte[] receivedMessage = myRSA.decriptByteArray(receivedMessageEncripted);
                    System.out.println("(originalFile+originalFileEncripted) decripted using Servers Private Key:");
                    System.out.println(bytesToString(receivedMessage));

                    byte[] originalFile = extractOriginalFile(receivedMessage);
                    System.out.println("originalFile not encripted:");
                    System.out.println(bytesToString(originalFile));

                    byte[] originalFileEncripted = extractOriginalFileEncripted(receivedMessage);
                    System.out.println("originalFile encripted with Clients Private Key:");
                    System.out.println(bytesToString(originalFileEncripted));

                    //byte[] originalFileDecripted = otherRSA.decriptByteArray(originalFileEncripted);
                    byte[] originalFileDecripted = myRSA.decriptByteArrayWithOther(originalFileEncripted);

                    System.out.println("originalFile decripted using Clients Public Key:");
                    System.out.println(bytesToString(originalFileDecripted));

                    System.out.print("Comparison between originalFile and originalFileDecripted: " + isByteArrayEqual(originalFile, originalFileDecripted));

//part to split what was received
//writes in the file the decripted byte array
                    bos.write(originalFile);//writes the file
//RSA Decription
//------------------------------------------------------------------------------   
                    //bos.write(baos.toByteArray());//writes the file
                    bos.flush();
                    bos.close();//closes the output file
                    connectionSocket.close();//closes the connection
                    System.out.println("\nFile received successfully and saved as " + fileOutput);
                    return;//leaves the loop and closes the server
                } catch (IOException ex) {
                    System.out.println("Error in writing the file");
                }
            }
        } catch (IOException ex) {
            System.out.println("Could not create input stream");
        }

    }

    private byte[] extractOriginalFile(byte[] receivedMessage) {
        int size = receivedMessage.length / 3;
        byte[] originalFile = new byte[size];
        for (int i = 0; i < size; i++) {
            originalFile[i] = receivedMessage[i];
        }

        return originalFile;
    }

    private byte[] extractOriginalFileEncripted(byte[] receivedMessage) {
        int size = receivedMessage.length / 3;
        byte[] originalFileEncripted = new byte[size * 2];
        int j = 0;
        for (int i = size; i < size * 3; i++) {
            originalFileEncripted[j] = receivedMessage[i];
            j++;
        }

        return originalFileEncripted;
    }

    private boolean isByteArrayEqual(byte[] original, byte[] decripted) {
        if (original.length != decripted.length) {
            return false;
        }
        for (int i = 0; i < decripted.length; i++) {
            if (original[i] != decripted[i]) {
                return false;
            }
        }

        return true;
    }

    private String bytesToString(byte[] e) {
        String test = "";
        for (byte b : e) {
            test += " " + Byte.toString(b);
        }
        return test;
    }

    //represents an int in how many bytes I want
    public byte[] intToBytes(int x, int n) {
        byte[] bytes = new byte[n];
        for (int i = 0; i < n; i++, x >>>= 8) {
            bytes[i] = (byte) (x & 0xFF);
        }
        return bytes;
    }

    //goes back from bytes to int
    public int bytesToInt(byte[] x) {
        int value = 0;
        for (int i = 0; i < x.length; i++) {
            value += ((long) x[i] & 0xffL) << (8 * i);
        }
        return value;
    }

    public void setClientPublicKey(int clientPublicKey) {
        this.clientPublicKey = clientPublicKey;
    }

    public void setClientN(int clientN) {
        this.clientN = clientN;
    }

    public void setKeys() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter the N: ");
        this.myN = reader.nextInt();
        System.out.println("Enter the PublicKey: ");
        this.myPublicKey = reader.nextInt();
        System.out.println("Enter the PrivateKey: ");
        this.myPrivateKey = reader.nextInt();
        System.out.println("");

    }

}
