package org.painter.client;

public class Client {

    public static void main(String[] args) {
        var GUI = GuiClient.GetInstance();

        var socketClient = new SocketClient();
//
        var t = new Thread(socketClient::run);
        t.start();


        GUI.setSocketClient(socketClient);
        GUI.init_ui();

    }

}
