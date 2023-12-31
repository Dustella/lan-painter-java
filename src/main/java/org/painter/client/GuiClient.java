package org.painter.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class GuiClient {

    private SocketClient socketClient;

    private JFrame jf = new JFrame();;

    // Singleton
    public GuiClient() {
    }

    public static GuiClient instance = null;
    public static GuiClient GetInstance() {
        if (instance == null) {
            instance = new GuiClient();
        }
        return instance;
    }


    public void setSocketClient(SocketClient socketClient) {
        this.socketClient = socketClient;
    }

    public void init_ui() {
        /*界面初始化*/
        jf.addWindowListener(new MyWindowListener());
        jf.setSize(1000, 700);
        jf.setTitle("画图板");
        jf.setDefaultCloseOperation(3);
        //设置窗口相对于指定组件的位置，此窗口将置于屏幕的中央。
        jf.setLocationRelativeTo(null);

        jf.setLayout(new BorderLayout());//设置布局

        //实例化事件监听类
        DrawListener dl = new DrawListener();
        dl.setSocketClient(socketClient);


        jf.setVisible(true);
        jf.addMouseListener(dl);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        Graphics gr = jf.getGraphics();
        gr.drawLine(x1, y1, x2, y2);
    }


    private class MyWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {

            try {
                socketClient.closeSocket();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


}
