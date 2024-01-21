package edu.p.lodz.pl;

import java.util.Scanner;

public class ExampleServerApp {
    public static void main(String[] args) throws InterruptedException {
        Server server = new Server("localhost", 6666);
        Thread thread = new Thread(server);
        thread.start();

        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Type \"stop\" to close server");
        } while(!scanner.nextLine().equals("stop"));

        server.close();
        thread.join();
    }
}
