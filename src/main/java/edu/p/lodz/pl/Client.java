package edu.p.lodz.pl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class Client {
    private final static Logger logger = LogManager.getLogger(Client.class);

    private final Socket socket;
    private final BufferedWriter out;
    private final BufferedReader in;

    public Client(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        logger.info("Connected with server");
    }

    public void send(String payload) {
        try {
            out.write(payload);
            out.newLine();
            out.flush();
            logger.info("Send (" + payload.length() + ") characters");
        } catch (IOException e) {
            logger.error("Failed to send msg");
            close();
        }
    }

    public String recv() {
        String msg = null;
        try {
            msg = in.readLine();
            logger.info("Received (" + msg.length() + ") characters");
        } catch (IOException e) {
            logger.error("Failed to receive msg");
            close();
        }
        return msg;
    }


    public synchronized void close() {
        logger.debug("Closing client");
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
