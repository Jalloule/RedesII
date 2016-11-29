package Client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPServer {

    public static void run() throws Exception {
        //Some variables
        byte[] aByte = new byte[1];//array of bytes so put the input in
        int bytesRead;
        String fileOutput;//nameof the outFile
        int port = 6789;//Port to open the server

        InputStream is = null;

        Scanner reader = new Scanner(System.in);

        //Opens the socket
        ServerSocket welcomeSocket = new ServerSocket(6789);
        //Control messages
        System.out.println("Socked opened");
        System.out.println("Server is On");
        System.out.println("Listening on Port:" + port + "\n");

        //Reader to read the user input
        System.out.println("Write the name of the output file: ");

        // Reading from users keyboars input
        fileOutput = reader.nextLine();
        System.out.println("");

        //Server loop, will wait for messages
        System.out.println("\nWaiting to receive something from the Client...");
        while (true) {
            //Connection socket openned
            Socket connectionSocket = welcomeSocket.accept();

            BufferedReader dataInFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream dataOutToClient = new DataOutputStream(connectionSocket.getOutputStream());

            String clientMessage = dataInFromClient.readLine();
            System.out.println("Received" + " '" + clientMessage + "' " + "from client\n");

            dataOutToClient.writeBytes("HiFromServer" + "\n");
            System.out.println("Sent " + "HiFromServer\n");

            //FILE RECEIVE           
            //Input stream to read the file
            is = connectionSocket.getInputStream();
            //Output stream to write the file
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

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
                    bos.write(baos.toByteArray());//writes the file
                    bos.flush();
                    bos.close();//closes the output file
                    connectionSocket.close();//closes the connection
                    System.out.println("\nFile received successfully and saved as " + fileOutput);
                    return;//leaves the loop and closes the server
                } catch (IOException ex) {
                    System.out.println("Error in writing the file");
                }
            }

            //FILE RECEIVE         
        }
    }
}
