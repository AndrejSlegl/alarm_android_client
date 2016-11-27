package com.example.andrej.alarmandroidclient;

import android.util.Log;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteOrder;

/**
 * Created by Andrej on 26. 11. 2016.
 */

public class SocketServer implements Runnable, Closeable {

    private ServerSocket serverSocket;
    private IServerDelegate delegate;

    public SocketServer(int port, IServerDelegate delegate) throws IOException {
        this.delegate = delegate;
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();

                //SocketAddress address = socket.getRemoteSocketAddress();

                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                StringBuilder builder = new StringBuilder();
                char c;

                do {
                    byte b = dataInputStream.readByte();
                    c = (char)b;

                    builder.append(c);
                } while (c != '\n');

                delegate.serverNewClientMessage(builder.toString());

                socket.close();

            } catch (IOException e) {
                delegate.serverGeneralException(e);
            }
        }
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
    }
}
