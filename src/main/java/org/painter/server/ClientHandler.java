package org.painter.server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class ClientHandler extends Thread {
    private final Socket socket;
    private PrintWriter out;

    private BufferedReader in;
    private static final Set<PrintWriter> clientWriters = new HashSet<>();

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }


    private void handle() throws IOException {
        synchronized (clientWriters) {
            clientWriters.add(out);
        }

        String message;
        while ((message = in.readLine()) != null) {
            Logger.getGlobal().info("广播消息: " + message);
            broadcast(message);
        }
    }

    public void run() {
        try {
            handle();
        } catch (IOException e) {
            Logger.getGlobal().severe(e.getMessage());
        } finally {
            closeSocket();
            cleanWriter();
        }
    }

    public void cleanWriter() {
        synchronized (clientWriters) {
            clientWriters.remove(out);
        }
    }


    public void closeSocket()  {
        try {
            socket.close();
        } catch (IOException e) {
            Logger.getGlobal().severe(e.getMessage());
        }
    }

    private void broadcast(String message) {
        synchronized (clientWriters) {
            for (PrintWriter writer : clientWriters) {
                writer.println(message);
            }
        }
    }
}