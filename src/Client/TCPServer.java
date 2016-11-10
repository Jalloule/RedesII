package Client;

import java.io.*;
import java.net.*;

public class TCPServer {

    public static void run() throws Exception {
        //Port to open the server
        int port = 6789;

        String clientSentence;
        String capitalizedSentence;
        //Opens the socket
        ServerSocket welcomeSocket = new ServerSocket(6789);
        //Control messages
        System.out.println("Socked opened");
        System.out.println("Server is On");
        System.out.println("Listening on Port:" + port + "\n");
        //Server loop, will wait for messages
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            //BufferedReader to read clients input
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            //Creates stream to answer the client
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            //Reads the client message from bufferedReader
            clientSentence = inFromClient.readLine();
            System.out.println("Received from Client: " + clientSentence);
            capitalizedSentence = clientSentence.toUpperCase() + '\n';
            //Sends the message back to client, capitalized
            outToClient.writeBytes(capitalizedSentence);
            System.out.println("Sent to Client: " + capitalizedSentence);
        }
    }
}
