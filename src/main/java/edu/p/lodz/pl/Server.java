package edu.p.lodz.pl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
    private final static Logger logger = LogManager.getLogger(Server.class);

    private ServerSocket serverSocket;

    private final String addr;
    private final int port;

    private final List<ClientHandler> clientHandlers = new ArrayList<>();

    public Server(String addr, int port) {
        this.addr = addr;
        this.port = port;
    }

    private void start() {
        try {
            serverSocket = new ServerSocket();
        } catch (IOException e) {
            logger.error("Failed to create socket");
            return;
        }
        try {
            serverSocket.bind(new InetSocketAddress(addr, port));
        } catch (IOException e) {
            logger.error("Failed to bind");
            return;
        }
        logger.info("Started server");
        listen();
    }

    public void close() {
        logger.info("Closing server");
        try {
            serverSocket.close();
        } catch (IOException e) {
            logger.warn("Failed to gracefully close server");
        }
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.close();
        }
    }

    private void listen() {
        while (!serverSocket.isClosed()) {
            try {
                logger.debug("Waiting for client");
                Socket clientSocket = serverSocket.accept();
                logger.debug("Client joined");
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandlers.add(clientHandler);
                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (SocketException e) {
                logger.debug("Server closed while waiting for client");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    @Override
    public void run() {
        start();
    }
}
