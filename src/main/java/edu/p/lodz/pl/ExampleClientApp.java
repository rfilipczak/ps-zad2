package edu.p.lodz.pl;

import java.io.IOException;
import java.util.Scanner;

public class ExampleClientApp {
    private final static String prompt = "Write something to send to server or \"stop\" to close:";

    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 6666);
        Scanner scanner = new Scanner(System.in);
        System.out.println(prompt);
        String msg = scanner.nextLine();
        while (!msg.equals("stop")) {
            client.send(msg);
            String rsp = client.recv();
            System.out.println("Response: " + rsp);
            System.out.println(prompt);
            msg = scanner.nextLine();
        }
        client.close();
    }
}
