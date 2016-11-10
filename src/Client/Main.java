package Client;

import java.util.Scanner;

public class Main {

    public static void main(String argv[]) throws Exception {

        //Gets user input for mode
        System.out.println("1 for Server Mode");
        System.out.println("2 for Client Mode");
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        int n = reader.nextInt();
        //Picks and runs the mode
        switch (n) {
            case 1:
                TCPServer.run();
                break;
            case 2:
                TCPClient.run();
                break;
            default:
                System.out.println("Incorrect Mode");

        }

    }

}
