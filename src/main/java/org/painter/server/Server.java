package org.painter.server;

import java.io.*;
import java.net.*;
import java.util.logging.Logger;

public class Server {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        Logger.getGlobal().setLevel(java.util.logging.Level.ALL);
        Logger.getGlobal().fine("服务器启动");
        while (true) {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                var newSocket = serverSocket.accept();
                var newHandlerThread = new ClientHandler(newSocket);
                newHandlerThread.start();
            } catch (IOException e) {
                Logger.getGlobal().severe(e.getMessage());
            }
        }
    }

}

