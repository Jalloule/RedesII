package Client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPServer {

    //Public and Prvate keys and Algorithms classes
//------------------------------------------------------------------------------
    private static int myPrivateKey = 29;
    private static int myPublicKey = 1625;
    private static int myN = 2881;

    private static int clientPublicKey = 1625;
    private static int clientN = 2881;

    private static RSA myRSA = new RSA(myPrivateKey, myPublicKey, myN);
    private static RSA otherRSA = new RSA(clientPublicKey, clientN);

//------------------------------------------------------------------------------
    public static void run() throws Exception {

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
            BufferedReader dataInFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream dataOutToClient = new DataOutputStream(connectionSocket.getOutputStream());

            String clientMessage = dataInFromClient.readLine();
            System.out.println("Received" + " '" + clientMessage + "' " + "from client\n");

            dataOutToClient.writeBytes("HiFromServer" + "\n");
            System.out.println("Sent " + "HiFromServer\n");
//------------------------------------------------------------------------------ 

//Calls the send to file function
//------------------------------------------------------------------------------     
            receiveFile(fileOutput, connectionSocket);
//------------------------------------------------------------------------------        
        }
    }

    private static void printControlMessages(int port) {
        System.out.println("Socked opened");
        System.out.println("Server is On");
        System.out.println("Listening on Port:" + port + "\n");

    }

    private static String askFileName() {
        String fileName;//name of the outFile

        //Reader to read the user input
        System.out.println("Write the name of the output file: ");

        // Reading from users keyboars input
        Scanner reader = new Scanner(System.in);
        fileName = reader.nextLine();
        System.out.println("");

        return fileName;
    }

    public static void receiveFile(String fileOutput, Socket connectionSocket) {

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
                    byte[] receivedByteArray = baos.toByteArray();
                    System.out.println("byteArray before decription:");
                    System.out.println(bytesToString(receivedByteArray));
                    byte[] decriptedByteArray = myRSA.decriptByteArray(receivedByteArray);
                    System.out.println("byteArray after decription:");
                    System.out.println(bytesToString(decriptedByteArray));
//part to split what was received

//writes in the file the decripted byte array
                    bos.write(decriptedByteArray);//writes the file
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
