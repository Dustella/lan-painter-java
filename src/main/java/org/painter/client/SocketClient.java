package org.painter.client;

import javafx.util.Pair;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Logger;

public class SocketClient {
    private static final String SERVER_ADDRESS = "localhost"; // 服务器地址
    private static final int SERVER_PORT = 12345; // 服务器端口
    private static Socket socket;
    private static PrintWriter out;

    public static void main(String[] args) {
        var client = new SocketClient();
        client.run();
    }


    public void sendLine(int x1, int y1, int x2, int y2) {

        String msg = "newLine " +
                x1 +
                " " +
                y1 +
                " " +
                x2 +
                " " +
                y2;
        out.println(msg);
    }

    public void sendHello() {
        out.println("hello");
    }

    public void closeSocket() throws IOException {
        socket.shutdownOutput();
    }

    private void handle() throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        out = new PrintWriter(socket.getOutputStream(), true);

        Logger.getGlobal().info("连接服务器成功");

        Thread inputThread = new Thread(() -> {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                while ((message = in.readLine()) != null) {
                    Logger.getGlobal().info("收到消息: " + message);
                    var tokens = message.split(" ");
                    var x1 = Integer.parseInt(tokens[1]);
                    var y1 = Integer.parseInt(tokens[2]);
                    var x2 = Integer.parseInt(tokens[3]);
                    var y2 = Integer.parseInt(tokens[4]);
                    GuiClient.GetInstance().drawLine(x1, y1, x2, y2);
                }
            } catch (IOException e) {
                Logger.getGlobal().warning(e.getMessage());
            }
        });

        inputThread.start();

        Scanner scanner = new Scanner(System.in);
        String userInput;
        while ((userInput = scanner.nextLine()) != null) {
            out.println(userInput);
        }
        socket.shutdownOutput();

    }

    public void run() {
        try {
            handle();
        } catch (IOException e) {
            Logger.getGlobal().warning(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                Logger.getGlobal().warning(e.getMessage());
            }
        }
    }
}
