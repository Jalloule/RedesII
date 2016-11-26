package Client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPClient {

    public static void run() throws Exception {
        //Port and adress to connect to the server
        int port = 6789;
        String address;
        String fileToSend;
        //gets the host address to connect
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter the host address: ");
        address = reader.nextLine();
        //control messages
        System.out.println("Client is On");
        System.out.println("Will sent to Port:" + port + " on: " + address);

        //Stream to send to server
        BufferedOutputStream outToServer = null;

        //Reader to read the user input
        System.out.println("\nWrite the name of the file to send: ");

        // Reading from users keyboars input
        fileToSend = reader.nextLine();
        System.out.println("");

        try {//tries to connect to server
            //Creates stream to send message to server
            Socket clientSocket = new Socket(address, port);
            outToServer = new BufferedOutputStream(clientSocket.getOutputStream());

        } catch (IOException ex) {
            System.out.println("Could not connect to server");
        }
        if (outToServer != null) {
            //Creates the file to send
            File myFile = new File(fileToSend);
            //byteArray to read the file to
            byte[] mybytearray = new byte[(int) myFile.length()];

            FileInputStream fis = null;

            try {
                fis = new FileInputStream(myFile);
            } catch (FileNotFoundException ex) {
                // Do exception handling
            }
            //Stream that will get the array to send to the server
            BufferedInputStream bis = new BufferedInputStream(fis);

            try {
                
                bis.read(mybytearray, 0, mybytearray.length);
                outToServer.write(mybytearray, 0, mybytearray.length);
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
        
    }
}
