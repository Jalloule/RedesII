package Client;

import java.util.Scanner;

public class Main {

    public static void main(String argv[]) throws Exception {

        //Gets user input for mode
        System.out.println("1 for Server Mode (With pre-set keys)");
        System.out.println("2 for Client Mode (With pre-set keys)");
        System.out.println("3 for Server Mode (Without pre-set keys)");
        System.out.println("4 for Client Mode (Without pre-set keys)");
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        int n = reader.nextInt();
        TCPServer server = new TCPServer();
        TCPClient client = new TCPClient();
        //Picks and runs the mode
        switch (n) {
            case 1:
                server.run();
                break;
            case 2:
                client.run();
                break;
            case 3:
                server.setKeys();
                server.run();
                break;
            case 4:
                client.setKeys();
                client.run();
                break;
            default:
                System.out.println("Incorrect Mode");

        }

    }

}
