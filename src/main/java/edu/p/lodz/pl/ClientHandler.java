package edu.p.lodz.pl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final static Logger logger = LogManager.getLogger(ClientHandler.class);

    private final Socket socket;
    private final BufferedWriter out;
    private final BufferedReader in;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        logger.info("Started client handler");
    }

    @Override
    public void run() {
        while (socket.isConnected() && !socket.isClosed()) {
            try {
                String payload = in.readLine();
                if (payload == null) {
                    logger.info("Client closed");
                    break;
                }
                String response = payload;
                out.write(response);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        close();
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Closed client handler");
    }
}
