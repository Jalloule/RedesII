package Client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPClient {

    public static void run() throws Exception {
        //Port and adress to connect to the server
        int port = 6789;
        
        String address;
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter the host address: ");
        address = reader.nextLine();

        String sentence;
        String modifiedSentence;

        System.out.println("Client is On");
        System.out.println("Will sent to Port:" + port + " on: " + address);

        while (true) {
            //Reader to read the user input
            System.out.println("Write message and press ENTER");
            
            // Reading from users keyboars input
            String inFromUser = reader.nextLine();
            System.out.println("");

            try (Socket clientSocket = new Socket(address, port)) {
                //Creates stream to send message to server
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                //BufferedReader to read servers answer
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                //Puts the user input on variable sentence
                sentence = inFromUser;
                //Sends the message to server
                outToServer.writeBytes(sentence + '\n');
                System.out.println("Sent to Server: " + sentence);
                //Gets message from server
                modifiedSentence = inFromServer.readLine();
                System.out.println("Received from Server: " + modifiedSentence + "\n");
            }
        }
    }
}
