package Client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPClient {

//Public and Prvate keys and Algorithms classes
//------------------------------------------------------------------------------
    private static int myPrivateKey;
    private static int myPublicKey;
    private static int serverPublicKey;

    private static RSA rsa = new RSA();

//------------------------------------------------------------------------------
    public static void run() throws Exception {

//Sets and Prints Connection Info
//------------------------------------------------------------------------------
        //Port and adress to connect to the server
        int port = 6789;
        String address;
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter the host address: ");
        //gets the host address to connect
        address = reader.nextLine();
        //control messages
        System.out.println("Client is On");
        System.out.println("Will sent to Port:" + port + " on: " + address);
//------------------------------------------------------------------------------

//Sets file to send Info
//------------------------------------------------------------------------------
        String fileToSend;
        //Reader to read the user input
        System.out.println("\nWrite the name of the file to send: ");
        // Reading from users keyboars input
        fileToSend = reader.nextLine();
        System.out.println("");
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

    public static void sendFile(String fileToSend, Socket clientSocket) {

        try {
            //creates the buffered output stream to server
            //if output stream to server is created with no error, send the file
            BufferedOutputStream outToServer = new BufferedOutputStream(clientSocket.getOutputStream());

            //Creates the file to send
            File myFile = new File(fileToSend);
            //byteArray to read the file to
            byte[] mybytearray = new byte[(int) myFile.length()];

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

                    bis.read(mybytearray, 0, mybytearray.length);

//part to add info on the array                
//RSA Encription
//------------------------------------------------------------------------------             
                    //encripts the array
                    byte[] mybytearrayEncripted = rsa.encriptByteArray(mybytearray, serverPublicKey);
                    //outToServer.write(mybytearray, 0, mybytearray.length);
                    outToServer.write(mybytearrayEncripted, 0, mybytearrayEncripted.length);
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

    private int getMyPrivateKey() {
        return myPrivateKey;
    }

    public int getMyPublicKey() {
        return myPublicKey;
    }

    public int getServerPublicKey() {
        return serverPublicKey;
    }

    public RSA getRsa() {
        return rsa;
    }

}
